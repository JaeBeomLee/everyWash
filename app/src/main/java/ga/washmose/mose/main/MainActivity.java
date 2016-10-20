package ga.washmose.mose.main;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import org.json.JSONArray;
import org.json.JSONObject;

import ga.washmose.mose.MoreFragment;
import ga.washmose.mose.R;
import ga.washmose.mose.UserInfo;
import ga.washmose.mose.User.UserLaundryFragment;
import ga.washmose.mose.User.UserOrderRequestFragment;
import ga.washmose.mose.Util.UHttps;
import ga.washmose.mose.seller.SellerManageFragment;
import ga.washmose.mose.seller.SellerOrderRequestFragment;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static ga.washmose.mose.UserInfo.isSeller;

public class MainActivity extends AppCompatActivity {

    BottomBar mainBar;
    Toolbar toolbar;

    SellerOrderRequestFragment requestFragment;
    SellerManageFragment manageFragment;
    UserLaundryFragment laundryFragment;
    UserOrderRequestFragment userRequestFragment;
    MoreFragment moreFragment;
    FragmentManager fragmentManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        mainBar = (BottomBar)findViewById(R.id.main_bar);

        initUserData();
        fragmentManager = getSupportFragmentManager();


        if (isSeller){
            fragmentManager.beginTransaction()
                    .add(R.id.main_content, requestFragment = new SellerOrderRequestFragment())
                    .commit();
        }else{
        fragmentManager.beginTransaction()
                .add(R.id.main_content, laundryFragment = new UserLaundryFragment())
                .commit();
        }

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
                            fragmentManager.beginTransaction()
                                    .replace(R.id.main_content, laundryFragment = new UserLaundryFragment())
                                    .commit();
                            break;
                        case R.id.tab_list:
                            fragmentManager.beginTransaction()
                                    .replace(R.id.main_content, userRequestFragment = new UserOrderRequestFragment())
                                    .commit();
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
                JSONObject res = UHttps.okHttp(UHttps.IP+"/user", UserInfo.apiKey);
                UserInfo.userID = res.optInt("user_id");
                UserInfo.user_level = res.optInt("user_level");
                UserInfo.loginType = res.optInt("open_id_type");
                UserInfo.openID = res.optString("open_id");
                UserInfo.name = res.optString("user_name");
                UserInfo.address = res.optString("address");
                UserInfo.phone = res.optString("phone");
                UserInfo.profileURL = res.optString("profile_image");

                Log.d("MA response", res.toString());

                initUserOrders();
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
                JSONObject res = UHttps.okHttp(UHttps.IP+"/orders", UserInfo.apiKey);
                if (res != null){
                    code = res.optInt("code");
                    if (code == 200){
                        JSONArray orders = res.optJSONArray("order");
                        Log.d("MA response", orders.toString());
                    }
                }else {

                }
            }
        });

        thread.setDaemon(true);
        thread.start();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}

