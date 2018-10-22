package com.example.power.gasdetectorapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.List;

public class ConnectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:  {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_STATE) == PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            ){
                    }
                }
                break;
            }

        }

    }

    public void startButtonClicked(View v){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

                         ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.CHANGE_WIFI_STATE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                        },
                        1);

        }
        else {
            WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
            if (!wifiManager.isWifiEnabled()){
                Toast.makeText(this, "You need a wifi connection to connet to the device, please enable it!",Toast.LENGTH_LONG).show();
            } else {
                String networkSSID = "GSens";
                String networkPass = "!SmellDetect!";
                if (connect(networkSSID,networkPass)){


                } else Toast.makeText(this, "There was a problem with the connection, please check the device and try again!",Toast.LENGTH_LONG).show();
            }
        }
    }

    public boolean connect(String ssid, String password) {
        WifiManager mWifiManager = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
        boolean findWifi = false;
        List <ScanResult> results = mWifiManager.getScanResults();
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
        WifiManager mWifiManager =     (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
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
        WifiManager wifiManager = (WifiManager) (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
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
}
