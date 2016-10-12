package ga.washmose.mose.seller;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import ga.washmose.mose.R;

public class SellerOrderRequestFragment extends Fragment {
    sellerRequestAdapter adapter;
    ArrayList<SellerRequestItem> items =new ArrayList<>();

    public SellerOrderRequestFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SellerOrderRequestFragment newInstance() {
        SellerOrderRequestFragment fragment = new SellerOrderRequestFragment();
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
        items.add(new SellerRequestItem("http://www.vipstudio.co.kr/bbs/data/gallery01/남자증명01.jpg", "김강현", "분당구 수내동 10-1 트라펠리스 910호", "10월 3일 세탁 요청", "속옷 30, 상의 2", false));
        items.add(new SellerRequestItem("http://pds19.egloos.com/pds/201011/22/73/f0095273_4cea0b1348cfe.jpg", "정시후", "분당구 정자동 두산위브파빌리온 A동 1820호", "10월 5일 세탁 요청", "속옷 10, 하의 5", true));
        items.add(new SellerRequestItem("http://cfile202.uf.daum.net/image/2429503F5507C4E6203EF1", "최지훈", "분당구 운중동 산운마을판교월든힐스2단지아파트 203동 111호", "10월 5일 세탁 요청", "상의 4, 하의 5", true));

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
            holder.location.setText(items.get(position).location);
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