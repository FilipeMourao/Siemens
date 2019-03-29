package siemens.PhotoGallery.helpfulStructures;

import android.graphics.Bitmap;

import com.google.firebase.database.Exclude;

import java.util.UUID;

public class Photo {
    private int  id;
    private String  UniqueID;
    private Bitmap imageBitMap;
    private String name;

    public Photo() {
    }

    public Photo(Bitmap imageBitMap) {
        this.imageBitMap = imageBitMap;
        this.UniqueID = UUID.randomUUID().toString();
    }

    public Photo(int id, String name, Bitmap imageBitMap, String uniqueID) {
        this.id = id;
        UniqueID = uniqueID;
        this.imageBitMap = imageBitMap;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    @Exclude
    public Bitmap getImageBitMap() {
        return imageBitMap;
    }

    public String getUniqueID() {
        return UniqueID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}


//package siemens.PhotoGallery.helpfulStructures;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.util.Base64;
//
//import java.io.ByteArrayOutputStream;
//import java.util.UUID;
//
//public class Photo {
//    private int  id;
//    private String  UniqueID;
//    private String imageBitMap;
//    //private Bitmap imageBitMap;
//    private String name;
//
//    public Photo() {
//    }
//
//    public Photo(Bitmap imageBitMap) {
//        this.imageBitMap = BitMapToString(imageBitMap) ;
//        this.UniqueID = UUID.randomUUID().toString();
//    }
//
//    public Photo(int id, String name, Bitmap imageBitMap, String uniqueID) {
//        this.id = id;
//        UniqueID = uniqueID;
//        this.imageBitMap = BitMapToString(imageBitMap);
//        this.name = name;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public Bitmap getImageBitMap() {
//        return StringToBitMap(imageBitMap);
//    }
//
//    public String getUniqueID() {
//        return UniqueID;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//    public String BitMapToString(Bitmap bitmap){
//        ByteArrayOutputStream baos=new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
//        byte [] b=baos.toByteArray();
//        String temp=Base64.encodeToString(b, Base64.DEFAULT);
//        return temp;
//    }
//    public Bitmap StringToBitMap(String encodedString){
//        try {
//            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
//            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
//            return bitmap;
//        } catch(Exception e) {
//            e.getMessage();
//            return null;
//        }
//    }
//}
