package com.example.weatherforecast;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class FailureActivity extends AppCompatActivity {

    public void retryButton(View view){
//        startActivity(new Intent(FailureActivity.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_failure);
    }
}
