package ga.washmose.mose;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;
import ga.washmose.mose.Util.UDate;
import ga.washmose.mose.Util.UHttps;

/**
 * Created by leejaebeom on 2016. 10. 16..
 */

public class OrderInfo extends AppCompatActivity {
    protected OrderData orderData;
    ImageView firstStep, secondStep, thirdStep, forthStep, fifthStep, between12, between23, between34, between45;
    TextView code, collectionDate, completeDate, address;
    ViewGroup itemView;
    protected Button customButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order_info);
        Intent intent = getIntent();
        orderData = (OrderData) intent.getSerializableExtra("orderData");
        firstStep = (ImageView) findViewById(R.id.first_step);
        secondStep = (ImageView) findViewById(R.id.second_step);
        thirdStep = (ImageView) findViewById(R.id.thrid_step);
        forthStep = (ImageView) findViewById(R.id.forth_step);
        fifthStep = (ImageView) findViewById(R.id.fifth_step);

        between12 = (ImageView) findViewById(R.id.between_step_12);
        between23 = (ImageView) findViewById(R.id.between_step_23);
        between34 = (ImageView) findViewById(R.id.between_step_34);
        between45 = (ImageView) findViewById(R.id.between_step_45);

        code = (TextView) findViewById(R.id.order_info_code);
        collectionDate = (TextView) findViewById(R.id.order_info_collection_date);
        completeDate = (TextView) findViewById(R.id.order_info_complete_date);
        address = (TextView) findViewById(R.id.order_info_address);
        itemView = (ViewGroup) findViewById(R.id.order_info_item_list);
        customButton = (Button) findViewById(R.id.order_info_button);

        initProgress();
        initView();
        initItems();
    }

    public void initProgress(){
        switch (orderData.progress){
            case 0:
                firstStep.setImageResource(R.drawable.ic_progress);
                between12.setImageResource(R.drawable.ic_progress_circle_bar);
                between12.setBackgroundColor(0xffffff);
                break;
            case 1:
                secondStep.setImageResource(R.drawable.ic_progress);
                between23.setImageResource(R.drawable.ic_progress_circle_bar);
                between23.setBackgroundColor(0xffffff);
                break;
            case 2:
                thirdStep.setImageResource(R.drawable.ic_progress);
                between34.setImageResource(R.drawable.ic_progress_circle_bar);
                between34.setBackgroundColor(0xffffff);
                break;
            case 3:
                forthStep.setImageResource(R.drawable.ic_progress);
                between45.setImageResource(R.drawable.ic_progress_circle_bar);
                between45.setBackgroundColor(0xffffff);
                break;
            case 4:
                fifthStep.setImageResource(R.drawable.ic_progress);
                break;
        }
    }

    public void initView(){
        code.setText(orderData.code + "");

        String collectionDateString = UDate.getDateFormat(orderData.collectionDate.getTime());
        collectionDate.setText(collectionDateString);

        String completeDateString = UDate.getDateFormat(orderData.completeDate.getTime());
        completeDate.setText(completeDateString);

        address.setText(orderData.address);
    }

    public void initItems(){
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i< orderData.items.size(); i++){
            View view = inflater.inflate(R.layout.item_order_info_items, itemView, false);
            CircleImageView icon = (CircleImageView) view.findViewById(R.id.order_info_item_icon);
            TextView name = (TextView)view.findViewById(R.id.order_info_item_name);
            TextView count = (TextView)view.findViewById(R.id.order_info_item_count);
            TextView progress = (TextView)view.findViewById(R.id.order_info_item_progress);
            Button reButton = (Button) view.findViewById(R.id.order_info_item_rebtn);

            Glide.with(this).load(UHttps.IP + orderData.items.get(i).iconURL).into(icon);
            name.setText(orderData.items.get(i).name);
            count.setText(orderData.items.get(i).minimum + " 벌");
            progress.setText(orderData.items.get(i).progress);
            if (orderData.progress == 0){
                progress.setVisibility(View.GONE);
                reButton.setVisibility(View.GONE);
            }
            itemView.addView(view);
        }
    }

}
