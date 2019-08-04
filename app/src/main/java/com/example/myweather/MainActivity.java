package com.example.myweather;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.kwabenaberko.openweathermaplib.models.threehourforecast.ThreeHourForecast;

import java.util.ArrayList;

import mumayank.com.airlocationlibrary.AirLocation;

public class MainActivity extends AppCompatActivity {

    private AirLocation airLocation;
    private double longi, lat;
    RecyclerView perday;
    private ArrayList<String> days = new ArrayList<>();
    private ArrayList<Integer> imgs = new ArrayList<>();
    private ArrayList<String> temps = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        perday = findViewById(R.id.prognosysDays);

        // Fetch location simply like this whenever you need
        airLocation = new AirLocation(this, true, true, new AirLocation.Callbacks() {
            @Override
            public void onSuccess(@NonNull Location location)
            {
                // do something
                lat = location.getLatitude();
                longi = location.getLongitude();
                locationGot();

            }

            @Override
            public void onFailed(@NonNull AirLocation.LocationFailedEnum locationFailedEnum)
            {
                // do something
            }

        });


    }

    private void locationGot()
    {
        WeatherFiveDays mWeather = new WeatherFiveDays(lat,longi);

        days = mWeather.getDays();
        imgs = mWeather.getImgs();
        temps = mWeather.getTemps();

        Log.d("DEBUGG", "Location Done ....updating screen");
        updateScreen();

    }

    private void updateScreen()
    {
        initRecycleView();


    }


    // override and call airLocation object's method by the same name
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        airLocation.onActivityResult(requestCode, resultCode, data);
    }

    // override and call airLocation object's method by the same name
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        airLocation.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    void initRecycleView()
    {
        Log.d("DEBUGG", "RecycleView is set");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        perday.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(days, imgs, temps, this);
        perday.setAdapter(adapter);
    }



}
