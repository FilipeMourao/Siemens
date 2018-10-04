package com.example.power.ledcode;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class AlarmActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
       // ArrayList<String> ipAdresses = getIntent().getStringArrayListExtra("validIpAdresses");
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource( this,R.array.colors,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Get Current Date
       final Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Europe/Berlin"));
       // final Calendar c = Calendar.getInstance();

        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        int mHour = c.get(Calendar.HOUR_OF_DAY);
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
        ColorSetting colorSetting = new ColorSetting("ON", 75, null, "SOLID");
        switch (spinnerText){
            case "Red":
                Color red  = new Color("rgb", 255, 0, 0);
                colorSetting.setColor(red);
                break;
            case "Blue" :
                Color blue  = new Color("rgb", 0, 0, 255);
                colorSetting.setColor(blue);
                break;
            case "Green" :
                Color green  = new Color("rgb", 0, 255, 0);
                colorSetting.setColor(green);
                break;
            case "Yellow" :
                Color yellow  = new Color("rgb", 255, 255, 0);
                colorSetting.setColor(yellow);
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
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.SECOND, 0);
        String descriptionString = description.getText().toString() ;
        String basicIpAdress = getIntent().getStringExtra("IpAdress");
        createAlarm( c, colorSetting,  basicIpAdress,  descriptionString,this );
    }
    public void cancelButtonPressed(View v){
        Intent intent = new Intent(this, MainActivity.class);
        String companyLogIn = getIntent().getStringExtra("CompanyLogIn");
        String basicIpAdress = getIntent().getStringExtra("IpAdress");
        intent.putExtra("IpAdress",basicIpAdress);
        intent.putExtra("CompanyLogIn",companyLogIn);
        startActivity(intent);
    }
    public void createAlarm(Calendar calendar, ColorSetting colorSetting, String basicIpAdress, String description, Context context){
        Long time = calendar.getTimeInMillis();
        Intent intent = new Intent(context, Alarm.class);
        Gson gson = new Gson();
        intent.putExtra("Color Configuration",gson.toJson(colorSetting).toString());
        intent.putExtra("Description",description );
        intent.putExtra("IpAdress",basicIpAdress);
        final int _id = (int) System.currentTimeMillis();
        PendingIntent p1=PendingIntent.getBroadcast(context,_id , intent,  PendingIntent.FLAG_UPDATE_CURRENT);
        Long correctingTime = time - Math.abs( Calendar.getInstance().get(Calendar.HOUR_OF_DAY) - Calendar.getInstance(TimeZone.getTimeZone("Europe/Berlin")).get(Calendar.HOUR_OF_DAY) )*3600*1000;
        AlarmManager a = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        a.set(AlarmManager.RTC,correctingTime ,p1);
        Toast.makeText(context,"Alarm created!",Toast.LENGTH_LONG).show();


    }
}
