package siemens.EmotionRecognition.helpfulStructures;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


public class ResultsDatabase extends SQLiteOpenHelper {
    private  static final int DATABASE_VERSION = 1;
    private  static final String DATABASE_NAME = "ResultsDatabase";
    private  static final String RESULTS = "Results";
    private  static final String KEY_ID = "id";
    private  static final String BITMAP= "bitmap";
    private  static final String EMOTIONS_NAMES = "emotionsNames";
    private  static final String EMOTIONS_VALUES = "emotionsValues";
    private  static final String GENRE = "genre";
    private  static final String ETHINICITY = "ethinicity";
    private  static final String AGE = "age";
    private Gson gson = new Gson();


    public ResultsDatabase(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_GAS_TABLE = "CREATE TABLE " + RESULTS +
                " (" + KEY_ID + " INTEGER PRIMARY KEY," + BITMAP+ " TEXT,"
                + EMOTIONS_NAMES+ " TEXT,"+ EMOTIONS_VALUES + " TEXT,"+ GENRE+ " TEXT,"
                + ETHINICITY+ " TEXT,"+ AGE+ " TEXT" + ")";
        db.execSQL(CREATE_GAS_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + RESULTS);
        onCreate(db);
    }
    public void  addResult(Result result){
        if (getResult(result.getImageBitMap()) == null){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(BITMAP,BitMapToString(result.getImageBitMap()));
            values.put(EMOTIONS_NAMES,gson.toJson(result.getEmotionString()));
            values.put(EMOTIONS_VALUES,gson.toJson(result.getEmotionValues()));
            values.put(GENRE,result.getGender());
            values.put(ETHINICITY,result.getEthinicity());
            values.put(AGE,result.getAge());
            db.insert(RESULTS,null,values);
            db.close();

        } else {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(EMOTIONS_NAMES,gson.toJson(result.getEmotionString()));
            values.put(EMOTIONS_VALUES,gson.toJson(result.getEmotionValues()));
            values.put(GENRE,result.getGender());
            values.put(ETHINICITY,result.getEthinicity());
            values.put(AGE,result.getAge());
           db.update(RESULTS,values, KEY_ID + "=?", new String[]{String.valueOf( getResult(result.getImageBitMap()).getId())} );
        }

    }
    public Result getResult(Bitmap bitmap){
        String bitmapString = BitMapToString(bitmap);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(RESULTS,
                null,
                BITMAP + "=?",
                new String[]{bitmapString.toLowerCase()} ,
                null, null, null, null );
        if ((cursor != null)  && (cursor.getCount() > 0) ){
            cursor.moveToFirst();
        }
        else {
            return null;
        }
        List<String> emotionsName =   gson.fromJson(cursor.getString(2), new TypeToken<List<String>>(){}.getType());
        List<Float> emotionsValue = gson.fromJson(cursor.getString(3), new TypeToken<List<Float>>(){}.getType());
        Result result = new Result(
                StringToBitMap(cursor.getString(1)),
                emotionsName,emotionsValue,
                cursor.getString(4),cursor.getString(5),
                cursor.getString(6));

        result.setId(Integer.parseInt(cursor.getString(0)));
        return result;
    }
    public List<Result> getAllResults(){
        List<Result> listOfResult = new ArrayList<Result>();
        String selectQuery = "SELECT * FROM " + RESULTS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        Result result;
        if (cursor.moveToFirst()){
            while (!cursor.isAfterLast()) {
                List<String> emotionsName =   gson.fromJson(cursor.getString(2), new TypeToken<List<String>>(){}.getType());
                List<Float> emotionsValue = gson.fromJson(cursor.getString(3), new TypeToken<List<Float>>(){}.getType());
                result = new Result(
                        StringToBitMap(cursor.getString(1)),
                        emotionsName,emotionsValue,
                        cursor.getString(4),cursor.getString(5),
                        cursor.getString(6));
                result.setId(Integer.parseInt(cursor.getString(0)));
                listOfResult.add(result);
                cursor.moveToNext();
            }
        }
        return listOfResult;
    }
    public void delete(Bitmap bitmap) {
        String bitmapString = BitMapToString(bitmap);
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+RESULTS+" where "+BITMAP+"='"+bitmapString+"'");
    }
    public void removeAll(){
        // db.delete(String tableName, String whereClause, String[] whereArgs);
        // If whereClause is null, it will delete all rows.
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(RESULTS, null, null);
 //       db.delete(DatabaseHelper.TAB_USERS_GROUP, null, null);
    }
    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
}
