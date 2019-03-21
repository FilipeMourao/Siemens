package com.example.power.blingmeapp.Objects;

import android.os.AsyncTask;

import java.io.IOException;

public class configureColorIndividually extends AsyncTask<ConfigureLed , Void, Void> {


    @Override
    protected  Void doInBackground(ConfigureLed... configureLeds) {

        ConfigureLed configureLed = configureLeds[0];
        configureLed.configureColors(configureLed.getIpAdress(), configureLed.getColorStetting());
        return null;
    }


}
