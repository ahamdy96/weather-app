package com.example.wheather.service.repository;

import com.example.wheather.BuildConfig;
import com.example.wheather.service.model.WeatherResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface OpenWeatherAPI {

    final int cityId = 360630;
    final String APIKEY = BuildConfig.APIKEY;

    @GET("/data/2.5/forecast?id=" + cityId + "&appid=" + APIKEY + "&units=metric")
    Call<WeatherResponseModel> getWeatherResponse();
}
