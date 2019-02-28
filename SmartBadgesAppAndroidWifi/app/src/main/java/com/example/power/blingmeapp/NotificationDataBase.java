package com.example.power.blingmeapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class NotificationDataBase extends SQLiteOpenHelper {
    private  static final int DATABASE_VERSION = 1;
    private  static final String DATABASE_NAME = "NotificationsDatabase";
    private  static final String NOTIFICATIONS = "Notifications";
    private  static final String KEY_ID = "id";
    private  static final String APP_NAME = "APP_NAME";
    private  static final String COLOR = "COLOR";

    public NotificationDataBase(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_GAS_TABLE = "CREATE TABLE " + NOTIFICATIONS +
                " (" + KEY_ID + " INTEGER PRIMARY KEY," + APP_NAME + " TEXT,"
                + COLOR  + " TEXT" + ")";

        db.execSQL(CREATE_GAS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + NOTIFICATIONS);
        onCreate(db);
    }
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
        return listOfNotifications;
    }
    public void removeAll(){
        // db.delete(String tableName, String whereClause, String[] whereArgs);
        // If whereClause is null, it will delete all rows.
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(NOTIFICATIONS, null, null);
 //       db.delete(DatabaseHelper.TAB_USERS_GROUP, null, null);
    }
}
