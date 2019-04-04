package siemens.PhotoGallery.helpfulStructures;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class FirebaseAppConnection {
    Context context;
    private DatabaseReference mDatabase;
    private StorageReference sReference;
    private Database database;
    public FirebaseAppConnection(Context context) {
        this.context = context;
        this.database = new Database(context);
    }
    public void getAllPhotos(){ // get all photos from the online database
        database.removeAllPhotos();// remove all the current photos from the online database
        List<UserAccount> userAccounts = database.getAllUserAccounts(); // get all the acounts that the device has acess
        String authName;
        for (UserAccount userAccount : userAccounts){ // for each account get all the pictures from the databse and the storage
            authName = userAccount.getUserID();
            mDatabase = FirebaseDatabase.getInstance().getReference("Photos/"+authName);
            String finalAuthName = authName;
            mDatabase .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        int onlineUserDatabasePhotos = 0;
                        // dataSnapshot is the "issue" node with all children with id 0
                        for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                            PhotoFirebaseDatabase photoFirebaseDatabase = messageSnapshot.getValue(PhotoFirebaseDatabase.class);// get the picture information
                            onlineUserDatabasePhotos =  onlineUserDatabasePhotos + 1;
                            getPhotoImages(finalAuthName,photoFirebaseDatabase);// request to get the picture image from the storage
                        }
                        userAccount.setNumberOfImages(onlineUserDatabasePhotos);
                        database.addUserAccount(userAccount);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }
    public void getPhotoImages(String authName,PhotoFirebaseDatabase photoFirebaseDatabase){
        sReference =  FirebaseStorage.getInstance().getReference().child("photos/" + authName + "/" + photoFirebaseDatabase.getUniqueID() + ".jpg");// get the path of the image on the online storage
        final long ONE_MEGABYTE = 1024 * 1024; // set maximum image size
        sReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) { // getting the image as array of bites, convert to bitmap and save the image with the image information on the database
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                Photo photo  = new Photo(photoFirebaseDatabase.getId(), photoFirebaseDatabase.getName(), bitmap, photoFirebaseDatabase.getUniqueID());
                database.addPhoto(photo);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

}
