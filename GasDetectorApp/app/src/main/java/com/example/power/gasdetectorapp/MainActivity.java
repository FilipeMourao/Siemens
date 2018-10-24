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
    public void saveMeasureInDataBase(GasSensorMeasure measure){
        GasSensorDataBase db = new GasSensorDataBase(this.getApplicationContext());
        db.getWritableDatabase();
        db.addMeasure(measure);

    }
//    public void saveGasSensorObject(String measureString){
//        GasSensorMeasure measure = null;
//        Gson gson = new Gson();
//        measure  = gson.fromJson(measureString, GasSensorMeasure.class);
//        Toast.makeText(this.getApplicationContext(),"New measurement available",Toast.LENGTH_LONG).show();
//        AlertDialog.Builder altdial = new AlertDialog.Builder(this.getApplicationContext());
//        final GasSensorMeasure finalMeasure = measure;
//        altdial.setMessage(  measureString + "\n Do you want to save in the database?").setCancelable(false)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        saveMeasureInDataBase(finalMeasure);
//                    }
//                })
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//
//        AlertDialog alert = altdial.create();
//        alert.setTitle("New measurement available:");
//        alert.show();
//    }


}
