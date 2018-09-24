package com.example.power.ledcode;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
        if(ipAdressString.length() != 13) return;
        else{
            ipAdresses.add(ipAdressString);
            ipAdress.setText("");
        }

    }
    public void rainbowButtonClicked(View v) throws IOException, InterruptedException {
         new ConfigureLed().rainbomColor(ipAdresses);
    }

}