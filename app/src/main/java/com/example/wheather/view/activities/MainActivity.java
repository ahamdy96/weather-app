package com.example.wheather.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.wheather.R;
import com.example.wheather.service.model.Temperature;
import com.example.wheather.service.model.WeatherResponseModel;
import com.example.wheather.view.adapters.RecyclerViewAdapter;
import com.example.wheather.viewmodel.WeatherViewModel;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rv;
    private ImageView imageView;
    private ArrayList<Temperature> weatherList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    }
}
