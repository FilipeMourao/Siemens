package com.example.power.ledcode;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SettingContactColors extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_contact_colors);
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource( this,R.array.colors,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        TextView contactName = findViewById(R.id.ContactName);
        contactName.setText(getIntent().getStringExtra("Contact Name"));

    }
    public void confirmButtonPressed(View v){
        Intent intent = new Intent(SettingContactColors.this,  ContactList.class);
        Spinner spinner = findViewById(R.id.spinner);
        List<String> coloredTitles = getIntent().getStringArrayListExtra("Colored Titles");
        List<String> colorOfTheTitles = getIntent().getStringArrayListExtra("Color of the Titles");
        String basicIpAdress = getIntent().getStringExtra("IpAdress");
        if(coloredTitles.indexOf(getIntent().getStringExtra("Contact Name")) != -1){
            colorOfTheTitles.remove(coloredTitles.indexOf(getIntent().getStringExtra("Contact Name")));
            coloredTitles.remove(coloredTitles.indexOf(getIntent().getStringExtra("Contact Name")));
        }
        ColorSetting colorSetting = new ColorSetting("ON", 75, null, "SOLID");
        ColorSetting colorSetting2 = new ColorSetting("ON", 0, null, "SOLID");
        String color = spinner.getSelectedItem().toString().toLowerCase();
        switch (color){
            case "red":
                Color red  = new Color("rgb", 255, 0, 0);
                colorSetting.setColor(red);
                colorSetting2.setColor(red);
                colorOfTheTitles.add("red");
                break;
            case "blue":
                Color blue  = new Color("rgb", 0, 0, 255);
                colorSetting.setColor(blue);
                colorSetting2.setColor(blue);
                colorOfTheTitles.add("blue");
                break;
            case "green":
                Color green  = new Color("rgb", 0, 255, 0);
                colorSetting.setColor(green);
                colorSetting2.setColor(green);
                colorOfTheTitles.add("green");
                break;
            case "yellow":
                Color yellow  = new Color("rgb", 255, 255, 0);
                colorSetting.setColor(yellow);
                colorSetting2.setColor(yellow);
                colorOfTheTitles.add("yellow");
                break;
        }
        if(basicIpAdress == null) basicIpAdress = "192.168.1.117";
        intent.putExtra("IpAdress",basicIpAdress);
        coloredTitles.add(getIntent().getStringExtra("Contact Name"));
        Calendar calendar = (Calendar) getIntent().getSerializableExtra("Date");
        intent.putStringArrayListExtra("Colored Titles", (ArrayList<String>) coloredTitles);
        intent.putStringArrayListExtra("Color of the Titles", (ArrayList<String>) colorOfTheTitles);
        startActivity(intent);
    }
    public void cancelButtonPressed(View v){
        Intent intent = new Intent(this, ContactList.class);
        String basicIpAdress = getIntent().getStringExtra("IpAdress");
        intent.putExtra("IpAdress",basicIpAdress);
        startActivity(intent);
    }


}
