package ga.washmose.mose.main;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import ga.washmose.mose.R;
import ga.washmose.mose.UserLaundryFragment;
import ga.washmose.mose.seller.SellerManageFragment;
import ga.washmose.mose.seller.SellerOrderRequestFragment;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    BottomBar mainBar;
    Toolbar toolbar;

    SellerOrderRequestFragment requestFragment;
    SellerManageFragment manageFragment;
    UserLaundryFragment laundryFragment;
    FragmentManager fragmentManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        mainBar = (BottomBar)findViewById(R.id.main_bar);

        fragmentManager = getSupportFragmentManager();

//        fragmentManager.beginTransaction()
//                .add(R.id.main_content, laundryFragment = new UserLaundryFragment())
//                .commit();
        fragmentManager.beginTransaction()
                .add(R.id.main_content, requestFragment = new SellerOrderRequestFragment())
                .commit();

        mainBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
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
                }
            }
        });

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}

