package com.example.power.gasdetectorappFirebase;

import android.content.Context;
import android.os.AsyncTask;
import com.example.power.gasdetectorappFirebase.ObjectsAndDatabase.GasSensorDataBase;
import com.example.power.gasdetectorappFirebase.ObjectsAndDatabase.GasSensorMeasure;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

// this is the class used to  take the device information by HTTP requests, must be an async task
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
            //add request header
            con.setConnectTimeout(15000); // defining a time limit for the connection
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            int responseCode = con.getResponseCode();
                System.out.println("\nSending 'GET' request to URL : " + url);
                System.out.println("Response Code : " + responseCode);
            BufferedReader in =new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            } in .close();
        } catch (SocketTimeoutException e) { // if the time limit is done go to the results view without a new measure
            JavaScriptInterface.callJSMethod("" +
                    "var gas_name = 'Not able to get the new measure!';" +
                    "$('.result-name').text(gas_name);"+
                    "app.showresult(getRandomColor());" +
                    "");
            break;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    return response.toString();
        }
    protected void onPostExecute(String response) {
        if (!response.isEmpty()) saveGasSensorObject(response);
    }
    public void saveGasSensorObject(String measureString){
        Gson gson = new Gson();
        GasSensorMeasure measure   = gson.fromJson(measureString, GasSensorMeasure.class);
        GasSensorMeasure finalMeasure = new GasSensorMeasure(measure);// create a measure with a unique ID
        saveMeasureInDataBase(finalMeasure );
    }
    public void saveMeasureInDataBase(GasSensorMeasure measure){
        GasSensorDataBase db = new GasSensorDataBase(context);
        db.getWritableDatabase();
        db.addMeasure(measure);
        JavaScriptInterface.callJSMethod("" +
                        "var gas_name = 'New measure available!';" +
                        "$('.result-name').text(gas_name);"+
                        "app.showresult(getRandomColor());" +
                "");
    }
}
