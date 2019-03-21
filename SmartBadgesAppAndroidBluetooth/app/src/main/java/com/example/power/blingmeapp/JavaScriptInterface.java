package com.example.power.blingmeapp;
import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.example.power.blingmeapp.Handlers.Alarm;
import com.example.power.blingmeapp.Objects.*;
//import com.example.power.blingmeapp.Objects.NotificationDataBase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static android.content.Context.ALARM_SERVICE;
public class JavaScriptInterface {
    public static BluetoothDevice BLUETOOTH_BADGE;
    private static Activity activity;
    private Boolean bluetoothConnected = false;
    BluetoothAdapter mBluetoothAdapter;
    List<BluetoothDevice> bluetoothDevices = new ArrayList<>();
    List<String> bluetoothDevicesNames = new ArrayList<>();
    public BluetoothDevice btDevice = null;
    public Database db;
    public JavaScriptInterface(Activity activity) {
        this.activity = activity;
        this.db = new Database(activity.getApplicationContext());
    }

    @JavascriptInterface
    public void connectToDevice() {
        if (// ask permission needed for the app
                ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
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
                            Manifest.permission.PROCESS_OUTGOING_CALLS,
                            Manifest.permission.BLUETOOTH,
                            Manifest.permission.BLUETOOTH_ADMIN,
                            Manifest.permission.READ_CALL_LOG

                    },
                    1);
            if (ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE) != PackageManager.PERMISSION_GRANTED) {
                //ask for permission to show  notifications
                Toast.makeText(activity.getApplicationContext(), "Enable the notification access for our app... ", Toast.LENGTH_LONG).show();
                activity.startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
            }
//            if (ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE) != PackageManager.PERMISSION_GRANTED) {
//                activity.startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
//                Toast.makeText(activity.getApplicationContext(), "Enable the notification access for our app... ", 3 * Toast.LENGTH_LONG).show();
//            }

        } else {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); // check if the user phone has a bluetooth adapter
            if (mBluetoothAdapter == null ) { // if the device does not have a bluetooth adapter it is not possible to use the app
                Toast.makeText(activity.getApplicationContext(), "Your device cannot connect to bluetooth device", Toast.LENGTH_LONG).show();
            }
            else if (mBluetoothAdapter.isEnabled()){// if the bluetooth adapter is enabled, start looking for devices
                getDevice();
           } else if (!mBluetoothAdapter.isEnabled()){// if the bluetooth adapater is not enabled, enable it and start looking for devices
                mBluetoothAdapter.enable();
                getDevice();
                if (bluetoothConnected) JavaScriptInterface.callJSMethod("app.connectDevice();");//return true;
            }
        }
    }

    @JavascriptInterface
    public void saveConfiguration(String[] appNames, String[] colorString) {// this fucntion save the configurations of the apps notifications chosen by the user to the user database
        db.getWritableDatabase();
        CustomizedNotification notification;
        for (int i = 0; i < appNames.length; i++) {
            notification = new CustomizedNotification(appNames[i], colorString[i]);
            db.addNotification(notification);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @JavascriptInterface
    public String getAllPhoneContacts() {// send all the phone contacts to the front end
        int hasPhone;
        Gson gson = new Gson();
        List<Contact> contactsInAgenda = new ArrayList<>();
        Cursor cursor = activity.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        int i = 0;
        while (cursor.moveToNext()) {
            i++;
            if (cursor != null) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                hasPhone = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));// check if the contact has a phone number
                if (hasPhone > 0) {// if the contact has a phone number get the contact information
                    Cursor cp = activity.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    if (cp != null && cp.moveToFirst()) { // get the contact information and add the contact in the list
                        String number = cp.getString(cp.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        // taake out the non numeric chars and left zeros
                        number = number.replaceAll("[\\s\\-\\(\\)\\+]", "");
                        char c  = number.charAt(0);
                        while (c == '0'){
                            number = number.substring(1);
                            c  = number.charAt(0);
                        }
                        contactsInAgenda.add(new Contact(i, cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)), number));
                        cp.close();
                    }
                }
            }
        }
        Contact contact1;
        for (Contact contact : contactsInAgenda) { // update the contact colors with the alrrady saved colors before sending it to the frontend
            contact1 = db.getContact(contact.getNumber());
            if (contact1 != null) {
                contact.setColor(contact1.getColor());
                contact.setColorBrihgtness(contact1.getColorBrihgtness());
            }

        }
        Collections.sort(contactsInAgenda);// convert to gson string
        return gson.toJson(contactsInAgenda);
    }

    @JavascriptInterface
    public void saveContactsColors(String contactColors) {// save the contact colors chosen by the user in the database
        Gson gson = new Gson();
        List<Contact> contacts =  gson.fromJson(contactColors, new TypeToken<List<Contact>>() {// convert the gson string in the list of objects
        }.getType());
        db.getWritableDatabase();
        for (Contact contact : contacts){
            db.addContact(contact);
        }
        System.out.println(contacts);
    }

    @JavascriptInterface
    public void createAlarmForMeetings() throws ParseException {// function is called from the frontend
        db.getReadableDatabase();
        List<Event> events = db.getAllEvents();
        for(Event event: events){
            createAlarmForMeeting(event.getTitle(),event.getCalendar(),event.getColor());// for each event, create an alarm
        }
    }
    public void createAlarmForMeeting(String title, Calendar calendar, String colorString) throws ParseException { // create an event given the time when the event is begining, the name and the color
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            ColorSetting colorSetting = new ColorSetting(new ColorCustomized(colorString));
            ColorSetting colorSetting2 = new ColorSetting(new ColorCustomized(colorString));
            String descriptionEventAlmostBeginning =  title + " will start in 2 minutes... " ;// set the title of the first notification
            String descriptionEventStarted =  title + " is starting..."; // set the title of the second notification
            colorSetting2.setBrightness(0);
            ///////////////////////////////////////////////////////////////////////////////////////ATTENTION TO THIS LINE HERE ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // real code
          //  DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
           // Calendar calendar = Calendar.getInstance();
           // calendar.setTime(dateFormat.parse(calendarString));
//        createAlarm(calendar, colorSetting2, descriptionEventStarted, activity.getApplicationContext());
//        if ((calendar.getTimeInMillis() - 2 * 60 * 1000 - System.currentTimeMillis()) > 0 ){
//            calendar.setTimeInMillis(calendar.getTimeInMillis() - 2 * 60 * 1000);
//            createAlarm(calendar, colorSetting, descriptionEventAlmostBeginning, activity.getApplicationContext());
//        }
        // test mode
        calendar.setTimeInMillis(System.currentTimeMillis() + 3000);
        createAlarm(calendar, colorSetting, descriptionEventAlmostBeginning , activity.getApplicationContext()); // create the alarm
        calendar.setTimeInMillis(System.currentTimeMillis() + 8000);
        createAlarm(calendar, colorSetting2, descriptionEventStarted, activity.getApplicationContext()); // create the alarm
    }

    public void createAlarm(Calendar calendar, ColorSetting colorSetting, String description, Context context) {
        // convert the information in an alarm manager form and create the alarm
        Long time = calendar.getTimeInMillis();
        Intent intent = new Intent(context, Alarm.class);
        Gson gson = new Gson();
        intent.putExtra("Color Configuration", gson.toJson(colorSetting).toString());
        intent.putExtra("Description", description);
        final int _id = (int) System.currentTimeMillis();
        PendingIntent p1 = PendingIntent.getBroadcast(context, _id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Long correctingTime = time - Math.abs(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) - Calendar.getInstance(TimeZone.getTimeZone("Europe/Berlin")).get(Calendar.HOUR_OF_DAY)) * 3600 * 1000;
        AlarmManager a = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        a.set(AlarmManager.RTC,  correctingTime, p1);
        Toast.makeText(context, "Alarm created!", Toast.LENGTH_LONG).show();
    }

    @JavascriptInterface
    public String getEventList() throws ParseException {
        // get all the events from the user callendar
        List<Event> events = new ArrayList<Event>();
        int titleId;
        String titleValue;
        int locationId;
        String locationValue;
        int startTimeId;
        Long startTimeValue;
        String eventString;
        Event event;
        Cursor cursor = activity.getContentResolver().query(CalendarContract.Events.CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                if (cursor != null) {
                    startTimeId = cursor.getColumnIndex(CalendarContract.Events.DTSTART);
                    startTimeValue = Long.parseLong(cursor.getString(startTimeId));
                    Long correctingTime = startTimeValue - Math.abs(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) - Calendar.getInstance(TimeZone.getTimeZone("Europe/Berlin")).get(Calendar.HOUR_OF_DAY)) * 3600 * 1000;
                    titleId = cursor.getColumnIndex(CalendarContract.Events.TITLE);
                    titleValue = cursor.getString(titleId);
                    long time = correctingTime - System.currentTimeMillis();
                    if (correctingTime > System.currentTimeMillis()) {// just take the future events
                        locationId = cursor.getColumnIndex(CalendarContract.Events.EVENT_LOCATION);
                        locationValue = cursor.getString(locationId);
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(startTimeValue);
                        event = new Event(0, titleValue, locationValue, calendar);
                        char hashtag ='#'; //character c is static...can be modified to accept user input
                        int hashtagCounter = 0;
                        String color = "";
                        for(int i=0;i<locationValue.length() - 7;i++)
                            if(locationValue.charAt(i)==hashtag){
                                if(locationValue.charAt(i + 1) == hashtag){
                                    for (int j = 0; j < 7; j++){
                                        color += locationValue.charAt(i + 1 + j);
                                    }
                                    try { // check if the color is on the right rgb hex format, if it is add the event in the database
                                        new ColorCustomized(color);
                                        event.setColor(color);
                                        events.add(event);
                                        break;
                                    }catch (Exception e){

                                    }
                                }
                            }
                    }
                } else {
                    break;

                }
            }
        }
        //save the events in the database and send it to the front end
        Collections.sort(events);
        Gson gson = new Gson();
        db.removeAllEvents();
        for(Event eventCreated: events){
            db.addEvent(eventCreated);
        }
        return gson.toJson(events);
    }
    // Create a BroadcastReceiver for ACTION_FOUND
    public static void callJSMethod(final String script) { // this method is used to call javascript functions from android we should write the script that must be run
        final WebView webView = (WebView) activity.findViewById(R.id.webView);
        webView.post(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void run() {
                webView.evaluateJavascript(script, null);
            }
        });
        System.out.println("javscript done..");
    }

    private BroadcastReceiver searchDevicesReceiver = new BroadcastReceiver() {// react if devices were found
        @Override
        public void onReceive(Context context, Intent intent) {
            Message msg = Message.obtain();
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                //Found, add to a device list
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                bluetoothDevices.add(device);
                bluetoothDevicesNames.add(device.getName());

            }
        }
    };
    private void startSearching() {// start searching for bluetooth devices
        Log.i("Log", "in the start searching method");
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        activity.registerReceiver(searchDevicesReceiver, intentFilter);
        mBluetoothAdapter.startDiscovery();
    }
    public boolean createBond(String name) throws Exception {// check if th device with the right name is found, if is connect with it
        for (String deviceName: bluetoothDevicesNames){
            if (deviceName != null && deviceName.toLowerCase().contains(name)){
                btDevice = bluetoothDevices.get(bluetoothDevicesNames.indexOf(deviceName));
                break;
            }
        }
        if (btDevice != null){
            Class class1 = Class.forName("android.bluetooth.BluetoothDevice");
            Method createBondMethod = class1.getMethod("createBond");
            Boolean returnValue = (Boolean) createBondMethod.invoke(btDevice);
            bluetoothConnected = returnValue;
            if (bluetoothConnected) {
                BLUETOOTH_BADGE = btDevice;
                Handler handler = new Handler();
                handler.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Toast.makeText(activity.getApplicationContext(), "Badge was found!", Toast.LENGTH_LONG).show();
                        callJSMethod("app.connectDevice();");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                            ConfigureLed configureLed = new ConfigureLed(null);
                            configureLed.initializeDevice(activity.getApplicationContext());
                        }
                    }
                }, 2000);
            }
        }
        return bluetoothConnected;
    }
    @JavascriptInterface
    public void getDevice() {// initialize the search for devices, this fuction is called from the front end
        Toast.makeText(activity.getApplicationContext(), "Checking for the Blueetooth badge...", Toast.LENGTH_SHORT).show();
        startSearching();
        try {
            TimeUnit.MILLISECONDS.sleep(400);// dont know how to wait for an amount of broadcast receivers
            if (createBond("badge")){

            } else {
                Toast.makeText(activity.getApplicationContext(), "Bluetooth badge was not found...", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
