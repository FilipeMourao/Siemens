package com.example.power.ledcode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class HandlingThePhoneCall extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle extras = intent.getExtras();
            String state = extras.getString(TelephonyManager.EXTRA_STATE);
            String incomingNumber ;
            ColoredContactDatabase db = new ColoredContactDatabase(context);
            db.getReadableDatabase();
            Contact contact = null;
            List<Contact> listOfContacts;
            if(state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
                listOfContacts = db.getAllContacts();
                if (!listOfContacts.isEmpty()) contact = listOfContacts.get(0);
            }

            else if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
                incomingNumber = extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                List<Contact> contactList = db.getAllContacts();
                 contact = db.getContact(incomingNumber);
            }
            else if(state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
                listOfContacts = db.getAllContacts();
                if (!listOfContacts.isEmpty()) contact = listOfContacts.get(0);
            }


            if (contact != null){
                List<String> ipadresses = new ArrayList<String>();
                ipadresses.add(contact.getIpAdress());
                ColorSetting colorSetting =  new ColorSetting("ON", 75, null, "SOLID");
                ColorSetting colorSetting2 =  new ColorSetting("ON", 0, null, "SOLID");
                switch (contact.getColor().toLowerCase()){
                    case "red":
                        Color red  = new Color("rgb", 255, 0, 0);
                        colorSetting.setColor(red);
                        colorSetting2.setColor(red);
                        break;
                    case "blue" :
                        Color blue  = new Color("rgb", 0, 0, 255);
                        colorSetting.setColor(blue);
                        colorSetting2.setColor(blue);
                        break;
                    case "green" :
                        Color green  = new Color("rgb", 0, 255, 0);
                        colorSetting.setColor(green);
                        colorSetting2.setColor(green);
                        break;
                    case "yellow" :
                        Color yellow  = new Color("rgb", 255, 255, 0);
                        colorSetting.setColor(yellow);
                        colorSetting2.setColor(yellow);
                        break;
                    default:
                        break;
                }

                if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)){
                    ConfigureLed configureLed = new ConfigureLed(ipadresses ,colorSetting,null);
                    configureColorIndividually task = new configureColorIndividually();
                    task.execute(configureLed);
                }
                if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_OFFHOOK)){
                    ConfigureLed configureLed = new ConfigureLed(ipadresses ,colorSetting2,null);
                    configureColorIndividually task = new configureColorIndividually();
                    task.execute(configureLed);
                }
                if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_IDLE)){
                    ConfigureLed configureLed = new ConfigureLed(ipadresses ,colorSetting2,null);
                    configureColorIndividually task = new configureColorIndividually();
                    task.execute(configureLed);
                }
            }


        } catch (Exception e){
            e.printStackTrace();
        }
//        String action = intent.getAction();
//        Bundle extras = intent.getExtras();
//        Gson gson = new Gson();
//        String ipAdress =  extras.getString("IpAdress");
//        List<String> ipAdresses = new ArrayList<String>();

    }
}
