package com.example.wheather.view.fragments;

import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.wheather.R;
import com.example.wheather.databinding.FragmentMainBinding;
import com.example.wheather.service.model.WeatherAPIResponse;
import com.example.wheather.view.activities.MainActivity;
import com.example.wheather.viewmodel.LocationViewModel;
import com.example.wheather.viewmodel.WeatherViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class MainFragment extends Fragment {

    private WeatherViewModel weatherViewModel;
    private WeatherViewModel.Factory weatherViewModelFactory;
    private LocationViewModel locationViewModel;
    private FragmentMainBinding binding;
    private int colorAccentNight;
    private int colorMainNight;
    private Drawable backgroundDrawableNight;
    private Window window;
    private Map<String, Integer> weatherIconMap;
    private int weatherIconCount = 9;
    private FloatingActionButton fabDeatiledWeatherForecast;


    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_main, container, false);

        binding.fabForecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 3/23/2020
            }
        });

        populateWeatherMap();

        updateUI();

        getLastLocation();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        mViewModel = ViewModelProviders.of(this).get(BlankViewModel.class);
        // TODO: Use the ViewModel
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
        window = getActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        colorAccentNight = getResources().getColor(R.color.colorAccentNight);
        colorMainNight = getResources().getColor(R.color.colorMainNight);

        backgroundDrawableNight = getResources().getDrawable(R.drawable.night);

        Date date = new Date();
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        if (calendar.get(Calendar.HOUR_OF_DAY) >= 17 || calendar.get(Calendar.HOUR_OF_DAY) < 5) {
            updateUINight();
        }
    }

    private void getLastLocation() {
        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
        locationViewModel.getObservableLocation().observe(getActivity(), new Observer<Location>() {
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
                    Toast.makeText(getContext(), "failed to get weather info", Toast.LENGTH_LONG).show();
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
            }
        });
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

    private void setTextViewDrawableColor(TextView textView, int color) {
        for (Drawable drawable : textView.getCompoundDrawablesRelative()) {
            if (drawable != null) {
                drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MainActivity.LOCATION_PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

}
