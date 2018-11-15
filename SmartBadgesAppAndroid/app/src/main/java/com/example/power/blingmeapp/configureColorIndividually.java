package com.example.power.blingmeapp;

import android.os.AsyncTask;

import java.io.IOException;

public class configureColorIndividually extends AsyncTask<ConfigureLed , Void, Void> {


    @Override
    protected  Void doInBackground(ConfigureLed... configureLeds) {

        ConfigureLed configureLed = configureLeds[0];
        try {
            configureLed.configureColors(configureLed.getIpAdress(), configureLed.getColorStetting());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


}
