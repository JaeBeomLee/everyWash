package ga.washmose.mose.seller;

import android.content.Context;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.hedgehog.ratingbar.RatingBar;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import ga.washmose.mose.ItemData;
import ga.washmose.mose.R;
import ga.washmose.mose.ReviewData;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SellerDetail extends AppCompatActivity implements OnMapReadyCallback{

    ImageView profile;
    TextView name, location, rateCount;
    RatingBar ratingBar;
    Button contact;

    ViewGroup itemListLayout, reviewLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        profile = (ImageView)findViewById(R.id.seller_detail_profile);
        name = (TextView)findViewById(R.id.seller_detail_name);
        location = (TextView)findViewById(R.id.seller_detail_loaction);
        ratingBar = (RatingBar) findViewById(R.id.seller_detail_rating);
        rateCount = (TextView)findViewById(R.id.seller_detail_rate_count);
        contact = (Button) findViewById(R.id.seller_detail_contact);

        itemListLayout = (ViewGroup)findViewById(R.id.seller_detail_item_list_layout);
        reviewLayout = (ViewGroup)findViewById(R.id.seller_detail_review_layout);

        Glide.with(this).load("https://api2.washmose.ga/data/test/3.jpeg").centerCrop().into(profile);
        name.setText("바이오 컴퓨터 세탁");
        location.setText("수내동");
        ratingBar.setStar(3);
        rateCount.setText("529");

        initItem();
        initReview();
    }

    private void initItem(){
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ArrayList<ItemData> items = new ArrayList<>();
        items.add(new ItemData("https://cleanfly.link/image/1001_icon.jpg", "티셔츠", 1, 3000));
        items.add(new ItemData("https://cleanfly.link/image/1002_icon.jpg", "남성 속옷 상의", 10, 1500));
        items.add(new ItemData("https://cleanfly.link/image/1003_icon.jpg", "남성 속옷 하의", 10, 2000));

        for (int i = 0; i< items.size(); i++){
            View view = inflater.inflate(R.layout.item_seller_detail_stuff, itemListLayout, false);
            TextView name = (TextView) view.findViewById(R.id.stuff_name);
            TextView count = (TextView) view.findViewById(R.id.stuff_count);
            TextView price = (TextView) view.findViewById(R.id.stuff_price);
            CircleImageView icon = (CircleImageView) view.findViewById(R.id.stuff_icon);

            Glide.with(this).load(items.get(i).iconURL).into(icon);
            name.setText(items.get(i).name);
            count.setText(items.get(i).count + "벌 당");
            price.setText(items.get(i).price + " 원");

            itemListLayout.addView(view);
        }
    }

    private void initReview(){
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ArrayList<ReviewData> reviews = new ArrayList<>();
        reviews.add(new ReviewData("http://chem.unl.edu/cheung.jpg", "도윤", "16년 4월", "속옷이 누런 색이었는데 맡기고 나니 흰색으로 변해 있었어요. 세탁도 완전 빨리해서 주시네요."));
        reviews.add(new ReviewData("http://chem.unl.edu/guo_profile_pic-2.jpg", "조세현", "16년 8월", "거리가 먼게 흠이긴 하지만 세탁을 잘 해서 주시니 너무 좋네요. 다리미질에 깔끔하게 접어서 주셨어요."));

        for (int i = 0; i< reviews.size(); i++){
            View view = inflater.inflate(R.layout.item_seller_detail_review, reviewLayout, false);
            TextView name = (TextView) view.findViewById(R.id.review_item_name);
            TextView date = (TextView) view.findViewById(R.id.review_item_date);
            TextView review = (TextView) view.findViewById(R.id.review_item_content);
            CircleImageView icon = (CircleImageView) view.findViewById(R.id.review_item_icon);

            Glide.with(this).load(reviews.get(i).iconURL).into(icon);
            name.setText(reviews.get(i).name);
            date.setText(reviews.get(i).date);
            review.setText(reviews.get(i).review);

            reviewLayout.addView(view);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
