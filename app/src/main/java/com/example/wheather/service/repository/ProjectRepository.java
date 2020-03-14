package com.example.wheather.service.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.wheather.service.model.WeatherResponseModel;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProjectRepository {
    private static ProjectRepository repository;
    private OpenWeatherAPI openWeatherAPI;

    public ProjectRepository() {

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

    public static ProjectRepository getInstance() {
        if (repository == null) {
            repository = new ProjectRepository();
        }

        return repository;
    }

    public LiveData<WeatherResponseModel> getWeather() {
        final MutableLiveData<WeatherResponseModel> data = new MutableLiveData<>();

        openWeatherAPI.getWeatherResponse().enqueue(new Callback<WeatherResponseModel>() {
            @Override
            public void onResponse(Call<WeatherResponseModel> call, Response<WeatherResponseModel> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<WeatherResponseModel> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

}
