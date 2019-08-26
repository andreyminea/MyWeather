package com.example.myweather;

import android.util.Log;

import com.kwabenaberko.openweathermaplib.constants.Units;
import com.kwabenaberko.openweathermaplib.implementation.OpenWeatherMapHelper;
import com.kwabenaberko.openweathermaplib.implementation.callbacks.CurrentWeatherCallback;
import com.kwabenaberko.openweathermaplib.models.currentweather.CurrentWeather;

public class WeatherInCity
{
    /*
        Aici ni s-au returnat date cu privire la singura zi, am folosit o medtoda oferita de Api diferita fata de celelalte
        clase care comunica cu serverul, si anume cea care ne intorce date meteo corespunzatoare u nui oras
     */
    private double Humidity;
    private double Pressure;
    private int Temp;
    private double Wind;
    private int Icon;
    private int TempMin;
    private int TempMax;
    private String Main;
    private ISearchCallBack iSearchCallBack;

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

                //debug(currentWeather);
                setEverything(currentWeather);
                iSearchCallBack.callback(currentWeather.getName().toString());

            }

            @Override
            public void onFailure(Throwable throwable) {

                Log.d("DEBUGG", "ERROR !!!!!!!!!!!!!");
                iSearchCallBack.exists();
            }



        });
    }

    private void setEverything(CurrentWeather currentWeather)
    {
        /*
            Aici pur si simplu s-au luat date din elementul returnat de api si s-au pus in variabilele declarate in clasa

            ca apoi cu cu metode de get sa se poata apela in activitatea ce are nevoie de aceste date( search city activ)
         */
        Humidity = currentWeather.getMain().getHumidity();
        Pressure = currentWeather.getMain().getPressure();
        Temp = (int)currentWeather.getMain().getTemp();
        Wind = currentWeather.getWind().getSpeed();
        Main = currentWeather.getWeather().get(0).getMain();
        TempMax = (int)currentWeather.getMain().getTempMax();
        TempMin = (int)currentWeather.getMain().getTempMin();

        String main = currentWeather.getWeather().get(0).getMain();
        switch (main)
        {
            case "Rain" :
            {
                Icon = R.drawable.rain_cloud;
                break;
            }
            case "Clear":
            {
                Icon = R.drawable.sun;
                break;
            }
            case "Clouds":
            {
                Icon = R.drawable.sun_clouds;
                break;
            }
            case "Snow":
            {
                Icon = R.drawable.snow;
                break;
            }
            case "Storm":
            {
                Icon = R.drawable.thunder_storm;
                break;
            }
            default:
            {
                Icon = R.drawable.cloud;
                break;
            }
        }

    }

    private void debug(CurrentWeather currentWeather)
    {
        Log.d("DEBUGG", currentWeather.getWeather().get(0).getMain());
    }

    public double getHumidity() {
        return Humidity;
    }

    public double getPressure() {
        return Pressure;
    }

    public int getTemp() {
        return Temp;
    }

    public double getWind() {
        return Wind;
    }

    public int getIcon() {
        return Icon;
    }

    public int getTempMin() {
        return TempMin;
    }

    public int getTempMax() {
        return TempMax;
    }

    public String getMain() {
        return Main;
    }

    public OpenWeatherMapHelper getHelper() {
        return helper;
    }

}
