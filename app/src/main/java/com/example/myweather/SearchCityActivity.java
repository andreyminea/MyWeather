package com.example.myweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchCityActivity extends AppCompatActivity implements ISearchCallBack{

    ImageView icon;
    TextView cityName;
    TextView cityMain;
    TextView temp;
    TextView tempMin;
    TextView tempMax;
    TextView humidity;
    TextView pressure;
    TextView wind;
    WeatherInCity weather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_city);

        String city = getIntent().getStringExtra("String_City");

        Log.d("DEBUGG", "\n \n \n \n \n \n");

        Log.d("DEBUGG", "ON ACTIVITY CREATE");

        icon = findViewById(R.id.cityArt);
        cityName = findViewById(R.id.city);
        cityMain = findViewById(R.id.cityCondition);
        temp = findViewById(R.id.currCityTemp);
        tempMin = findViewById(R.id.lowTemp);
        tempMax = findViewById(R.id.highTemp);
        humidity = findViewById(R.id.Humidity);
        pressure = findViewById(R.id.Pressure);
        wind = findViewById(R.id.Wind);

        weather = new WeatherInCity(city, this);

    }


    @Override
    public void callback(String city)
    {
        cityName.setText(city);
        cityMain.setText(weather.getMain());
        icon.setImageResource(weather.getIcon());
        temp.setText(String.valueOf(weather.getTemp())+"°C");
        tempMax.setText(String.valueOf(weather.getTempMax())+"°C");
        tempMin.setText(String.valueOf(weather.getTempMin())+"°C");
        humidity.setText(String.valueOf(weather.getHumidity()));
        pressure.setText(String.valueOf(weather.getPressure()));
        wind.setText(String.valueOf(weather.getWind()));

    }

    @Override
    public void exists()
    {
        Toast.makeText(SearchCityActivity.this, "Incorrect city", Toast.LENGTH_SHORT).show();
        finish();
    }
}
