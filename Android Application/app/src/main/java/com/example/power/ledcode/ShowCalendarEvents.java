package com.example.power.ledcode;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Activity;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.widget.TextView;

import java.util.Calendar;

public class ShowCalendarEvents extends Activity {

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
        int titleId ;
        String titleValue;
        int locationId ;
        String locationValue;
        int startTimeId;
        Long startTimeValue;
        int test;
        String descriptionEventAlmostBeginning;
        String descriptionEventStarted;
        AlarmActivity alarmActivity = new AlarmActivity();
        TextView syncMessage = findViewById(R.id.Description);
        boolean thereWereSomeSiemensEvent = false;
        if (cursor == null){
            syncMessage.setText("The were no events to sync!");
        }
        else {
            while (cursor.moveToNext()){
                if (cursor !=null){
                    titleId = cursor.getColumnIndex(CalendarContract.Events.TITLE);
                    titleValue = cursor.getString(titleId);
                    if(titleValue.contains("Siemens")){
                        thereWereSomeSiemensEvent = true;
                        locationId  = cursor.getColumnIndex(CalendarContract.Events.EVENT_LOCATION);
                        locationValue = cursor.getString(locationId);
                        ColorSetting colorSetting = new ColorSetting("ON", 75, null, "SOLID");
                        String color = locationValue.split("\\s")[0].toLowerCase();
                        switch (color){
                            case "red":
                                Color red  = new Color("rgb", 255, 0, 0);
                                colorSetting.setColor(red);
                                break;
                            case "blue":
                                Color blue  = new Color("rgb", 0, 0, 255);
                                colorSetting.setColor(blue);
                                break;
                            case "green":
                                Color green  = new Color("rgb", 0, 255, 0);
                                colorSetting.setColor(green);
                                break;
                            case "yellow":
                                Color yellow  = new Color("rgb", 255, 255, 0);
                                colorSetting.setColor(yellow);
                                break;

                        }
                        descriptionEventAlmostBeginning = "Attention! The event:" + titleValue + "will start in 2 minutes";
                        descriptionEventStarted = "Attention! The event:" + titleValue + "is starting";
                        startTimeId = cursor.getColumnIndex(CalendarContract.Events.DTSTART);
                        startTimeValue = Long.parseLong(cursor.getString(startTimeId));
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(startTimeValue - 2*60*1000);
                        //  alarmActivity.createAlarm(calendar,colorSetting,getIntent().getStringExtra("IpAdress"),descriptionEventAlmostBeginning);
                        calendar.setTimeInMillis(startTimeValue);
                        //  alarmActivity.createAlarm(calendar,colorSetting,getIntent().getStringExtra("IpAdress"),descriptionEventStarted);
                        syncMessage.setText("The sync was completed!");

                    }

                } else {
                    break;

                }
            }


        }
        if (!thereWereSomeSiemensEvent)syncMessage.setText("The were no events to sync!");
    }



}
