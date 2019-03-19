package com.example.power.blingmeapp.Objects;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.IOException;

// this class sends the color information to the badge, must be an async task
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
