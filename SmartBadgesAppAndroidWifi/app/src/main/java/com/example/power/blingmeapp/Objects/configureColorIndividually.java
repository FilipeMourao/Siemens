package com.example.power.blingmeapp.Objects;

import android.os.AsyncTask;

import java.io.IOException;
// this class sends the color information to the badge, must be an async task
public class configureColorIndividually extends AsyncTask<ConfigureLed , Void, Void> {


    @Override
    protected  Void doInBackground(ConfigureLed... configureLeds) {

        ConfigureLed configureLed = configureLeds[0];
        configureLed.configureColors(configureLed.getIpAdress(), configureLed.getColorStetting());
        return null;
    }


}
