package com.example.power.ledcode;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SettingAnAlarmFromCalendar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_an_alarm_from_calendar);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Calendar calendar = (Calendar) getIntent().getSerializableExtra("Date");
        TextView title = findViewById(R.id.ContactName);
        title.setText(getIntent().getStringExtra("Title"));
        TextView location = findViewById(R.id.Location);
        location.setText(getIntent().getStringExtra("Location"));
        TextView time = findViewById(R.id.Time);
        time.setText(sdf.format(calendar.getTime()));


    }
    public void cancelButtonPressed(View v){
        Intent intent = new Intent(this, ShowCalendarEvents.class);
        String basicIpAdress = getIntent().getStringExtra("IpAdress");
        intent.putExtra("IpAdress",basicIpAdress);
        startActivity(intent);
    }
    public void confirmButtonPressed(View v){
        Intent intent = new Intent(this, ShowCalendarEvents.class);
        AlarmActivity alarmActivity = new AlarmActivity();
        String basicIpAdress = getIntent().getStringExtra("IpAdress");
        intent.putExtra("IpAdress",basicIpAdress);
        ColorSetting colorSetting = new ColorSetting("ON", 75, null, "SOLID");
        ColorSetting colorSetting2 = new ColorSetting("ON", 0, null, "SOLID");
        String color = getIntent().getStringExtra("Location").split("\\s")[0].toLowerCase();
        switch (color){
            case "red":
                Color red  = new Color("rgb", 255, 0, 0);
                colorSetting.setColor(red);
                colorSetting2.setColor(red);
                break;
            case "blue":
                Color blue  = new Color("rgb", 0, 0, 255);
                colorSetting.setColor(blue);
                colorSetting2.setColor(blue);
                break;
            case "green":
                Color green  = new Color("rgb", 0, 255, 0);
                colorSetting.setColor(green);
                colorSetting2.setColor(green);
                break;
            case "yellow":
                Color yellow  = new Color("rgb", 255, 255, 0);
                colorSetting.setColor(yellow);
                colorSetting2.setColor(yellow);
                break;

        }
        List<String> coloredTitles =  getIntent().getStringArrayListExtra("Colored Titles");
        coloredTitles.add(getIntent().getStringExtra("Title"));
        Calendar calendar = (Calendar) getIntent().getSerializableExtra("Date");
        String descriptionEventAlmostBeginning = "Reminder! The event " + getIntent().getStringExtra("Title") + " will start in 2 minutes";
        String descriptionEventStarted = "Reminder! The event " + getIntent().getStringExtra("Title") + " is starting";
        alarmActivity.createAlarm(calendar,colorSetting2,basicIpAdress,descriptionEventStarted,this);
        calendar.setTimeInMillis(calendar.getTimeInMillis() - 2*60*1000);
        alarmActivity.createAlarm(calendar,colorSetting,getIntent().getStringExtra("IpAdress"),descriptionEventAlmostBeginning,this);
        intent.putStringArrayListExtra("Colored Titles", (ArrayList<String>) coloredTitles);
        startActivity(intent);
    }


}
