package com.example.wheather.viewmodel;

import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.wheather.service.model.WeatherAPIResponse;
import com.example.wheather.service.repository.WeatherRepository;

public class WeatherViewModel extends ViewModel {
    private LiveData<WeatherAPIResponse> data;

    public WeatherViewModel(Location location) {
        data = WeatherRepository.getInstance().getWeatherWithGeoLocation(location);
    }

    public LiveData<WeatherAPIResponse> getObservableWeather() {
        return data;
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        private Location location;

        public Factory(Location location) {
            this.location = location;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new WeatherViewModel(location);
        }
    }
}
