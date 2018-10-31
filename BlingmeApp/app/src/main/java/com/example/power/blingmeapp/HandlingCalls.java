package com.example.power.blingmeapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import java.util.ArrayList;
import java.util.List;

public class HandlingCalls extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            NotificationDataBase db = new NotificationDataBase(context);
            db.getReadableDatabase();
            Bundle extras = intent.getExtras();
            String state = extras.getString(TelephonyManager.EXTRA_STATE);
            if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)){
                if (db.getNotification("calls") != null){
                    ConfigureLed configureLed =  new ConfigureLed(new ColorSetting(new ColorCustomized(db.getNotification("calls").getColorString()))) ;
                    configureColorIndividually myTask = new configureColorIndividually();
                    myTask.execute(configureLed);
                }
            }
            if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_OFFHOOK)){
                ConfigureLed configureLed =  new ConfigureLed(new ColorSetting(new ColorCustomized(db.getNotification("calls").getColorString()))) ;
                configureLed.getColorStetting().setBrightness(0);
                configureColorIndividually myTask = new configureColorIndividually();
                myTask.execute(configureLed);
            }
            if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_IDLE)){
                ConfigureLed configureLed =  new ConfigureLed(new ColorSetting(new ColorCustomized(db.getNotification("calls").getColorString()))) ;
                configureLed.getColorStetting().setBrightness(0);
                configureColorIndividually myTask = new configureColorIndividually();
                myTask.execute(configureLed);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
