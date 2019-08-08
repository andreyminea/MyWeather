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
import java.util.GregorianCalendar;

public class WeatherFiveDays
{
    IMainCallBack iMainCallBack;

    private OpenWeatherMapHelper helper = new OpenWeatherMapHelper("edda4badb05cc4d5f46bc0152c1d13fc");
    private double longi, lat;
    private String city;
    private String forecastToday;
    private ArrayList<String> days = new ArrayList<>();
    private ArrayList<Integer> imgs = new ArrayList<>();
    private ArrayList<String> temps = new ArrayList<>();

    public WeatherFiveDays(double latitude, double longitude, IMainCallBack ic)
    {
        iMainCallBack =ic;
        longi=longitude;
        lat=latitude;
        helper.setUnits(Units.METRIC);
        helper.getThreeHourForecastByGeoCoordinates(lat, longi, new ThreeHourForecastCallback() {
            @Override
            public void onSuccess(ThreeHourForecast weather) {
                Log.d("DEBUGG", "City/Country: " + weather.getCity().getName() + "/" + weather.getList().get(4).getWeatherArray().get(0).getIcon() + "\n \n");

                temps = getAverageTemp(weather);
                days = getDays(weather);
                imgs = getIcons(weather);

                city = weather.getCity().getName();

                iMainCallBack.callback(temps, days, imgs);

            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.d("DEBUGG", throwable.getMessage());
            }
        });


    }

    private void bebug(ThreeHourForecast weather)
    {
        int n = weather.getList().size();

        String debug = "TEMPERATURE \n";
        for(int i=0; i<n; i++)
        {
            debug = debug + weather.getList().get(i).getDtTxt() + "\n";
        }

        //Log.d("DEBUGG", debug);

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
        ArrayList<String> main = new ArrayList<>();
        ArrayList<String> forecastDay = new ArrayList<>();

        for(int i=0; i<n; i++)
        {
            if(aux.equals(arrayDate.get(i)))
            {
                forecastDay.add(weather.getList().get(i).getWeatherArray().get(0).getMain());
            }
            else
            {
                aux = arrayDate.get(i);

                String forecast = forecastDay.get(forecastDay.size()/2);

                main.add(forecast);

                forecastDay.clear();

                forecastDay.add(weather.getList().get(i).getWeatherArray().get(0).getMain());
            }
        }
        if(aux.equals(arrayDate.get(n-1)))
        {
            String forecast = forecastDay.get(forecastDay.size()/2);
            main.add(forecast);
        }



        ArrayList<Integer> icons = new ArrayList<>();

        String debug = "\n";

        forecastToday = main.get(0);


        for(int i=0; i<main.size(); i++)
        {
            debug = debug+main.get(i) +"\n";
            switch (main.get(i))
            {
                case "Rain" :
                    {
                        icons.add(R.drawable.rain_cloud);
                        break;
                    }
                case "Clear":
                    {
                        icons.add(R.drawable.sun);
                        break;
                    }
                case "Clouds":
                {
                    icons.add(R.drawable.sun_clouds);
                    break;
                }
                case "Snow":
                {
                    icons.add(R.drawable.snow);
                    break;
                }
                case "Storm":
                {
                    icons.add(R.drawable.thunder_storm);
                    break;
                }
                default:
                {
                    icons.add(R.drawable.cloud);
                    break;
                }
            }
        }

        //Log.d("DEBUGG", debug);

        return icons;

    }

    private ArrayList<String> getDays(ThreeHourForecast weather)
    {
        int n = weather.getList().size();

        ArrayList<String> dayOfWeek = new ArrayList<>();
            Date temp ;
            Calendar calendar;
            temp = getDateString(weather.getList().get(0).getDtTxt(), true);
            calendar = GregorianCalendar.getInstance();
            calendar.setTime(temp);
            int firstDay = calendar.get(Calendar.DAY_OF_WEEK);

            temp = getDateString(weather.getList().get(n-1).getDtTxt(), true);
            calendar = GregorianCalendar.getInstance();
            calendar.setTime(temp);
            int lastDay = calendar.get(Calendar.DAY_OF_WEEK);

            //Log.d("DEBUGG", "" + lastDay);

            while(true)
            {
                switch (firstDay) {
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
                firstDay++;
                if(firstDay==8)
                    firstDay=1;

                if(firstDay-1==lastDay)
                    break;

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

        int max=-100;

        for(int i=0; i<n; i++)
        {
            if(aux.equals(arrayDate.get(i)))
            {
                if(max<(int)weather.getList().get(i).getMain().getTemp())
                    max = (int)weather.getList().get(i).getMain().getTemp();
            }
            else
            {
                aux = arrayDate.get(i);
                //Log.d("DEBUGG", max + " " + min);

                dayAverage.add(max);
                max=-100;
            }
        }
        if(aux.equals(arrayDate.get(n-1)))
        {
            max = (int)weather.getList().get(n-1).getMain().getTemp();
            dayAverage.add(max);
        }

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


    public ArrayList<String> getDays() {
        return days;
    }

    public ArrayList<Integer> getImgs() {
        return imgs;
    }

    public ArrayList<String> getTemps() {
        return temps;
    }

    public String getCity() {
        return city;
    }

    public String getForecastToday() {
        return forecastToday;
    }


}
