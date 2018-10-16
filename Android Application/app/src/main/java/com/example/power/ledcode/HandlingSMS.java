package com.example.power.ledcode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class HandlingSMS extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Object[] pdus = (Object[]) bundle.get("pdus");
        final SmsMessage[] messages = new SmsMessage[pdus.length];
        for (int i = 0; i < pdus.length; i++) {
            messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
        }
        String incomingNumber = messages[0].getOriginatingAddress();
        ColoredContactDatabase db = new ColoredContactDatabase(context);
        db.getReadableDatabase();
       // List<Contact> contactList = db.getAllContacts();
        Contact contact = db.getContact(incomingNumber);
        if (contact != null) {
            List<String> ipadresses = new ArrayList<String>();
            ipadresses.add(contact.getIpAdress());
            ColorSetting colorSetting = new ColorSetting("ON", 75, null, "SOLID");
            ColorSetting colorSetting2 = new ColorSetting("ON", 0, null, "SOLID");
            switch (contact.getColor().toLowerCase()) {
                case "red":
                    Color red = new Color("rgb", 255, 0, 0);
                    colorSetting.setColor(red);
                    colorSetting2.setColor(red);
                    break;
                case "blue":
                    Color blue = new Color("rgb", 0, 0, 255);
                    colorSetting.setColor(blue);
                    colorSetting2.setColor(blue);
                    break;
                case "green":
                    Color green = new Color("rgb", 0, 255, 0);
                    colorSetting.setColor(green);
                    colorSetting2.setColor(green);
                    break;
                case "yellow":
                    Color yellow = new Color("rgb", 255, 255, 0);
                    colorSetting.setColor(yellow);
                    colorSetting2.setColor(yellow);
                    break;
                default:
                    break;
            }
            ConfigureLed configureLed = new ConfigureLed(ipadresses, colorSetting, null);
            ConfigureLed configureLed2 = new ConfigureLed(ipadresses, colorSetting2, null);
            configureColorIndividually task = new configureColorIndividually();
            configureColorIndividually task2 = new configureColorIndividually();
            task.execute(configureLed);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            task.cancel(true);
            task2.execute(configureLed2);
        }
    }
}

