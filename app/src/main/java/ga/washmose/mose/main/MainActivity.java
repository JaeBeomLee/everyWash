package ga.washmose.mose.main;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.SystemClock;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.ProtocolException;

import ga.washmose.mose.MoreFragment;
import ga.washmose.mose.R;
import ga.washmose.mose.UserInfo;
import ga.washmose.mose.User.UserLaundryFragment;
import ga.washmose.mose.User.UserOrderRequestFragment;
import ga.washmose.mose.Util.UGeoPoint;
import ga.washmose.mose.Util.UGeocoder;
import ga.washmose.mose.Util.UHttps;
import ga.washmose.mose.Util.ULocationService;
import ga.washmose.mose.seller.SellerManageFragment;
import ga.washmose.mose.seller.SellerOrderRequestFragment;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static ga.washmose.mose.UserInfo.isSeller;

public class MainActivity extends AppCompatActivity {

    public final static int REQ_LOCATION = 4332;
    public final static int REQ_PERMISSION_LOCATION = 44;
    private final static int WAIT_COUNT = 600;  // 1sec * 100
    public final static int UserOrderRequestActivity = 2002;

    BottomBar mainBar;
    Toolbar toolbar;

    SellerOrderRequestFragment requestFragment;
    SellerManageFragment manageFragment;
    UserLaundryFragment laundryFragment;
    UserOrderRequestFragment userRequestFragment;
    MoreFragment moreFragment;
    FragmentManager fragmentManager = null;
    JSONArray requestOrders, sellers;

    ULocationService mLocationService;
    boolean SearchFlag = false, isSearching = false;
    String myLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        mainBar = (BottomBar)findViewById(R.id.main_bar);
        mLocationService = new ULocationService(MainActivity.this);
//        getMyLocation();
        fragmentManager = getSupportFragmentManager();
        initUserData();
    }

    private void initMain(){
        initSellerData();
        mainBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (isSeller){
                    switch (tabId){
                        case R.id.tab_laundry:
                            fragmentManager.beginTransaction()
                                    .replace(R.id.main_content, requestFragment = new SellerOrderRequestFragment())
                                    .commit();
                            break;
                        case R.id.tab_list:
                            fragmentManager.beginTransaction()
                                    .replace(R.id.main_content, manageFragment = new SellerManageFragment())
                                    .commit();
                            break;
                        case R.id.tab_menu:
                            fragmentManager.beginTransaction()
                                    .replace(R.id.main_content, moreFragment = new MoreFragment())
                                    .commit();
                    }
                }else{
                    switch (tabId){
                        case R.id.tab_laundry:
                            initSellerData();
                            break;
                        case R.id.tab_list:
                            initUserOrders();
                            break;
                        case R.id.tab_menu:
                            fragmentManager.beginTransaction()
                                    .replace(R.id.main_content, moreFragment = new MoreFragment())
                                    .commit();
                    }
                }
            }
        });
    }
    private void initUserData(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject res = UHttps.okHttp(UHttps.IP+"/v1/user", UserInfo.apiKey);
                UserInfo.userID = res.optInt("user_id");
                UserInfo.user_level = res.optInt("user_level");
                UserInfo.loginType = res.optInt("open_id_type");
                UserInfo.openID = res.optString("open_id");
                UserInfo.name = res.optString("user_name");
                UserInfo.address = res.optString("address");
                UserInfo.phone = res.optString("phone");
                UserInfo.profileURL = res.optString("profile_image");
                initMain();
                Log.d("MA response", res.toString());
            }
        });

        thread.setDaemon(true);
        thread.start();
    }

    private void initUserOrders(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
            int code;
            JSONObject res;
            res = UHttps.okHttp(UHttps.IP+"/v1/orders", UserInfo.apiKey);
            if (res != null){
                code = res.optInt("code");
                if (code == 200){
                    JSONArray orders = res.optJSONArray("order");
                    requestOrders = orders;
                    Log.d("MA response orders", orders.toString());
                }else {
                }
            }else {
                requestOrders = new JSONArray();
            }

            fragmentManager.beginTransaction()
                    .replace(R.id.main_content, userRequestFragment = UserOrderRequestFragment.newInstance(requestOrders))
                    .commit();
            }
        });

        thread.setDaemon(true);
        thread.start();
    }


    private void initSellerData(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int code;
                JSONObject res = UHttps.okHttp(UHttps.IP+"/v1/findSeller/37.4578/127.129", UserInfo.apiKey);
                if (res != null){
                    code = res.optInt("code");
                    if (code == 200){
                        JSONArray seller = res.optJSONArray("seller");
                        sellers = seller;
                        Log.d("MA response seller", seller.toString());
                    }

                    if (isSeller){
                        fragmentManager.beginTransaction()
                                .add(R.id.main_content, requestFragment = new SellerOrderRequestFragment())
                                .commit();
                    }else{
                        fragmentManager.beginTransaction()
                                .add(R.id.main_content, laundryFragment = UserLaundryFragment.newInstance(sellers))
                                .commit();
                    }
                }else {

                }
            }
        });

        thread.setDaemon(true);
        thread.start();
    }

    private void refresh(){
        initUserData();
        initSellerData();
        initMain();
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            refresh();
        }
    }

    public void getMyLocation() {
        if (mLocationService.open()) {
            new GetLocationDataTask().execute();
        }
    }

    private class GetLocationDataTask extends AsyncTask<Void, Void, UGeoPoint> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isSearching = true;
//            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected UGeoPoint doInBackground(Void... params) {
            UGeoPoint geoPoint = null;
            for (int i = 0; i < WAIT_COUNT; i++) {
                SystemClock.sleep(10);
                geoPoint = ULocationService.getLocationData();
                if (geoPoint != null) {
                    break;
                }
            }
            return geoPoint;
        }

        @Override
        protected void onPostExecute(UGeoPoint geoPoint) {
            if (geoPoint != null) {
                String address = UGeocoder.findAddress(MainActivity.this, geoPoint);
                if (address != null) {
                    myLocation = address;
                    Toast.makeText(MainActivity.this, myLocation, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "내 위치를 가져올 수 없습니다", Toast.LENGTH_SHORT).show();
            }
            if (mLocationService != null) {
                mLocationService.close();
            }
            isSearching = false;
            super.onPostExecute(geoPoint);
        }
    }
}

