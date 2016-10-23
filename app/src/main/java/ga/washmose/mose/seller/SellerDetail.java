package ga.washmose.mose.seller;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hedgehog.ratingbar.RatingBar;

import java.util.ArrayList;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import ga.washmose.mose.ItemData;
import ga.washmose.mose.R;
import ga.washmose.mose.ReviewData;
import ga.washmose.mose.User.UserLaundryItem;
import ga.washmose.mose.Util.UHttps;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SellerDetail extends AppCompatActivity implements OnMapReadyCallback{

    ImageView profile;
    TextView name, location, rateCount;
    RatingBar ratingBar;
    Button contact;

    ViewGroup itemListLayout, reviewLayout;

    UserLaundryItem seller;
    LatLng sellerLocation;
    private GoogleMap map;
    MapFragment mapFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_detail);
        Intent intent = getIntent();
        seller = (UserLaundryItem) intent.getSerializableExtra("seller");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.seller_detail_map, mapFragment);
        fragmentTransaction.commit();
        mapFragment.getMapAsync(this);

        profile = (ImageView)findViewById(R.id.seller_detail_profile);
        name = (TextView)findViewById(R.id.seller_detail_name);
        location = (TextView)findViewById(R.id.seller_detail_loaction);
        ratingBar = (RatingBar) findViewById(R.id.seller_detail_rating);
        rateCount = (TextView)findViewById(R.id.seller_detail_rate_count);
        contact = (Button) findViewById(R.id.seller_detail_contact);

        itemListLayout = (ViewGroup)findViewById(R.id.seller_detail_item_list_layout);
        reviewLayout = (ViewGroup)findViewById(R.id.seller_detail_review_layout);

        Random random = new Random();
        sellerLocation = new LatLng(seller.latitude, seller.longitude);
        Glide.with(this).load(UHttps.IP+seller.imageUrl).centerCrop().into(profile);
        name.setText(seller.name);
        location.setText(seller.location);

        ratingBar.setStar(random.nextInt(4) + 1);
        int count = random.nextInt(9) +1;
        rateCount.setText(count +"");

        initItem();
        initReview();
    }

    private void initItem(){
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ArrayList<ItemData> items = seller.items;

        for (int i = 0; i< items.size(); i++){
            View view = inflater.inflate(R.layout.item_seller_detail_stuff, itemListLayout, false);
            TextView name = (TextView) view.findViewById(R.id.stuff_name);
            TextView count = (TextView) view.findViewById(R.id.stuff_count);
            TextView price = (TextView) view.findViewById(R.id.stuff_price);
            CircleImageView icon = (CircleImageView) view.findViewById(R.id.stuff_icon);

            Glide.with(this).load(UHttps.IP + items.get(i).iconURL).into(icon);
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
        map = googleMap;
        Marker marker = map.addMarker(new MarkerOptions().position(sellerLocation));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sellerLocation, 17));
        map.getUiSettings().setScrollGesturesEnabled(false);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
