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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kwabenaberko.openweathermaplib.models.threehourforecast.ThreeHourForecast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import mumayank.com.airlocationlibrary.AirLocation;

public class MainActivity extends AppCompatActivity implements ICallBack {

    private AirLocation airLocation;
    private double longi, lat;
    RecyclerView perday;
    private ArrayList<String> days = new ArrayList<>();
    private ArrayList<Integer> imgs = new ArrayList<>();
    private ArrayList<String> temps = new ArrayList<>();

    RelativeLayout background;

    WeatherFiveDays mWeather;

    ImageView todayIcon;
    TextView todayTemp;
    TextView todayForecast;
    TextView currentCity;

    LinearLayoutManager layoutManager;
    RecyclerViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        todayIcon = findViewById(R.id.weatherArt);
        todayTemp = findViewById(R.id.cityTemp);
        currentCity = findViewById(R.id.cityShow);
        todayForecast = findViewById(R.id.cityCondition);
        background = findViewById(R.id.background);


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
        mWeather = new WeatherFiveDays(lat,longi, this);
        days = mWeather.getDays();
        imgs = mWeather.getImgs();
        temps = mWeather.getTemps();
    }

    private void updateScreen()
    {
        initRecycleView();
        todayIcon.setImageResource(imgs.get(0));
        todayTemp.setText(temps.get(0)+"°C");
        todayForecast.setText(mWeather.getForecastToday());
        currentCity.setText(mWeather.getCity());
        setDayNight();

    }

    private void setDayNight()
    {
        Date currentTime = Calendar.getInstance().getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentTime);
        Log.d("DEBUGG", calendar.get(Calendar.HOUR)+ "ORAAAAAAAAAAAAAA" +
                "");
        if(calendar.get(Calendar.HOUR)>7 && calendar.get(Calendar.HOUR)<20)
        {
            background.setBackgroundResource(R.drawable.sky_day);
        }
        else
            background.setBackgroundResource(R.drawable.nisght_sky);

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

        adapter = new RecyclerViewAdapter(days, imgs, temps, this);
        perday.setAdapter(adapter);

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
