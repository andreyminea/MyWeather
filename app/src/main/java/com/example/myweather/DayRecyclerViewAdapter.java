package com.example.myweather;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DayRecyclerViewAdapter extends RecyclerView.Adapter<DayRecyclerViewAdapter.DayViewHolder>
{
    private ArrayList<String> hours = new ArrayList<>();
    private ArrayList<Integer> imgs = new ArrayList<>();
    private ArrayList<String> temps = new ArrayList<>();
    private ArrayList<String> mains = new ArrayList<>();
    private Context context;

    public DayRecyclerViewAdapter(ArrayList<String> hours, ArrayList<Integer> imgs, ArrayList<String> temps, ArrayList<String> mains, Context context) {
        this.hours = hours;
        this.imgs = imgs;
        this.temps = temps;
        this.mains = mains;
        this.context = context;
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate((R.layout.day), parent, false);

        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, final int position) {
        holder.image.setImageResource(imgs.get(position));
        holder.temp.setText(temps.get(position));
        holder.hour.setText(hours.get(position));
        holder.main.setText(mains.get(position));

    }

    @Override
    public int getItemCount() {
        return imgs.size();
    }

    public class DayViewHolder extends RecyclerView.ViewHolder
    {
        ImageView image;
        TextView hour;
        TextView temp;
        TextView main;

        public DayViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.hour_sky);
            hour = itemView.findViewById(R.id.hour);
            temp = itemView.findViewById(R.id.hour_temp);
            main = itemView.findViewById(R.id.hour_main);
        }
    }



}
