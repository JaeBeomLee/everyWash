package ga.washmose.mose.seller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ga.washmose.mose.OrderInfo;
import ga.washmose.mose.R;

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
                Toast.makeText(SellerOrderRequestActivity.this, "버튼을 누름", Toast.LENGTH_SHORT).show();
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
}
