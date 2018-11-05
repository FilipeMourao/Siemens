package com.example.power.gasdetectorapp;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

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
        Intent intent = new Intent(this,PlottingGraphics.class);
        startActivity(intent);

    }

    public void newMeasureButtonPressed(View v ){
        Toast.makeText(this,"Waiting measurement...",Toast.LENGTH_LONG).show();
        GasSensorMeasure measure = null;
        Gson gson = new Gson();
        String response;
        new GettingGasMeauseHTTPRequest(this).execute();
    }
    public void goToHtmlButtonPressed(View v ){
        Intent intent = new Intent(this,htmlTest.class);
        startActivity(intent);
        finish();
    }



}
