package com.example.wheather.service.repository;

import android.location.Location;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import static com.example.wheather.WeatherApplication.getAppContext;

public class LocationRepository {
    private static final int PERMISSION_ID = 100;
    private static LocationRepository locationRepository;
    private FusedLocationProviderClient fusedLocationClient;
    private MutableLiveData<Location> locationMutableLiveData;
    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location lastLocation = locationResult.getLastLocation();
            locationMutableLiveData.setValue(lastLocation);
            Log.d("LOCATION_INFO",
                    "location is " + lastLocation.getLatitude() + " lat " + lastLocation.getLongitude() + " lng");
        }
    };


    private LocationRepository() {
        super();
    }

    public static LocationRepository getInstance() {
        if (locationRepository == null)
            locationRepository = new LocationRepository();
        return locationRepository;
    }

    public LiveData<Location> getLastLocation() {
        locationMutableLiveData = new MutableLiveData<>();
        fusedLocationClient = new FusedLocationProviderClient(getAppContext());
        fusedLocationClient.getLastLocation().addOnCompleteListener(
                new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            Log.d("LOCATION_INFO", "Requesting new location data ...");
                            requestNewLocationData();
                        } else {
                            locationMutableLiveData.setValue(location);
                        }
                    }
                }
        );
        return locationMutableLiveData;
    }

    private void requestNewLocationData() {

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(0);
        locationRequest.setFastestInterval(0);
        locationRequest.setNumUpdates(1);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getAppContext());
        fusedLocationClient.requestLocationUpdates(
                locationRequest, locationCallback,
                Looper.myLooper()
        );

    }

}
