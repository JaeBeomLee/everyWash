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

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
    Calendar collection, complete;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order_info);
        Intent intent = getIntent();
        orderData = intent.getParcelableExtra("orderData");


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

        initData();
        initProgress();
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

        if (orderData.collectionDate != null){
            String collectionDateString = UDate.getDateFormat(orderData.collectionDate.getTime());
            collectionDate.setText(collectionDateString);
        }

        if (orderData.completeDate != null){
            String completeDateString = UDate.getDateFormat(orderData.completeDate.getTime());
            completeDate.setText(completeDateString);
        }

        address.setText(orderData.address);
    }

    public void initItems(){
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (orderData.items != null){
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

    private void initData(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int code;
                JSONObject res;
                if (UserInfo.isSeller){
                    res = UHttps.okHttp(UHttps.IP+"/v1/orders/s/"+ orderData.code, UserInfo.apiKey);
                }else{
                    res = UHttps.okHttp(UHttps.IP+"/v1/orders/"+ orderData.code, UserInfo.apiKey);
                }
                code = res.optInt("code");
                if (code == 200) {
                    JSONArray orders = res.optJSONArray("order");
                    collection = Calendar.getInstance();
                    for (int i = 0; i < orders.length(); i++) {
                        JSONObject order = orders.optJSONObject(i);
                        try {
                            Date date = UDate.getDate(order.optString("send_date"));
                            collection.setTime(date);
                            complete = (Calendar) collection.clone();
                            complete.set(Calendar.DAY_OF_MONTH, complete.get(Calendar.DAY_OF_MONTH) + 4);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        orderData.collectionDate = collection;
                        orderData.completeDate = complete;

                        JSONObject user = order.optJSONObject("consumer");
                        JSONArray items = order.optJSONArray("items");
                        ArrayList<ItemData> itemsList = new ArrayList<>();
                        if (items != null){
                            for (int j = 0; j < items.length(); j++) {
                                JSONObject item = items.optJSONObject(j);
                                itemsList.add(new ItemData(item.optString("goods_name"), item.optString("goods_image"), item.optInt("unit_amount"), item.optInt("total_price"), item.optInt("item_code"), item.optInt("goods_code")));
                            }

                        }

                        orderData.items = itemsList;
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initView();
                            initItems();
                        }
                    });
                }
            }
        });

        thread.setDaemon(true);
        thread.start();
    }

}
