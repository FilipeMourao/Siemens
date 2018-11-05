package com.example.power.gasdetectorapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import static android.content.Context.WIFI_SERVICE;

public class JavaScriptInterface {
    private Activity activity;

    public JavaScriptInterface(Activity activity) {
        this.activity = activity;
    }

    @JavascriptInterface
    public boolean connectToDevice() {
        if (ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(activity, new String[]{
                            Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.CHANGE_WIFI_STATE,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    1);

        }
        else {WifiManager wifiManager = (WifiManager) activity.getApplicationContext().getSystemService(WIFI_SERVICE);
            if (!wifiManager.isWifiEnabled()) {
                Toast.makeText(activity.getApplicationContext(), "You need a wifi connection to connet to the device, please enable it!", Toast.LENGTH_LONG).show();
            } else {
                String networkSSID = "GSens";
                String networkPass = "!SmellDetect!";
                if (connect(networkSSID, networkPass)) {
                    //Intent intent = new Intent(this, MainActivity.class);
                    //startActivity(intent);
                    GasSensorDataBase db = new GasSensorDataBase(activity.getApplicationContext());
                    db.getReadableDatabase();
                    db.removeAll();
                    return true;

                } else
                    Toast.makeText(activity.getApplicationContext(), "There was a problem with the connection, please check the device and try again!", Toast.LENGTH_LONG).show();
            }

        }
        return false;
    }

    public boolean connect(String ssid, String password) {
        WifiManager mWifiManager = (WifiManager) activity.getApplicationContext().getSystemService(WIFI_SERVICE);
        boolean findWifi = false;
        List<ScanResult> results = mWifiManager.getScanResults();
        for (ScanResult result : results) {
            if (ssid.equals(result.SSID)) {
                findWifi = true;
                connectTo(result, password, ssid);
            }
        }
        return findWifi;
    }

    public void connectTo(ScanResult result, String password, String ssid) {
        //Make new configuration
        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"" + ssid + "\"";
        String Capabilities = result.capabilities;
        if (Capabilities.contains("WPA2")) {
            conf.preSharedKey = "\"" + password + "\"";
        } else if (Capabilities.contains("WPA")) {
            conf.preSharedKey = "\"" + password + "\"";
        } else if (Capabilities.contains("WEP")) {
            conf.wepKeys[0] = "\"" + password + "\"";
            conf.wepTxKeyIndex = 0;
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        } else {
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        }
        //Remove the existing configuration for this netwrok
        WifiManager mWifiManager = (WifiManager) activity.getApplicationContext().getSystemService(WIFI_SERVICE);
        List<WifiConfiguration> mWifiConfigList = mWifiManager.getConfiguredNetworks();
        String comparableSSID = ('"' + ssid + '"'); //Add quotes because wifiConfig.SSID has them
        for (WifiConfiguration wifiConfig : mWifiConfigList) {
            if (wifiConfig.SSID.equals(comparableSSID)) {
                int networkId = wifiConfig.networkId;
                mWifiManager.removeNetwork(networkId);
                mWifiManager.saveConfiguration();
            }
        }
        //Add configuration to Android wifi manager settings...
        WifiManager wifiManager = (WifiManager) (WifiManager) activity.getApplicationContext().getSystemService(WIFI_SERVICE);
        mWifiManager.addNetwork(conf);
        //Enable it so that android can connect
        List<WifiConfiguration> list = mWifiManager.getConfiguredNetworks();
        for (WifiConfiguration i : list) {
            if (i.SSID != null && i.SSID.equals("\"" + ssid + "\"")) {
                wifiManager.disconnect();
                wifiManager.enableNetwork(i.networkId, true);
                wifiManager.reconnect();
                break;
            }
        }
    }

    @JavascriptInterface
    public boolean analyze() throws ExecutionException, InterruptedException {
        GasSensorDataBase db = new GasSensorDataBase(activity.getApplicationContext());
        db.getWritableDatabase();
        Toast.makeText(activity.getApplicationContext(), "Waiting new measurement...", Toast.LENGTH_SHORT).show();
        int databasePreviousSize = db.getAllMeasures().size();
        GasSensorMeasure measure = null;
        Gson gson = new Gson();
        String response;
        GettingGasMeauseHTTPRequestHTML httpRequest = new GettingGasMeauseHTTPRequestHTML(activity.getApplicationContext());
        httpRequest.execute();
        return true;
    }

    @JavascriptInterface
    public String getSensorPoints() {
        GasSensorDataBase db = new GasSensorDataBase(activity.getApplicationContext());
        db.getReadableDatabase();
//            db.addMeasure(mes);
//        }
        List<GasSensorMeasure> measures = db.getAllMeasures();
        int[][] sensorMeasurements = new int[4][measures.size()];
        for (int i = 0; i < measures.size(); i++) {
            sensorMeasurements[0][i] = i;
            sensorMeasurements[1][i] = measures.get(i).getSensor1();
            sensorMeasurements[2][i] = measures.get(i).getSensor2();
            sensorMeasurements[3][i] = measures.get(i).getSensor3();
        }
        // normalizing the results
        int sensor1MinResult = getMinValue(sensorMeasurements[1]);
        int sensor2MinResult = getMinValue(sensorMeasurements[2]);
        int sensor3MinResult = getMinValue(sensorMeasurements[3]);
        for (int i = 0; i < measures.size();i++){
            sensorMeasurements[1][i] = (sensorMeasurements[1][i] - sensor1MinResult);
            sensorMeasurements[2][i] = (sensorMeasurements[2][i] - sensor2MinResult);
            sensorMeasurements[3][i] = (sensorMeasurements[3][i] - sensor3MinResult);
        }

        Gson gson = new Gson();
        return gson.toJson(sensorMeasurements).toString();

    }
    @JavascriptInterface
    public boolean isAlcohol() {
        GasSensorDataBase db = new GasSensorDataBase(activity.getApplicationContext());
        db.getReadableDatabase();
//        Random rand = new Random();
//        db.getWritableDatabase();
//        for (int i = 0; i < 10; i++) {
//            GasSensorMeasure mes = new GasSensorMeasure(rand.nextInt(100),
//                    rand.nextInt(100),
//                    rand.nextInt(100),
//                    rand.nextInt(100),
//                    rand.nextInt(100),
//                    rand.nextInt(100),
//                    "test"
//            );
//            db.addMeasure(mes);
//        }
        List<GasSensorMeasure> measures = db.getAllMeasures();
        if (measures.size() > 0)
            if (measures.get(measures.size() - 1).getSensor1() > 1000) return true;
        return false;

    }


    public static int getMinValue(int[] numbers){
        int minValue = numbers[0];
        for(int i=1;i<numbers.length;i++){
            if(numbers[i] < minValue){
                minValue = numbers[i];
            }
        }
        return minValue;
    }
}

