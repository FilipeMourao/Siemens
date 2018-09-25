package com.example.power.ledcode;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;

public class AlarmActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        ArrayList<String> ipAdresses = getIntent().getStringArrayListExtra("validIpAdresses");
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource( this,R.array.colors,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        int mHour = c.get(Calendar.HOUR);
        int mMin = c.get(Calendar.MINUTE);
        DatePicker datePicker = (DatePicker) findViewById(R.id.AlarmDate);
        datePicker.init(mYear,mMonth,mDay,null);
        TimePicker timePicker = (TimePicker) findViewById(R.id.AlarmTime);
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(mHour);
        timePicker.setCurrentMinute(mMin);

    }

    public void confirmButtonPressed(View v){
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        String spinnerText = spinner.getSelectedItem().toString();
        ColorSetting colorsetting = new ColorSetting("ON", 75, null, "SOLID");
        switch (spinnerText){
            case "Red":
                Color red  = new Color("rgb", 255, 0, 0);
                colorsetting.setColor(red);
                break;
            case "Blue" :
                Color blue  = new Color("rgb", 0, 0, 255);
                colorsetting.setColor(blue);
                break;
            case "Green" :
                Color green  = new Color("rgb", 0, 255, 0);
                colorsetting.setColor(green);
                break;
            case "Yellow" :
                Color yellow  = new Color("rgb", 255, 255, 0);
                colorsetting.setColor(yellow);
                break;
            default:
                break;
        }
        DatePicker datePicker = (DatePicker) findViewById(R.id.AlarmDate);
        TimePicker timePicker = (TimePicker) findViewById(R.id.AlarmTime);
        EditText description = (EditText) findViewById(R.id.Description);


        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int day = datePicker.getDayOfMonth();
        int hour = timePicker.getCurrentHour();
        int minutes = timePicker.getCurrentMinute();
        Calendar c = Calendar.getInstance();
        c.set(year,month,day,hour,minutes);
        Long time = c.getTimeInMillis();
        Intent intent = new Intent(this, Alarm.class);
        intent.putExtra("Color Configuration", ColorSetting colorsetting);
        PendingIntent p1=PendingIntent.getBroadcast(getApplicationContext(),0, intent,0);
        AlarmManager a=(AlarmManager)getSystemService(ALARM_SERVICE);
        a.set(AlarmManager.RTC,time - System.currentTimeMillis()  ,p1);
        //Toast.makeText(getApplicationContext(),"Alarm set in "+time+"seconds",Toast.LENGTH_LONG).show();
        //http://luboganev.github.io/post/alarms-pending-intent/
        //https://www.youtube.com/watch?v=QcF4M2yUpY4


    }
    public void cancelButtonPressed(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
