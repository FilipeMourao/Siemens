package com.example.power.blingmeapp;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import java.util.ArrayList;
import java.util.List;

public class HandlingSMS extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationDataBase db = new NotificationDataBase(context);
        db.getReadableDatabase();
        Bundle extras = intent.getExtras();
        if (db.getNotification("messages") != null){
            IpAdress ipAdress= ((IpAdress)context.getApplicationContext());
            ConfigureLed configureLed =  new ConfigureLed(ipAdress.getIPADRESS(),new ColorSetting(new ColorCustomized(db.getNotification("messages").getColorString()))) ;
        //    ConfigureLed configureLed =  new ConfigureLed(new ColorSetting(new ColorCustomized(db.getNotification("messages").getColorString()))) ;
            configureColorIndividually myTask = new configureColorIndividually();
            myTask.execute(configureLed);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            myTask.cancel(true);
            myTask = new configureColorIndividually();
            configureLed.getColorStetting().setBrightness(0);
             myTask.execute(configureLed);
        }
    }
}

