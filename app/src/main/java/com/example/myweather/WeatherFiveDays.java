package com.example.myweather;

import android.util.Log;
import android.widget.Toast;

import com.kwabenaberko.openweathermaplib.constants.Units;
import com.kwabenaberko.openweathermaplib.implementation.OpenWeatherMapHelper;
import com.kwabenaberko.openweathermaplib.implementation.callbacks.ThreeHourForecastCallback;
import com.kwabenaberko.openweathermaplib.models.threehourforecast.ThreeHourForecast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class WeatherFiveDays
{
    private OpenWeatherMapHelper helper = new OpenWeatherMapHelper("edda4badb05cc4d5f46bc0152c1d13fc");
    private double longi, lat;
    private String city;
    private ArrayList<String> days = new ArrayList<>();
    private ArrayList<Integer> imgs = new ArrayList<>();
    private ArrayList<String> temps = new ArrayList<>();

    public WeatherFiveDays(double latitude, double longitude)
    {
        longi=longitude;
        lat=latitude;
        helper.setUnits(Units.METRIC);
        helper.getThreeHourForecastByGeoCoordinates(lat, longi, new ThreeHourForecastCallback() {
            @Override
            public void onSuccess(ThreeHourForecast weather) {
                Log.d("DEBUGG", "City/Country: " + weather.getCity().getName() + "/" + weather.getCity().getCountry() + "\n \n");

                setArrays(weather);
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.d("DEBUGG", throwable.getMessage());
            }
        });


    }

    public ArrayList<String> getDays() {
        return days;
    }

    public ArrayList<Integer> getImgs() {
        return imgs;
    }

    public ArrayList<String> getTemps() {
        return temps;
    }

    private void setArrays(ThreeHourForecast weather)
    {
        temps = getAverageTemp(weather);
        days = getDays(weather);
        imgs = getIcons(weather);
    }

    private ArrayList<Integer> getIcons(ThreeHourForecast weather)
    {
        int n = weather.getList().size();

        ArrayList<Date> arrayDate = new ArrayList<>();

        for(int i=0; i<n; i++)
        {
            Date temp = getDateString(weather.getList().get(i).getDtTxt(), true);
            arrayDate.add(temp);
        }

        Date aux = arrayDate.get(0);
        ArrayList<Integer> positions = new ArrayList<>();

        for(int i=1; i<n; i++)
        {
            if(aux.equals(arrayDate.get(i)))
            {
                continue;
            }
            else
            {
                aux = arrayDate.get(i);
                positions.add(i);
            }
        }
        positions.add(n-1);

        int k=0;
        ArrayList<String> main = new ArrayList<>();

        for(int i=0; i<n; i++)
        {
            if(i==positions.get(k))
            {
                int med;
                if(k==0)
                {
                    med = i/2;
                }
                else
                {
                    med=i-positions.get(k-1);
                    med=med/2;
                }
                main.add(weather.getList().get(med).getWeatherArray().get(0).getMain());
                k++;
            }
            else
            {

            }

        }

        ArrayList<Integer> icons = new ArrayList<>();

        for(int i=0; i<main.size(); i++)
        {
            switch (main.get(i))
            {
                case "Rain" : icons.add(R.drawable.rain_cloud);
                break;
                case "Clear": icons.add(R.drawable.sun);
                break;
                case "Clouds": icons.add(R.drawable.sun_clouds);
                break;
                case "Snow": icons.add(R.drawable.snow);
                break;
                case "Storm": icons.add(R.drawable.thunder_storm);
                break;
            }
        }

        return icons;

    }

    private ArrayList<String> getDays(ThreeHourForecast weather)
    {
        int n = weather.getList().size();

        ArrayList<String> dayOfWeek = new ArrayList<>();
            Date temp = getDateString(weather.getList().get(0).getDtTxt(), true);
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.setTime(temp);
            int day = calendar.get(Calendar.DAY_OF_WEEK);
            for(int i=0; i<5; i++)
            {
                switch (day) {
                    case 2:
                        dayOfWeek.add("MONDAY");
                        break;
                    case 3:
                        dayOfWeek.add("TUESDAY");
                        break;
                    case 4:
                        dayOfWeek.add("WEDNESDAY");
                        break;
                    case 5:
                        dayOfWeek.add("THURSDAY");
                        break;
                    case 6:
                        dayOfWeek.add("FRIDAY");
                        break;
                    case 7:
                        dayOfWeek.add("SATURDAY");
                        break;
                    case 1:
                        dayOfWeek.add("SUNDAY");
                        break;
                    default:
                        break;
                }
                day++;
                if(day==1)
                    day=1;

            }


        return dayOfWeek;
    }

    private ArrayList<String> getAverageTemp(ThreeHourForecast weather)
    {
        int n = weather.getList().size();

        ArrayList<Date> arrayDate = new ArrayList<>();

        for(int i=0; i<n; i++)
        {
            Date temp = getDateString(weather.getList().get(i).getDtTxt(), true);
            arrayDate.add(temp);
        }

        Date aux = arrayDate.get(0);
        ArrayList<Integer> dayAverage = new ArrayList<>();

        int sum=0;
        int k=1;

        for(int i=1; i<n; i++)
        {
            if(aux.equals(arrayDate.get(i)))
            {
                sum = sum + (int)weather.getList().get(i).getMain().getTemp();
                k++;
            }
            else
            {
                aux = arrayDate.get(i);
                sum = sum / k;
                dayAverage.add(sum);
                sum = (int)weather.getList().get(i).getMain().getTemp();
                k=1;
            }
        }
        sum = sum / k;
        dayAverage.add(sum);

        ArrayList<String> result = new ArrayList<>();

        for(int i=0; i<dayAverage.size(); i++)
        {
            result.add(dayAverage.get(i).toString());
        }

        return result;
    }

    private Date getDateString(String sDate, Boolean simple)
    {
        SimpleDateFormat dataFormat;
        if(simple)
            dataFormat = new SimpleDateFormat("yyyy-MM-dd");
        else
            dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        Date date= new Date();
        try {
            date = dataFormat.parse(sDate);
        }catch (ParseException e){}

        //Log.d("DEBUGG", date.getTime()+"");
        return date;
    }


}
