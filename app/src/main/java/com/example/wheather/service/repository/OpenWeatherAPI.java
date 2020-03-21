package com.example.wheather.service.repository;

import com.example.wheather.BuildConfig;
import com.example.wheather.service.model.WeatherAPIResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherAPI {

    final int cityId = 360630;
    final String APIKEY = BuildConfig.APIKEY;

    @GET("/data/2.5/forecast?appid=" + APIKEY + "&units=metric")
    Call<WeatherAPIResponse> getWeatherWithCityID(@Query("id") int cityId);

    @GET("/data/2.5/forecast?appid=" + APIKEY + "&units=metric")
    Call<WeatherAPIResponse> getWeatherWithGeoLocation(@Query("lat") Double lat, @Query("lon") Double lon);
}
