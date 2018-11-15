package com.example.power.blingmeapp;

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
import java.util.List;

public class EventDatabase extends SQLiteOpenHelper {
    private  static final int DATABASE_VERSION = 1;
    private  static final String DATABASE_NAME = "ColoredEventsManager";
    private  static final String TABLE_EVENTS = "events";
    private  static final String KEY_ID = "id";
    private  static final String KEY_TITLE = "title";
    private  static final String KEY_LOCATION = "location";
    private  static final String KEY_CALENDAR = "calendar";

    public EventDatabase(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_EVENTS  +
                " (" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT,"
                + KEY_LOCATION + " TEXT," + KEY_CALENDAR + " TEXT" + ")";

        db.execSQL(CREATE_CONTACTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        onCreate(db);
    }
    public void  addEvent(Event event){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE,event.getTitle());
        values.put(KEY_LOCATION,event.getLocation());
        values.put(KEY_CALENDAR, dateFormat.format(event.getCalendar()));
        db.insert(TABLE_EVENTS,null,values);
        db.close();
    }
    public Event getContact(String title ) throws ParseException {
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
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateFormat.parse(cursor.getString(1)));
        Event event = new Event(Integer.parseInt(cursor.getString(0)),cursor.getString(1) , cursor.getString(2),calendar);
        return event;
    }
    public int  updateContact(Event event){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE,event.getTitle());
        values.put(KEY_LOCATION,event.getLocation());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        values.put(KEY_CALENDAR, dateFormat.format(event.getCalendar()));
        return db.update(TABLE_EVENTS,values, KEY_ID + "=?", new String[]{String.valueOf(event.getId())} );
    }

    public List<Event> getAllEvents() throws ParseException {
        List<Event> listOfEvents = new ArrayList<Event>();
        String selectQuery = "SELECT * FROM " + TABLE_EVENTS;
        SQLiteDatabase db = this.getWritableDatabase();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor.moveToFirst()){
            while (!cursor.isAfterLast()) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateFormat.parse(cursor.getString(1)));
                Event event = new Event(Integer.parseInt(cursor.getString(0)),cursor.getString(1) , cursor.getString(2),calendar);
                listOfEvents.add(event);
                cursor.moveToNext();
            }


        }

        return listOfEvents;
    }
    public void removeAll(){
        // db.delete(String tableName, String whereClause, String[] whereArgs);
        // If whereClause is null, it will delete all rows.
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EVENTS, null, null);
        //       db.delete(DatabaseHelper.TAB_USERS_GROUP, null, null);
    }

}