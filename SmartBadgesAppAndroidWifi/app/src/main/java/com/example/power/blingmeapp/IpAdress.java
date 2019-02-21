package com.example.power.blingmeapp;

import android.app.Application;

public class IpAdress extends Application {
    private String IPADRESS = new String();

    public String getIPADRESS() {
        return IPADRESS;
    }

    public void setIPADRESS(String IPADRESS) {
        this.IPADRESS = IPADRESS;
    }
}
