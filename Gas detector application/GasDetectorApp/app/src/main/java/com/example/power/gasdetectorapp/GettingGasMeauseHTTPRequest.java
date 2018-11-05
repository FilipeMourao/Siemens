package com.example.power.gasdetectorapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GettingGasMeauseHTTPRequest extends AsyncTask<Void , Void, String> {
    private Context context;
    public GettingGasMeauseHTTPRequest(Context context){
        this.context=context;
    }
@Override
protected  String doInBackground(Void... voids) {
    StringBuffer response = new StringBuffer();
    while (response.toString().isEmpty()){
        try {
            String url = "http://192.168.4.1/";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            //int responseCode = con.getResponseCode();
//                System.out.println("\nSending 'GET' request to URL : " + url);
//                System.out.println("Response Code : " + responseCode);
            BufferedReader in =new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            } in .close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    return response.toString();
        }
    protected void onPostExecute(String response) {
        saveGasSensorObject(response);
    }
    public void saveGasSensorObject(String measureString){
        GasSensorMeasure measure = null;
        Gson gson = new Gson();
        measure  = gson.fromJson(measureString, GasSensorMeasure.class);
        Toast.makeText(context,"New measurement available",Toast.LENGTH_LONG).show();
        AlertDialog.Builder altdial = new AlertDialog.Builder(context);
        final GasSensorMeasure finalMeasure = measure;
        altdial.setMessage(  finalMeasure.toString() + " Save in the database?").setCancelable(false)
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
    public void saveMeasureInDataBase(GasSensorMeasure measure){
        GasSensorDataBase db = new GasSensorDataBase(context);
        db.getWritableDatabase();
        db.addMeasure(measure);

    }
}
