package com.example.wheather.view.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wheather.R;
import com.example.wheather.databinding.DialogFragmentDetailedWeatherForcastBinding;
import com.example.wheather.service.model.Temperature;
import com.example.wheather.view.adapters.RecyclerViewAdapter;

import java.util.ArrayList;

public class DetailedWeatherForcastDialogFragment extends DialogFragment {
    private RecyclerView detailedWeatherForcastRecyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;

    public static DetailedWeatherForcastDialogFragment newInstance() {
        Bundle args = new Bundle();

        DetailedWeatherForcastDialogFragment fragment = new DetailedWeatherForcastDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        DialogFragmentDetailedWeatherForcastBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.dialog_fragment_detailed_weather_forcast,
                null,
                false);
        View view = binding.getRoot();
        detailedWeatherForcastRecyclerView = binding.rvDetailedWeatherForecast;
        detailedWeatherForcastRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ArrayList<Temperature> tmp = new ArrayList<Temperature>();
        recyclerViewAdapter = new RecyclerViewAdapter(tmp);

        detailedWeatherForcastRecyclerView.setAdapter(recyclerViewAdapter);

        Dialog dialog = new Dialog(getContext(), R.style.ThemeOverlay_AppCompat_Dialog);
        dialog.setContentView(view);
        dialog.show();

        return dialog;
    }
}
