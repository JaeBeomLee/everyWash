package ga.washmose.mose.User;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import ga.washmose.mose.ItemData;
import ga.washmose.mose.OrderData;
import ga.washmose.mose.R;
import ga.washmose.mose.Util.UDate;
import ga.washmose.mose.Util.UHttps;
import ga.washmose.mose.main.MainActivity;

public class UserOrderRequestFragment extends Fragment {
    sellerRequestAdapter adapter;
    private static final String ARG_ORDERS = "orders";
    ArrayList<OrderData> Orders =new ArrayList<>();
    JSONArray requestOrders;
    String []summary;
    public UserOrderRequestFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static UserOrderRequestFragment newInstance(JSONArray orders) {
        UserOrderRequestFragment fragment = new UserOrderRequestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ORDERS, orders.toString());
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //items의 배열이 비어 있어서 아무것도 안나올 것으로 추정
//        items.add(new SellerRequestItem("http://www.vipstudio.co.kr/bbs/data/gallery01/남자증명01.jpg", "바이오 컴퓨터 세탁소", "분당구 수내동 10-1 트라펠리스 910호", "10월 3일 세탁 요청", "속옷 30, 상의 2", false));

        if (getArguments() != null) {
            try {
                requestOrders = new JSONArray(getArguments().getString(ARG_ORDERS));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i< requestOrders.length(); i++){
            summary = new String[requestOrders.length()];
            summary[i] = "";
            JSONObject order = requestOrders.optJSONObject(i);
            Calendar requestDate = null, collectionDate = null, completeDate = null;
            try {
                requestDate = Calendar.getInstance();
                requestDate.setTime(UDate.getDate(order.optString("request_date")));

                collectionDate = Calendar.getInstance();
                collectionDate.setTime(UDate.getDate(order.optString("send_date")));

                completeDate = Calendar.getInstance();
                completeDate.setTime(UDate.getDate(order.optString("finish_date")));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            int count = 0;
            String oldItemName = null;
            ArrayList<ItemData> items = new ArrayList<ItemData>();
            JSONArray itemsJSON = order.optJSONArray("items");
            if (itemsJSON != null){
                for (int j = 0; j< itemsJSON.length(); j++){
                    JSONObject item = itemsJSON.optJSONObject(j);
                    //{"item_code":"1","order_code":"3","goods_code":"1","goods_name":"남성 속옷 (하의)","goods_image":"\/data\/imgs\/goods\/1_man_underwear_down.png","unit_amount":"3","price":"1000","amount":"2","is_color":"0","item_status":"0","total_price":"2000"}
                    items.add(new ItemData(item.optString("goods_name"), item.optString("goods_image"), item.optInt("unit_amount"), item.optInt("price"), item.optInt("item_code"), item.optInt("goods_code")));

                    String itemName = item.optString("goods_name").substring(0, 2);
                    if (oldItemName != null){
                        if (oldItemName.equals(itemName)){
                            count += item.optInt("unit_amount");
                        }else {
                            summary[i]+= " " + count;
                            count = item.optInt("unit_amount");
                        }

                        if (j != itemsJSON.length()-1){
                            summary[i] += ", " + itemName;
                        }else {
                            summary[i] += " " + count;
                        }
                    }else{
                        summary[i] = summary[i] + itemName;
                        oldItemName = itemName;
                        count = item.optInt("unit_amount");
                    }
                }
            }
//            items.add(new ItemData("Url","티셔츠 (not)", 3, 2000, "세탁 진행중.."));
//            items.add(new ItemData("Url","남성 속옷 하의 (not)", 8, 1000, "세탁 안함"));

            JSONObject seller = order.optJSONObject("seller");
            Orders.add(new OrderData(seller.optString("header_image"),seller.optString("profile_image"),seller.optString("seller_name"), seller.optString("title"), summary[i], requestDate, true, order.optInt("order_code"),
                    order.optInt("order_status"), order.optString("phone") + "data", collectionDate, completeDate, seller.optString("address"),  items));
        }
        adapter = new sellerRequestAdapter(Orders, getContext());
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_seller_order_request, container, false);
        ListView list = (ListView)rootView.findViewById(R.id.seller_order_request_list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), UserOrderRequestActivity.class);
                intent.putExtra("orderData", (Parcelable) Orders.get(position));
                startActivityForResult(intent, MainActivity.UserOrderRequestActivity);
            }
        });
        return rootView;
    }

    public class sellerRequestAdapter extends BaseAdapter {

        ArrayList<OrderData> items;
        Context context;
        public sellerRequestAdapter(ArrayList<OrderData> items, Context context) {
            this.items = items;
            this.context = context;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(convertView == null){
                convertView = inflater.inflate(R.layout.item_seller_request, parent, false);
                holder = new ViewHolder();
                holder.profile = (ImageView) convertView.findViewById(R.id.list_item_profile);
                holder.name = (TextView) convertView.findViewById(R.id.list_item_name);
                holder.location = (TextView)convertView.findViewById(R.id.list_item_location);
                holder.summary = (TextView)convertView.findViewById(R.id.list_item_summary);
                holder.request = (TextView)convertView.findViewById(R.id.list_item_request);
                holder.author = (ViewGroup) convertView.findViewById(R.id.list_item_author);

                convertView.setTag(holder);
            }else{
                holder = (ViewHolder)convertView.getTag();
            }

            holder.name.setText(items.get(position).title);
            holder.location.setText(items.get(position).address);
            holder.summary.setText(items.get(position).summary);
            Glide.with(context).load(UHttps.IP + items.get(position).headerImageUrl).into(holder.profile);
            String request = UDate.getSimpleDateFormat(items.get(position).request.getTime()) + " 세탁 요청";
            holder.request.setText(request);
            if (items.get(position).author){
                holder.author.setVisibility(View.VISIBLE);
            }else{
                holder.author.setVisibility(View.INVISIBLE);

            }

            return convertView;
        }

        class ViewHolder{
            ImageView profile;
            TextView name, location, summary, request;
            ViewGroup author;
        }
    }
}
