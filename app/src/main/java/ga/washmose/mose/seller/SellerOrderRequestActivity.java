package ga.washmose.mose.seller;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONObject;

import ga.washmose.mose.OrderInfo;
import ga.washmose.mose.R;
import ga.washmose.mose.UserInfo;
import ga.washmose.mose.Util.UDialog;
import ga.washmose.mose.Util.UHttps;

/**
 * Created by leejaebeom on 2016. 10. 16..
 */

public class SellerOrderRequestActivity extends OrderInfo{

    Button customCancelButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setCustomButton();
        customButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initRequestAccept();
            }
        });

        customCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initRequestCancel();
            }
        });
    }

    public void setCustomButton(){
        customCancelButton = (Button)findViewById(R.id.order_info_cancel_button);
        customCancelButton.setVisibility(View.VISIBLE);
        customButton.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
        customButton.setText("요청 수락");
        customCancelButton.setText("요청 거절");
    }


    public void initRequestAccept(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int code;
                String url = UHttps.IP + "/v1/orders/" +orderData.code+ "/status/" + 1;
                JSONObject res = UHttps.okHttpPut(url, UserInfo.apiKey);
                code = res.optInt("code");
                Log.d("SORA res", res.toString());
                if (code == 200){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            UDialog.setDialog(SellerOrderRequestActivity.this, "요청이 수락되었습니다.", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent();
                                    intent.putExtra("pageNum", 0);
                                    setResult(RESULT_OK, intent);
                                    finish();
                                }
                            });
                        }
                    });
                }
            }
        });

        thread.setDaemon(true);
        thread.start();
    }

    public void initRequestCancel(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int code;
                String url = UHttps.IP + "/v1/orders/s/" +orderData.code;
                JSONObject res = UHttps.okHttpDelete(url, UserInfo.apiKey);
                code = res.optInt("code");
                Log.d("SORA res", res.toString());
                if (code == 200){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            UDialog.setDialog(SellerOrderRequestActivity.this, "요청이 거절되었습니다.", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent();
                                    intent.putExtra("pageNum", 0);
                                    setResult(RESULT_OK, intent);
                                    finish();
                                }
                            });
                        }
                    });

                }
            }
        });

        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
    }
}
