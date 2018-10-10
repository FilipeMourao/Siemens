package com.example.power.ledcode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class Receiver extends BroadcastReceiver {
    List<Contact> listOfColoredContacts = new ArrayList<Contact>();
    String ipAdress;
    // catch messages from intent
        @Override
        public void onReceive(Context context, Intent intent) {

            if("com.example.power.ledcode.SOME_MESSAGE".equals(intent.getAction().toString())) {
                Gson gson = new Gson();
                ipAdress =  intent.getExtras().getString("IpAdress" );
                Contact contact =  gson.fromJson( intent.getExtras().getString("Colored Contacts"), Contact.class );
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
            }
            else if("com.example.myproject.DELETE_ITEM_BASKET".equals(intent.getAction().toString()))
            {

            }


        }

    public List<Contact> getListOfColoredContacts() {
        return listOfColoredContacts;
    }

    public void setListOfColoredContacts(List<Contact> listOfColoredContacts) {
        this.listOfColoredContacts = listOfColoredContacts;
    }

    public String getIpAdresses() {
        return ipAdress;
    }

    public void setIpAdresses(String ipAdress) {
        this.ipAdress = ipAdress;
    }
    }


