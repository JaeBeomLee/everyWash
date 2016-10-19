package ga.washmose.mose.User;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hedgehog.ratingbar.RatingBar;

import de.hdodenhof.circleimageview.CircleImageView;
import ga.washmose.mose.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class UserEvaluationActivity extends AppCompatActivity {

    CircleImageView informationProfile;
    TextView information;
    RatingBar rating;
    EditText ratingDetail;
    Button accept;

    int ratingNum = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_evaluation);

        informationProfile = (CircleImageView) findViewById(R.id.information_profile);
        information = (TextView) findViewById(R.id.information);
        rating = (RatingBar) findViewById(R.id.rating);
        ratingDetail = (EditText) findViewById(R.id.rate_detail);
        accept = (Button) findViewById(R.id.accept);

        rating.setOnRatingChangeListener(new RatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(float RatingCount) {
                ratingNum = (int)RatingCount;
            }
        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("RatingNum", ratingNum + " " + ratingDetail.getText().toString());
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
