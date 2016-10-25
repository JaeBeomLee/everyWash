package ga.washmose.mose.pre;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import ga.washmose.mose.R;
import ga.washmose.mose.UserInfo;
import ga.washmose.mose.Util.UHttps;
import ga.washmose.mose.Util.UKakaoLogin;
import ga.washmose.mose.Util.UPreferences;
import ga.washmose.mose.main.MainActivity;
import okhttp3.FormBody;
import okhttp3.RequestBody;

import static ga.washmose.mose.UserInfo.PREF_GCM;
import static ga.washmose.mose.UserInfo.PREF_SUB_GCM_REG;

public class SplashActivity extends AppCompatActivity {

    // Kakao
    private UKakaoLogin.SessionCallback kakaoCallback = null;

    // Facebook
    private CallbackManager facebookCallback;
    Handler handler = new Handler();

    boolean isLogin = false;
    boolean isSnsLogin = false;
    UKakaoLogin kakaoLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        kakaoLogin = new UKakaoLogin(SplashActivity.this);
        initKakao();

    }


    private void initGCM() {
//        context = getApplicationContext();
//
//        // Check device for Play Services APK. If check succeeds, proceed with GCM registration.
//        if (checkPlayServices()) {
//            gcm = GoogleCloudMessaging.getInstance(this);
//            regid = getRegistrationId(context);
//
//            if (regid.isEmpty()) {
//                registerInBackground();
//            }
//
//            if (regid != null) {
//                Log.i("reg", regid);
//            }
//
//        } else {
//            Log.i(TAG, "No valid Google Play Services APK found.");
//        }
    }


    private void initFacebook() {
        facebookCallback = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(facebookCallback, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                Log.d("facebook login", "success");
                Log.d("loginResult", loginResult.getAccessToken().getApplicationId());

                Bundle params = new Bundle();
                params.putString("fields", "id,email,gender,cover,picture.type(large)");
                new GraphRequest(loginResult.getAccessToken(), "me", params, HttpMethod.GET, new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        JSONObject data = response.getJSONObject();
                        if (data.has("picture")) {
                            String profilePicUrl = data.optJSONObject("picture").optJSONObject("data").optString("url");
                            // set profile image to imageview using Picasso or Native methods
                            UserInfo.profileURL = profilePicUrl;
                            Log.d("profile", UserInfo.profileURL);
                        }
                    }
                }).executeAsync();

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        UHttps body = new UHttps();
                        body.initBody();
                        body.addParameter("open_id", loginResult.getAccessToken().getUserId());
                        body.addParameter("open_id_type", String.valueOf(UserInfo.TYPE_FACEBOOK));
                        final JSONObject response = UHttps.okHttp(UHttps.IP + "/v1/login", body.getBody());

                        UPreferences.setStringPref(SplashActivity.this, UserInfo.PREF_USER, UserInfo.PREF_SUB_USER_APIKEY,response.optString("api_key"));
                        UserInfo.apiKey = UPreferences.getStringPref(SplashActivity.this, UserInfo.PREF_USER, UserInfo.PREF_SUB_USER_APIKEY,"");
                        UserInfo.isSeller = UPreferences.getBooleanPref(SplashActivity.this, UserInfo.PREF_USER, UserInfo.PREF_SUB_IS_SELLER, false);
                        Intent intent2 = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent2);
                        finish();
                    }
                });

                thread.setDaemon(true);
                thread.start();


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

        if (AccessToken.getCurrentAccessToken()== null){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }else{
            LoginManager.getInstance().logInWithReadPermissions(SplashActivity.this, Arrays.asList("public_profile", "user_friends"));
        }
    }

    private void initKakao() {
        // 세션 콜백 추가
        isSnsLogin = true;
        kakaoCallback = new UKakaoLogin.SessionCallback();
        Session.getCurrentSession().addCallback(kakaoCallback);
        boolean isCheck = Session.getCurrentSession().checkAndImplicitOpen();
        Log.d("kakao", isCheck +"");
        if (!isCheck){
            initFacebook();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        facebookCallback.onActivityResult(requestCode, resultCode, data);
    }
}
