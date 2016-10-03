package ga.washmose.mose;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class temp extends AppCompatActivity {

    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R
                .layout.activity_temp);
        imageView = (ImageView)findViewById(R.id.image);

        Glide.with(this).load("http://goo.gl/gEgYUd").centerCrop().into(imageView);
    }
}
