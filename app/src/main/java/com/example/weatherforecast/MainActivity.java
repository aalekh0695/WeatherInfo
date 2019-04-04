package com.example.weatherforecast;

import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherforecast.adapter.ListAdapter;
import com.example.weatherforecast.pojo.MyForecastDay;
import com.example.weatherforecast.pojo.Weather;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private TextView tv_weather_today, tv_location, tv_weather_state_today;
    ImageView iv_weather_today, iv_loading;
    LinearLayout ll_main_activity;
    RelativeLayout rl_main_activity;
    ListView list;
    ArrayList<String> dayArray;
    ArrayList<String> tempArray;
    MyForecastDay myForecastDay;
    ListAdapter adapter;
    ProgressDialog progressDoalog;
    public int REQUEST_CODE = 101;
    private String API_KEY = "18148b1aabd34fd28c1183937191403";
    private String CITY_NAME = "Bangalore";
    private int DAYS = 1;
    WeatherViewModel model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        RotateAnimation rotate = new RotateAnimation(
                0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        rotate.setDuration(1000);
        rotate.setRepeatCount(Animation.INFINITE);
        iv_loading.startAnimation(rotate);

        model = ViewModelProviders.of(this).get(WeatherViewModel.class);
    }

    private void init() {
        tv_weather_today = findViewById(R.id.tv_weather_today);
        tv_location = findViewById(R.id.tv_location);
        tv_weather_state_today = findViewById(R.id.tv_weather_state_today);
        iv_weather_today = findViewById(R.id.iv_weather_today);
        iv_loading = findViewById(R.id.iv_loading);
        ll_main_activity = findViewById(R.id.ll_main_activity);
        rl_main_activity = findViewById(R.id.rl_main_activity);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("MainActivity", "on start called");
        fetchWeatherInfo(null);
        rl_main_activity.setVisibility(View.VISIBLE);
        ll_main_activity.setVisibility(View.INVISIBLE);

    }

    private void fetchWeatherInfo(final String arg) {
        SharedPreferences weatherPrefs = getSharedPreferences("weatherPrefs", Context.MODE_PRIVATE);
        CITY_NAME = weatherPrefs.getString("CITY_NAME", "Bangalore");
        DAYS = weatherPrefs.getInt("DAYS", 5);
        Log.i("Main Activity", CITY_NAME+" "+DAYS);

//        showProgressSpinner();
        model.getWeatherInfo(API_KEY, CITY_NAME, DAYS, arg).observe(this, new Observer<Weather>() {
            @Override
            public void onChanged(@Nullable Weather weather) {
//                progressDoalog.dismiss();
                rl_main_activity.setVisibility(View.GONE);
                ll_main_activity.setVisibility(View.VISIBLE);

                if(weather == null){
                    startActivity(new Intent(MainActivity.this, FailureActivity.class));
                    finish();
                    return;
                }

                dayArray = new ArrayList<>();
                tempArray = new ArrayList<>();
                ArrayList<MyForecastDay> myForecastDayArrayList = weather.getForecast().getForecastday();
                Log.i("Main Activity", myForecastDayArrayList.size()+"");
                for(int i=0;i<myForecastDayArrayList.size();i++){
                    Log.i("Main Activity", myForecastDayArrayList.get(i).getDay().getAvgtemp_c()+"");
                }

                myForecastDay = myForecastDayArrayList.get(0);

                //set image
                model.getBitmap(getApplicationContext(), myForecastDay.getDay().getCondition().getIcon(), arg)
                        .observe(MainActivity.this, new Observer<Bitmap>() {
                    @Override
                    public void onChanged(@Nullable Bitmap bitmap) {
                        iv_weather_today.setImageBitmap(bitmap);
                    }
                });

                for(int i = 1;i<myForecastDayArrayList.size();i++){
                    myForecastDay = myForecastDayArrayList.get(i);
                    dayArray.add(dateToDay(myForecastDay.getDate()));
                    tempArray.add(myForecastDay.getDay().getAvgtemp_c()+(char) 0x00B0 +"C");
                }

                //SETTING UP UI ELEMENTS
                if(myForecastDay == null)
                    tv_weather_today.setText("null");
                else{
                    tv_weather_today.setText(myForecastDayArrayList.get(0).getDay().getAvgtemp_c()+(char) 0x00B0 +"C");
                    tv_weather_state_today.setText(myForecastDayArrayList.get(0).getDay().getCondition().getText());
                    tv_location.setText(weather.getLocation().getName());
                }

                adapter=new ListAdapter(MainActivity.this, dayArray, tempArray);
                list=findViewById(R.id.list);
                list.setAdapter(adapter);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.main_menu_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
//                startActivity(intent);
                startActivityForResult(intent, REQUEST_CODE);
                break;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("Main Activity", "on activity result");
        if(requestCode == REQUEST_CODE ){
            if(resultCode == RESULT_OK){
//                CITY_NAME = data.getStringExtra("city");
//                DAYS = data.getIntExtra("days", -1);
                fetchWeatherInfo("update");
            }
        }
    }

    private void showProgressSpinner() {
        progressDoalog = new ProgressDialog(MainActivity.this);
        progressDoalog.setMessage("Loading....");
        progressDoalog.setTitle("Fetching weather info..");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // show it
        progressDoalog.show();
    }

    String dateToDay(String dt){
        String dayOfTheWeek = "NA";
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dt);
            dayOfTheWeek = (String) DateFormat.format("EEEE", date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dayOfTheWeek;
    }
}
