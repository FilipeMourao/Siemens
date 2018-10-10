package com.example.power.ledcode;

import android.support.annotation.NonNull;

public class Contact implements  Comparable<Contact> {
    int id;
    String name;
    String number;
    String color;

    public Contact(int id,String name, String number) {
        this.id = id;
        this.name = name;
        this.number = number;
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

    @Override
    public int compareTo(@NonNull Contact contact) {
        return this.getName().compareTo(contact.getName());
    }
}
