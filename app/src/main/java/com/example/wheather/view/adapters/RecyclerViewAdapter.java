package com.example.wheather.view.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wheather.R;
import com.example.wheather.service.model.Temperature;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.example.wheather.WeatherApplication.getAppContext;

public class RecyclerViewAdapter extends RecyclerView.Adapter {

    private ArrayList<Temperature> data;

    public RecyclerViewAdapter(ArrayList<Temperature> list) {
        data = list;
    }

    public void updateRecyclerView() {
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(getAppContext()).inflate(R.layout.rv_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ItemViewHolder) holder).textView.setText("Temp is " + data.get(position).getTemp());
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: " + data.size());
        return data.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_item_weather);
        }
    }
}
