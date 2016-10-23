package ga.washmose.mose.pre;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import ga.washmose.mose.R;
import ga.washmose.mose.UserInfo;
import ga.washmose.mose.Util.UHttps;
import ga.washmose.mose.Util.UPreferences;
import ga.washmose.mose.main.MainActivity;
import okhttp3.FormBody;
import okhttp3.RequestBody;

public class SignUpActivity extends AppCompatActivity {

    EditText name, phone, sex, address, email;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Intent intent = getIntent();
        name = (EditText)findViewById(R.id.sign_up_name);
        phone = (EditText)findViewById(R.id.sign_up_phone);
        sex = (EditText)findViewById(R.id.sign_up_sex);
        address = (EditText)findViewById(R.id.sign_up_address);
        email = (EditText)findViewById(R.id.sign_up_email);
        btn = (Button) findViewById(R.id.sign_up_btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        RequestBody body;
                        FormBody.Builder builder = new FormBody.Builder()
                                .add("open_id", UserInfo.openID)
                                .add("open_id_type", String.valueOf(UserInfo.loginType))
                                .add("user_name", name.getText().toString())
                                .add("phone", phone.getText().toString())
                                .add("sex", sex.getText().toString())
                                .add("email", email.getText().toString());
                        if (!address.getText().toString().equals("")){
                            builder.add("address", address.getText().toString());
                        }

                        body = builder.build();

                        JSONObject res = UHttps.okHttp(UHttps.IP+"/v1/register", body);
                        Log.d("SUA res", res.toString());
                        if (res.optInt("code") == 201){
                            UPreferences.setStringPref(SignUpActivity.this, UserInfo.PREF_USER, UserInfo.PREF_SUB_USER_APIKEY, res.optString("api_key"));
                            UserInfo.apiKey = UPreferences.getStringPref(SignUpActivity.this, UserInfo.PREF_USER, UserInfo.PREF_SUB_USER_APIKEY,"");
                            UserInfo.isSeller = false;
                            UserInfo.isSeller = false;
                            Intent intent2 = new Intent(SignUpActivity.this, MainActivity.class);
                            startActivity(intent2);
                        }
                    }
                });

                thread.setDaemon(true);
                thread.start();
            }
        });
    }
}
