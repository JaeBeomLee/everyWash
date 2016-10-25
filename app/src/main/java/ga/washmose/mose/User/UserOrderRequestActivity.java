package ga.washmose.mose.User;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import org.json.JSONObject;

import ga.washmose.mose.OrderInfo;
import ga.washmose.mose.UserInfo;
import ga.washmose.mose.Util.UDialog;
import ga.washmose.mose.Util.UHttps;

/**
 * Created by leejaebeom on 2016. 10. 16..
 */

public class UserOrderRequestActivity extends OrderInfo {

    Handler handler = new Handler();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setCustomButton();
        customButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCancelOrder();
            }
        });
    }

    public void setCustomButton(){
        customButton.setText("취소 하기");
    }

    public void setCancelOrder(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                String URLString = UHttps.IP + "/v1/orders/" +  orderData.code;
                JSONObject response = UHttps.okHttpDelete(URLString, UserInfo.apiKey);
                if (response.optInt("code") == 200){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            DialogInterface.OnClickListener positive = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent();
                                    intent.putExtra("pageNum", -1);
                                    setResult(RESULT_OK, intent);
                                    finish();
                                }
                            };

                            UDialog.setDialog(UserOrderRequestActivity.this, "취소되었습니다.", positive);
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
