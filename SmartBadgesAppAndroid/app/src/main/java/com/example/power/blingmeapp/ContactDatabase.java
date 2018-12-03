package com.example.power.blingmeapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class ContactDatabase extends SQLiteOpenHelper {
    private  static final int DATABASE_VERSION = 1;
    private  static final String DATABASE_NAME = "ColoredContactManager";
    private  static final String TABLE_CONTACTS = "contacts";
    private  static final String KEY_ID = "id";
    private  static final String KEY_NAME = "name";
    private  static final String KEY_PHONE = "phone";
    private  static final String KEY_COLOR = "color";
    private  static  final String KEY_BRIGHTNESS = "brightness";

    public ContactDatabase(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS  +
                " (" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_PHONE + " TEXT," + KEY_COLOR + " TEXT," + KEY_BRIGHTNESS + " TEXT" + ")";

        db.execSQL(CREATE_CONTACTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
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
            return null;
        }
        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2),cursor.getString(3),Integer.parseInt(cursor.getString(4)));
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

        return listOfContacts;
    }
    public void removeAll(){
        // db.delete(String tableName, String whereClause, String[] whereArgs);
        // If whereClause is null, it will delete all rows.
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, null, null);
        //       db.delete(DatabaseHelper.TAB_USERS_GROUP, null, null);
    }
}