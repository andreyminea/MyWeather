package com.example.myweather;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
{
    final String TAG = "RecyclerViewAdapter";
    private ArrayList<String> days = new ArrayList<>();
    private ArrayList<Integer> imgs = new ArrayList<>();
    private ArrayList<String> temps = new ArrayList<>();
    private Context context;

    public RecyclerViewAdapter(ArrayList<String> days, ArrayList<Integer> imgs, ArrayList<String> temps, Context context) {
        this.days = days;
        this.imgs = imgs;
        this.temps = temps;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate((R.layout.future_days), parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.day.setText((days.get(position)));
        holder.image.setImageResource(imgs.get(position));
        Log.d("DEBUGG",""+imgs.get(position));
        holder.temp.setText(temps.get(position));

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "You pressed on : "+days.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return imgs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView image;
        TextView day;
        TextView temp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.weatherImg);
            day = itemView.findViewById(R.id.dayName);
            temp = itemView.findViewById(R.id.dayTemp);
        }
    }



}
