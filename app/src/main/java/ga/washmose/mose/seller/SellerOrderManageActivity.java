package ga.washmose.mose.seller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import ga.washmose.mose.OrderInfo;
import ga.washmose.mose.R;

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
                Toast.makeText(SellerOrderManageActivity.this, "버튼을 누름", Toast.LENGTH_SHORT).show();
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
}
