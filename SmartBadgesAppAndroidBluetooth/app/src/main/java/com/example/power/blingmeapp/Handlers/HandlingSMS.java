package com.example.power.blingmeapp.Handlers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsMessage;

import com.example.power.blingmeapp.Objects.ColorCustomized;
import com.example.power.blingmeapp.Objects.ColorSetting;
import com.example.power.blingmeapp.Objects.ConfigureLed;
import com.example.power.blingmeapp.Objects.Contact;
import com.example.power.blingmeapp.Objects.Database;
import com.example.power.blingmeapp.Objects.configureColorIndividually;

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
    }
}

