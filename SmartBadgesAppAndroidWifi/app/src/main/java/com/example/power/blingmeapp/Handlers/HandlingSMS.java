package com.example.power.blingmeapp.Handlers;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import java.util.ArrayList;
import java.util.List;
import com.example.power.blingmeapp.Objects.*;
public class HandlingSMS extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Database db = new Database(context);
        db.getReadableDatabase(); // get a readable database
        Object[] pdus = (Object[]) bundle.get("pdus");
        final SmsMessage[] messages = new SmsMessage[pdus.length];// get the message content
        for (int i = 0; i < pdus.length; i++) {
            messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
        }
        String incomingNumber = messages[0].getOriginatingAddress();// get the incoming number
        if (incomingNumber != null){
            incomingNumber  = incomingNumber .replaceAll("[\\s\\-\\(\\)\\+]", "");
            char c  = incomingNumber .charAt(0);
            while (c == '0'){
                incomingNumber  = incomingNumber .substring(1);
                c  = incomingNumber .charAt(0);
            }
        }
        Contact contact = db.getContact(incomingNumber);// check if there is a contact with a color with this number in the database
        if (contact != null){
            IpAdress ipAdress = ((IpAdress)context.getApplicationContext());
            ConfigureLed configureLed =  new ConfigureLed(ipAdress.getIPADRESS(),new ColorSetting(new ColorCustomized(contact.getColor())));
            configureLed.getColorStetting().setBrightness(contact.getColorBrihgtness());
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

