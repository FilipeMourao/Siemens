package com.example.power.gasdetectorapp;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class SendingGasMeausesToServer extends AsyncTask<GasSensorMeasure, Void, Void> {
    private Context context;
    public SendingGasMeausesToServer(Context context){
        this.context=context;
    }
    private final String USER_AGENT = "Mozilla/5.0";

    @Override
    protected Void doInBackground(GasSensorMeasure... gasSensorMeasures) {
        try{
            Gson gson = new Gson();
            String jsonString = gson.toJson(gasSensorMeasures[0]);
//            String url = "http://192.168.1.107:8888/addMeasure";
           // String url = "http://10.192.150.251:8888/addMeasure";
            String url = "http://192.168.4.2:8888/addMeasure";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setConnectTimeout(5000);
            con.setRequestProperty("Accept-Language", "en-GB,en;q=0.5");
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Content-length", Integer.toString(jsonString.length()));
            con.setRequestProperty("Content-Language", "en-GB");
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestMethod("POST");
            DataOutputStream os = new DataOutputStream(con.getOutputStream());
            os.writeBytes(jsonString);
            os.flush();
            os.close();
            System.out.println(jsonString);
            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);
        } catch  (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
