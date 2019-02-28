package com.example.power.blingmeapp;

import java.util.Calendar;

public class Event implements Comparable<Event> {
    int id;
    Calendar calendar;
    String title;
    String location;
    String color;

    public Event(int id, String title, String location,Calendar calendar) {
        this.id = id;
        this.calendar = calendar;
        this.title = title;
        this.location = location;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public int compareTo(Event event) {
        long result = (this.getCalendar().getTimeInMillis() -  event.getCalendar().getTimeInMillis() )/100000;
        return  (int) result;
    }
}