package com.example.power.gasdetectorapp;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class GettingGasMeausesFromServerHTTPRequestHTML extends AsyncTask<Void , Void, String> {
    private Context context;
    public GettingGasMeausesFromServerHTTPRequestHTML(Context context){
        this.context=context;
    }
@Override
protected  String doInBackground(Void... voids) {
    StringBuffer response = new StringBuffer();
    GasSensorDataBase db = new GasSensorDataBase(context);
    db.getReadableDatabase();
    GasSensorMeasure measure = null;
    Gson gson = new Gson();
    while (measure == null || db.contains(measure)){
        try {
            // Toast.makeText(context.getApplicationContext(),"Waiting measurement...",Toast.LENGTH_LONG).show();
            String url = "http://192.168.4.2/getAllResults";
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
            if (!response.toString().isEmpty())  measure  = gson.fromJson(response.toString(), GasSensorMeasure.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    return response.toString();
}

    protected void onPostExecute(String response) {
        GasSensorDataBase db = new GasSensorDataBase(context);
        db.getWritableDatabase();
        db.removeAll();
        Gson gson = new Gson();
        List<GasSensorMeasure> measures =  gson.fromJson(response, new TypeToken<List<GasSensorMeasure>>(){}.getType());
        for (GasSensorMeasure measure : measures){
            db.addMeasure(measure);
        }
    }

}
