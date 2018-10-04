package com.example.power.ledcode;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.provider.CalendarContract;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

public class ShowCalendarEvents extends Activity {
    int titleId ;
    String titleValue;
    int locationId ;
    String locationValue;
    int startTimeId;
    Long startTimeValue;
    String eventString;
    int position;
    ArrayList<String> eventsListView = new ArrayList<String>();
    ArrayList<Event> listOfEvents = new ArrayList<Event>();
    List<String> coloredTitles = new  ArrayList<String>();

    Event event;
    String color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_calendar_events);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Cursor cursor = getContentResolver().query(CalendarContract.Events.CONTENT_URI, null, null, null, null);
        TextView syncMessage = findViewById(R.id.Description);
        final ListView listView = findViewById(R.id.EventsList);
        boolean thereWereSomeSiemensEvent = false;
        if(getIntent().getStringArrayListExtra("Colored Titles") != null) coloredTitles = getIntent().getStringArrayListExtra("Colored Titles");
        if (cursor == null){
            syncMessage.setText("The were no events to sync!");
        }
        else {
            while (cursor.moveToNext()){
                if (cursor !=null){
                    startTimeId = cursor.getColumnIndex(CalendarContract.Events.DTSTART);
                    startTimeValue = Long.parseLong(cursor.getString(startTimeId));
                    Long correctingTime = startTimeValue - Math.abs( Calendar.getInstance().get(Calendar.HOUR_OF_DAY) - Calendar.getInstance(TimeZone.getTimeZone("Europe/Berlin")).get(Calendar.HOUR_OF_DAY) )*3600*1000;
                    titleId = cursor.getColumnIndex(CalendarContract.Events.TITLE);
                    titleValue = cursor.getString(titleId);
                    long time =  correctingTime  - System.currentTimeMillis();
                    if(correctingTime > System.currentTimeMillis() && titleValue.contains("Siemens")){
                        thereWereSomeSiemensEvent = true;
                        locationId  = cursor.getColumnIndex(CalendarContract.Events.EVENT_LOCATION);
                        locationValue = cursor.getString(locationId);
                        syncMessage.setText("The sync was completed!");
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(startTimeValue);
                        event = new Event(calendar,titleValue,locationValue);
                        listOfEvents.add(event);
                    }

                } else {
                    break;

                }
            }
            Collections.sort(listOfEvents);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            for (int i = 0; i <  listOfEvents.size();i++) {
                eventString = listOfEvents.get(i).title + "  " + sdf.format(listOfEvents.get(i).calendar.getTime());
                eventsListView.add(eventString);
            }
        //    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,eventsListView);
         //   listView.setAdapter(adapter);
            listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, eventsListView) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View row = super.getView(position, convertView, parent);
                    if (!coloredTitles.isEmpty()){
                        for ( final String title : coloredTitles){
                            if(listOfEvents.get(position).getTitle().equals(title)) {
                                color = listOfEvents.get(position).getLocation().split("\\s")[0].toLowerCase();
                                switch (color){
                                    case "red":
                                        row.setBackgroundColor(Color.RED);
                                        break;
                                    case "blue":
                                        row.setBackgroundColor(Color.BLUE);
                                        break;
                                    case "green":
                                        row.setBackgroundColor(Color.GREEN);
                                        break;
                                    case "yellow":
                                        row.setBackgroundColor(Color.YELLOW);
                                        break;
                                }
                            }
                        }
                    }
                    return row;
                }
            });

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(ShowCalendarEvents.this, SettingAnAlarmFromCalendar.class);
                    String[] splitStr = listView.getItemAtPosition(position).toString().split("\\s+");
                    Event event = listOfEvents.get(position);
                    intent.putExtra("Date",event.getCalendar());
                    intent.putExtra("Title",event.getTitle());
                    intent.putExtra("Location",event.getLocation());
                    String basicIpAdress = getIntent().getStringExtra("IpAdress");
                    if(basicIpAdress == null) basicIpAdress = "192.168.1.107";
                    intent.putExtra("IpAdress",basicIpAdress);
                    intent.putStringArrayListExtra("Colored Titles", (ArrayList<String>) coloredTitles);
                    startActivity(intent);
                }
            });
//            if (!coloredTitles.isEmpty()){
//                for ( final String title : coloredTitles){
//                    for(Event ev : listOfEvents){
//                        if(ev.getTitle().equals(title)) {
//                            position = listOfEvents.indexOf(ev);
//                            color = listOfEvents.get(position).getLocation().split("\\s")[0].toLowerCase();
//                            switch (color){
//                                case "red":
//                                    listView.getChildAt(position).setBackgroundColor(Color.RED);
//                                    break;
//                                case "blue":
//                                    listView.getChildAt(position).setBackgroundColor(Color.BLUE);
//                                    break;
//                                case "green":
//                                    listView.getChildAt(position).setBackgroundColor(Color.GREEN);
//                                    break;
//                                case "yellow":
//                                    listView.getChildAt(position).setBackgroundColor(Color.YELLOW);
//                                    break;
//
//                            }
//                        }
//                    }
//
//                }
//
//            }


        }
        if (!thereWereSomeSiemensEvent)syncMessage.setText("The were no events to sync!");
    }




}
