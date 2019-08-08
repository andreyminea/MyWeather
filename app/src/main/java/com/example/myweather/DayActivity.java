package com.example.myweather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

public class DayActivity extends AppCompatActivity implements IDayCallback {

    WeatherDay weather;
    RecyclerView recyclerView;
    TextView weekDay;

    LinearLayoutManager layoutManager;
    DayRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);

        recyclerView = findViewById(R.id.hours_temps);
        weekDay = findViewById(R.id.week_day);

        Intent intent = getIntent();
        double lt = intent.getDoubleExtra("latitude", 30);
        double ld = intent.getDoubleExtra("longitude", 30);
        int nr = intent.getIntExtra("position", 0);
        weather = new WeatherDay(ld, lt,this, nr);

        weekDay.setText(intent.getStringExtra("day"));

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    public void callback()
    {
        init_recyclerView();

    }

    private void init_recyclerView()
    {
        adapter = new DayRecyclerViewAdapter(weather.getHours(),weather.getImgs(), weather.getTemps(), weather.getMains(),getApplicationContext());
        recyclerView.setAdapter(adapter);
    }
}
