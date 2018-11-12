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
import com.google.gson.reflect.TypeToken;

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
                || ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity, new String[]{
                            Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.CHANGE_WIFI_STATE,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    1);

        } else {
            WifiManager wifiManager = (WifiManager) activity.getApplicationContext().getSystemService(WIFI_SERVICE);
            if (!wifiManager.isWifiEnabled()) {
                Toast.makeText(activity.getApplicationContext(), "You need a wifi connection to connet to the device, please enable it!", Toast.LENGTH_LONG).show();
            } else {
                String networkSSID = "GSens";
                String networkPass = "!SmellDetect!";
                if (connect(networkSSID, networkPass)) {
                    CheckingLocalServerConnection serverConnection = new CheckingLocalServerConnection(activity.getApplicationContext());
                    serverConnection.execute();
                    long pastTime = System.currentTimeMillis();
                    long timeDifference = System.currentTimeMillis() - pastTime;
                    while (timeDifference < 150)
                        timeDifference = System.currentTimeMillis() - pastTime;
                    if (serverConnection.getStatus() == AsyncTask.Status.FINISHED) {
//                        GasSensorDataBase db = new GasSensorDataBase(activity.getApplicationContext());
//                        db.getReadableDatabase();
//                        db.removeAll();
                        return true;
                    } else {
                        serverConnection.cancel(true);
                        Toast.makeText(activity.getApplicationContext(), "There was a problem with the server connection, please check the server and try again!", Toast.LENGTH_LONG).show();
                    }
                } else
                    Toast.makeText(activity.getApplicationContext(), "There was a problem with the device connection, please check the device and try again!", Toast.LENGTH_LONG).show();
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
        GasSensorMeasure measure = null;
        Gson gson = new Gson();
        String response;
        GettingGasMeauseHTTPRequestHTML httpRequest = new GettingGasMeauseHTTPRequestHTML(activity.getApplicationContext());
        httpRequest.execute();
        return true;
    }

    @JavascriptInterface
    public void saveMeasureIntoServer() throws ExecutionException, InterruptedException {
        GasSensorDataBase db = new GasSensorDataBase(activity.getApplicationContext());
        db.getWritableDatabase();
        List<GasSensorMeasure> measures = db.getAllMeasures();
        for (GasSensorMeasure measure : measures) {
            // check if this multi task works
            SendingGasMeausesToServer httpPost = new SendingGasMeausesToServer(activity.getApplicationContext());
            httpPost.execute(measure);
        }
        GasSensorMeasure measure = new GasSensorMeasure(959, 960, 961, 962, 963, 964, "964");
        SendingGasMeausesToServer httpPost = new SendingGasMeausesToServer(activity.getApplicationContext());
        httpPost.execute(measure);
    }

    @JavascriptInterface
    public String getSensorPoints() throws ExecutionException, InterruptedException {
        GettingGasMeausesFromServerHTTPRequestHTML httpGetAllResults = new GettingGasMeausesFromServerHTTPRequestHTML(activity.getApplicationContext());
        String str_result = httpGetAllResults.execute().get();
        // GasSensorDataBase db = new GasSensorDataBase(activity.getApplicationContext());
        //db.getReadableDatabase();
        // List<GasSensorMeasure> measures = db.getAllMeasures();
        Gson gson = new Gson();
        List<GasSensorMeasure> measures = gson.fromJson(str_result, new TypeToken<List<GasSensorMeasure>>() {
        }.getType());
        int[][] sensorMeasurements = new int[4][measures.size()];
        if (!str_result.isEmpty()) {
            for (int i = 0; i < measures.size(); i++) {
                sensorMeasurements[0][i] = i;
                sensorMeasurements[1][i] = measures.get(i).getSensor1();
                sensorMeasurements[2][i] = measures.get(i).getSensor2();
                sensorMeasurements[3][i] = measures.get(i).getSensor3();
            }
            // normalizing the results
            int initialValueSensor1 = sensorMeasurements[1][0];
            int initialValueSensor2 = sensorMeasurements[2][0];
            int initialValueSensor3 = sensorMeasurements[3][0];
            for (int i = 0; i < measures.size(); i++) {
                sensorMeasurements[1][i] = (sensorMeasurements[1][i] - initialValueSensor1);
                sensorMeasurements[2][i] = (sensorMeasurements[2][i] - initialValueSensor2);
                sensorMeasurements[3][i] = (sensorMeasurements[3][i] - initialValueSensor3);
            }
        }

        return gson.toJson(sensorMeasurements);

    }

    @JavascriptInterface
    public String getSensorPointClassification() throws ExecutionException, InterruptedException {
        ClassifyingGasMeausesFromServer httpGetAllResults = new ClassifyingGasMeausesFromServer(activity.getApplicationContext());
        String str_result = httpGetAllResults.execute().get();
        Gson gson = new Gson();
        return str_result;

    }
}

