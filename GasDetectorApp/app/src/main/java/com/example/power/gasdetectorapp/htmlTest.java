package com.example.power.gasdetectorapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Toast;

import java.util.List;

public class htmlTest extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_html_test);
        String myUrl = "file:///android_asset/index.html";
        WebView webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        JavaScriptInterface jsInterface = new JavaScriptInterface(this);
        webView.addJavascriptInterface(jsInterface, "JSInterface");
        webView.loadUrl(myUrl);
        //setContentView(webView);

    }

}
//    public  boolean connectToDevice(){
//        WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
//        if (!wifiManager.isWifiEnabled()){
//             //   Toast.makeText(this, "You need a wifi connection to connet to the device, please enable it!",Toast.LENGTH_LONG).show();
//            } else {
//                String networkSSID = "GSens";
//                String networkPass = "!SmellDetect!";
//                if (connect(networkSSID,networkPass)){
//                    //Intent intent = new Intent(this, MainActivity.class);
//                    //startActivity(intent);
//                    return true;
//
//                } else Toast.makeText(this, "There was a problem with the connection, please check the device and try again!",Toast.LENGTH_LONG).show();
//             }
//         return false;
//    }
//    public boolean connect(String ssid, String password) {
//        WifiManager mWifiManager = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
//        boolean findWifi = false;
//        List<ScanResult> results = mWifiManager.getScanResults();
//        for (ScanResult result: results) {
//            if (ssid.equals(result.SSID)) {
//                findWifi = true;
//                connectTo(result, password, ssid);
//            }
//        }
//        return findWifi;
//    }
//
//    public void connectTo(ScanResult result, String password, String ssid) {
//        //Make new configuration
//        WifiConfiguration conf = new WifiConfiguration();
//        conf.SSID = "\"" + ssid + "\"";
//        String Capabilities = result.capabilities;
//        if (Capabilities.contains("WPA2")) {
//            conf.preSharedKey = "\"" + password + "\"";
//        } else if (Capabilities.contains("WPA")) {
//            conf.preSharedKey = "\"" + password + "\"";
//        } else if (Capabilities.contains("WEP")) {
//            conf.wepKeys[0] = "\"" + password + "\"";
//            conf.wepTxKeyIndex = 0;
//            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
//            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
//        } else {
//            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
//        }
//        //Remove the existing configuration for this netwrok
//        WifiManager mWifiManager =     (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
//        List<WifiConfiguration> mWifiConfigList = mWifiManager.getConfiguredNetworks();
//        String comparableSSID = ('"' + ssid + '"'); //Add quotes because wifiConfig.SSID has them
//        for(WifiConfiguration wifiConfig : mWifiConfigList){
//            if(wifiConfig.SSID.equals(comparableSSID)){
//                int networkId = wifiConfig.networkId;
//                mWifiManager.removeNetwork(networkId);
//                mWifiManager.saveConfiguration();
//            }
//        }
//        //Add configuration to Android wifi manager settings...
//        WifiManager wifiManager = (WifiManager) (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
//        mWifiManager.addNetwork(conf);
//        //Enable it so that android can connect
//        List < WifiConfiguration > list = mWifiManager.getConfiguredNetworks();
//        for (WifiConfiguration i: list) {
//            if (i.SSID != null && i.SSID.equals("\"" + ssid + "\"")) {
//                wifiManager.disconnect();
//                wifiManager.enableNetwork(i.networkId, true);
//                wifiManager.reconnect();
//                break;
//            }
//        }
//    }
//}
