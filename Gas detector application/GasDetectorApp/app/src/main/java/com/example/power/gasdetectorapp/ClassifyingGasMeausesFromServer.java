package com.example.power.gasdetectorapp;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ClassifyingGasMeausesFromServer extends AsyncTask<Void , Void, String> {
    private Context context;
    public ClassifyingGasMeausesFromServer(Context context){
        this.context=context;
    }
@Override
protected  String doInBackground(Void... voids) {
    StringBuffer response = new StringBuffer();
    GasSensorDataBase db = new GasSensorDataBase(context);
    Gson gson = new Gson();
    try {
            // Toast.makeText(context.getApplicationContext(),"Waiting measurement...",Toast.LENGTH_LONG).show();
           // String url = "http://192.168.4.2:8888/getAllResults";
           // String url = "http://10.192.150.251:8888/getAllResults";
            String url = "http://192.168.4.2:8888/classifyGases";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
//        int responseCode = con.getResponseCode();
//            int responseCode = con.getResponseCode();
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
    return response.toString();
}

//    protected void onPostExecute(String response) {
//        if (!response.isEmpty()){
//            GasSensorDataBase db = new GasSensorDataBase(context);
//            db.getWritableDatabase();
//            db.removeAll();
//            Gson gson = new Gson();
//            List<GasSensorMeasure> measures =  gson.fromJson(response, new TypeToken<List<GasSensorMeasure>>(){}.getType());
//            for (GasSensorMeasure measure : measures){
//                db.addMeasure(measure);
//            }
//        }
//    }

}
