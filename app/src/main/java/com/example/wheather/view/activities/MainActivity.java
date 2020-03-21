package com.example.wheather.view.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wheather.R;
import com.example.wheather.service.model.Temperature;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_ID = 100;
    private RecyclerView rv;
    private ImageView imageView;
    private ArrayList<Temperature> weatherList;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location lastLocation = locationResult.getLastLocation();
            Toast.makeText(getApplicationContext(),
                    "location is " + lastLocation.getLatitude() + " lat " + lastLocation.getLongitude() + " lng",
                    Toast.LENGTH_LONG)
                    .show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

/*
        weatherList = new ArrayList<Temperature>();

        rv = findViewById(R.id.rv);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(weatherList);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        final WeatherViewModel.Factory factory = new WeatherViewModel.Factory();
        final WeatherViewModel weatherViewModel = new ViewModelProvider(this, factory).get(WeatherViewModel.class);

        weatherViewModel.getObservableWeather().observe(this, new Observer<WeatherResponseModel>() {
            @Override
            public void onChanged(WeatherResponseModel weatherResponseModel) {
                Toast.makeText(getApplicationContext(), weatherResponseModel.getList().get(0).getWeather().get(0).getDescription(), Toast.LENGTH_LONG).show();

                for (int i = 0; i < weatherResponseModel.getList().size(); i++) {
                    double temp = weatherResponseModel.getList().get(i).getMain().getTemp();
                    double minTemp = weatherResponseModel.getList().get(i).getMain().getTempMin();
                    double maxTemp = weatherResponseModel.getList().get(i).getMain().getTempMax();

                    Temperature t = new Temperature(temp, minTemp, maxTemp);
                    weatherList.add(t);
                }

                adapter.updateRecyclerView();
            }
        });

        int actionImage = R.drawable.weather2;

        imageView = findViewById(R.id.action_image);
        imageView.setImageResource(actionImage);

        Bitmap image = BitmapFactory.decodeResource(getResources(), actionImage);
        Window window = getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        Palette palette = Palette.from(image).generate();
        Palette.Swatch dominantSwatch = palette.getDominantSwatch();

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingtoolbar);
        collapsingToolbarLayout.setContentScrimColor(dominantSwatch != null ? dominantSwatch.getRgb() : getResources().getColor(R.color.colorWhite));
        collapsingToolbarLayout.setScrimAnimationDuration(200);
        collapsingToolbarLayout.setCollapsedTitleTextColor(dominantSwatch != null ? (dominantSwatch.getTitleTextColor() | 0xff000000) : getResources().getColor(R.color.colorBlack));
        window.setStatusBarColor((dominantSwatch != null ? dominantSwatch.getRgb() : getResources().getColor(R.color.colorWhite)));
*/

    }

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                fusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    Toast.makeText(getApplicationContext(),
                                            "Requesting new data ...",
                                            Toast.LENGTH_LONG)
                                            .show();
                                    requestNewLocationData();
                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            "location is " + location.getLatitude() + " lat " + location.getLongitude() + " lng",
                                            Toast.LENGTH_LONG)
                                            .show();
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    private void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

}
