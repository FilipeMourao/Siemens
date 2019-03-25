package siemens.PhotoGallery.helpfulStructures;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FirebaseAppConnection {
    Context context;
    private DatabaseReference mDatabase;
    private StorageReference sReference;
    private PhotoDatabase photoDatabase;
    List<PhotoFirebaseDatabase>  photosFirebaseDatabase = new ArrayList<>();
    private int onlineDatabasePhotos = 0;
    private int localDatabasePhotos = 0;
    public FirebaseAppConnection(Context context) {
        this.context = context;
        this.photoDatabase = new PhotoDatabase(context);
    }
    public void saveCurrentlyUserPhotos(){
        List<Photo> photos = photoDatabase.getAllPhotos();
        for( Photo photo: photos){
            PhotoFirebaseDatabase photoFirebaseDatabase = new PhotoFirebaseDatabase(photo);
            saveCurrentlyUserPhotoInformation(photoFirebaseDatabase);
            saveCurrentlyUserPhotoImage(photo.getUniqueID(),photo.getImageBitMap());
        }
        // show alert that all the photos were updated
        localDatabasePhotos = photos.size();
        int newPictures = localDatabasePhotos - onlineDatabasePhotos;
        if (newPictures < 0) newPictures = 0;
        String meessage = Integer.toString(newPictures)+ " new photos were updated to your database";
        onlineDatabasePhotos = localDatabasePhotos;
        Toast.makeText(context,meessage,Toast.LENGTH_LONG).show();
    }
    private void saveCurrentlyUserPhotoInformation(PhotoFirebaseDatabase photoFirebaseDatabase){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Photos").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(photoFirebaseDatabase.getUniqueID()).setValue(photoFirebaseDatabase);
    }
    private void saveCurrentlyUserPhotoImage(String uniqueID, Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        sReference = FirebaseStorage.getInstance().getReference().child("photos/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + uniqueID + ".jpg");
        sReference.putBytes(data);
    }
//    private void saveCurrentlyUser(Photo photo){
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//        mDatabase.child("Photos").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(photo.getUniqueID()).setValue(photo);
//    }
    public void deletePhoto(Photo photo, Boolean deleteFromOnlineDatabase){
        photoDatabase.delete(photo.getUniqueID());
        if (deleteFromOnlineDatabase){
            onlineDatabasePhotos =  onlineDatabasePhotos - 1;
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("Photos").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(photo.getUniqueID()).removeValue();
            sReference =  FirebaseStorage.getInstance().getReference().child("photos/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + photo.getUniqueID() + ".jpg");
            sReference.delete();
        }
    }
    public void deleteAllPhotos(Boolean deleteFromOnlineDatabase){
        if (deleteFromOnlineDatabase){
            onlineDatabasePhotos =  0;
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("Photos").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
            List<Photo> photos = photoDatabase.getAllPhotos();
            for(Photo photo : photos){
                sReference =   FirebaseStorage.getInstance().getReference().child("photos/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + photo.getUniqueID() + ".jpg");
                sReference.delete();
            }

        }
        photoDatabase.removeAll();
    }
    public void getAllPhotos(){
        String authName = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Photos/"+authName);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                        PhotoFirebaseDatabase photoFirebaseDatabase = messageSnapshot.getValue(PhotoFirebaseDatabase.class);
                        //photosFirebaseDatabase.add(photoFirebaseDatabase);
                        onlineDatabasePhotos =  onlineDatabasePhotos + 1;
                        getPhotoImages(photoFirebaseDatabase);
                    }
                    String meessage = Integer.toString(onlineDatabasePhotos)+ " photos downloaded";
                    Toast.makeText(context,meessage,Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public void getPhotoImages(PhotoFirebaseDatabase photoFirebaseDatabase){
        sReference =  FirebaseStorage.getInstance().getReference().child("photos/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + photoFirebaseDatabase.getUniqueID() + ".jpg");
        final long ONE_MEGABYTE = 1024 * 1024;
        sReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                Photo photo  = new Photo(photoFirebaseDatabase.getId(), photoFirebaseDatabase.getName(), bitmap, photoFirebaseDatabase.getUniqueID());
                photoDatabase.addPhoto(photo);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

}
