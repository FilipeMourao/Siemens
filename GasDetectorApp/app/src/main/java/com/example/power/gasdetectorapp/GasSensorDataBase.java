package com.example.power.gasdetectorapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class GasSensorDataBase extends SQLiteOpenHelper {
    private  static final int DATABASE_VERSION = 1;
    private  static final String DATABASE_NAME = "GasSensorDatabase";
    private  static final String TABLE_GAS_SENSOR  = "GasSensor";
    private  static final String KEY_ID = "id";
    private  static final String KEY_ID1 = "ID1";
    private  static final String KEY_ID2 = "ID2";
    private  static final String KEY_ID3 = "ID3";
    private  static final String KEY_SENSOR1 = "sensor1";
    private  static final String KEY_SENSOR2 = "sensor2";
    private  static final String KEY_SENSOR3 = "sensor3";
    private  static final String KEY_THERMISTOR= "thermistor";

    public GasSensorDataBase(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_GAS_TABLE = "CREATE TABLE " + TABLE_GAS_SENSOR  +
                " (" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ID1 + " TEXT,"
                + KEY_ID2 + " TEXT," + KEY_ID3 + " TEXT,"
                + KEY_SENSOR1 + " TEXT," + KEY_SENSOR2 + " TEXT,"
                + KEY_SENSOR3 + " TEXT," + KEY_THERMISTOR + " TEXT" + ")";

        db.execSQL(CREATE_GAS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAS_SENSOR);
        onCreate(db);
    }
    public void  addMeasure(GasSensorMeasure gasSensorMeasure){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID1,gasSensorMeasure.getID1());
        values.put(KEY_ID2,gasSensorMeasure.getID2());
        values.put(KEY_ID3,gasSensorMeasure.getID3());
        values.put(KEY_SENSOR1,gasSensorMeasure.getSensor1());
        values.put(KEY_SENSOR2,gasSensorMeasure.getSensor2());
        values.put(KEY_SENSOR3,gasSensorMeasure.getSensor3());
        db.insert(TABLE_GAS_SENSOR,null,values);
        db.close();
    }
//    public Contact getContact(String number ){
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.query(TABLE_CONTACTS,
//                null,
//                KEY_PHONE + "=?",
//                new String[]{number} ,
//                null, null, null, null );
//
//
//        if ((cursor != null)  && (cursor.getCount() > 0) ){
//            cursor.moveToFirst();
//        }
//        else {
//            return null;
//        }
//        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2));
//        contact.setColor( cursor.getString(3));
//        contact.setIpAdress( cursor.getString(4));
//        return contact;
//    }
//    public int  updateContact(Contact contact){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(KEY_NAME,contact.getName());
//        values.put(KEY_PHONE,contact.getNumber());
//        values.put(KEY_COLOR,contact.getColor());
//        values.put(KEY_IP,contact.getIpAdress());
//        return db.update(TABLE_CONTACTS,values, KEY_ID + "=?", new String[]{String.valueOf(contact.getId())} );
//    }
//    public  void deleteContact(Contact contact){
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_CONTACTS,KEY_ID + "=?", new String[]{String.valueOf(contact.getId())} );
//        db.close();
//    }
    public List<GasSensorMeasure> getAllMeasures(){
        List<GasSensorMeasure> listOfMeasures = new ArrayList<GasSensorMeasure>();
        String selectQuery = "SELECT * FROM " + TABLE_GAS_SENSOR;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor.moveToFirst()){
            while (!cursor.isAfterLast()) {
                String test = cursor.getString(0);
                GasSensorMeasure measure = new GasSensorMeasure(
                        Integer.parseInt(cursor.getString(0)),
                        Integer.parseInt(cursor.getString(1)),
                        Integer.parseInt(cursor.getString(2)),
                        Integer.parseInt(cursor.getString(3)),
                        Integer.parseInt(cursor.getString(4)),
                        Integer.parseInt(cursor.getString(5)),
                        cursor.getString(6));
                listOfMeasures.add(measure);
                cursor.moveToNext();
            }


        }
        return listOfMeasures;
    }
}
