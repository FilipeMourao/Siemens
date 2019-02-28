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
        Bundle bundle = intent.getExtras();
        ContactDatabase db = new ContactDatabase(context);
        db.getReadableDatabase();
        Object[] pdus = (Object[]) bundle.get("pdus");
        final SmsMessage[] messages = new SmsMessage[pdus.length];
        for (int i = 0; i < pdus.length; i++) {
            messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
        }
        String incomingNumber = messages[0].getOriginatingAddress();

        Contact contact = db.getContact(incomingNumber);
        if (contact != null){
            ConfigureLed configureLed =  new ConfigureLed(new ColorSetting(new ColorCustomized(contact.getColor())));
            configureLed.getColorStetting().setBrightness(contact.getColorBrihgtness());
            configureColorIndividually myTask = new configureColorIndividually(context);
            myTask.execute(configureLed);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            myTask.cancel(true);
            myTask = new configureColorIndividually(context);
            configureLed.getColorStetting().setBrightness(0);
            myTask.execute(configureLed);
        }
//        NotificationDataBase db = new NotificationDataBase(context);
//        db.getReadableDatabase();
//        Bundle extras = intent.getExtras();
//        if (db.getNotification("messages") != null){
//            IpAdress ipAdress= ((IpAdress)context.getApplicationContext());
//            ConfigureLed configureLed =  new ConfigureLed(ipAdress.getIPADRESS(),new ColorSetting(new ColorCustomized(db.getNotification("messages").getColorString()))) ;
//        //    ConfigureLed configureLed =  new ConfigureLed(new ColorSetting(new ColorCustomized(db.getNotification("messages").getColorString()))) ;
//            configureColorIndividually myTask = new configureColorIndividually();
//            myTask.execute(configureLed);
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            myTask.cancel(true);
//            myTask = new configureColorIndividually();
//            configureLed.getColorStetting().setBrightness(0);
//             myTask.execute(configureLed);
//        }
    }
}

