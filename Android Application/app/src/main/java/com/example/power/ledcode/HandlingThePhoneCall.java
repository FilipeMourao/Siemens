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
            String incomingNumber = extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);

            Gson gson = new Gson();
            String  ipAdress =  intent.getExtras().getString("IpAdress" );
            Contact contact =  gson.fromJson( intent.getExtras().getString("Colored Contacts"), Contact.class );
            List<Contact> listOfColoredContacts = new ArrayList<Contact>();
            listOfColoredContacts.add(contact);
            List<String> ipadresses = new ArrayList<String>();
            ipadresses.add(ipAdress);
            ipadresses.add("192.168.1.117");
            Color color = new Color("rgb",200,0,0);
            ColorSetting colorSetting =  new ColorSetting("ON", 75, color, "SOLID");
            ColorSetting colorSetting2 =  new ColorSetting("ON", 0, color, "SOLID");
            ConfigureLed configureLed = new ConfigureLed(ipadresses ,colorSetting,null);
            configureColorIndividually task = new configureColorIndividually();
            task.execute(configureLed);
            Toast.makeText(context, "Test",Toast.LENGTH_LONG).show();

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
