package ga.washmose.mose.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.kakao.auth.ISessionCallback;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;

import org.json.JSONObject;

import ga.washmose.mose.UserInfo;
import ga.washmose.mose.main.MainActivity;
import ga.washmose.mose.pre.LoginActivity;
import ga.washmose.mose.pre.SignUpActivity;
import ga.washmose.mose.pre.SplashActivity;

/**
 * Created by leejaebeom on 2016. 10. 23..
 */

public class UKakaoLogin {
    static Handler handler = new Handler();
    static Context context;
    static boolean isLogin = false;

    public UKakaoLogin(Context context) {
        this.context = context;
    }

    public static class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            if (!isLogin){
                kakaoRequestMe(context);
                isLogin = true;
            }
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
//            Log.d("SessionCallback LA", exception.getMessage());
        }
    }

    protected static void kakaoRequestMe(final Context context) {
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
                        if (context.getClass().getName().equals("ga.washmose.mose.pre.SplashActivity")){
                            UHttps.initBody();
                            UHttps.addParameter("open_id", String.valueOf(result.getId()));
                            UHttps.addParameter("open_id_type", String.valueOf(UserInfo.TYPE_KAKAO));

                            final JSONObject response = UHttps.okHttp(UHttps.IP + "/login", UHttps.getBody());

                            UPreferences.setStringPref(context, UserInfo.PREF_USER, UserInfo.PREF_SUB_USER_APIKEY, response.optString("api_key"));
                            UserInfo.apiKey = UPreferences.getStringPref(context, UserInfo.PREF_USER, UserInfo.PREF_SUB_USER_APIKEY,"");
                            UserInfo.isSeller = false;
                            isLogin = false;

                            Intent intent = new Intent(context, MainActivity.class);
                            context.startActivity(intent);
                            ((Activity)context).finish();
                        }else {
                            final int code;
                            Log.d("LA", "open_id: " + String.valueOf(result.getId()));
                            UHttps.initBody();
                            UHttps.addParameter("open_id", String.valueOf(result.getId()));
                            UHttps.addParameter("open_id_type", String.valueOf(UserInfo.TYPE_KAKAO));

                            final JSONObject response = UHttps.okHttp(UHttps.IP + "/login", UHttps.getBody());
                            code = response.optInt("code");
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    switch (code) {
                                        case 400:
                                            //찾을 수 없음 (에러)
                                            UDialog.setDialog(context, "관리자에게 문의 해주세요! \n코드 : " + code);
                                            break;
                                        case 401:   //인증 받지 않은 요청. (회원 가입으로 감)
                                            UserInfo.openID = String.valueOf(result.getId());
                                            UserInfo.loginType = UserInfo.TYPE_KAKAO;
                                            Intent intent = new Intent(context, SignUpActivity.class);
                                            context.startActivity(intent);
                                            ((Activity)context).finish();
                                            break;
                                        case 500:
                                            //내부 서버 오류

                                            break;
                                        case 200:   //로그인
                                            UPreferences.setStringPref(context, UserInfo.PREF_USER, UserInfo.PREF_SUB_USER_APIKEY,response.optString("api_key"));
                                            UserInfo.apiKey = UPreferences.getStringPref(context, UserInfo.PREF_USER, UserInfo.PREF_SUB_USER_APIKEY,"");
                                            UserInfo.isSeller = false;
                                            isLogin = false;
                                            Intent intent2 = new Intent(context, MainActivity.class);
                                            context.startActivity(intent2);
                                            ((Activity)context).finish();
                                            break;
                                    }
                                }
                            });
                            Log.d("LA", "response : " + response);
                        }
                    }
                });
                thread.setDaemon(true);
                thread.start();
            }
        });
    }
}
