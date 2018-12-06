package com.example.power.blingmeapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
//            TelephonyManager telephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
//            telephony.listen(new PhoneStateListener(){
//                @Override
//                public void onCallStateChanged(int state, String incomingNumber) {
//                    super.onCallStateChanged(state, incomingNumber);
//                   // System.out.println("incomingNumber : "+incomingNumber);
//                    switch (state) {
//                        case TelephonyManager.CALL_STATE_RINGING:
//
//                            break;
//                        case TelephonyManager.CALL_STATE_OFFHOOK:
//                            //Transition of ringing->offhook are pickups of incoming calls.  Nothing done on them
//
//                            break;
//                        case TelephonyManager.CALL_STATE_IDLE:
//
//                            break;
//                    }
//                }
//            },PhoneStateListener.LISTEN_CALL_STATE);

//            NotificationDataBase db = new NotificationDataBase(context);
//            db.getReadableDatabase();
//            if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)){
//                if (db.getNotification("calls") != null){
//                    IpAdress ipAdress= ((IpAdress)context.getApplicationContext());
//                    ConfigureLed configureLed =  new ConfigureLed(ipAdress.getIPADRESS(),new ColorSetting(new ColorCustomized(db.getNotification("calls").getColorString()))) ;
//                    //ConfigureLed configureLed =  new ConfigureLed(new ColorSetting(new ColorCustomized(db.getNotification("calls").getColorString()))) ;
//                    configureColorIndividually myTask = new configureColorIndividually();
//                    myTask.execute(configureLed);
//                }
//            }
//            if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_OFFHOOK)){
//                ConfigureLed configureLed =  new ConfigureLed(new ColorSetting(new ColorCustomized(db.getNotification("calls").getColorString()))) ;
//                configureLed.getColorStetting().setBrightness(0);
//                configureColorIndividually myTask = new configureColorIndividually();
//                myTask.execute(configureLed);
//            }
//            if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_IDLE)){
//                ConfigureLed configureLed =  new ConfigureLed(new ColorSetting(new ColorCustomized(db.getNotification("calls").getColorString()))) ;
//                configureLed.getColorStetting().setBrightness(0);
//                configureColorIndividually myTask = new configureColorIndividually();
//                myTask.execute(configureLed);
//            }
//

                if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)){
                    String incomingNumber = extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                     contact = db.getContact(incomingNumber);
                    if (contact != null){
                        IpAdress ipAdress = ((IpAdress)context.getApplicationContext());
                        ConfigureLed configureLed =  new ConfigureLed(ipAdress.getIPADRESS(),new ColorSetting(new ColorCustomized(contact.getColor())));
                        configureLed.getColorStetting().setBrightness(contact.getColorBrihgtness());
                        configureColorIndividually myTask = new configureColorIndividually();
                        myTask.execute(configureLed);
                    }
                }
            if (contact != null){
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
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
