package com.example.myweather;

import android.util.Log;

import com.kwabenaberko.openweathermaplib.constants.Units;
import com.kwabenaberko.openweathermaplib.implementation.OpenWeatherMapHelper;
import com.kwabenaberko.openweathermaplib.implementation.callbacks.CurrentWeatherCallback;
import com.kwabenaberko.openweathermaplib.models.currentweather.CurrentWeather;

public class WeatherInCity
{
    ISearchCallBack iSearchCallBack;
    private OpenWeatherMapHelper helper = new OpenWeatherMapHelper("edda4badb05cc4d5f46bc0152c1d13fc");

    public WeatherInCity(String city, ISearchCallBack ic)
    {
        this.iSearchCallBack = ic;
        helper.setUnits(Units.METRIC);
        helper.getCurrentWeatherByCityName(city, new CurrentWeatherCallback() {
            @Override
            public void onSuccess(CurrentWeather currentWeather) {
                Log.d("DEBUGG", "Coordinates: " + currentWeather.getCoord().getLat() + ", "+currentWeather.getCoord().getLon() +"\n"
                        +"Weather Description: " + currentWeather.getWeather().get(0).getDescription() + "\n"
                        +"Temperature: " + currentWeather.getMain().getTempMax()+"\n"
                        +"Wind Speed: " + currentWeather.getWind().getSpeed() + "\n"
                        +"City, Country: " + currentWeather.getName() + ", " + currentWeather.getSys().getCountry()
                );

                debug(currentWeather);
                iSearchCallBack.callback();

            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.d("DEBUGG", "ERROR !!!!!!!!!!!!!");
            }



        });
    }

    private void debug(CurrentWeather currentWeather)
    {
        Log.d("DEBUGG", currentWeather.getMain().toString());
    }

}
