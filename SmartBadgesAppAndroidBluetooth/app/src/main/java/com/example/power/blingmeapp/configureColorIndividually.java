package com.example.power.blingmeapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.IOException;

public class configureColorIndividually extends AsyncTask<ConfigureLed , Void, Void> {
    private Context mContext;

    public configureColorIndividually (Context context){
        this.mContext = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected  Void doInBackground(ConfigureLed... configureLeds) {
        ConfigureLed configureLed = configureLeds[0];
        try {
            configureLed.configureColors(mContext);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


}
