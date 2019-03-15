package com.example.power.blingmeapp;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import java.util.ArrayList;
import java.util.List;

public class HandlingCalls extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Contact contact = null;
            Bundle extras = intent.getExtras();
            String state = extras.getString(TelephonyManager.EXTRA_STATE);
            ContactDatabase db = new ContactDatabase(context);
            db.getReadableDatabase();
                if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)){
                    String incomingNumber = extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                    if (incomingNumber != null) contact = db.getContact(incomingNumber);
                    if (contact != null){
                        IpAdress ipAdress = ((IpAdress)context.getApplicationContext());
                        ConfigureLed configureLed =  new ConfigureLed(ipAdress.getIPADRESS(),new ColorSetting(new ColorCustomized(contact.getColor())));
                        configureLed.getColorStetting().setBrightness(contact.getColorBrihgtness());
                        configureColorIndividually myTask = new configureColorIndividually();
                        myTask.execute(configureLed);
                    }
                }
            if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_OFFHOOK)){
                IpAdress ipAdress = ((IpAdress)context.getApplicationContext());
                ConfigureLed configureLed =  new ConfigureLed(ipAdress.getIPADRESS(),new ColorSetting(new ColorCustomized("#000000")));
                configureLed.getColorStetting().setBrightness(0);
                configureColorIndividually myTask = new configureColorIndividually();
                myTask.execute(configureLed);
            }
            if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_IDLE)){
                IpAdress ipAdress = ((IpAdress)context.getApplicationContext());
                ConfigureLed configureLed =  new ConfigureLed(ipAdress.getIPADRESS(),new ColorSetting(new ColorCustomized("#000000")));
                configureLed.getColorStetting().setBrightness(0);
                configureColorIndividually myTask = new configureColorIndividually();
                myTask.execute(configureLed);
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
