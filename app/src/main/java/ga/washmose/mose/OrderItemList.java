package ga.washmose.mose;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ga.washmose.mose.seller.ManageCompleteFragment;

/**
 * Created by leejaebeom on 2016. 10. 12..
 */

public class OrderItemList extends AppCompatActivity{
    ListView list;
    OrderListAdapter adapter;
    ArrayList<OrderItemData> items = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_item_list);
        items.add(new OrderItemData("남성 속옷 상의", 500));
        items.add(new OrderItemData("남성 속옷 하의", 500));
        adapter = new OrderListAdapter(this, items);
        list = (ListView)findViewById(R.id.order_item_list);
        list.setAdapter(adapter);


    }

    public class OrderItemData{
        String name;
        int price, count = 0;

        public OrderItemData(String name, int price) {
            this.name = name;
            this.price = price;
        }
    }

    public class OrderListAdapter extends BaseAdapter{
        ArrayList<OrderItemData> items;
        Context context;
        public OrderListAdapter(Context context, ArrayList<OrderItemData> items) {
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(convertView == null){
                convertView = inflater.inflate(R.layout.item_order_items_list, parent, false);
                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.price= (TextView) convertView.findViewById(R.id.price);
                holder.count= (TextView) convertView.findViewById(R.id.count);
                holder.plus = (ImageButton) convertView.findViewById(R.id.plus);
                holder.minus= (ImageButton) convertView.findViewById(R.id.minus);

                convertView.setTag(holder);
            }else{
                holder = (ViewHolder)convertView.getTag();
            }

//            holder.date.setText(items.get(position).date);
            holder.name.setText(items.get(position).name);
            holder.price.setText(items.get(position).price + "원");
            holder.count.setText(items.get(position).count + "");
            holder.plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ++items.get(position).count;
                    notifyDataSetChanged();
                }
            });

            holder.minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    --items.get(position).count;
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }

        class ViewHolder{
            TextView name, price, count;
            ImageButton plus, minus;
        }
    }
}
