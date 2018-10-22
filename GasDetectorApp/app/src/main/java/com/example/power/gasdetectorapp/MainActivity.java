package com.example.power.gasdetectorapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;
import android.service.autofill.FillEventHistory;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void plotGraphsButtonPressed(View v){
        GasSensorDataBase db = new GasSensorDataBase(this.getApplicationContext());
        db.getReadableDatabase();
        List<GasSensorMeasure> measures = db.getAllMeasures();


    }

    public void newMeasureButtonPressed(View v ){
        Toast.makeText(this,"Waiting measurement...",Toast.LENGTH_LONG).show();
        GasSensorMeasure measure = null;
        Gson gson = new Gson();
        while (measure == null){
            try {
                String url = "http://192.168.4.1/";
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                int responseCode = con.getResponseCode();
//                System.out.println("\nSending 'GET' request to URL : " + url);
//                System.out.println("Response Code : " + responseCode);
                BufferedReader in =new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                } in .close();
                if (!response.toString().isEmpty()) {
                    measure  = gson.fromJson(response.toString(), GasSensorMeasure.class);
                    Toast.makeText(this,"New measurement available",Toast.LENGTH_LONG).show();
                    AlertDialog.Builder altdial = new AlertDialog.Builder(MainActivity.this);
                    final GasSensorMeasure finalMeasure = measure;
                    altdial.setMessage(  response.toString() + "\n Do you want to save in the database?").setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    saveMeasureInDataBase(finalMeasure);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert = altdial.create();
                    alert.setTitle("New measurement available:");
                    alert.show();

                }

            } catch(Exception e) {
                System.out.println(e);
            }


        }

    }
    public void saveMeasureInDataBase(GasSensorMeasure measure){
        GasSensorDataBase db = new GasSensorDataBase(this.getApplicationContext());
        db.getWritableDatabase();
        db.addMeasure(measure);

    }

}
