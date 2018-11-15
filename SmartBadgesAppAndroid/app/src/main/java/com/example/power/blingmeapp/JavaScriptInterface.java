package com.example.power.blingmeapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.text.InputType;
import android.webkit.JavascriptInterface;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.WIFI_SERVICE;

class JavaScriptInterface {
    private Activity activity;

    public JavaScriptInterface(Activity activity) {
        this.activity = activity;
    }

    @JavascriptInterface
    public void getIpAdress() {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(" Card Number");

// Set up the input
        final EditText input = new EditText(activity.getApplicationContext());
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!input.getText().toString().isEmpty()) {
                    String ipAdress = "192.168." + input.getText().toString();
                    ((IpAdress) activity.getApplication()).setIPADRESS(ipAdress);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });

        builder.show();


    }

    @JavascriptInterface
    public boolean connectToDevice() {
        if (((IpAdress) activity.getApplication()).getIPADRESS().isEmpty()) {
            Toast.makeText(activity.getApplicationContext(), "Please add your card number in the connection", Toast.LENGTH_LONG).show();
            this.getIpAdress();
            return false;
        }
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
            if (ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(activity.getApplicationContext(), "Enable the notification access for our app... ", Toast.LENGTH_LONG).show();
                activity.startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
            }
            if (ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE) != PackageManager.PERMISSION_GRANTED) {
                activity.startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
                Toast.makeText(activity.getApplicationContext(), "Enable the notification access for our app... ", 3 * Toast.LENGTH_LONG).show();
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
        for (int i = 0; i < appNames.length; i++) {
            String das = appNames[i];
            String der = colorString[i];
            notification = new CustomizedNotification(appNames[i], colorString[i]);
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
        if (contacts.isEmpty()) {
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
                            contacts.add(new Contact(i, cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)), number));
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

    public void sendCollorToJewlery(String colorString) {
        ConfigureLed configureLed = new ConfigureLed(new ColorSetting(new ColorCustomized(colorString)));
        configureColorIndividually myTask = new configureColorIndividually();
        myTask.execute(configureLed);
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
            String descriptionEventAlmostBeginning = "Reminder!  " + title + " will start in 2 minutes... " ;
            String descriptionEventStarted = "Reminder!  " + title + " is starting...";
            colorSetting2.setBrightness(0);
          //  DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
           // Calendar calendar = Calendar.getInstance();
           // calendar.setTime(dateFormat.parse(calendarString));
            createAlarm(calendar, colorSetting2, descriptionEventStarted, activity.getApplicationContext());
            calendar.setTimeInMillis(calendar.getTimeInMillis() - 2 * 60 * 1000);
            createAlarm(calendar, colorSetting, descriptionEventAlmostBeginning, activity.getApplicationContext());

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
        time = System.currentTimeMillis();

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
}
