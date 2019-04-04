package com.example.weatherforecast;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

public class LoadingActivity extends AppCompatActivity {
    ImageView iv_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        iv_loading = findViewById(R.id.iv_loading);

        RotateAnimation rotate = new RotateAnimation(
                0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        rotate.setDuration(1000);
        rotate.setRepeatCount(Animation.INFINITE);
        iv_loading.startAnimation(rotate);
    }
}
