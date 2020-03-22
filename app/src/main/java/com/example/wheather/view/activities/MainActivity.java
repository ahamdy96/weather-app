package com.example.wheather.view.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wheather.R;
import com.example.wheather.databinding.ActivityMainBinding;
import com.example.wheather.service.model.Temperature;
import com.example.wheather.service.model.WeatherAPIResponse;
import com.example.wheather.viewmodel.LocationViewModel;
import com.example.wheather.viewmodel.WeatherViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_ID = 100;
    private RecyclerView rv;
    private ImageView imageView;
    private ArrayList<Temperature> weatherList;
    private FusedLocationProviderClient fusedLocationClient;
    private WeatherViewModel weatherViewModel;
    private WeatherViewModel.Factory weatherViewModelFactory;
    private LocationViewModel locationViewModel;
    private ActivityMainBinding binding;
    private int colorAccentDay;
    private int colorAccentNight;
    private int colorMainNight;
    private int colorMainDay;
    private Drawable backgroundDrawableDay;
    private Drawable backgroundDrawableNight;
    private Window window;
    private Map<String, Integer> weatherIconMap;
    private int weatherIconCount = 9;

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location lastLocation = locationResult.getLastLocation();
            Log.d("LOCATION_INFO",
                    "location is " + lastLocation.getLatitude() + " lat " + lastLocation.getLongitude() + " lng");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);


        populateWeatherMap();
        updateUI();
        checkPermissions();
/*
        weatherList = new ArrayList<Temperature>();

        rv = findViewById(R.id.rv);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(weatherList);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        final WeatherViewModel.Factory factory = new WeatherViewModel.Factory();
        final WeatherViewModel weatherViewModel = new ViewModelProvider(this, factory).get(WeatherViewModel.class);

        weatherViewModel.getObservableWeather().observe(this, new Observer<WeatherAPIResponse>() {
            @Override
            public void onChanged(WeatherAPIResponse weatherResponseModel) {
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

    private void updateUINight() {
        window.setStatusBarColor(colorMainNight);
        binding.frameLayoutAppBackground.setBackground(backgroundDrawableNight);


        binding.loadingBar.setIndeterminateTintList(ColorStateList.valueOf(colorMainNight));
        binding.tvLoading.setTextColor(colorMainNight);

        binding.fabForecast.setBackgroundTintList(ColorStateList.valueOf(colorAccentNight));
        binding.fabForecast.setRippleColor(colorMainNight);
        binding.fabForecast.setImageTintList(ColorStateList.valueOf(colorMainNight));

        binding.tvLocation.setTextColor(colorAccentNight);
        binding.tvCloud.setTextColor(colorAccentNight);
        binding.tvWind.setTextColor(colorAccentNight);
        binding.tvHumidity.setTextColor(colorAccentNight);
        binding.tvWeatherDescription.setTextColor(colorAccentNight);
        binding.tvTemp.setTextColor(colorAccentNight);
        binding.tvTempHigh.setTextColor(colorAccentNight);
        binding.tvTempLow.setTextColor(colorAccentNight);
        binding.tvTempDegree.setTextColor(colorAccentNight);

        binding.imgBtnEdit.setBackgroundTintList(ColorStateList.valueOf(colorAccentNight));
        binding.imgBtnEdit.setImageTintList(ColorStateList.valueOf(colorMainNight));
        binding.imgCloud.setImageTintList(ColorStateList.valueOf(colorAccentNight));
        binding.imgHumidity.setImageTintList(ColorStateList.valueOf(colorAccentNight));
        binding.imgWeatherStatus.setImageTintList(ColorStateList.valueOf(colorAccentNight));
        binding.imgWind.setImageTintList(ColorStateList.valueOf(colorAccentNight));

        setTextViewDrawableColor(binding.tvTempHigh, colorAccentNight);
        setTextViewDrawableColor(binding.tvTempLow, colorAccentNight);

    }

    private void updateUI() {
        window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        colorAccentDay = getResources().getColor(R.color.colorAccentDay);
        colorMainDay = getResources().getColor(R.color.colorMainDay);
        colorAccentNight = getResources().getColor(R.color.colorAccentNight);
        colorMainNight = getResources().getColor(R.color.colorMainNight);

        backgroundDrawableDay = getResources().getDrawable(R.drawable.morning);
        backgroundDrawableNight = getResources().getDrawable(R.drawable.night);

        Date date = new Date();
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        if (calendar.get(Calendar.HOUR_OF_DAY) >= 17 && calendar.get(Calendar.HOUR_OF_DAY) < 5) {
            updateUINight();
        }
    }

    private void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (isLocationEnabled()) {
                getLastLocation();
            } else {
                Toast.makeText(getApplicationContext(), "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
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

    private void getLastLocation() {
        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
        locationViewModel.getObservableLocation().observe(this, new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
                Log.d("LOCATION_INFO",
                        "location is " + location.getLatitude() + " lat " + location.getLongitude() + " lng");

                binding.tvLoading.setText("Getting weather info...");
                getWeatherInfo(location);
            }
        });
    }

    private void getWeatherInfo(Location location) {

        weatherViewModelFactory = new WeatherViewModel.Factory(location);
        weatherViewModel = new ViewModelProvider(this, weatherViewModelFactory).get(WeatherViewModel.class);
        weatherViewModel.getObservableWeather().observe(this, new Observer<WeatherAPIResponse>() {
            @Override
            public void onChanged(WeatherAPIResponse weatherResponseModel) {

                if (weatherResponseModel == null) {
                    Toast.makeText(getApplicationContext(), "failed to get weather info", Toast.LENGTH_LONG).show();
                } else {
                    String weatherIcon = weatherResponseModel.getList().get(0).getWeather().get(0).getIcon();
                    int icon = weatherIconMap.get(weatherIcon.substring(0, 2));

                    String city = weatherResponseModel.getCity().getName();
                    String country = weatherResponseModel.getCity().getCountry();
                    String weatherDescription = weatherResponseModel.getList().get(0).getWeather().get(0).getDescription();
                    int clouds = weatherResponseModel.getList().get(0).getClouds().getAll();
                    double wind = weatherResponseModel.getList().get(0).getWind().getSpeed();
                    int humidity = weatherResponseModel.getList().get(0).getMain().getHumidity();
                    long temp = Math.round(weatherResponseModel.getList().get(0).getMain().getTemp());
                    long tempHigh = Math.round(weatherResponseModel.getList().get(0).getMain().getTemp_max());
                    long tempLow = Math.round(weatherResponseModel.getList().get(0).getMain().getTemp_min());

                    binding.tvLocation.setText(city + ", " + country);
                    binding.tvCloud.setText(clouds + "%");
                    binding.tvWind.setText(wind + "m/s");
                    binding.tvHumidity.setText(humidity + "%");
                    binding.tvWeatherDescription.setText(weatherDescription);
                    binding.tvTemp.setText(String.valueOf(temp));
                    binding.tvTempHigh.setText(String.valueOf(tempHigh));
                    binding.tvTempLow.setText(String.valueOf(tempLow));
                    binding.imgWeatherStatus.setImageResource(icon);
                }

                binding.linearLayoutLoading.setVisibility(View.INVISIBLE);
                /*for (int i = 0; i < weatherResponseModel.getList().size(); i++) {
                    double temp = weatherResponseModel.getList().get(i).getMain().getTemp();
                    double minTemp = weatherResponseModel.getList().get(i).getMain().getTempMin();
                    double maxTemp = weatherResponseModel.getList().get(i).getMain().getTempMax();

                    Temperature t = new Temperature(temp, minTemp, maxTemp);
                    weatherList.add(t);
                }

                adapter.updateRecyclerView();*/
            }
        });
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    private void setTextViewDrawableColor(TextView textView, int color) {
        for (Drawable drawable : textView.getCompoundDrawablesRelative()) {
            if (drawable != null) {
                drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            }
        }
    }

    private void populateWeatherMap() {
        weatherIconMap = new HashMap<>(weatherIconCount);
        weatherIconMap.put("01", R.drawable.weather_sunny);
        weatherIconMap.put("02", R.drawable.weather_partly_cloudy);
        weatherIconMap.put("03", R.drawable.weather_cloudy);
        weatherIconMap.put("04", R.drawable.weather_cloudy);
        weatherIconMap.put("09", R.drawable.weather_pouring);
        weatherIconMap.put("10", R.drawable.weather_rainy);
        weatherIconMap.put("11", R.drawable.weather_lightning);
        weatherIconMap.put("13", R.drawable.weather_snowy_heavy);
        weatherIconMap.put("50", R.drawable.weather_fog);
    }
}
