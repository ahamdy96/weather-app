package com.example.wheather.viewmodel;

import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.wheather.service.repository.LocationRepository;

public class LocationViewModel extends ViewModel {
    private LiveData<Location> locationLiveData;

    public LiveData<Location> getObservableLocation() {
        locationLiveData = LocationRepository.getInstance().getLastLocation();
        return locationLiveData;
    }

}
