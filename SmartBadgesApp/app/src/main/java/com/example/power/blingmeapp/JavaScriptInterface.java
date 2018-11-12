package com.example.power.blingmeapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.wifi.WifiManager;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.WIFI_SERVICE;

class JavaScriptInterface {
    private Activity activity;

    public JavaScriptInterface(Activity activity) {
        this.activity = activity;
    }

    @JavascriptInterface
    public boolean connectToDevice() {
        if (
                ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{
                            Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.CHANGE_WIFI_STATE,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.READ_CALENDAR,
                    Manifest.permission.READ_SMS,
                            Manifest.permission.RECEIVE_SMS,
                            Manifest.permission.PROCESS_OUTGOING_CALLS
                    },
                    1);
                    if (  ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE) != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(activity.getApplicationContext(), "Enable the notification access for our app... ", Toast.LENGTH_LONG).show();
                activity.startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
            }
//        if (
//                ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED
//                        || ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED
//                        || ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                        || ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED
//                        || ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
//                        || ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(activity, new String[]{
//                            Manifest.permission.ACCESS_WIFI_STATE,
//                            Manifest.permission.CHANGE_WIFI_STATE,
//                            Manifest.permission.ACCESS_COARSE_LOCATION,
//                            Manifest.permission.ACCESS_FINE_LOCATION,
//                            Manifest.permission.READ_PHONE_STATE,
//                            Manifest.permission.READ_SMS,
//                            Manifest.permission.RECEIVE_SMS,
//                            Manifest.permission.PROCESS_OUTGOING_CALLS
//                    },
//                    1);
            if (  ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE) != PackageManager.PERMISSION_GRANTED){
                activity.startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
                Toast.makeText(activity.getApplicationContext(), "Enable the notification access for our app... ", 3*Toast.LENGTH_LONG).show();
            }

        } else {
            WifiManager wifiManager = (WifiManager) activity.getApplicationContext().getSystemService(WIFI_SERVICE);
            if (!wifiManager.isWifiEnabled()) {
                Toast.makeText(activity.getApplicationContext(), "You need a wifi connection to connet to the device, please enable it!", Toast.LENGTH_LONG).show();
            } else {
                return true;
            }
        }
        return false;
    }
    @JavascriptInterface
    public void saveConfiguration(String[] appNames, String[] colorString) {
        NotificationDataBase db = new NotificationDataBase(activity.getApplicationContext());
        db.getWritableDatabase();
        CustomizedNotification notification;
        for (int i = 0; i < appNames.length; i++ ){
            String das = appNames[i];
            String der = colorString[i];
            notification = new CustomizedNotification(appNames[i],colorString[i]);
            db.addNotification(notification);
        }
    }
    @JavascriptInterface
    public String getAllPhoneContacts() {
        String contactValue;
        int hasPhone;
        Gson gson = new Gson();
        ColoredContactDatabase db = new ColoredContactDatabase(activity.getApplicationContext());
        db.getWritableDatabase();
        List<Contact> contacts = db.getAllContacts();
        if (contacts.isEmpty()){
            Cursor cursor = activity.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            int i = 0;
            while (cursor.moveToNext()){
                i++;
                if (cursor !=null){
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    contactValue = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    hasPhone = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                    if(hasPhone > 0){
                        Cursor cp = activity.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                        if (cp != null && cp.moveToFirst()) {
                            String number = cp.getString(cp.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            number = number.replaceAll("[\\s\\-\\(\\)]","");
                            contacts.add( new Contact(i,cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)),number));
                            cp.close();
                        }
                    }
                } else {
                    break;
                }
            }
        }
        return gson.toJson(contacts);
    }

    public void sendCollorToJewlery(String colorString){
        ConfigureLed configureLed =  new ConfigureLed(new ColorSetting(new ColorCustomized(colorString))) ;
        configureColorIndividually myTask = new configureColorIndividually();
        myTask.execute(configureLed);
    }

}
