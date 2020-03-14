package com.example.wheather;

import android.app.Application;
import android.content.Context;

public class WeatherApplication extends Application {
    private static Context context;

    public static Context getAppContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
