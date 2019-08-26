package com.example.myweather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

public class DayActivity extends AppCompatActivity implements IDayCallback {

    /*
        activitatea asta apare cand utilizatorul a dat click pe una din zilele ce apare in activitatea principala

        Si in activitatea asta avem un recycler view ce contine orele prognozei, iconita si temp de la ora respectiva
     */
    WeatherDay weather;
    RecyclerView recyclerView;
    TextView weekDay;

    LinearLayoutManager layoutManager;
    DayRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);

        recyclerView = findViewById(R.id.hours_temps);
        weekDay = findViewById(R.id.week_day);

        /*
            Cand am deschis activitatea asta din main am trimis variabila , iar mai jos le luam si le salvam in activitatea asta
            pentru a ne folosi de ele mai tarziu
         */
        Intent intent = getIntent();
        double lt = intent.getDoubleExtra("latitude", 30);
        double ld = intent.getDoubleExtra("longitude", 30);

        int nr = intent.getIntExtra("position", 0);


        /*
        Aici avem o clasa care va transmite serverului, la fel in mai, lat si long,  iar noi ne asteaptam sa ne intoarca liste cu orele
        iconitele si temperatura
         Toate listele au poziliie conectate, adica sunt corespunzatoare

         */
        weather = new WeatherDay(ld, lt,this, nr);

        weekDay.setText(intent.getStringExtra("day"));

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    public void callback()
    {
        /*
            Aici se primeste confirmarea ca datele au fost luate cu succes de la server si
            putem updata UI-ul
         */
        init_recyclerView();

    }

    private void init_recyclerView()
    {
        /*
            La fel ca in main folosim un adapter custom pt a pune datele in recycler view
         */
        adapter = new DayRecyclerViewAdapter(weather.getHours(),weather.getImgs(), weather.getTemps(), weather.getMains(),getApplicationContext());
        recyclerView.setAdapter(adapter);
    }
}
