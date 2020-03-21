package com.example.wheather.service.repository;

import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.wheather.service.model.WeatherAPIResponse;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherRepository {
    private static WeatherRepository repository;
    private OpenWeatherAPI openWeatherAPI;

    public WeatherRepository() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        openWeatherAPI = retrofit.create(OpenWeatherAPI.class);
    }

    public static WeatherRepository getInstance() {
        if (repository == null) {
            repository = new WeatherRepository();
        }

        return repository;
    }

    public LiveData<WeatherAPIResponse> getWeatherWithCityID(int cityId) {
        final MutableLiveData<WeatherAPIResponse> data = new MutableLiveData<>();
        openWeatherAPI.getWeatherWithCityID(cityId).enqueue(new Callback<WeatherAPIResponse>() {
            @Override
            public void onResponse(Call<WeatherAPIResponse> call, Response<WeatherAPIResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<WeatherAPIResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<WeatherAPIResponse> getWeatherWithGeoLocation(Location location) {
        final MutableLiveData<WeatherAPIResponse> data = new MutableLiveData<>();
        openWeatherAPI.getWeatherWithGeoLocation(location.getLatitude(), location.getLongitude()).enqueue(new Callback<WeatherAPIResponse>() {
            @Override
            public void onResponse(Call<WeatherAPIResponse> call, Response<WeatherAPIResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<WeatherAPIResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

}
