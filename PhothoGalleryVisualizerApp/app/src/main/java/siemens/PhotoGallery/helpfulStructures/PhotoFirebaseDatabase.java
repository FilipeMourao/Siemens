package siemens.PhotoGallery.helpfulStructures;

import android.graphics.Bitmap;

public class PhotoFirebaseDatabase {
    private int  id;
    private String  UniqueID;
    private String name;
    public PhotoFirebaseDatabase(){

    }
    public PhotoFirebaseDatabase(Photo photo) {
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
