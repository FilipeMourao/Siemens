package com.example.power.ledcode;

import android.os.AsyncTask;

import java.io.IOException;

public class configureColorIndividually extends AsyncTask<ConfigureLed , Void, Void> {


    @Override
    protected  Void doInBackground(ConfigureLed... configureLeds) {

        ConfigureLed configureLed = configureLeds[0];
        try {
            configureLed.configureColors(configureLed.getIpAdresses().get(0), configureLed.getColorStetting());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


}
