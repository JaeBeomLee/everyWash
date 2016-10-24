package ga.washmose.mose;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Random;

import ga.washmose.mose.Util.UDialog;
import ga.washmose.mose.Util.UHttps;
import ga.washmose.mose.main.MainActivity;

public class RegisterSellerActivity extends AppCompatActivity {

    CheckBox mon, tue, wed, thu, fri, sat, sun;
    CheckBox manUp, manDown, womanUp, womanDown;
    EditText title;
    Button register;
    String abilityDate = "", titleStr, openTime, closeTime;
    double latitude, longitude;
    double latitudeInterval, longitudeInterval;
    Random random = new Random();
    int itemCount, check = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_seller);
        mon = (CheckBox)findViewById(R.id.mon);
        tue = (CheckBox)findViewById(R.id.tue);
        wed = (CheckBox)findViewById(R.id.wed);
        thu = (CheckBox)findViewById(R.id.thu);
        fri = (CheckBox)findViewById(R.id.fri);
        sat = (CheckBox)findViewById(R.id.sat);
        sun = (CheckBox)findViewById(R.id.sun);

        manUp = (CheckBox)findViewById(R.id.man_underwear_up);
        manDown = (CheckBox)findViewById(R.id.man_underwear_down);
        womanUp = (CheckBox)findViewById(R.id.woman_underwear_up);
        womanDown = (CheckBox)findViewById(R.id.woman_underwear_down);

        title = (EditText)findViewById(R.id.register_title);
        register = (Button) findViewById(R.id.register_btn);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!title.getText().toString().equals("")){
                    setRegister();
                }else{
                    UDialog.setDialog(RegisterSellerActivity.this, "타이틀을 지정해 주세요");
                }
            }
        });
    }

    private void setRegister(){
        if (mon.isChecked()){
            abilityDate += "월";
        }
        if (tue.isChecked()){
            abilityDate += ",화";
        }

        if (wed.isChecked()){
            abilityDate += ",수";
        }

        if (thu.isChecked()){
            abilityDate += ",목";
        }

        if (fri.isChecked()){
            abilityDate += ",금";
        }

        if (sat.isChecked()){
            abilityDate += ",토";
        }

        if (sun.isChecked()){
            abilityDate += ",일";
        }

        if (womanDown.isChecked()){
            ++itemCount;
        }
        if (womanUp.isChecked()){
            ++itemCount;
        }
        if (manUp.isChecked()){
            ++itemCount;
        }
        if (manDown.isChecked()){
            ++itemCount;
        }

        if (manUp.isChecked()){
            initItems(0, 500);
        }

        if (manDown.isChecked()){
            initItems(1, 300);
        }

        if (womanUp.isChecked()){
            initItems(2, 700);
        }

        if (womanDown.isChecked()){
            ++itemCount;
            initItems(3, 300);
        }
        titleStr = title.getText().toString();
        openTime = "00:00:00";
        closeTime = "11:59:59";

        latitude = 37.372950;
        longitude = 127.116572;
        String pattern = "###.######";
        final DecimalFormat decimalFormat = new DecimalFormat(pattern);
        latitudeInterval = random.nextDouble()/100;
        longitudeInterval = random.nextDouble()/200;

        latitude += latitudeInterval;
        longitude += longitudeInterval;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                UHttps body = new UHttps();
                body.initBody();
                body.addParameter("title", titleStr);
                body.addParameter("latitude", decimalFormat.format(latitude));
                body.addParameter("longitude", decimalFormat.format(longitude));
                body.addParameter("ability_date", abilityDate);
                body.addParameter("open_time", openTime);
                body.addParameter("close_time", closeTime);
                JSONObject res = UHttps.okHttp(UHttps.IP +"/v1/seller",UserInfo.apiKey, body.getBody());
                Log.d("RSA res", res.toString());
            }
        });
        thread.setDaemon(true);
        thread.start();

    }

    private void initItems(final int goodsCode, final int price){
        check++;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                UHttps body = new UHttps();
                body.initBody();
                body.addParameter("goods_code", String.valueOf(goodsCode));
                body.addParameter("price", String.valueOf(price));
                body.addParameter("unit_amount", "1");
                JSONObject res = UHttps.okHttp(UHttps.IP +"/v1/prices",UserInfo.apiKey, body.getBody());
                Log.d("RSA res", res.toString());

                Intent intent = new Intent(RegisterSellerActivity.this, MainActivity.class);
                UserInfo.isSeller = !UserInfo.isSeller;
                startActivity(intent);
                finish();
            }
        });
        thread.setDaemon(true);
        thread.start();
    }
}
