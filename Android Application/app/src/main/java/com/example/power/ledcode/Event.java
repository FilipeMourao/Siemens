package com.example.power.ledcode;

import android.support.annotation.NonNull;

import java.util.Calendar;

import static java.lang.Math.toIntExact;

public class Event implements Comparable<Event> {
    Calendar calendar;
    String title;
    String location;

    public Event(Calendar calendar, String title, String location) {
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

    @Override
    public int compareTo(Event event) {
        long result = (this.getCalendar().getTimeInMillis() -  event.getCalendar().getTimeInMillis() )/100000;
        return  (int) result;
    }
}