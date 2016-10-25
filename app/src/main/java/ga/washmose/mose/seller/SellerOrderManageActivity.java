package ga.washmose.mose.seller;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
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

public class SellerOrderManageActivity extends OrderInfo{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setCustomButton();
        customButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initStatusChange();
            }
        });
    }

    public void setCustomButton(){
        if (orderData.progress == 1){
            customButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            customButton.setText("전달 받음으로  변경");
        }else if (orderData.progress == 2){
            customButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            customButton.setText("세탁 중으로 변경");
        }else if (orderData.progress == 3){
            customButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            customButton.setText("세탁 완료하기");
        }else if (orderData.progress == 4){
            customButton.setVisibility(View.GONE);
        }
    }

    public void initStatusChange(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int code;
                String url = UHttps.IP + "/v1/orders/" +orderData.code+ "/status/" + (orderData.progress+1);
                JSONObject res = UHttps.okHttpPut(url, UserInfo.apiKey);
                code = res.optInt("code");
                Log.d("SORA res", res.toString());
                if (code == 200){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            UDialog.setDialog(SellerOrderManageActivity.this, "상태가 변경되었습니다.", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent();
                                    intent.putExtra("pageNum", 1);
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
