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

public class MainActivity extends AppCompatActivity implements ICallBack {

    private AirLocation airLocation;
    private double longi, lat;
    RecyclerView perday;
    private ArrayList<String> days = new ArrayList<>();
    private ArrayList<Integer> imgs = new ArrayList<>();
    private ArrayList<String> temps = new ArrayList<>();
    LinearLayoutManager layoutManager;
    RecyclerViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        perday = findViewById(R.id.prognosysDays);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        perday.setLayoutManager(layoutManager);
        adapter = new RecyclerViewAdapter(days, imgs, temps, this);
        perday.setAdapter(adapter);

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
        WeatherFiveDays mWeather = new WeatherFiveDays(lat,longi, this);



        days = mWeather.getDays();
        imgs = mWeather.getImgs();
        temps = mWeather.getTemps();

        Log.d("DEBUGG", "List size: " + mWeather.getDays().size());

        Log.d("DEBUGG", "Location Done ....updating screen");


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

        adapter = new RecyclerViewAdapter(days, imgs, temps, this);
        perday.setAdapter(adapter);

        Log.d("DEBUGG", "RecycleView size: " + adapter.getItemCount());

    }


    @Override
    public void callback(ArrayList<String> callTemps, ArrayList<String> callDays, ArrayList<Integer> callImgs)
    {
        days = callDays;
        temps = callTemps;
        imgs = callImgs;
        updateScreen();

    }
}
