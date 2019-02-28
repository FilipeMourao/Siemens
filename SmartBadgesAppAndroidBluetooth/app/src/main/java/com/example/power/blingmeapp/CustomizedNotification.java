package com.example.power.blingmeapp;

import android.graphics.Color;

import java.util.Random;

public class CustomizedNotification {
    private String appName;
    private String colorString;
    private int id ;



    public CustomizedNotification(String appName, String colorString) {
        this.appName = appName;
        this.colorString = colorString;
    }
    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getColorString() {
        return colorString;
    }

    public void setColorString(String colorString) {
        this.colorString = colorString;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
