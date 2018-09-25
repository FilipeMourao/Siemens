package com.example.power.ledcode;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    List<String> ipAdresses  = new ArrayList<String>();
    public void addIP(View v){
        EditText ipAdress = (EditText)findViewById(R.id.IP);
        String ipAdressString = ipAdress.getText().toString();
        // check if  it's a valid IP is correct
        if(ipAdressString.length() != 13){
            Toast.makeText(getApplicationContext(),
                    "Invalid IP Adress", Toast.LENGTH_LONG).show();
            return;
        }
        else{
            ipAdresses.add(ipAdressString);
            ipAdress.setText("");
        }

    }
    public void rainbowButtonClicked(View v) throws IOException, InterruptedException {

       ipAdresses.add("192.168.1.116");
        ipAdresses.add("192.168.1.117");
        ipAdresses.add("192.168.1.118");

        RainbomTask myTask = new RainbomTask();
        myTask.execute(ipAdresses);


    }
    public void toAlarmButtonPressed (View v) throws IOException, InterruptedException {
        ipAdresses.add("192.168.1.100");
       // ipAdresses.add("192.168.1.108");
        //ipAdresses.add("192.168.1.105");
        //ipAdresses.add("192.168.1.109");
         new IpCheckerTask(this).execute(ipAdresses);
    }

}