package com.example.power.gasdetectorappFirebase;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;

import com.example.power.gasdetectorappFirebase.ObjectsAndDatabase.ClassificationGasMeasure;
import com.example.power.gasdetectorappFirebase.ObjectsAndDatabase.GasSensorDataBase;
import com.example.power.gasdetectorappFirebase.ObjectsAndDatabase.GasSensorMeasure;
import com.example.power.gasdetectorappFirebase.ServerConnection.FirebaseAppConnection;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.content.Context.WIFI_SERVICE;

public class JavaScriptInterface {
    static private Activity activity;
    private FirebaseAppConnection firebaseAppConnection;
    private GasSensorDataBase db;
    private int countingNumberOfOcurrences = 0;
    private static int numberOfTimesToAccessData = 5;
    public JavaScriptInterface(Activity activity) {
        this.activity = activity;
        this.firebaseAppConnection = new FirebaseAppConnection(activity);
        db = new GasSensorDataBase(activity);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @JavascriptInterface
    public boolean testConnection() {
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
                if (connectToDevice()) {
                    countingNumberOfOcurrences = 0;
                    callJSMethod("app.deviceConnected();");

                } else{
                    Toast.makeText(activity.getApplicationContext(), "There was a problem with the device connection, please check the device and try again!", Toast.LENGTH_LONG).show();
                    callJSMethod("backToConnectScreen();");
                    countingNumberOfOcurrences = countingNumberOfOcurrences +1;
                    if (countingNumberOfOcurrences >= numberOfTimesToAccessData){ // if the number of times was achieved open the app without the device
                        countingNumberOfOcurrences = 0;
                        AlertDialog.Builder altdial = new AlertDialog.Builder(activity);
                        altdial.setMessage("No device found, do you want to watch the measurements without the device?").setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        callJSMethod("goDirectToMeasures();");
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });

                        AlertDialog alert = altdial.create();
                        try {
                            alert.show();
                        } catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }

            }
        }
        return false;
    }
    public static boolean connect(String ssid, String password) {
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

    public static void connectTo(ScanResult result, String password, String ssid) {
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
        GettingGasMeauseHTTPRequest httpRequest = new GettingGasMeauseHTTPRequest(activity.getApplicationContext());
        httpRequest.execute();
        return true;
    }

    @JavascriptInterface
    public void saveMeasureIntoServer() throws ExecutionException, InterruptedException {
        firebaseAppConnection.saveSensorMeasures();
    }

    @JavascriptInterface
    public void getSensorPoints() throws ExecutionException, InterruptedException {
        db.getReadableDatabase();
        List<GasSensorMeasure> measures = db.getAllMeasures();
        Gson gson = new Gson();
        int[][] sensorMeasurements = new int[4][measures.size()];
        if (!measures.isEmpty()) {
            for (int i = 0; i < measures.size(); i++) {
//                sensorMeasurements[0][i] = measures.get(i).getId();
                sensorMeasurements[0][i] = i;
                sensorMeasurements[1][i] = measures.get(i).getSensor1();
                sensorMeasurements[2][i] = measures.get(i).getSensor2();
                sensorMeasurements[3][i] = measures.get(i).getSensor3();
            }
        }
        String gsonString = gson.toJson(sensorMeasurements);
        callJSMethod("app.createChartNew("+gsonString+");");


    }
    @JavascriptInterface
    public void deleteOrClassifyMeasure(int i) throws ExecutionException, InterruptedException {
        AlertDialog.Builder altdial = new AlertDialog.Builder(activity);
        final int id = i;
        altdial.setMessage( "You select the measure with ID "+ Integer.toString(i)).setCancelable(false)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        try {
                            deleteData(id);
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Classify", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        try {
                            classifyMeasure(id);
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });

        AlertDialog alert = altdial.create();
        try {
            alert.show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void deleteData(int i) throws ExecutionException, InterruptedException {
                //Toast.makeText(context,"New measurement available",Toast.LENGTH_LONG).show();
        AlertDialog.Builder altdial = new AlertDialog.Builder(activity);
        final int id = i;
        altdial.setMessage( "What information of element with ID "+ Integer.toString(i) +" do you want to delete ?").setCancelable(false)
                .setPositiveButton("Measure", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (connectToSupperBecca()) firebaseAppConnection.deleteGasSensorMeasure(db.getGasSensorMeasure(id));
                        else db.removeGasSensorMeasure(db.getGasSensorMeasure(id));
                        try {
                            getSensorPoints();
                            callJSMethod("app.createTable();");
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                })
                .setNegativeButton("Classification", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (connectToSupperBecca()) firebaseAppConnection.deleteGasSensorClassification(db.getClassificationDataByID(id));
                        else db.removeGasSensorClassification(db.getClassificationDataByID(id));
                        try {
                            getSensorPoints();
                            callJSMethod("app.createTable();");
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = altdial.create();
        alert.setTitle("Delete");
        try {
            alert.show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public  void classifyMeasure(int i)throws ExecutionException, InterruptedException {
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        final EditText edittext = new EditText(activity);
        final int id = i;
        alert.setMessage("Enter the classification name");
        alert.setView(edittext);
        alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String classifyierString = edittext.getText().toString();
                System.out.println(classifyierString);
                GasSensorMeasure gasSensorMeasure = db.getGasSensorMeasure(id);
                db.addClassification(new ClassificationGasMeasure(gasSensorMeasure.getSensor1(),gasSensorMeasure.getSensor2(),gasSensorMeasure.getSensor3(),classifyierString));
                try {
                    getSensorPoints();
                    callJSMethod("app.createTable();");
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });

        alert.show();
    }
    @JavascriptInterface
    public String getSensorPointClassification() throws ExecutionException, InterruptedException {
        List<String[]> gasesInformation = new ArrayList<>();
        String currentlyString;
        String[] currentlyArray;
        db.getReadableDatabase();
        List<GasSensorMeasure> measures = db.getAllMeasures();
        Gson gson = new Gson();
        for (GasSensorMeasure measure : measures){
            currentlyString = db.getClassification(measure) ;
            currentlyArray = new String[]{currentlyString, Integer.toString(measure.getId())};
            gasesInformation.add(currentlyArray);
        }
        return gson.toJson(gasesInformation);
    }

    @JavascriptInterface
    public void downloadData() throws ExecutionException, InterruptedException {
        //Toast.makeText(context,"New measurement available",Toast.LENGTH_LONG).show();
        AlertDialog.Builder altdial = new AlertDialog.Builder(activity);
        altdial.setMessage( "Do you want to download gas measures or classifiers  ?").setCancelable(false)
                .setPositiveButton("Measures", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (connectToSupperBecca()){
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Measures");
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @RequiresApi(api = Build.VERSION_CODES.N)
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        // dataSnapshot is the "issue" node with all children with id 0
                                        for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                                            try{
                                                GasSensorMeasure gasSensorMeasure = eventSnapshot.getValue(GasSensorMeasure.class);
                                                if (gasSensorMeasure != null)
                                                    db.addMeasure(gasSensorMeasure);
                                            }catch (Exception e){
                                                e.getMessage();
                                            }
                                        }
                                        try {
                                            getSensorPoints();
                                            callJSMethod("app.createTable();");
                                        } catch (ExecutionException e) {
                                            e.printStackTrace();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Classifiers", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (connectToSupperBecca()){
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Classifications");
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @RequiresApi(api = Build.VERSION_CODES.N)
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        // dataSnapshot is the "issue" node with all children with id 0
                                        for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                                            try{
                                                ClassificationGasMeasure classificationGasMeasure= eventSnapshot.getValue(ClassificationGasMeasure.class);
                                                if (classificationGasMeasure != null)
                                                    db.addClassification(classificationGasMeasure);
                                            }catch (Exception e){
                                                e.getMessage();
                                            }
                                        }
                                        try {
                                            getSensorPoints();
                                            callJSMethod("app.createTable();");
                                        } catch (ExecutionException e) {
                                            e.printStackTrace();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                });
        AlertDialog alert = altdial.create();
        alert.setTitle("Download");
        try {
            alert.show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    @JavascriptInterface
    public void updloadData() throws ExecutionException, InterruptedException {
        //Toast.makeText(context,"New measurement available",Toast.LENGTH_LONG).show();
        AlertDialog.Builder altdial = new AlertDialog.Builder(activity);
        altdial.setMessage( "Do you want to upload gas measures or classifiers ?").setCancelable(false)
                .setPositiveButton("Measures", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (connectToSupperBecca()) firebaseAppConnection.saveSensorMeasures();
                       // connectToDevice();
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Classifiers", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (connectToSupperBecca()) firebaseAppConnection.saveSensorMeasuresClassification();
                       // connectToDevice();
                    }
                });
        AlertDialog alert = altdial.create();
        alert.setTitle("Upload");
        try {
            alert.show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
public static void callJSMethod(final String script) {
    final WebView webView = (WebView) activity.findViewById(R.id.webView);
    webView.post(new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public void run() {
            webView.evaluateJavascript(script, null);
        }
    });
    System.out.println("javscript done..");
    }
    public static boolean connectToSupperBecca(){
        String networkSSID = "SuperBecca";
        String networkPass = "beccabecca";
        return connect(networkSSID,networkPass);
    }
    public static boolean connectToDevice(){
        String networkSSID = "GSens";
        String networkPass = "!SmellDetect!";
        return connect(networkSSID,networkPass);
    }
}

