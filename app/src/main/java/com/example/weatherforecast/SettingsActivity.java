package com.example.weatherforecast;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    Button btn_update;
    TextInputEditText et_current_city, et_no_of_days;
    String cityname;
    int days;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        init();

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cityname = et_current_city.getText().toString();
                days = Integer.parseInt(et_no_of_days.getText().toString());

                if(cityname.trim().length()==0)
                    Toast.makeText(SettingsActivity.this, "Enter city name", Toast.LENGTH_SHORT).show();
                else if(days<1 || days>10)
                    Toast.makeText(SettingsActivity.this, "No. of days should be between 1-10", Toast.LENGTH_SHORT).show();
                else{
                    SharedPreferences weatherPrefs = getSharedPreferences("weatherPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = weatherPrefs.edit();
                    editor.putString("CITY_NAME", cityname);
                    editor.putInt("DAYS", days);
                    editor.apply();
                    Intent intent = new Intent();
//                    intent.putExtra("city", cityname);
//                    intent.putExtra("days", days);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

    }

    private void init() {
        btn_update = findViewById(R.id.btn_update);
        et_current_city = findViewById(R.id.et_current_city);
        et_no_of_days = findViewById(R.id.et_no_of_days);
    }
}
