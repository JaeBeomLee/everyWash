package ga.washmose.mose.main;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

import ga.washmose.mose.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    ListView list;
    TextView location;
    Toolbar toolbar;
    mainAdapter adapter;
    ArrayList<MainItem> items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        items = new ArrayList<>();
        items.add(new MainItem("https://api2.washmose.ga/data/test/0.jpeg", "주부 5년차", "수내동","속옷 3, 겉옷 5"));
        items.add(new MainItem("https://api2.washmose.ga/data/test/3.jpeg", "바이오 컴퓨터 세탁소", "수내동","속옷 2, 겉옷 3"));
        items.add(new MainItem("https://api2.washmose.ga/data/test/2.jpeg", "자취생", "수내동","속옷 1"));
        items.add(new MainItem("https://api2.washmose.ga/data/test/1.jpeg", "트레벨 오피스텔 가능, 수선 가능", "수내동","속옷, 상의, 하의"));
        items.add(new MainItem("https://api2.washmose.ga/data/test/4.jpeg", "보보스 오피스텔 4층", "수내동","속옷 10, 겨울 옷"));
        adapter = new mainAdapter(items, this);
        toolbar = (Toolbar)findViewById(R.id.main_toolbar);
        list = (ListView)findViewById(R.id.main_list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
    public class mainAdapter extends BaseAdapter{

        ArrayList<MainItem> items;
        Context context;
        public mainAdapter(ArrayList<MainItem> items, Context context) {
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
                convertView = inflater.inflate(R.layout.main_list_item,parent, false);
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

        public class ViewHolder{
            ImageView profile;
            TextView name, location, summary;
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}

