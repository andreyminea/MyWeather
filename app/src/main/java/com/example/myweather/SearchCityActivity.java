package com.example.myweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchCityActivity extends AppCompatActivity implements ISearchCallBack{

    /*
        in aceasta activitate se ajunge dca dorim cautarea vremii in unul din orase adica
        din main noi scriem in searchbar si dam submit
     */
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
        /*
            declar toate componentele vizuale ale activitatii
         */

        icon = findViewById(R.id.cityArt);
        cityName = findViewById(R.id.city);
        cityMain = findViewById(R.id.cityCondition);
        temp = findViewById(R.id.currCityTemp);
        tempMin = findViewById(R.id.lowTemp);
        tempMax = findViewById(R.id.highTemp);
        humidity = findViewById(R.id.Humidity);
        pressure = findViewById(R.id.Pressure);
        wind = findViewById(R.id.Wind);

        /*
            apelez clasa weather in city ce vca lua date de server
            pentru a lua date de la server, de aceasta data, voi trimite serverului orasul introdus de utilizator

         */
        weather = new WeatherInCity(city, this);

    }


    @Override
    public void callback(String city)
    {
        /*
            aceasta metoda se declaseaza cand serverul a returnat cu succes toate detaliile meteo legate de oras si imi face update la comp
            vizuale alea activitatiii, si deci pune datle orasului
         */
        cityName.setText(city);
        cityMain.setText(weather.getMain());
        icon.setImageResource(weather.getIcon());
        temp.setText(String.valueOf(weather.getTemp())+"°C");
        tempMax.setText(String.valueOf(weather.getTempMax())+"°C");
        tempMin.setText(String.valueOf(weather.getTempMin())+"°C");
        humidity.setText(String.valueOf(weather.getHumidity())+ " g/Hg");
        pressure.setText(String.valueOf(weather.getPressure())+ " Pa");
        wind.setText(String.valueOf(weather.getWind())+ " Km/h");

    }

    @Override
    public void exists()
    {
        /*
            Metoda asta se declanseaza cand serverul returneaza o eroare, am apreciat ca in cele mai multe cazul serverul va returna eroarea
            ca orasul introdus nu exista si in cazul aceasta oprim activitatea curenta, iar aplicatia va revenii la activitatea principala
            Inchidera activitatii se f`ce impreuna cu un toast( mesaj in josul ecranului) care indica utilizatorului ca a introdus un oras
            ce n u exista
         */
        Toast.makeText(SearchCityActivity.this, "Incorrect city", Toast.LENGTH_SHORT).show();
        finish();
    }
}
