package ga.washmose.mose.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation.OnTabSelectedListener;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import ga.washmose.mose.ItemData;
import ga.washmose.mose.MoreFragment;
import ga.washmose.mose.OrderData;
import ga.washmose.mose.R;
import ga.washmose.mose.UserInfo;
import ga.washmose.mose.User.UserLaundryFragment;
import ga.washmose.mose.User.UserOrderRequestFragment;
import ga.washmose.mose.Util.UDate;
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
    public final static int SellerOrderRequestActivity = 2334;
    public final static int SellerOrderManageActivity = 3592;

    AHBottomNavigation mainBar;
    AHBottomNavigationItem item1;
    AHBottomNavigationItem item2;
    AHBottomNavigationItem item3;
    Toolbar toolbar;

    SellerOrderRequestFragment requestFragment;
    SellerManageFragment manageFragment;
    UserLaundryFragment laundryFragment;
    UserOrderRequestFragment userRequestFragment;
    MoreFragment moreFragment;
    FragmentManager fragmentManager = null;
    JSONArray requestOrders, sellers;

    ArrayList<OrderData> requestDatas = new ArrayList<>();
    ArrayList<OrderData> ingDatas = new ArrayList<>();
    ArrayList<OrderData> completeDatas = new ArrayList<>();

    ULocationService mLocationService;
    boolean SearchFlag = false, isSearching = false;
    String myLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        UserInfo.token = FirebaseInstanceId.getInstance().getToken();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        mainBar = (AHBottomNavigation) findViewById(R.id.main_bar);
        if (!UserInfo.isSeller){
            item1 = new AHBottomNavigationItem("세탁", R.drawable.ic_laundry, R.color.colorPrimary);
            item2 = new AHBottomNavigationItem("주문상황", R.drawable.ic_list, R.color.colorPrimary);
            item3 = new AHBottomNavigationItem("더보기", R.drawable.ic_menu, R.color.colorPrimary);
        }else{
            item1 = new AHBottomNavigationItem("주문 요청", R.drawable.ic_list, R.color.colorPrimary);
            item2 = new AHBottomNavigationItem("판매 관리", R.drawable.ic_manage, R.color.colorPrimary);
            item3 = new AHBottomNavigationItem("더보기", R.drawable.ic_menu, R.color.colorPrimary);
        }


        mainBar.addItem(item1);
        mainBar.addItem(item2);
        mainBar.addItem(item3);
        mainBar.setAccentColor(Color.parseColor("#00C1F9"));
        mLocationService = new ULocationService(MainActivity.this);
//        getMyLocation();
        fragmentManager = getSupportFragmentManager();
        initUserData();
        sendProfileImage();
    }

    private void initMain(){
        if (UserInfo.isSeller){
            initSellerItems(0);
        }else{
            initSellerData();
        }
        mainBar.setOnTabSelectedListener(new OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                if (UserInfo.isSeller){
                    initSellerItems(position);
                }else{
                    switch (position){
                        case 0:
                            initSellerData();
                            break;
                        case 1:
                            initUserOrders();
                            break;
                        case 2:
                            fragmentManager.beginTransaction()
                                    .replace(R.id.main_content, moreFragment = new MoreFragment())
                                    .commit();
                    }
                }
                return true;
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
//                UserInfo.profileURL = res.optString("profile_image");
                initMain();
                registerToken();
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

    private void sendProfileImage(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int code;
                UHttps body = new UHttps();
                body.initBody();
                body.addParameter("profile_image", UserInfo.profileURL);
                JSONObject res;
                res = UHttps.okHttpPut(UHttps.IP+"/v1/register", UserInfo.apiKey, body.getBody());
                if (res != null){
                    code = res.optInt("code");
                    if (code == 200){
                        Log.d("MA progile", res.toString());
                    }else {
                    }
                }else {
                }
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
                JSONObject res = UHttps.okHttp(UHttps.IP+"/v1/findSeller/37.457813/127.128931", UserInfo.apiKey);
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

    private void initSellerItems(final int position){
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int code;
                JSONObject res = UHttps.okHttp(UHttps.IP+"/v1/orders/s", UserInfo.apiKey);
//                JSONObject res = UHttps.okHttp(UHttps.IP+"/v1/orders/s", "f36c48d3357fb2b964cff1532f431693");
                //{"order_code":"89","order_status":"0","request_date":"2016-10-25 03:07:37","send_date":"2016-11-04 00:00:00","delivery_date":null,"finish_date":null,"total_price":null,"consumer":{"user_id":"23","user_name":"테스터 1","sex":"2","phone":"010-0000-0000","address":"테스트 입니다.","profile_image":null},"items":null}
                if (res != null){
                    code = res.optInt("code");
                    if (code == 200){
                        JSONArray orders = res.optJSONArray("order");
                        ArrayList<OrderData> orderDatas = new ArrayList<>();
                        for (int i = 0; i< orders.length(); i++){
                            JSONObject order = orders.optJSONObject(i);
                            JSONObject user = order.optJSONObject("consumer");
                            JSONArray items = order.optJSONArray("items");
                            Calendar request = Calendar.getInstance();
                            Calendar collection = Calendar.getInstance();
                            Calendar complete = (Calendar) collection.clone();
                            complete.set(Calendar.DAY_OF_MONTH, complete.get(Calendar.DAY_OF_MONTH) + 4);
                            try {
                                request.setTime(UDate.getDate(order.optString("request_date")));
                                collection.setTime(UDate.getDate(order.optString("send_date")));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            ArrayList<ItemData> itemsList = new ArrayList<>();
                            if (items != null){
                                for (int j = 0; j < items.length(); j++){
                                    JSONObject item = items.optJSONObject(j);
                                    itemsList.add(new ItemData(item.optString("goods_name"), item.optString("goods_image"), item.optInt("unit_amount"), item.optInt("total_price"), item.optInt("item_code"), item.optInt("goods_code")));
                                }
                            }
                            orderDatas.add(new OrderData(user.optString("profile_image"), user.optString("user_name"), "summary", request, false, order.optInt("order_code"), order.optInt("order_status"), user.optString("phone"), collection, complete, user.optString("address"), itemsList));
                        }
                        requestDatas = new ArrayList<>();
                        ingDatas = new ArrayList<>();
                        completeDatas = new ArrayList<>();
                        for (int i = 0;  i < orderDatas.size(); i++){
                            if (orderDatas.get(i).progress == 0){
                                requestDatas.add(orderDatas.get(i));
                            }else if (orderDatas.get(i).progress == 4){
                                completeDatas.add(orderDatas.get(i));
                            }else{
                                ingDatas.add(orderDatas.get(i));
                            }
                        }
//                        JSONArray seller = res.optJSONArray("seller");
//                        sellers = seller;
                        Log.d("MA response seller", res.toString());
                    }


                }else {

                }

                switch (position){
                    case 0:
                        fragmentManager.beginTransaction()
                                .replace(R.id.main_content, requestFragment = SellerOrderRequestFragment.newInstance(requestDatas))
                                .commit();
                        break;
                    case 1:
                        fragmentManager.beginTransaction()
                                .replace(R.id.main_content, manageFragment = SellerManageFragment.newInstance(ingDatas, completeDatas))
                                .commit();
                        break;
                    case 2:
                        fragmentManager.beginTransaction()
                                .replace(R.id.main_content, moreFragment = new MoreFragment())
                                .commit();
                }
            }
        });

        thread.setDaemon(true);
        thread.start();
    }
    private void refresh(int pageNum){
        if (UserInfo.isSeller){
            initSellerItems(pageNum);
        }else{
            initUserData();
            initSellerData();
        }
//        initMain();
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            int pageNum = data.getIntExtra("pageNum", 0);
            refresh(pageNum);
        }
    }

    public void getMyLocation() {
        if (mLocationService.open()) {
            new GetLocationDataTask().execute();
        }
    }

    public void registerToken(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                UHttps body = new UHttps();
                body.initBody();
                body.addParameter("device_key", UserInfo.token);
                body.addParameter("device_type", "0");
                String url = UHttps.IP + "/v1/register/push";
                JSONObject res = UHttps.okHttp(url, UserInfo.apiKey, body.getBody());
                Log.d("token", res.toString());
            }
        });
        thread.setDaemon(true);
        thread.start();
    }


    public void deleteToken(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                UHttps body = new UHttps();
                body.initBody();
                body.addParameter("device_key", UserInfo.token);
                body.addParameter("device_type", "0");
                String url = UHttps.IP + "/v1/register/push";
                JSONObject res = UHttps.okHttpDelete(url, UserInfo.apiKey, body.getBody());
                Log.d("token", res.toString());
            }
        });
        thread.setDaemon(true);
        thread.start();
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

