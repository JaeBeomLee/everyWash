package ga.washmose.mose.User;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Calendar;

import ga.washmose.mose.ItemData;
import ga.washmose.mose.OrderData;
import ga.washmose.mose.R;
import ga.washmose.mose.seller.SellerOrderManageActivity;
import ga.washmose.mose.seller.SellerRequestItem;

public class UserOrderRequestFragment extends Fragment {
    sellerRequestAdapter adapter;
    ArrayList<SellerRequestItem> items =new ArrayList<>();
    OrderData data;

    public UserOrderRequestFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static UserOrderRequestFragment newInstance() {
        UserOrderRequestFragment fragment = new UserOrderRequestFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //items의 배열이 비어 있어서 아무것도 안나올 것으로 추정
        items.add(new SellerRequestItem("http://www.vipstudio.co.kr/bbs/data/gallery01/남자증명01.jpg", "바이오 컴퓨터 세탁소", "분당구 수내동 10-1 트라펠리스 910호", "10월 3일 세탁 요청", "속옷 30, 상의 2", false));

        data = new OrderData();
        adapter = new sellerRequestAdapter(items, getContext());
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
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
                data.code = 1111;
                data.progress = 0;
                Calendar collection = Calendar.getInstance();
                collection.set(Calendar.YEAR, 2016);
                collection.set(Calendar.MONTH, 10);
                collection.set(Calendar.DAY_OF_MONTH, 3);
                data.collectionDate = collection;
                data.completeDate = collection;
                data.address = items.get(position).address;

                ArrayList<ItemData> items = new ArrayList<ItemData>();
                items.add(new ItemData("Url","티셔츠", 3, 2000, "세탁 진행중.."));
                items.add(new ItemData("Url","남성 속옷 하의", 8, 1000, "세탁 안함"));
                data.items = items;
                Intent intent = new Intent(getContext(), UserOrderRequestActivity.class);
                intent.putExtra("orderData", data);
                startActivity(intent);
            }
        });
        return rootView;
    }

    public class sellerRequestAdapter extends BaseAdapter {

        ArrayList<SellerRequestItem> items;
        Context context;
        public sellerRequestAdapter(ArrayList<SellerRequestItem> items, Context context) {
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

            holder.name.setText(items.get(position).name);
            holder.location.setText(items.get(position).address);
            holder.summary.setText(items.get(position).summary);
            Glide.with(context).load(items.get(position).imageUrl).into(holder.profile);
            holder.request.setText(items.get(position).request);
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
