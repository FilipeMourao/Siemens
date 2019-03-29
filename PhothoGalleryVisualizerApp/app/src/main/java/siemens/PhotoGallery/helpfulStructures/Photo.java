package siemens.PhotoGallery.helpfulStructures;

import android.graphics.Bitmap;

import com.google.firebase.database.Exclude;

import java.util.UUID;

public class Photo {
    private int  id;
    private String  UniqueID;
    private Bitmap imageBitMap;
    private String name;

    public Photo() {// initializer for the firabase interface
    }

    public Photo(int id, String name, Bitmap imageBitMap, String uniqueID) {// contructor to build the photo from the database elements
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

