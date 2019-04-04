package com.example.weatherforecast;

import com.example.weatherforecast.pojo.Weather;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JSONPlaceHolderApi {
    String weatherUrl = "http://api.apixu.com/v1/";

    //forecast.json?key=18148b1aabd34fd28c1183937191403&q={cityname}&days=5

    @GET("forecast.json")
    Call<Weather> getWeatherInfo(@Query("key") String key,
                                 @Query("q") String cityname,
                                 @Query("days") int days);
}
