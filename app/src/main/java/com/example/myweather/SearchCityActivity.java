package com.example.myweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class SearchCityActivity extends AppCompatActivity implements ISearchCallBack{




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_city);

        Log.d("DEBUGG", "\n \n \n \n \n \n");

        Log.d("DEBUGG", "ON ACTIVITY CREATE");

        WeatherInCity weather = new WeatherInCity("Bucharest", this);

    }


    @Override
    public void callback() {

    }
}
