package com.example.power.gasdetectorapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;

import static android.content.Context.WIFI_SERVICE;

public class JavaScriptInterface {
    private Activity activity;
    public JavaScriptInterface(Activity activity) {
        this.activity = activity;
    }

    @JavascriptInterface
    public  boolean connectToDevice(){
        WifiManager wifiManager = (WifiManager)activity.getApplicationContext().getSystemService(WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()){
              Toast.makeText(activity.getApplicationContext(), "You need a wifi connection to connet to the device, please enable it!",Toast.LENGTH_LONG).show();
        } else {
            String networkSSID = "GSens";
            String networkPass = "!SmellDetect!";
            if (connect(networkSSID,networkPass)){
                //Intent intent = new Intent(this, MainActivity.class);
                //startActivity(intent);
                return true;

            } else Toast.makeText(activity.getApplicationContext(), "There was a problem with the connection, please check the device and try again!",Toast.LENGTH_LONG).show();
        }
        return false;
    }
    public boolean connect(String ssid, String password) {
        WifiManager mWifiManager = (WifiManager)activity.getApplicationContext().getSystemService(WIFI_SERVICE);
        boolean findWifi = false;
        List<ScanResult> results = mWifiManager.getScanResults();
        for (ScanResult result: results) {
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
        WifiManager mWifiManager =     (WifiManager)activity.getApplicationContext().getSystemService(WIFI_SERVICE);
        List<WifiConfiguration> mWifiConfigList = mWifiManager.getConfiguredNetworks();
        String comparableSSID = ('"' + ssid + '"'); //Add quotes because wifiConfig.SSID has them
        for(WifiConfiguration wifiConfig : mWifiConfigList){
            if(wifiConfig.SSID.equals(comparableSSID)){
                int networkId = wifiConfig.networkId;
                mWifiManager.removeNetwork(networkId);
                mWifiManager.saveConfiguration();
            }
        }
        //Add configuration to Android wifi manager settings...
        WifiManager wifiManager = (WifiManager) (WifiManager)activity.getApplicationContext().getSystemService(WIFI_SERVICE);
        mWifiManager.addNetwork(conf);
        //Enable it so that android can connect
        List < WifiConfiguration > list = mWifiManager.getConfiguredNetworks();
        for (WifiConfiguration i: list) {
            if (i.SSID != null && i.SSID.equals("\"" + ssid + "\"")) {
                wifiManager.disconnect();
                wifiManager.enableNetwork(i.networkId, true);
                wifiManager.reconnect();
                break;
            }
        }
    }
    @JavascriptInterface
    public boolean analyze(){
        Toast.makeText(activity.getApplicationContext(),"Waiting measurement...",Toast.LENGTH_LONG).show();
        GasSensorDataBase db = new GasSensorDataBase(activity.getApplicationContext());db.getWritableDatabase();
        int databasePreviousSize = db.getAllMeasures().size();
        GasSensorMeasure measure = null;
        Gson gson = new Gson();
        String response;
        GettingGasMeauseHTTPRequestHTML httpRequest = new GettingGasMeauseHTTPRequestHTML(activity.getApplicationContext());
        httpRequest.execute();
       // while (httpRequest.getStatus() == AsyncTask.Status.RUNNING){};
        return true;
    }

    @JavascriptInterface
    public int[][] getSensor1Points(){
        GasSensorDataBase db = new GasSensorDataBase(activity.getApplicationContext());
        db.getReadableDatabase();
        List<GasSensorMeasure> measures = db.getAllMeasures();
        int[][] sensorMeasurements = new int[measures.size()][2];
        for (int i = 0; i < measures.size(); i++){
            sensorMeasurements[i][0] = i;
            sensorMeasurements[i][1] = measures.get(i).getSensor1();
        }
        return sensorMeasurements;

    }

}

