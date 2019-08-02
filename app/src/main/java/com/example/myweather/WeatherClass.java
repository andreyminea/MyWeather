package com.example.myweather;

import android.nfc.Tag;
import android.util.Log;

import com.kwabenaberko.openweathermaplib.constants.Units;
import com.kwabenaberko.openweathermaplib.implementation.OpenWeatherMapHelper;
import com.kwabenaberko.openweathermaplib.implementation.callbacks.CurrentWeatherCallback;
import com.kwabenaberko.openweathermaplib.implementation.callbacks.ThreeHourForecastCallback;
import com.kwabenaberko.openweathermaplib.models.currentweather.CurrentWeather;
import com.kwabenaberko.openweathermaplib.models.threehourforecast.ThreeHourForecast;

import static android.content.ContentValues.TAG;

public class WeatherClass
{
    private OpenWeatherMapHelper helper = new OpenWeatherMapHelper("edda4badb05cc4d5f46bc0152c1d13fc");
    String city;
    private String TAG = "For testing";


    WeatherClass(double lat, double longt)
    {
        helper.setUnits(Units.METRIC);
        helper.getThreeHourForecastByGeoCoordinates(lat,longt, new ThreeHourForecastCallback() {
            @Override
            public void onSuccess(ThreeHourForecast threeHourForecast) {
                Log.d(TAG, "City/Country: "+ threeHourForecast.getCity().getName() + "/" + threeHourForecast.getCity().getCountry() +"\n"
                        +"Forecast Array Count: " + threeHourForecast.getCnt() +"\n"
                        //For this example, we are logging details of only the first forecast object in the forecasts array
                        +"First Forecast Date Timestamp: " + threeHourForecast.getList().get(0).getDt() +"\n"
                        +"First Forecast WeatherClass Description: " + threeHourForecast.getList().get(0).getWeatherArray().get(0).getDescription()+ "\n"
                        +"First Forecast Max Temperature: " + threeHourForecast.getList().get(0).getMain().getTempMax()+"\n"
                        +"First Forecast Wind Speed: " + threeHourForecast.getList().get(0).getWind().getSpeed() + "\n"
                );
                city = threeHourForecast.getCity().getName();
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.d(TAG, throwable.getMessage());
            }
        });

    }

    String getCity()
    {
        return city;
    }







}
