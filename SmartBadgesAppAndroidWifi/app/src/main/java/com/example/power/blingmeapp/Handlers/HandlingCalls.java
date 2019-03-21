package com.example.power.blingmeapp.Handlers;

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
import com.example.power.blingmeapp.Objects.*;
public class HandlingCalls extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Contact contact = null;
            Bundle extras = intent.getExtras();
            String state = extras.getString(TelephonyManager.EXTRA_STATE);// get the current phone state, ringing, offhook or idle
            Database db = new Database(context);
            db.getReadableDatabase();// get an readable database
                if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)){// if the phone is ringing get the phone number
                    String incomingNumber = extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                    if (incomingNumber != null){
                        incomingNumber  = incomingNumber .replaceAll("[\\s\\-\\(\\)\\+]", "");
                        char c  = incomingNumber .charAt(0);
                        while (c == '0'){
                            incomingNumber  = incomingNumber .substring(1);
                            c  = incomingNumber .charAt(0);
                        }
                        contact = db.getContact(incomingNumber);// check if there is a contact with a color with this number in the database
                    }
                    if (contact != null){// if there is a contact, change the badge color
                        IpAdress ipAdress = ((IpAdress)context.getApplicationContext());
                        ConfigureLed configureLed =  new ConfigureLed(ipAdress.getIPADRESS(),new ColorSetting(new ColorCustomized(contact.getColor())));
                        configureLed.getColorStetting().setBrightness(contact.getColorBrihgtness());
                        configureColorIndividually myTask = new configureColorIndividually();
                        myTask.execute(configureLed);
                    }
                }
            if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_OFFHOOK)){// after the call is ansewerd turn off the color of the device
                IpAdress ipAdress = ((IpAdress)context.getApplicationContext());
                ConfigureLed configureLed =  new ConfigureLed(ipAdress.getIPADRESS(),new ColorSetting(new ColorCustomized("#000000")));
                configureLed.getColorStetting().setBrightness(0);
                configureColorIndividually myTask = new configureColorIndividually();
                myTask.execute(configureLed);
            }
            if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_IDLE)){// after the call is finished turn off the color of the device
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
