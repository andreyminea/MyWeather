package com.example.myweather;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.kwabenaberko.openweathermaplib.constants.Units;
import com.kwabenaberko.openweathermaplib.implementation.OpenWeatherMapHelper;
import com.kwabenaberko.openweathermaplib.implementation.callbacks.ThreeHourForecastCallback;
import com.kwabenaberko.openweathermaplib.models.threehourforecast.ThreeHourForecast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import mumayank.com.airlocationlibrary.AirLocation;

public class MainActivity extends AppCompatActivity {

    private AirLocation airLocation;
    private double longi, lat;
    private OpenWeatherMapHelper helper = new OpenWeatherMapHelper("edda4badb05cc4d5f46bc0152c1d13fc");
    private ArrayList<String> days = new ArrayList<>();
    private ArrayList<Integer> imgs = new ArrayList<>();
    private ArrayList<String> temps = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        helper.setUnits(Units.METRIC);
        helper.getThreeHourForecastByGeoCoordinates(lat,longi, new ThreeHourForecastCallback() {
            @Override
            public void onSuccess(ThreeHourForecast weather) {
                Log.d("DEBUGG", "City/Country: "+ weather.getCity().getName() + "/" + weather.getCity().getCountry() +"\n"
                        +"Forecast Array Count: " + weather.getCnt() +"\n"
                        //For this example, we are logging details of only the first forecast object in the forecasts array
                        +"First Forecast Date Timestamp: " + weather.getList().get(0).getDtTxt() +"\n"
                        +"First Forecast WeatherClass Description: " + weather.getList().get(0).getWeatherArray().get(0).getDescription()+ "\n"
                        +"First Forecast Max Temperature: " + weather.getList().get(0).getMain().getTempMax()+"\n"
                        +"First Forecast Wind Speed: " + weather.getList().get(0).getWind().getSpeed() + "\n");

                updateScreen(weather);

                Toast.makeText(MainActivity.this, "Locatia este: " + longi + " " + lat + "    " + weather.getCity().getName(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.d("DEBUGG", throwable.getMessage());
            }
        });


    }

    private void updateScreen(ThreeHourForecast weather)
    {

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");
        Log.d("DEBUGG", dataFormat.format(currentTime));
        String today = dataFormat.format(currentTime);

        int n = weather.getList().size();
        String display="\n"+ weather.getList().size()+"\n";
        Log.d("DEBUGG", "\n");
        for(int i=0; i<n; i++)
        {
           if(weather.getList().get(i).getDtTxt().contains(today))
            display = display + weather.getList().get(i).getDtTxt()+"\n";
            //weather.getList().get(0).getMain().getTemp()

        }

        Log.d("DEBUGG", display);


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


}
