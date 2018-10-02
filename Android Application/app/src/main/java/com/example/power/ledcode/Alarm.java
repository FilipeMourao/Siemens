package com.example.power.ledcode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shiva on 8/4/17.
 */

public class Alarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //ColorSetting colorsetting = getPendingIntent().getStringArrayListExtra("Color Configuration");
        Bundle extras = intent.getExtras();
        Gson gson = new Gson();
        String ipAdress =  extras.getString("IpAdress");
        ipAdress = "192.168.1.117";
        List<String> ipAdresses = new ArrayList<String>();
        ipAdresses.add(ipAdress);
        String description = extras.getString("Description") ;
        ColorSetting colorsetting = gson.fromJson(extras.getString("Color Configuration") ,ColorSetting.class );
        ConfigureLed configureLed = new ConfigureLed(ipAdresses,colorsetting,null);
        configureColorIndividually myTask = new configureColorIndividually();
        myTask.execute(configureLed);
        Toast.makeText(context,description,Toast.LENGTH_LONG*2).show();
        Vibrator v = (Vibrator)context.getSystemService(context.VIBRATOR_SERVICE);
        v.vibrate(1000);
    }
}