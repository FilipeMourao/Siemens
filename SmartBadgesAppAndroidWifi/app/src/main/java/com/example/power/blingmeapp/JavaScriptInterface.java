package com.example.power.blingmeapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.text.InputType;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;

import com.example.power.blingmeapp.Handlers.Alarm;
import com.example.power.blingmeapp.Objects.ColorCustomized;
import com.example.power.blingmeapp.Objects.ColorSetting;
import com.example.power.blingmeapp.Objects.ConfigureLed;
import com.example.power.blingmeapp.Objects.Contact;
import com.example.power.blingmeapp.Objects.CustomizedNotification;
import com.example.power.blingmeapp.Objects.Database;
import com.example.power.blingmeapp.Objects.Event;
import com.example.power.blingmeapp.Objects.IpAdress;
import com.example.power.blingmeapp.Objects.configureColorIndividually;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.WIFI_SERVICE;

public class JavaScriptInterface {
    private static Activity activity;
    private Database db;
    public JavaScriptInterface(Activity activity) {
        this.activity = activity;
        this.db = new Database(activity.getApplicationContext());
    }

    public static void getIpAdress() {

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
      //          if (!input.getText().toString().isEmpty()) {
                if (input.getText().toString().length() == 3) {
                    String ipAdress = "192.168.1." + input.getText().toString();
                    ((IpAdress) activity.getApplication()).setIPADRESS(ipAdress);
                    Toast.makeText(activity.getApplicationContext(), "Verifying connection with the badge...", 3*Toast.LENGTH_LONG).show();
                    ConfigureLed configureLed =  new ConfigureLed(ipAdress,new ColorSetting(new ColorCustomized(0,0,0)));
                    configureLed.getColorStetting().setBrightness(-50);
                    configureColorIndividually myTask = new configureColorIndividually();
                    myTask.execute(configureLed);


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
                            Manifest.permission.READ_CALL_LOG
                    },
                    1);
            if (ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(activity.getApplicationContext(), "Enable the notification access for our app... ", Toast.LENGTH_LONG).show();
                activity.startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
            }

        } else {
            WifiManager wifiManager = (WifiManager) activity.getApplicationContext().getSystemService(WIFI_SERVICE);
            if (!wifiManager.isWifiEnabled()) {// if the wifi is not enabled, ask to enable the wifi
                Toast.makeText(activity.getApplicationContext(), "You need a wifi connection to connet to the device, please enable it!", Toast.LENGTH_LONG).show();
            } else {// if the wifi is enabled and the permissons are given ask for the badge number
                if (((IpAdress) activity.getApplication()).getIPADRESS().isEmpty()) {
                    Toast.makeText(activity.getApplicationContext(), "Please add your card number in the connection", Toast.LENGTH_LONG).show();
                    this.getIpAdress();
                }
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
        String contactValue;
        int hasPhone;
        Gson gson = new Gson();
        Database db = new Database(activity.getApplicationContext());
        List<Contact> contactsInDatabase = db.getAllContacts();
        List<Contact> contactsInAgenda = new ArrayList<>();
        Cursor cursor = activity.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        int i = 0;
        while (cursor.moveToNext()) {
            i++;
            if (cursor != null) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                contactValue = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                hasPhone = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));// check if the contact has a phone number
                if (hasPhone > 0) {// if the contact has a phone number get the contact information
                    Cursor cp = activity.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    if (cp != null && cp.moveToFirst()) {// get the contact information and add the contact in the list
                        String number = cp.getString(cp.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        // taake out the non numeric chars and left zeros
                        number = number.replaceAll("[\\s\\-\\(\\)\\+]", "");
                        char c  = number.charAt(0);
                        while (c == '0') {
                            number = number.substring(1);
                            c = number.charAt(0);
                        }
                        contactsInAgenda.add(new Contact(i, cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)), number));
                        cp.close();
                    }
                }
            }
        }
        Contact contact1;
        for (Contact contact : contactsInAgenda) {// update the contact colors with the alrrady saved colors before sending it to the frontend
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
        String contactValue;
        Gson gson = new Gson();
        List<Contact> contacts =  gson.fromJson(contactColors, new TypeToken<List<Contact>>() {// convert the gson string in the list of objects
        }.getType());
        Database db = new Database(activity);
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
            createAlarmForMeeting(event.getTitle(),event.getCalendar(),event.getColor());
        }
    }
    public void createAlarmForMeeting(String title, Calendar calendar, String colorString) throws ParseException {// create an event given the time when the event is begining, the name and the color
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
        // convert the information in an alarm manager form and create the alarm
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
    public String getEventList() throws ParseException {// get the events from callendar and send to the frontend
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
        Collections.sort(events);
        Gson gson = new Gson();
        db.getWritableDatabase();
        db.removeeEventAll();
        for(Event eventCreated: events){
            db.addEvent(eventCreated);
        }
        return gson.toJson(events);
    }
    @JavascriptInterface
    public  void IpAdressError(){// function send by the frontend when the ipadress cannot be found anymore
        Toast.makeText(activity.getApplicationContext(), "Could not connect with the badge, please add the right card number", Toast.LENGTH_LONG).show();
        ((IpAdress) activity.getApplication()).setIPADRESS("");
        getIpAdress();
    }
    @JavascriptInterface
    public  void reconnectIpAdress(){// function send by the frontend when the user want to get a new ipadress
        Toast.makeText(activity.getApplicationContext(), "Please add the new card number", Toast.LENGTH_LONG).show();
        ((IpAdress) activity.getApplication()).setIPADRESS("");
        getIpAdress();
    }
    public static void callJSMethod(final String script) { // this method is used to call javascript functions from android we should write the script that must be run
        final WebView webView = (WebView) activity.findViewById(R.id.webView);
        webView.post(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void run() {
                webView.evaluateJavascript(script, null);
            }
        });
    }

}
