package com.example.wheather.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.wheather.service.model.WeatherResponseModel;
import com.example.wheather.service.repository.ProjectRepository;

public class WeatherViewModel extends ViewModel {
    private LiveData<WeatherResponseModel> data;

    public WeatherViewModel(/*@NonNull Application application*/) {
        data = ProjectRepository.getInstance().getWeather();
        //        super(application);
    }

    public LiveData<WeatherResponseModel> getObservableWeather() {
        return data;
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {


        public Factory() {
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new WeatherViewModel();
        }
    }
}
