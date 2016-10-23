package ga.washmose.mose.User;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import java.util.ArrayList;

import ga.washmose.mose.ItemData;
import ga.washmose.mose.R;
import ga.washmose.mose.seller.SellerDetail;

/**
 * Created by leejaebeom on 2016. 10. 8..
 */

public class UserLaundryFragment extends Fragment{

    userLaundryAdapter adapter;
    ArrayList<UserLaundryItem> sellers;
    private static String ARG_SELLERS = "sellers";
    JSONArray sellersJSON;
    public UserLaundryFragment() {
    }

    public static UserLaundryFragment newInstance(JSONArray sellers) {
        UserLaundryFragment fragment = new UserLaundryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SELLERS, sellers.toString());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            try {
                sellersJSON = new JSONArray(getArguments().getString(ARG_SELLERS));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_laundry, container, false);

        ListView list = (ListView)rootView.findViewById(R.id.user_laundry_list);

        sellers = new ArrayList<>();
//        sellers.add(new UserLaundryItem("https://api2.washmose.ga/data/test/0.jpeg", "주부 5년차", "수내동","속옷 3, 겉옷 5"));
//        sellers.add(new UserLaundryItem("https://api2.washmose.ga/data/test/3.jpeg", "바이오 컴퓨터 세탁소", "수내동","속옷 2, 겉옷 3"));
//        sellers.add(new UserLaundryItem("https://api2.washmose.ga/data/test/2.jpeg", "자취생", "수내동","속옷 1"));
//        sellers.add(new UserLaundryItem("https://api2.washmose.ga/data/test/1.jpeg", "트레벨 오피스텔 가능, 수선 가능", "수내동","속옷, 상의, 하의"));
//        sellers.add(new UserLaundryItem("https://api2.washmose.ga/data/test/4.jpeg", "보보스 오피스텔 4층", "수내동","속옷 10, 겨울 옷"));

        for (int i = 0; i< sellersJSON.length(); i++){
            JSONObject seller = sellersJSON.optJSONObject(i);

            ArrayList<ItemData> items = new ArrayList<ItemData>();
            items.add(new ItemData("Url","티셔츠 (not)", 3, 2000, "세탁 진행중.."));
            items.add(new ItemData("Url","남성 속옷 하의 (not)", 8, 1000, "세탁 안함"));

            sellers.add(new UserLaundryItem(seller.optString("header_image"), seller.optString("title"), seller.optString("address"),"속옷 3, 겉옷 5", items, seller.optInt("latitude"), seller.optInt("longitude")));

        }
        adapter = new userLaundryAdapter(sellers, getContext());

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), SellerDetail.class);
                intent.putExtra("seller", sellers.get(position));
                startActivity(intent);
            }
        });
        return rootView;
    }

    public class userLaundryAdapter extends BaseAdapter {

        ArrayList<UserLaundryItem> items;
        Context context;
        public userLaundryAdapter(ArrayList<UserLaundryItem> items, Context context) {
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
                convertView = inflater.inflate(R.layout.item_user_laundry,parent, false);
                holder = new ViewHolder();
                holder.profile = (ImageView) convertView.findViewById(R.id.list_item_profile);
                holder.name = (TextView) convertView.findViewById(R.id.list_item_name);
                holder.location = (TextView)convertView.findViewById(R.id.list_item_location);
                holder.summary = (TextView)convertView.findViewById(R.id.list_item_summary);


                convertView.setTag(holder);
            }else{
                holder = (ViewHolder)convertView.getTag();
            }

            holder.name.setText(items.get(position).name);
            holder.location.setText(items.get(position).location);
            holder.summary.setText(items.get(position).summary);
            Glide.with(context).load(items.get(position).imageUrl).into(holder.profile);

            return convertView;
        }

        class ViewHolder{
            ImageView profile;
            TextView name, location, summary;
        }
    }
}
