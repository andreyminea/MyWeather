package com.example.myweather;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import mumayank.com.airlocationlibrary.AirLocation;

public class MainActivity extends AppCompatActivity implements IMainCallBack, OpenDayCallback {

    private AirLocation airLocation;
    private double longi, lat;
    RecyclerView perday;
    /*
        Am declarat liste cu zilele, resursele pt imagini si temperatura
        fiecare pozitie din liste este corespondenta
        ex pozitia 0 din fiecare lista cuprinde numele zilei curente, iconita de vreme si temperatura zilei
        pozitia 1 urmatoarea zi cu icontita si temp si asa mai departe
     */
    private ArrayList<String> days = new ArrayList<>();
    private ArrayList<Integer> imgs = new ArrayList<>();
    private ArrayList<String> temps = new ArrayList<>();

    RelativeLayout background;

    WeatherFiveDays mWeather;
    SearchView searchBar;

    ImageView todayIcon;
    TextView todayTemp;
    TextView todayForecast;
    TextView currentCity;

    LinearLayoutManager layoutManager;
    RecyclerViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // am declarat resursele din xml aici
        todayIcon = findViewById(R.id.weatherArt);
        todayTemp = findViewById(R.id.cityTemp);
        currentCity = findViewById(R.id.cityShow);
        todayForecast = findViewById(R.id.cityCondition);
        background = findViewById(R.id.background);
        searchBar = findViewById(R.id.searchBar);
        perday = findViewById(R.id.prognosysDays);

        // aici am setat layout managerul pt a arata recycle view pe horizontala
        // se poate obs parametru horizontal
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        perday.setLayoutManager(layoutManager);


        /*
        mai jos este o metoda care se declaseaza de fiecare dta cand cineva apasa pe submit de la tastatura si
        ia textul scris in searbar si il transmite activitatii search city activity
         */
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String city = searchBar.getQuery().toString();
                //Toast.makeText(MainActivity.this, city, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, SearchCityActivity.class);
                intent.putExtra("String_City", city);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // Fetch location simply like this whenever you need
        /*
        mai jos parametrul airlocation va obtine long si lat device ului
        pentru a obtine asta se foloseste un api numit airlocation si acest api ne permite sa
        intrebam user ul de permisii( al doilea parametru din airLocation sa fie true) pentru ca in cazul in care aplicatia nu are permisiunea
        de a se folosi GPS ul, aplicatia va ruga user ul sa acorde aceastsa prioritate
         */
        airLocation = new AirLocation(this, true, true, new AirLocation.Callbacks() {
            @Override
            public void onSuccess(@NonNull Location location)
            {
                // do something
                lat = location.getLatitude();
                longi = location.getLongitude();
                //in mom asta avem lat si long si se va declkansa functia locationGot()
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
        /*
        aici se face apelarea la server prin clasa weather five days
        aceasta apelare necesita long si lat, exact ce s-a obtinut mai sus
         */
        mWeather = new WeatherFiveDays(lat,longi, this);
    }

    private void updateScreen()
    {
        initRecycleView();
        Log.d("DEBUGG", "\n \n \n \n \n \n");
        Log.d("DEBUGG", "" + days.size());
        /*
        am vorbit mai jos ce face...
         */

        todayIcon.setImageResource(imgs.get(0));
        todayTemp.setText(temps.get(0)+"°C");
        todayForecast.setText(mWeather.getForecastToday());
        currentCity.setText(mWeather.getCity());
        setDayNight();

    }

    private void setDayNight()
    {
        /*
        metoda asta seteaza imaginbea de fundal a ecranului in functie de ora
        deci la o ora de seara vom avea o imagine de fundal  mai inchisa si ziuya una mai deschisa
         */
        Date currentTime = Calendar.getInstance().getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentTime);
        int hour = calendar.get(Calendar.HOUR);
        if(calendar.get(Calendar.PM)==1)
            hour = hour+12;
        if(hour>6 && hour<20)
        {
            background.setBackgroundResource(R.drawable.day);
        }
        else
            background.setBackgroundResource(R.drawable.night);

        Log.d("DEBUGG", hour+ " ORAAAAAAAAAAAAAA" + "");
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

    void initRecycleView()
    {
        /*
            Aici initializez recyclerview cu lista pe care am primit-o de la server
            Practic adapterul se acupa cu logica din spatele recycler view ului si in cazul asta am creat un adapter custom ca
            lucrurile sa fie puse cum doresc eu in pagina
         */

        adapter = new RecyclerViewAdapter(days, imgs, temps, this, this);
        perday.setAdapter(adapter);

    }


    @Override
    public void callback(ArrayList<String> callTemps, ArrayList<String> callDays, ArrayList<Integer> callImgs)
    {
        /*
            Aici mi se intrc listele cu valorile de este nevoiue
         */
        days = callDays;
        temps = callTemps;
        imgs = callImgs;
        // update screen este o metoda creata de mine pentru
        // a initializa UI-ul cu toate datelke primite de la server
        updateScreen();

    }

    @Override
    public void open(int day)
    {
        /*
            Asta este metoda din callback care vine in momentul in care utilizatorul apasa pe
            o zi si doreste mai multe detalii legate de ziua aia

            Pentru a-mi fi mai usor transmit activitatii day activiti cateva variabile prin intermediul
            intentului
         */
        Intent intent = new Intent(MainActivity.this, DayActivity.class);
        intent.putExtra("longitude",longi);
        intent.putExtra("latitude", lat);
        intent.putExtra("position", day);
        intent.putExtra("day", days.get(day));
        startActivity(intent);
    }
}
