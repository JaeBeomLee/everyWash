package ga.washmose.mose.Util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import ga.washmose.mose.main.MainActivity;

/**
 * Created by C on 2014-12-30.
 */
public class ULocationService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    private Context mContext;
    private GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    FusedLocationProviderApi fusedLocationProviderApi = LocationServices.FusedLocationApi;

    boolean mRequestingLocationUpdates = false;

    private static UGeoPoint locationData = null;

    private boolean mResolvingError;
    public static int REQUEST_RESOLVE_ERROR = 987;

    public ULocationService(Context context) {
        mContext = context;
        createLocationRequest();
        buildGoogleApiClient();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(500);
        mLocationRequest.setFastestInterval(500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public static UGeoPoint getLocationData() {
        return locationData;
    }

    public boolean open() {
        if (ContextCompat.checkSelfPermission(mContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((MainActivity) mContext,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MainActivity.REQ_PERMISSION_LOCATION);
        } else {
            boolean b = showSettingsAlert();
            if (b) {
                if (mGoogleApiClient.isConnected() && !mRequestingLocationUpdates) {
                    startLocationUpdates();
                    mRequestingLocationUpdates = true;
                    return true;
                }
            }
        }
        return false;
    }

    private boolean showSettingsAlert() {
        String gps = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        //if (!(gps.matches(".*gps.*") && gps.matches(".*network.*"))) {
        if (!gps.matches(".*network.*")) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

            alertDialog.setTitle("위치 서비스 사용");
            alertDialog.setMessage("현재위치를 확인하시려면, 단말기의 설정에서 '위치 서비스'사용을 허용해주세요.");
            // OK 를 누르게 되면 설정창으로 이동합니다.
            alertDialog.setPositiveButton("설정하기", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    ((Activity) mContext).startActivityForResult(intent, MainActivity.REQ_LOCATION);
                }
            });
            // Cancle 하면 종료 합니다.
            alertDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            alertDialog.show();

            return false;
        } else {
            return true;
        }
    }

    public void close() {
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            mRequestingLocationUpdates = false;
            stopLocationUpdates();
        }
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onConnected(Bundle bundle) {
        /*if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }*/
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location.getLatitude() > 0) {
            locationData = new UGeoPoint(location.getLatitude(), location.getLongitude());
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (mResolvingError) {
            // Already attempting to resolve an error.
            return;
        } else if (connectionResult.hasResolution()) {
            try {
                mResolvingError = true;
                connectionResult.startResolutionForResult((Activity) mContext, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                mGoogleApiClient.connect();
            }
        } else {
            // Show dialog using GooglePlayServicesUtil.getErrorDialog()
            /** 다이얼로그 **/
            //showErrorDialog(connectionResult.getErrorCode());
            mResolvingError = true;
        }
    }
}
