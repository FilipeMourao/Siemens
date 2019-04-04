package siemens.PhotoGallery.helpfulStructures;

import android.graphics.Bitmap;

public class PhotoFirebaseDatabase {// this class is used to store the images information in the online datbase without the bitmap image
    private int  id;
    private String  UniqueID;
    private String name;
    public PhotoFirebaseDatabase(){// initializer for the firabase interface

    }
    public PhotoFirebaseDatabase(Photo photo) {// constructor to get the photo information without the image
        this.id = photo.getId();
        UniqueID = photo.getUniqueID();
        this.name = photo.getName();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUniqueID() {
        return UniqueID;
    }

    public void setUniqueID(String uniqueID) {
        UniqueID = uniqueID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
