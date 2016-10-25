package ga.washmose.mose;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import ga.washmose.mose.User.UserLaundryItem;
import ga.washmose.mose.Util.UDialog;
import ga.washmose.mose.Util.UHttps;

public class OrderActivity extends AppCompatActivity {
    final int REQ_ADD_ITEMS = 1;
    Button dateSelect, sendOrderBtn;

    ViewGroup itemsView;
    int year, month, day;

    UserLaundryItem seller;
    String selectDate = "";
    int isFinish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Intent intent = getIntent();
        seller = (UserLaundryItem) intent.getSerializableExtra("seller");

        dateSelect = (Button) findViewById(R.id.order_date_select);
        sendOrderBtn = (Button) findViewById(R.id.order_send);
        itemsView = (ViewGroup) findViewById(R.id.order_item_layout);
//        addItems = (Button)findViewById(R.id.order_add_items);

        initItemsView();
        dateSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GregorianCalendar calendar = new GregorianCalendar();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day= calendar.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(OrderActivity.this, dateSetListener, year, month, day).show();
            }
        });

        sendOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!selectDate.equals("")){
                    sendOrder();
                }else{
                    UDialog.setDialog(OrderActivity.this, "수거 날짜를 지정해 주세요");
                }
            }
        });
//        addItems.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(OrderActivity.this, OrderItemList.class);
//                startActivityForResult(intent, REQ_ADD_ITEMS);
//
//            }
//        });

    }
    public void initItemsView(){
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ArrayList<ItemData> items = seller.items;

        for (int i = 0; i< items.size(); i++){
            final ViewHolder holder = new ViewHolder();
            View view = inflater.inflate(R.layout.item_order_items_list, itemsView, false);
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.count = (TextView) view.findViewById(R.id.count);
            holder.price = (TextView) view.findViewById(R.id.price);
            holder.plus = (ImageButton) view.findViewById(R.id.plus);
            holder.minus = (ImageButton) view.findViewById(R.id.minus);

            holder.name.setText(items.get(i).name);
            holder.price.setText(items.get(i).price + "원");
            holder.count.setText("0");
            final int finalI = i;
            holder.plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ++items.get(finalI).count;
                    holder.count.setText(items.get(finalI).count +"");
                }
            });

            holder.minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (items.get(finalI).count > 0){
                        --items.get(finalI).count;
                    }else{
                        items.get(finalI).count = 0;
                    }
                    holder.count.setText(items.get(finalI).count + "");
                }
            });
            itemsView.addView(view);
        }

    }
    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {


            String msg = year + "-" + (monthOfYear+1) + "-" + dayOfMonth + " 00:00:00";
            selectDate = msg;
//            String msg = String.format("%d / %d / %d", year,monthOfYear+1, dayOfMonth);
            dateSelect.setText(msg);
//            Toast.makeText(OrderActivity.this, msg, Toast.LENGTH_SHORT).show();

        }
    };

    class ViewHolder{
        TextView name, price, count;
        ImageButton plus, minus;
    }

    public void sendOrder(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i<seller.items.size(); i++){
                    ArrayList<ItemData> item = seller.items;
                    if (item.get(i).count > 0){
                        ++isFinish;
                    }
                }
                UHttps body = new UHttps();
                body.initBody();
                body.addParameter("seller_id", String.valueOf(seller.id));
                body.addParameter("auto_accept", String.valueOf(0));
                body.addParameter("send_date", selectDate);
                String url = UHttps.IP + "/v1/orders";
                JSONObject res = UHttps.okHttp(url, UserInfo.apiKey, body.getBody());
                Log.d("OA", res.toString());
                if (res.optInt("code") == 201){
                    int count = 0;
                    for (int i = 0; i<seller.items.size(); i++){
                        ArrayList<ItemData> item = seller.items;
                        if (item.get(i).count > 0){
                            ++count;
                            sendItem(res.optInt("order_code"), item.get(i).priceCode, item.get(i).count, count);
                        }
                    }
                }
            }
        });
        thread.setDaemon(true);
        thread.start();

    }

    public void sendItem(final int orderCode, final int priceCode, final int amount, final int check){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                UHttps body = new UHttps();
                body.initBody();
                body.addParameter("price_code", String.valueOf(priceCode));
                body.addParameter("amount", String.valueOf(amount));
                String url = UHttps.IP + "/v1/orders/" + orderCode + "/items";
                JSONObject res = UHttps.okHttp(url, UserInfo.apiKey, body.getBody());
                Log.d("OA item", res.toString());

                if (isFinish == check){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            UDialog.setDialog(OrderActivity.this, "주문이 완료되었습니다.", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent();
                                    intent.putExtra("pageNum", -1);
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
