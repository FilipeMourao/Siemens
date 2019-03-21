package com.example.power.blingmeapp.Objects;

import android.app.Application;
// global class to store the ipadress of the badge you have to add this class in the manifest
public class IpAdress extends Application {
    private String IPADRESS = new String();

    public String getIPADRESS() {
        return IPADRESS;
    }

    public void setIPADRESS(String IPADRESS) {
        this.IPADRESS = IPADRESS;
    }
}
