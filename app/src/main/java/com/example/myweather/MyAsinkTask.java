package com.example.myweather;

import android.os.AsyncTask;

public class MyAsinkTask extends AsyncTask<Void, Void, Void>
{
    private MyEventListener callback;


    public MyAsinkTask(MyEventListener callback) {
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(Void... voids)
    {

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if(callback != null) {
            callback.onEventCompleted();
        }
    }
}
