package com.example.power.blingmeapp.Objects;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class Database extends SQLiteOpenHelper {
    private  static final int DATABASE_VERSION = 1;
    private  static final String DATABASE_NAME = "ColoredContactManager";
    private  static final String TABLE_CONTACTS = "contacts";
    private  static final String KEY_ID = "id";
    private  static final String KEY_NAME = "name";
    private  static final String KEY_PHONE = "phone";
    private  static final String KEY_COLOR = "color";
    private  static  final String KEY_BRIGHTNESS = "brightness";
//-------------------------------------------------------------------------//
    private  static final String NOTIFICATIONS = "Notifications";
    private  static final String APP_NAME = "APP_NAME";
    private  static final String COLOR = "COLOR";
//------------------------------------------------------------------------//
    private  static final String TABLE_EVENTS = "events";
    private  static final String KEY_TITLE = "title";
    private  static final String KEY_LOCATION = "location";
    private  static final String KEY_CALENDAR = "calendar";

    public Database(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS  +
                " (" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_PHONE + " TEXT," + KEY_COLOR + " TEXT," + KEY_BRIGHTNESS + " TEXT" + ")";

        db.execSQL(CREATE_CONTACTS_TABLE);

        String CREATE_NOTIFICATIONS_TABLE = "CREATE TABLE " + NOTIFICATIONS +
                " (" + KEY_ID + " INTEGER PRIMARY KEY," + APP_NAME + " TEXT,"
                + COLOR  + " TEXT" + ")";

        db.execSQL(CREATE_NOTIFICATIONS_TABLE);

        String CREATE_EVENT_TABLE = "CREATE TABLE " + TABLE_EVENTS  +
                " (" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT,"
                + KEY_LOCATION + " TEXT," + KEY_CALENDAR + " TEXT," + KEY_COLOR + " TEXT" + ")";

        db.execSQL(CREATE_EVENT_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        db.execSQL("DROP TABLE IF EXISTS " + NOTIFICATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        onCreate(db);
    }


    public void  addContact(Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME,contact.getName());
        values.put(KEY_PHONE,contact.getNumber());
        values.put(KEY_COLOR,contact.getColor());
        values.put(KEY_BRIGHTNESS,contact.getColorBrihgtness());
        if (getContact(contact.getNumber()) == null){
            db.insert(TABLE_CONTACTS,null,values);
            db.close();
        } else {
            db.update(TABLE_CONTACTS,values, KEY_ID + "=?", new String[]{String.valueOf( getContact(contact.getNumber()).getId())} );
            db.close();
        }


    }
    public Contact getContact(String number ){
        // remove  left zeros from the number
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CONTACTS,
                null,
                KEY_PHONE + "=?",
                new String[]{number} ,
                null, null, null, null );


        if ((cursor != null)  && (cursor.getCount() > 0) ){
            cursor.moveToFirst();
        }
        else {
            db.close();
            return null;
        }
        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2),cursor.getString(3),Integer.parseInt(cursor.getString(4)));
        db.close();
        return contact;
    }
    public int  updateContact(Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME,contact.getName());
        values.put(KEY_PHONE,contact.getNumber());
        values.put(KEY_COLOR,contact.getColor());
        values.put(KEY_BRIGHTNESS,contact.getColorBrihgtness());
        return db.update(TABLE_CONTACTS,values, KEY_ID + "=?", new String[]{String.valueOf(contact.getId())} );
    }
    public  void deleteContact(Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();
       db.delete(TABLE_CONTACTS,KEY_ID + "=?", new String[]{String.valueOf(contact.getId())} );
       db.close();
    }
    public List<Contact> getAllContacts(){
        List<Contact> listOfContacts = new ArrayList<Contact>();
        String selectQuery = "SELECT * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor.moveToFirst()){
            while (!cursor.isAfterLast()) {
                String test = cursor.getString(0);
                Contact contact = new Contact(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2),cursor.getString(3),Integer.parseInt(cursor.getString(4)));
                listOfContacts.add(contact);
                cursor.moveToNext();
            }


        }

        db.close();
        return listOfContacts;
    }
   //-------------------------------------------------------//
    public void  addNotification(CustomizedNotification notification){
        if (getNotification(notification.getAppName()) == null){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(APP_NAME,notification.getAppName().toLowerCase());
            values.put(COLOR,notification.getColorString());
            db.insert(NOTIFICATIONS,null,values);
            db.close();

        } else {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(APP_NAME,notification.getAppName());
            values.put(COLOR,notification.getColorString());
            db.update(NOTIFICATIONS,values, KEY_ID + "=?", new String[]{String.valueOf( getNotification(notification.getAppName()).getId())} );
            db.close();
        }

    }
    public CustomizedNotification getNotification(String appName){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(NOTIFICATIONS,
                null,
                APP_NAME + "=?",
                new String[]{appName.toLowerCase()} ,
                null, null, null, null );
        if ((cursor != null)  && (cursor.getCount() > 0) ){
            cursor.moveToFirst();
        }
        else {
            return null;
        }
        CustomizedNotification notification = new CustomizedNotification(cursor.getString(1), cursor.getString(2));
        notification.setId(Integer.parseInt(cursor.getString(0)));
        db.close();
        return notification;
    }
    public List<CustomizedNotification> getAllNotifications(){
        List<CustomizedNotification> listOfNotifications = new ArrayList<CustomizedNotification>();
        String selectQuery = "SELECT * FROM " + NOTIFICATIONS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        CustomizedNotification notification;
        if (cursor.moveToFirst()){
            while (!cursor.isAfterLast()) {
                notification = new CustomizedNotification(cursor.getString(1), cursor.getString(2));
                notification.setId(Integer.parseInt(cursor.getString(0)));
                listOfNotifications.add(notification);
                cursor.moveToNext();
            }
        }
        db.close();
        return listOfNotifications;
    }
    //-------------------------------------------------------------------------------------------//
    public void  addEvent(Event event){
        Date date = event.getCalendar().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String dateString = dateFormat.format(date);
        values.put(KEY_TITLE,event.getTitle());
        values.put(KEY_LOCATION,event.getLocation());
        values.put(KEY_CALENDAR, dateString);
        values.put(KEY_COLOR, event.getColor());
        db.insert(TABLE_EVENTS,null,values);
        db.close();
    }
    public Event getEvent(String title ) throws ParseException {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EVENTS,
                null,
                KEY_LOCATION + "=?",
                new String[]{title} ,
                null, null, null, null );


        if ((cursor != null)  && (cursor.getCount() > 0) ){
            cursor.moveToFirst();
        }
        else {
            return null;
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateFormat.parse(cursor.getString(3)));
        Event event = new Event(Integer.parseInt(cursor.getString(0)),cursor.getString(1) , cursor.getString(2),calendar);
        event.setColor(cursor.getString(4));
        return event;
    }
    public int  updateEvent(Event event){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE,event.getTitle());
        values.put(KEY_LOCATION,event.getLocation());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        values.put(KEY_CALENDAR, dateFormat.format(event.getCalendar()));
        return db.update(TABLE_EVENTS,values, KEY_ID + "=?", new String[]{String.valueOf(event.getId())} );
    }
    public List<Event> getAllEvents() throws ParseException {
        List<Event> listOfEvents = new ArrayList<Event>();
        String selectQuery = "SELECT * FROM " + TABLE_EVENTS;
        SQLiteDatabase db = this.getReadableDatabase();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");;
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor.moveToFirst()){
            while (!cursor.isAfterLast()) {
//                Calendar calendar = Calendar.getInstance();
                Calendar calendar = new GregorianCalendar();
                String test = cursor.getString(3);
                Date date = dateFormat.parse(test);
                calendar.setTime(date);
                Event event = new Event(Integer.parseInt(cursor.getString(0)),cursor.getString(1) , cursor.getString(2),calendar);
                event.setColor(cursor.getString(4));
                listOfEvents.add(event);
                cursor.moveToNext();
            }


        }
        db.close();
        return listOfEvents;
    }
    public void removeAllEvents(){
        // db.delete(String tableName, String whereClause, String[] whereArgs);
        // If whereClause is null, it will delete all rows.
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EVENTS, null, null);
        //       db.delete(DatabaseHelper.TAB_USERS_GROUP, null, null);
        db.close();
    }


}