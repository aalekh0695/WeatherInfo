package com.example.weatherforecast;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.weatherforecast.pojo.Weather;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherViewModel extends ViewModel {
    //this is the data that we will fetch asynchronously
    private MutableLiveData<Weather> weather;
    private MutableLiveData<Bitmap> bitmap;

    public LiveData<Bitmap> getBitmap(Context context, String icon, String arg){
        if(bitmap == null){
            bitmap = new MutableLiveData<>();
            loadBitmap(context, icon);
        }else if(arg!=null && arg.equalsIgnoreCase("update")){
            bitmap = new MutableLiveData<>();
            loadBitmap(context, icon);
        }

        return bitmap;
    }

    private void loadBitmap(Context context, String icon) {
        Glide.with(context).asBitmap()
                .load("http:"+icon)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                        iv_weather_today.setImageBitmap(resource);
                        bitmap.setValue(resource);
                    }
                });
    }

    public LiveData<Weather> getWeatherInfo(String API_KEY, String CITY_NAME, int DAYS, String arg){
        if(weather == null){
            weather = new MutableLiveData<>();
            loadWeatherInfo(API_KEY, CITY_NAME, DAYS);
        }else if(arg!=null && arg.equalsIgnoreCase("update")){
            weather = new MutableLiveData<>();
            loadWeatherInfo(API_KEY, CITY_NAME, DAYS);
        }

        return weather;
    }

    private void loadWeatherInfo(String API_KEY, String CITY_NAME, int DAYS){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(JSONPlaceHolderApi.weatherUrl)
                .addConverterFactory(GsonConverterFactory.create()).build();
        JSONPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JSONPlaceHolderApi.class);
        Call<Weather> call = jsonPlaceHolderApi.getWeatherInfo(API_KEY, CITY_NAME, DAYS);

        call.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                if(!response.isSuccessful()){
                    weather.setValue(null);
                    return;
                }

                weather.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                weather.setValue(null);
            }
        });
    }
}
