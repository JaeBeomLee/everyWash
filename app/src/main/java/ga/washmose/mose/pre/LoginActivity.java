package ga.washmose.mose.pre;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;

import org.json.JSONObject;

import ga.washmose.mose.R;
import ga.washmose.mose.UserInfo;
import ga.washmose.mose.Util.UDialog;
import ga.washmose.mose.Util.UHttps;
import ga.viewpagerindicator.CirclePageIndicator;

import java.util.Arrays;

import ga.washmose.mose.Util.UPreferences;
import ga.washmose.mose.main.MainActivity;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    CirclePageIndicator indicator;
    ViewGroup kakaoLogin, facebookLogin, SNSBtns;
    LoginButton hideLoginButton;

    // Facebook
    private CallbackManager facebookCallback;
    //Kakao
    private SessionCallback callback = null;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        indicator = (CirclePageIndicator) findViewById(R.id.login_indicator);
        SNSBtns = (ViewGroup) findViewById(R.id.SNS_btns);
        facebookLogin = (ViewGroup) findViewById(R.id.facebook_login);
        kakaoLogin = (ViewGroup) findViewById(R.id.kakao_login);
        hideLoginButton = (LoginButton) findViewById(R.id.hide_kakao_login);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        indicator.setViewPager(mViewPager);
        indicator.setPadding(20, 0, 0, 0);

        facebookLogin.setOnClickListener(this);
        kakaoLogin.setOnClickListener(this);

        //kakao
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
//        Session.getCurrentSession().checkAndImplicitOpen();


        //facebook
        facebookCallback = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(facebookCallback, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                Log.d("facebook login", "success");
                Log.d("loginResult", loginResult.getAccessToken().getToken());

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final int code;
                        Log.d("LA", "open_id: " + loginResult.getAccessToken().getUserId());
                        UHttps.initBody();
                        UHttps.addParameter("open_id", loginResult.getAccessToken().getApplicationId());
                        UHttps.addParameter("open_id_type", String.valueOf(UserInfo.TYPE_FACEBOOK));

                        final JSONObject response = UHttps.okHttp(UHttps.IP + "/login", UHttps.getBody());
                        code = response.optInt("code");
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                switch (code) {
                                    case 400:
                                        //찾을 수 없음 (에러)
                                        UDialog.setDialog(LoginActivity.this, "관리자에게 문의 해주세요! \n코드 : " + code);
                                        break;
                                    case 401:   //인증 받지 않은 요청. (회원 가입으로 감)
                                        UserInfo.openID = loginResult.getAccessToken().getApplicationId();
                                        UserInfo.loginType = UserInfo.TYPE_FACEBOOK;
                                        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                                        startActivity(intent);
                                        finish();
                                        break;
                                    case 500:
                                        //내부 서버 오류

                                        break;
                                    case 200:   //로그인
                                        UPreferences.setStringPref(LoginActivity.this, UserInfo.PREF_USER, UserInfo.PREF_SUB_USER_APIKEY,response.optString("api_key"));
                                        UserInfo.apiKey = UPreferences.getStringPref(LoginActivity.this, UserInfo.PREF_USER, UserInfo.PREF_SUB_USER_APIKEY,"");
                                        UserInfo.isSeller = false;
                                        Intent intent2 = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent2);
                                        finish();
                                        break;
                                }
                            }
                        });
                        Log.d("LA", "response : " + response);
                    }
                });
                thread.setDaemon(true);
                thread.start();


//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(intent);
//                finish();
            }

            @Override
            public void onCancel() {
                Log.d("facebook login", "cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("facebook login", "error");
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.facebook_login:
                kakaoLogin.setEnabled(false);
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "user_friends"));
//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(intent);
//                finish();
                break;
            case R.id.kakao_login:
                facebookLogin.setEnabled(false);
                hideLoginButton.performClick();
//                Intent intent2 = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(intent2);
//                finish();
                break;
//            case R.id.sign_up:
//                Intent intent = new Intent(LoginActivity.this, signUPActivity.class);
//                startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        facebookCallback.onActivityResult(requestCode, resultCode, data);
    }

    protected void kakaoRequestMe() {
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                int ErrorCode = errorResult.getErrorCode();
                int ClientErrorCode = -777;

                if (ErrorCode == ClientErrorCode) {
                    Log.e("Kakao LA", "카카오 서버 네트워크가 불안정함.");
                } else {
                    Log.d("Kakao LA", "오류로 카카오로그인 실패");
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.d("Kakao LA", "오류로 카카오로그인 실패");
            }

            @Override
            public void onNotSignedUp() {
                Log.d("Kakao LA", "onNotSignedUp");
            }

            @Override
            public void onSuccess(final UserProfile result) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final int code;
                        Log.d("LA", "open_id: " + String.valueOf(result.getId()));
                        UHttps.initBody();
                        UHttps.addParameter("open_id", String.valueOf(result.getId()));
                        UHttps.addParameter("open_id_type", String.valueOf(1));

                        final JSONObject response = UHttps.okHttp(UHttps.IP + "/login", UHttps.getBody());
                        code = response.optInt("code");
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                switch (code) {
                                    case 400:
                                        //찾을 수 없음 (에러)
                                        UDialog.setDialog(LoginActivity.this, "관리자에게 문의 해주세요! \n코드 : " + code);
                                        break;
                                    case 401:   //인증 받지 않은 요청. (회원 가입으로 감)
                                        UserInfo.openID = String.valueOf(result.getId());
                                        UserInfo.loginType = UserInfo.TYPE_KAKAO;
                                        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                                        startActivity(intent);
                                        finish();
                                        break;
                                    case 500:
                                        //내부 서버 오류

                                        break;
                                    case 200:   //로그인
                                        UPreferences.setStringPref(LoginActivity.this, UserInfo.PREF_USER, UserInfo.PREF_SUB_USER_APIKEY,response.optString("api_key"));
                                        UserInfo.apiKey = UPreferences.getStringPref(LoginActivity.this, UserInfo.PREF_USER, UserInfo.PREF_SUB_USER_APIKEY,"");
                                        UserInfo.isSeller = false;
                                        Intent intent2 = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent2);
                                        finish();
                                        break;
                                }
                            }
                        });
                        Log.d("LA", "response : " + response);
                    }
                });
                thread.setDaemon(true);
                thread.start();
            }
        });
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Login Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            kakaoRequestMe();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.d("SessionCallback LA", exception.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}

class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return LoginFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }
}