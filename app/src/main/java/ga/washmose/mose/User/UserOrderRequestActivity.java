package ga.washmose.mose.User;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import ga.washmose.mose.OrderInfo;

/**
 * Created by leejaebeom on 2016. 10. 16..
 */

public class UserOrderRequestActivity extends OrderInfo {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setCustomButton();
        customButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserOrderRequestActivity.this, "버튼을 누름", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setCustomButton(){
        customButton.setText("취소 하기");
    }
}
