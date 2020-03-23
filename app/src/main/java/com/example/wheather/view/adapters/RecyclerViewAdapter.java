package com.example.wheather.view.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wheather.R;
import com.example.wheather.databinding.RvItemBinding;
import com.example.wheather.service.model.Temperature;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static com.example.wheather.WeatherApplication.getAppContext;

public class RecyclerViewAdapter extends RecyclerView.Adapter {

    private ArrayList<Temperature> data;
    private RvItemBinding binding;

    public RecyclerViewAdapter(ArrayList<Temperature> list) {
        data = list;
    }

    public void updateRecyclerView() {
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(getAppContext()), R.layout.rv_item, parent, false);
        View view = binding.getRoot();
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        binding.tvItemWeatherTemp.setText(String.valueOf(data.get(position).getTemp()));
        binding.tvItemTempHigh.setText(String.valueOf(data.get(position).getMaxTemp()));
        binding.tvItemTempLow.setText(String.valueOf(data.get(position).getMinTemp()));
        /*((ItemViewHolder) holder).textView.setText("Temp is " + data.get(position).getTemp());*/
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: " + data.size());
        return data.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView weatherTemp = binding.tvItemWeatherTemp;
        private TextView weatherTempHigh = binding.tvItemTempHigh;
        private TextView weatherTempLow = binding.tvItemTempLow;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
