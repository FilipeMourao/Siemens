package com.example.power.ledcode;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<String> ipAdresses  = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String companyLogIn = getIntent().getStringExtra("CompanyLogIn");
        TextView welcomeMessage = findViewById(R.id.welcomeMessage);
        welcomeMessage.setText(
                "Welcome " + companyLogIn+ ", you can schedule your meetings right now!"
        );

    }

    public void rainbowButtonClicked(View v) throws IOException, InterruptedException {

       ipAdresses.add("192.168.1.116");
        ipAdresses.add("192.168.1.117");
        ipAdresses.add("192.168.1.118");
        String companyLogIn = getIntent().getStringExtra("CompanyLogIn");
        String basicIpAdress = getIntent().getStringExtra("IpAdress");

        RainbomTask myTask = new RainbomTask();
        myTask.execute(ipAdresses);
        ipAdresses  = new ArrayList<String>();
    }
    public void syncButtonClicked(View v) throws IOException, InterruptedException {
        Intent intent = new Intent(this, ShowCalendarEvents.class);
        String companyLogIn = getIntent().getStringExtra("CompanyLogIn");
        String basicIpAdress = getIntent().getStringExtra("IpAdress");
        intent.putExtra("IpAdress",basicIpAdress);
        intent.putExtra("CompanyLogIn",companyLogIn);
        startActivity(intent);
    }


    public void toAlarmButtonPressed (View v) throws IOException, InterruptedException {
        //ipAdresses.add(getIntent().getStringExtra("IpAdress"));
         //new IpCheckerTask(this).execute(ipAdresses);// not necessary right now
        Intent intent = new Intent(this, AlarmActivity.class);
        String companyLogIn = getIntent().getStringExtra("CompanyLogIn");
        String basicIpAdress = getIntent().getStringExtra("IpAdress");
        intent.putExtra("IpAdress",basicIpAdress);
        intent.putExtra("CompanyLogIn",companyLogIn);
        startActivity(intent);

    }
    public void syncWithContacts(View v) throws IOException, InterruptedException {
        Intent intent = new Intent(this, ContactList.class);
        String companyLogIn = getIntent().getStringExtra("CompanyLogIn");
        String basicIpAdress = getIntent().getStringExtra("IpAdress");
        intent.putExtra("IpAdress",basicIpAdress);
        intent.putExtra("CompanyLogIn",companyLogIn);
        startActivity(intent);
    }


}