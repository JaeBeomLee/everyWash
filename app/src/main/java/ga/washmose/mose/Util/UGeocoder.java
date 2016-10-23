package ga.washmose.mose.Util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by C on 15. 7. 21..
 */
public class UGeocoder {

    public static String findAddress(Context context, UGeoPoint p) {
        StringBuffer bf = new StringBuffer();
        Geocoder geocoder = new Geocoder(context, Locale.KOREA);
        List<Address> address;
        String currentLocationAddress = null;
        try {
            if (geocoder != null) {
                // 세번째 인수는 최대결과값인데 하나만 리턴받도록 설정했다
                address = geocoder.getFromLocation(p.lat, p.lng, 1);
                // 설정한 데이터로 주소가 리턴된 데이터가 있으면
                if (address != null && address.size() > 0) {
                    currentLocationAddress = address.get(0).getThoroughfare();
                    // 주소
                    //currentLocationAddress = address.get(0).getAddressLine(0).toString();
Log.e("XXX", address.get(0).getAdminArea() + "|" + address.get(0).getCountryCode() + "|" + address.get(0).getCountryName() + "|" + address.get(0).getFeatureName()
+ "|" + address.get(0).getLocality() + "|" + address.get(0).getSubAdminArea() + "|" + address.get(0).getSubLocality() + "|" + address.get(0).getThoroughfare()
+ "|" + address.get(0).getSubThoroughfare() + "|" + address.get(0).getUrl());
                    // 전송할 주소 데이터 (위도/경도 포함 편집)
                    /*bf.append(currentLocationAddress).append("#");
                    bf.append(lat).append("#");
                    bf.append(lng);*/
                }
            }

        } catch (IOException e) {
            Toast.makeText(context, "주소취득 실패"
                    , Toast.LENGTH_LONG).show();

            e.printStackTrace();
        }
        //return bf.toString();
        return currentLocationAddress;
    }

    public static UGeoPoint findGeoPoint(Context context, String address) {
        Geocoder geocoder = new Geocoder(context);
        Address addr;
        UGeoPoint location = null;
        try {
            List<Address> listAddress = geocoder.getFromLocationName(address, 1);
            if (listAddress.size() > 0) { // 주소값이 존재 하면
                addr = listAddress.get(0); // Address형태로
                //int lat = (int) (addr.getLatitude() * 1E6);
                //int lng = (int) (addr.getLongitude() * 1E6);
                double lat = addr.getLatitude();
                double lng = addr.getLongitude();
                location = new UGeoPoint(lat, lng);

                Log.d("UGeocoder", "주소로부터 취득한 위도 : " + lat + ", 경도 : " + lng);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return location;
    }

}
