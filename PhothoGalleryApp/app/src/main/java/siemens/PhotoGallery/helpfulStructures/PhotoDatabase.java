package siemens.PhotoGallery.helpfulStructures;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


public class PhotoDatabase extends SQLiteOpenHelper {
    private  static final int DATABASE_VERSION = 1;
    private  static final String DATABASE_NAME = "PhotoDatabase";
    private  static final String PHOTOS_TABLE = "Photos";
    private  static final String NAME = "name";
    private  static final String KEY_ID = "id";
    private  static final String BITMAP= "bitmap";
    private  static final String UNIQUE_KEY = "uniqueKey";
    private Gson gson = new Gson();


    public PhotoDatabase(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_GAS_TABLE = "CREATE TABLE " + PHOTOS_TABLE +
                " (" + KEY_ID + " INTEGER PRIMARY KEY,"
                + NAME+ " TEXT,"
                + BITMAP+ " TEXT,"
                + UNIQUE_KEY+ " TEXT" + ")";
        db.execSQL(CREATE_GAS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + PHOTOS_TABLE);
        onCreate(db);
    }
    public void addPhoto(Photo photo){
        Photo photoCheck = getPhoto(photo.getUniqueID());
        if (photoCheck == null){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(NAME,photo.getName());
            values.put(BITMAP,BitMapToString(photo.getImageBitMap()));
            values.put(UNIQUE_KEY,photo.getUniqueID());
            db.insert(PHOTOS_TABLE,null,values);
            db.close();
        } else {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(NAME,photo.getName());
            values.put(BITMAP,BitMapToString(photo.getImageBitMap()));
            values.put(UNIQUE_KEY,photo.getUniqueID());
            db.update(PHOTOS_TABLE,values, UNIQUE_KEY + "=?", new String[]{photo.getUniqueID()} );
        }

    }
public Photo getPhoto(String uniqueID){
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.query(PHOTOS_TABLE,
            null,
            UNIQUE_KEY + "=?",
            new String[]{uniqueID} ,
            null, null, null, null );
    if ((cursor != null)  && (cursor.getCount() > 0) ){
        cursor.moveToFirst();
    }
    else {
        return null;
    }
    Photo photo = new Photo(
            Integer.parseInt(cursor.getString(0)),
            cursor.getString(1),
            StringToBitMap(cursor.getString(2)),
            cursor.getString(3));

    return photo;
}

    public List<Photo> getAllPhotos(){
        List<Photo> listOfPhotos = new ArrayList<Photo>();
        String selectQuery = "SELECT * FROM " + PHOTOS_TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        Photo photo;
        //String cursorToString =   DatabaseUtils.dumpCursorToString(cursor);// check the database tables
        if (cursor.moveToFirst()){
            while (!cursor.isAfterLast()) {
                photo = new Photo(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        StringToBitMap(cursor.getString(2)),
                        cursor.getString(3));
                listOfPhotos.add(photo);
                cursor.moveToNext();
            }
        }
        return listOfPhotos;
    }
    public void delete(String uniqueID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ PHOTOS_TABLE +" where "+UNIQUE_KEY+"='"+uniqueID+"'");
    }
    public void removeAll(){
        // db.delete(String tableName, String whereClause, String[] whereArgs);
        // If whereClause is null, it will delete all rows.
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PHOTOS_TABLE, null, null);
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
