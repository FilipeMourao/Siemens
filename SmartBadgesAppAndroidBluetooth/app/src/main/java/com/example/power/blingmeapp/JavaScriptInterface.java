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
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
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
class JavaScriptInterface {
    public static BluetoothDevice BLUETOOTH_BADGE;
    private Activity activity;
    private Boolean bluetoothConnected = false;
    BluetoothAdapter mBluetoothAdapter;
    List<BluetoothDevice> bluetoothDevices = new ArrayList<>();
    List<String> bluetoothDevicesNames = new ArrayList<>();
    public BluetoothDevice btDevice = null;

    public JavaScriptInterface(Activity activity) {
        this.activity = activity;
    }

    private BroadcastReceiver searchDevicesReceiver = new BroadcastReceiver() {
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

    private void startSearching() {
        Log.i("Log", "in the start searching method");
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        activity.registerReceiver(searchDevicesReceiver, intentFilter);
        mBluetoothAdapter.startDiscovery();
    }
    public boolean createBond(String name)
            throws Exception
    {
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
                    }
                }, 2000);
            }
        }
        return bluetoothConnected;

    }
//    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
//        public void onReceive(Context context, Intent intent) {
//            String TAG = "BluetoothTask";
//            String action = intent.getAction();
//            // When discovery finds a device
//            if (action.equals(mBluetoothAdapter.ACTION_STATE_CHANGED)) {
//                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, mBluetoothAdapter.ERROR);
//                switch(state){
//                    case BluetoothAdapter.STATE_OFF:
//                        Log.d(TAG, "onReceive: STATE OFF");
//                        break;
//                    case BluetoothAdapter.STATE_TURNING_OFF:
//                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING OFF");
//                        break;
//                    case BluetoothAdapter.STATE_ON:
//                        Log.d(TAG, "mBroadcastReceiver1: STATE ON");
//                        break;
//                    case BluetoothAdapter.STATE_TURNING_ON:
//                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING ON");
//                        break;
//                }
//            }
//        }
//    };

    @JavascriptInterface
    public void getDevice() {
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
                Toast.makeText(activity.getApplicationContext(), "Enable the notification access for our app... ", Toast.LENGTH_LONG).show();
                activity.startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
            }
            if (ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE) != PackageManager.PERMISSION_GRANTED) {
                activity.startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
                Toast.makeText(activity.getApplicationContext(), "Enable the notification access for our app... ", 3 * Toast.LENGTH_LONG).show();
            }

        } else {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter == null ) {
                Toast.makeText(activity.getApplicationContext(), "Your device cannot connect to bluetooth device", Toast.LENGTH_LONG).show();
                return false;
            } else if (mBluetoothAdapter.isEnabled()){
                if (bluetoothConnected) {
                        // show that the led is on
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                            ConfigureLed configureLed = new ConfigureLed(null);
                            configureLed.initializeDevice(activity.getApplicationContext());
                        }
                    return true;
                }
                getDevice();
                return false;
            } else if (!mBluetoothAdapter.isEnabled()){
                mBluetoothAdapter.enable();
                getDevice();
                if (bluetoothConnected) return true;
                return false;
            }
        }
        return false;
    }

    @JavascriptInterface
    public void saveConfiguration(String[] appNames, String[] colorString) {
        NotificationDataBase db = new NotificationDataBase(activity.getApplicationContext());
        db.getWritableDatabase();
        CustomizedNotification notification;
        for (int i = 0; i < appNames.length; i++) {
            notification = new CustomizedNotification(appNames[i], colorString[i]);
            db.addNotification(notification);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @JavascriptInterface
    public String getAllPhoneContacts() {
        String contactValue;
        int hasPhone;
        Gson gson = new Gson();
        ContactDatabase db = new ContactDatabase(activity.getApplicationContext());
        List<Contact> contactsInDatabase = db.getAllContacts();
        List<Contact> contactsInAgenda = new ArrayList<>();
        Cursor cursor = activity.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        int i = 0;
        while (cursor.moveToNext()) {
            i++;
            if (cursor != null) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                contactValue = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                hasPhone = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                if (hasPhone > 0) {
                    Cursor cp = activity.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    if (cp != null && cp.moveToFirst()) {
                        String number = cp.getString(cp.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        number = number.replaceAll("[\\s\\-\\(\\)]", "");
                        contactsInAgenda.add(new Contact(i, cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)), number));
                        cp.close();
                    }
                }
            }
        }
        Contact contact1;
        for (Contact contact : contactsInAgenda) {
            contact1 = db.getContact(contact.getNumber());
            if (contact1 != null) {
                contact.setColor(contact1.getColor());
                contact.setColorBrihgtness(contact1.getColorBrihgtness());
            }

        }
        Collections.sort(contactsInAgenda);
        return gson.toJson(contactsInAgenda);
    }

    @JavascriptInterface
    public void saveContactsColors(String contactColors) {
        String contactValue;
        Gson gson = new Gson();
        List<Contact> contacts =  gson.fromJson(contactColors, new TypeToken<List<Contact>>() {
        }.getType());
        ContactDatabase db = new ContactDatabase(activity);
        db.getWritableDatabase();
        for (Contact contact : contacts){
            db.addContact(contact);
        }
        System.out.println(contacts);
    }

    @JavascriptInterface
    public void createAlarmForMeetings() throws ParseException {
        EventDatabase db = new EventDatabase(activity);
        db.getReadableDatabase();
        List<Event> events = db.getAllEvents();
        for(Event event: events){
            createAlarmForMeeting(event.getTitle(),event.getCalendar(),event.getColor());
        }
    }
    public void createAlarmForMeeting(String title, Calendar calendar, String colorString) throws ParseException {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            ColorSetting colorSetting = new ColorSetting(new ColorCustomized(colorString));
            ColorSetting colorSetting2 = new ColorSetting(new ColorCustomized(colorString));
            String descriptionEventAlmostBeginning =  title + " will start in 2 minutes... " ;
            String descriptionEventStarted =  title + " is starting...";
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
        createAlarm(calendar, colorSetting, descriptionEventAlmostBeginning , activity.getApplicationContext());
        calendar.setTimeInMillis(System.currentTimeMillis() + 8000);
        createAlarm(calendar, colorSetting2, descriptionEventStarted, activity.getApplicationContext());
    }

    public void createAlarm(Calendar calendar, ColorSetting colorSetting, String description, Context context) {
        // conference in germany?
        Long time = calendar.getTimeInMillis();
        Date date2 = new Date();
        Date date = calendar.getTime();
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
//                    if(correctingTime > System.currentTimeMillis() && titleValue.contains("Siemens")){
                    if (correctingTime > System.currentTimeMillis()) {
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
                                    try {
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
        Collections.sort(events);
        Gson gson = new Gson();
        EventDatabase db = new EventDatabase(activity);
        db.getWritableDatabase();
        db.removeAll();
        for(Event eventCreated: events){
            db.addEvent(eventCreated);
        }
        return gson.toJson(events);
    }
    // Create a BroadcastReceiver for ACTION_FOUND


}
