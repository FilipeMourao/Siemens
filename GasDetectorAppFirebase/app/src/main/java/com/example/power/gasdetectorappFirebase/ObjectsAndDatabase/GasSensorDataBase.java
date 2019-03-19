package com.example.power.gasdetectorappFirebase.ObjectsAndDatabase;

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
    private  static final String KEY_UUID = "UUID";

    private  static final String TABLE_GAS_SENSOR_CLASSIFY  = "GasSensorClassification";
    private  static final String KEY_CLASSIFICATION = "Classification";

    public GasSensorDataBase(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_GAS_TABLE = "CREATE TABLE " + TABLE_GAS_SENSOR  +
                " (" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ID1 + " TEXT,"
                + KEY_ID2 + " TEXT," + KEY_ID3 + " TEXT,"
                + KEY_SENSOR1 + " TEXT," + KEY_SENSOR2 + " TEXT,"
                + KEY_SENSOR3 + " TEXT," + KEY_THERMISTOR + " TEXT," + KEY_UUID + " TEXT" + ")";

        db.execSQL(CREATE_GAS_TABLE);

        String CREATE_CLASSIFICATION_TABLE = "CREATE TABLE " + TABLE_GAS_SENSOR_CLASSIFY  +
                " (" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_SENSOR1 + " TEXT,"+ KEY_SENSOR2 +
                " TEXT,"+ KEY_SENSOR3 + " TEXT,"
                + KEY_CLASSIFICATION + " TEXT" + ")";
        db.execSQL(CREATE_CLASSIFICATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAS_SENSOR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAS_SENSOR_CLASSIFY);
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
        values.put(KEY_UUID, gasSensorMeasure.getUniqueID());
        GasSensorMeasure newGasSensorMeasure = getGasSensorMeasureUUID(gasSensorMeasure.getUniqueID());
        if (newGasSensorMeasure == null){// new gas sensor measure
            db.insert(TABLE_GAS_SENSOR,null,values);
        } else { // update currently contact
            db.update(TABLE_GAS_SENSOR,values, KEY_UUID + "=?", new String[]{newGasSensorMeasure.getUniqueID()} );
        }
        db.close();
    }
    public void addClassification(ClassificationGasMeasure classificationGasMeasure){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SENSOR1,classificationGasMeasure.getSensor1());
        values.put(KEY_SENSOR2,classificationGasMeasure.getSensor2());
        values.put(KEY_SENSOR3,classificationGasMeasure.getSensor3());
        values.put(KEY_CLASSIFICATION, classificationGasMeasure.getClassification());
        ClassificationGasMeasure newClassificationGasMeasure = getClassificationData(classificationGasMeasure.getSensor1(),classificationGasMeasure.getSensor2(),classificationGasMeasure.getSensor3());
        if (newClassificationGasMeasure  == null){// new gas sensor measure
            db.insert(TABLE_GAS_SENSOR_CLASSIFY,null,values);
        } else { // update currently contact
            db.update(TABLE_GAS_SENSOR_CLASSIFY,values,
                    KEY_SENSOR1 + "=?" + " AND " + KEY_SENSOR2 + "=?"  +" AND " + KEY_SENSOR3+ "=?",
                    new String[]{
                            Integer.toString(classificationGasMeasure.getSensor1())
                            ,Integer.toString(classificationGasMeasure.getSensor2())
                            ,Integer.toString(classificationGasMeasure.getSensor3())});
        }
        db.close();
    }
    public ClassificationGasMeasure getClassificationData(int sensor1, int sensor2, int sensor3 ){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(true, TABLE_GAS_SENSOR_CLASSIFY,
                null,
                KEY_SENSOR1 + "=?" + " AND " + KEY_SENSOR2 + "=?"  +" AND " + KEY_SENSOR3+ "=?",
                new String[]{
                        Integer.toString(sensor1)
                        ,Integer.toString(sensor2)
                        ,Integer.toString(sensor3)} ,
                null, null, null, null );

        if ((cursor != null)  && (cursor.getCount() > 0) ){
            cursor.moveToFirst();
        }
        else {
            return null;
        }
        ClassificationGasMeasure classificationGasMeasure = new ClassificationGasMeasure(
                Integer.parseInt(cursor.getString(1)),
                Integer.parseInt(cursor.getString(2)),
                Integer.parseInt(cursor.getString(3)),
                cursor.getString(4));
        classificationGasMeasure.setId(Integer.parseInt(cursor.getString(0)));
        return classificationGasMeasure;
    }
    public String getClassification(GasSensorMeasure gasSensorMeasure){
        String classification;
        ClassificationGasMeasure classificationGasMeasure = getClassificationData(gasSensorMeasure.getSensor1(),gasSensorMeasure.getSensor2(),gasSensorMeasure.getSensor3());
        if (classificationGasMeasure != null && !classificationGasMeasure.getClassification().isEmpty()){
            classification = classificationGasMeasure.getClassification();
        } else {
            classification = "[" + gasSensorMeasure.getSensor1() + "," + gasSensorMeasure.getSensor2() + "," + gasSensorMeasure.getSensor3() + "]" ;
        }
        return classification;
    }
    public GasSensorMeasure getGasSensorMeasure(int id ){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(true, TABLE_GAS_SENSOR,
                null,
                KEY_ID+ "=?",
                new String[]{Integer.toString(id)} ,
                null, null, null, null );
        if ((cursor != null)  && (cursor.getCount() > 0) ){
            cursor.moveToFirst();
        }
        else {
            return null;
        }
        GasSensorMeasure measure = new GasSensorMeasure(
                Integer.parseInt(cursor.getString(1)),
                Integer.parseInt(cursor.getString(2)),
                Integer.parseInt(cursor.getString(3)),
                Integer.parseInt(cursor.getString(4)),
                Integer.parseInt(cursor.getString(5)),
                Integer.parseInt(cursor.getString(6)),
                cursor.getString(7),
                cursor.getString(8));
        measure.setId(Integer.parseInt(cursor.getString(0)));
        return measure;
    }
    public GasSensorMeasure getGasSensorMeasureUUID(String UUID ){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(true, TABLE_GAS_SENSOR,
                null,
                KEY_UUID+ "=?",
                new String[]{UUID} ,
                null, null, null, null );
        if ((cursor != null)  && (cursor.getCount() > 0) ){
            cursor.moveToFirst();
        }
        else {
            return null;
        }
        GasSensorMeasure measure = new GasSensorMeasure(
                Integer.parseInt(cursor.getString(1)),
                Integer.parseInt(cursor.getString(2)),
                Integer.parseInt(cursor.getString(3)),
                Integer.parseInt(cursor.getString(4)),
                Integer.parseInt(cursor.getString(5)),
                Integer.parseInt(cursor.getString(6)),
                cursor.getString(7),
                cursor.getString(8));
        measure.setId(Integer.parseInt(cursor.getString(0)));
        return measure;
    }
    public List<GasSensorMeasure> getAllMeasures(){
        List<GasSensorMeasure> listOfMeasures = new ArrayList<GasSensorMeasure>();
        String selectQuery = "SELECT * FROM " + TABLE_GAS_SENSOR;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor.moveToFirst()){
            while (!cursor.isAfterLast()) {
                GasSensorMeasure measure = new GasSensorMeasure(
                        Integer.parseInt(cursor.getString(1)),
                        Integer.parseInt(cursor.getString(2)),
                        Integer.parseInt(cursor.getString(3)),
                        Integer.parseInt(cursor.getString(4)),
                        Integer.parseInt(cursor.getString(5)),
                        Integer.parseInt(cursor.getString(6)),
                        cursor.getString(7),
                        cursor.getString(8));
                measure.setId(Integer.parseInt(cursor.getString(0)));
                listOfMeasures.add(measure);
                cursor.moveToNext();
            }


        }
        return listOfMeasures;
    }
    public List<ClassificationGasMeasure> getAllClassifications(){
        List<ClassificationGasMeasure> listOfClassifications = new ArrayList<ClassificationGasMeasure>();
        String selectQuery = "SELECT * FROM " + TABLE_GAS_SENSOR_CLASSIFY;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor.moveToFirst()){
            while (!cursor.isAfterLast()) {
                ClassificationGasMeasure classificationGasMeasure = new ClassificationGasMeasure(
                        Integer.parseInt(cursor.getString(1)),
                        Integer.parseInt(cursor.getString(2)),
                        Integer.parseInt(cursor.getString(3)),
                        cursor.getString(4));
                classificationGasMeasure.setId(Integer.parseInt(cursor.getString(0)));
                listOfClassifications.add(classificationGasMeasure);
                cursor.moveToNext();
            }


        }
        return listOfClassifications;
    }

    public void removeGasSensorMeasure(GasSensorMeasure gasSensorMeasure){
        GasSensorMeasure newGasSensorMeasure = getGasSensorMeasure(gasSensorMeasure.getId());
        if (newGasSensorMeasure != null){
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_GAS_SENSOR, KEY_UUID + "=?", new String[]{newGasSensorMeasure.getUniqueID()} );
        }
    }

    public void removeAllMeasures(){
        // db.delete(String tableName, String whereClause, String[] whereArgs);
        // If whereClause is null, it will delete all rows.
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GAS_SENSOR, null, null);
    }
    public void removeAllClassification(){
        // db.delete(String tableName, String whereClause, String[] whereArgs);
        // If whereClause is null, it will delete all rows.
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GAS_SENSOR_CLASSIFY, null, null);
    }
}
