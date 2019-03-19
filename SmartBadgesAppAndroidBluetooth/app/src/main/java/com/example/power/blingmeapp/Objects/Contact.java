package com.example.power.blingmeapp.Objects;

import android.support.annotation.NonNull;

public class Contact implements  Comparable<Contact> {// this class creates contacts with colors and brightness level from the contact customization view

    int id;
   private String name;
    private String number;
    private String color;
    private int colorBrihgtness;
     public Contact(int id, String name, String number) {
        this.id = id;
        this.name = name;
        this.number = number;
    }
    public Contact(int id, String name, String number,String color,int colorBrihgtness) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.color = color;
        this.colorBrihgtness = colorBrihgtness;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getColorBrihgtness() {
        return colorBrihgtness;
    }

    public void setColorBrihgtness(int colorBrihgtness) {
        this.colorBrihgtness = colorBrihgtness;
    }

    @Override
    public int compareTo(@NonNull Contact contact) {
        return this.getName().compareTo(contact.getName());
    }
}
