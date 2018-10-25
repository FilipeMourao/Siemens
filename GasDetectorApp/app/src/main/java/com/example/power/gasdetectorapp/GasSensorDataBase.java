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
    private  static final String KEY_THERMISTOR = "thermistor";

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
        values.put(KEY_THERMISTOR, gasSensorMeasure.getThermistor());
        db.insert(TABLE_GAS_SENSOR,null,values);
        db.close();
    }
    public GasSensorMeasure getContact(GasSensorMeasure gasSensorMeasure ){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(true, TABLE_GAS_SENSOR,
                new String[]{KEY_ID1,KEY_ID2,KEY_ID3,KEY_SENSOR1,KEY_SENSOR2,KEY_SENSOR3,TABLE_GAS_SENSOR},
                KEY_ID1 + "=?" + " AND " + KEY_ID2 + "=?"  +" AND " + KEY_ID3+ "=?" + KEY_SENSOR1 + "=?" + KEY_SENSOR2 + "=?"  + KEY_SENSOR3 + "=?" + KEY_THERMISTOR + "=?" ,
                new String[]{Integer.toString(gasSensorMeasure.getID1()),Integer.toString(gasSensorMeasure.getID2()),Integer.toString(gasSensorMeasure.getID3()),
                        Integer.toString(gasSensorMeasure.getSensor1()), Integer.toString(gasSensorMeasure.getSensor2()),Integer.toString(gasSensorMeasure.getSensor3()),gasSensorMeasure.getThermistor()} ,
                null, null, null, null );


        if ((cursor != null)  && (cursor.getCount() > 0) ){
            cursor.moveToFirst();
        }
        else {
            return null;
        }
        GasSensorMeasure measure = new GasSensorMeasure(Integer.parseInt(cursor.getString(0)),Integer.parseInt(cursor.getString(1)),
                Integer.parseInt(cursor.getString(2)),Integer.parseInt(cursor.getString(3)),
                Integer.parseInt(cursor.getString(4)),Integer.parseInt(cursor.getString(5)), cursor.getString(6));
        return measure;
    }
    public boolean contains(GasSensorMeasure gasSensorMeasure ){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(true, TABLE_GAS_SENSOR,
                new String[]{KEY_ID1,KEY_ID2,KEY_ID3,KEY_SENSOR1,KEY_SENSOR2,KEY_SENSOR3,KEY_THERMISTOR},
                KEY_ID1 + "=?" + " AND " + KEY_ID2 + "=?"  +" AND " + KEY_ID3+ "=?" + " AND " +KEY_SENSOR1 + "=?" + " AND " +KEY_SENSOR2 + "=?"  + " AND " +KEY_SENSOR3 + "=?" + " AND " +KEY_THERMISTOR + "=?" ,
                new String[]{Integer.toString(gasSensorMeasure.getID1()),Integer.toString(gasSensorMeasure.getID2()),Integer.toString(gasSensorMeasure.getID3()),
                        Integer.toString(gasSensorMeasure.getSensor1()), Integer.toString(gasSensorMeasure.getSensor2()),Integer.toString(gasSensorMeasure.getSensor3()),gasSensorMeasure.getThermistor()} ,
                null, null, null, null );


        if ((cursor != null)  && (cursor.getCount() > 0) ){
            return true;
        }
        else {
            return false;
        }

    }
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
