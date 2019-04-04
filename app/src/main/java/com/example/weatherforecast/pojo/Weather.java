package com.example.weatherforecast.pojo;

public class Weather {
    Location location;
    Current current;
    Forecast forecast;

    public Location getLocation() {
        return location;
    }

    public Current getCurrent() {
        return current;
    }

    public Forecast getForecast() {
        return forecast;
    }
}
