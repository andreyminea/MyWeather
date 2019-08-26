package com.example.myweather;

import android.util.Log;

import com.kwabenaberko.openweathermaplib.constants.Units;
import com.kwabenaberko.openweathermaplib.implementation.OpenWeatherMapHelper;
import com.kwabenaberko.openweathermaplib.implementation.callbacks.ThreeHourForecastCallback;
import com.kwabenaberko.openweathermaplib.models.threehourforecast.ThreeHourForecast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class WeatherDay
{
    private ArrayList<String> hours = new ArrayList<>();
    private ArrayList<Integer> imgs = new ArrayList<>();
    private ArrayList<String> temps = new ArrayList<>();
    private ArrayList<String> mains = new ArrayList<>();
    int position;

    IDayCallback iDayCallback;
    private OpenWeatherMapHelper helper = new OpenWeatherMapHelper("edda4badb05cc4d5f46bc0152c1d13fc");

    public WeatherDay(double latitude, double longitude, final IDayCallback iDayCallback, int pos) {
        this.iDayCallback = iDayCallback;
        this.position = pos;

        helper.setUnits(Units.METRIC);
        helper.getThreeHourForecastByGeoCoordinates(latitude, longitude, new ThreeHourForecastCallback() {
            @Override
            public void onSuccess(ThreeHourForecast weather) {
                Log.d("DEBUGG", "City/Country: " + weather.getCity().getName() + "/" + weather.getList().get(4).getWeatherArray().get(0).getIcon() + "\n \n");


                setEverything(weather, position);
                iDayCallback.callback();

            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.d("DEBUGG", throwable.getMessage());
            }
        });

    }

    private void setEverything(ThreeHourForecast weather, int position)
    {
        /*
            In cazul asta ne-a trebuit array ul cu dates, dar sa contina si orele
            Am stabilit unde se termina ziua si am pus temperaturile in lista de temp
         */
        int n = weather.getList().size();

        ArrayList<Date> arrayDate = new ArrayList<>();

        for(int i=0; i<n; i++)
        {
            Date temp = getDateString(weather.getList().get(i).getDtTxt(), true);
            arrayDate.add(temp);
        }

        Date aux = arrayDate.get(0);
        ArrayList<String> main = new ArrayList<>();
        ArrayList<String> forecastDay = new ArrayList<>();
        int k = 0;
        int i;
        i=0;
        if(k!=position) {
            for (i = 0; i < n; i++) {
                if (!aux.equals(arrayDate.get(i))) {
                    aux = arrayDate.get(i);
                    k++;
                    if (k == position)
                        break;
                }
            }
        }
        int begin = i;
        /*
            Aici s-a pus prognoza si in functie de prognoza s-a ales si o imagine sugestiva
         */

            for (i = begin; i < n; i++) {
                if (aux.equals(arrayDate.get(i)))
                {
                    Log.d("DEBUGG", weather.getList().get(i).getMain().getTemp() + "\n Temperaturaaa");
                    temps.add(String.valueOf((int) weather.getList().get(i).getMain().getTemp())+"°C");
                    mains.add(weather.getList().get(i).getWeatherArray().get(0).getMain());
                    String thisMain = mains.get(mains.size() - 1);
                    switch (thisMain) {
                        case "Rain": {
                            imgs.add(R.drawable.rain_cloud);
                            break;
                        }
                        case "Clear": {
                            imgs.add(R.drawable.sun);
                            break;
                        }
                        case "Clouds": {
                            imgs.add(R.drawable.sun_clouds);
                            break;
                        }
                        case "Snow": {
                            imgs.add(R.drawable.snow);
                            break;
                        }
                        case "Storm": {
                            imgs.add(R.drawable.thunder_storm);
                            break;
                        }
                        default: {
                            imgs.add(R.drawable.cloud);
                            break;
                        }
                    }

                    int dayHour = getDateString(weather.getList().get(i).getDtTxt());
                    if(dayHour==0)
                        dayHour=12;
                    hours.add(dayHour+":00");
                } else
                    break;
            }

    }



    private Date getDateString(String sDate, Boolean simple)
    {
        SimpleDateFormat dataFormat;
        if(simple)
            dataFormat = new SimpleDateFormat("yyyy-MM-dd");
        else
            dataFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");

        Date date= new Date();
        try {
            date = dataFormat.parse(sDate);
        }catch (ParseException e){}

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        //Log.d("DEBUGG", "\n" + calendar.get(Calendar.HOUR_OF_DAY) + "\n");
        return date;
    }

    private int getDateString(String sDate)
    {
        SimpleDateFormat dataFormat;
        dataFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");

        Date date= new Date();
        try {
            date = dataFormat.parse(sDate);
        }catch (ParseException e){}

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        Log.d("DEBUGG", "\n" + sDate + "\n");
        Log.d("DEBUGG", "\n" +calendar.get(Calendar.HOUR_OF_DAY) + "\n");

        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public ArrayList<String> getHours() {
        return hours;
    }

    public ArrayList<Integer> getImgs() {
        return imgs;
    }

    public ArrayList<String> getTemps() {
        return temps;
    }

    public ArrayList<String> getMains() {
        return mains;
    }

}
