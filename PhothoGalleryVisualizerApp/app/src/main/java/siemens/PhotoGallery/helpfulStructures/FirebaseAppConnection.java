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

import java.util.ArrayList;
import java.util.List;

public class FirebaseAppConnection {
    Context context;
    private DatabaseReference mDatabase;
    private StorageReference sReference;
    private Database database;
    private int onlineDatabasePhotos = 0;
    private int localDatabasePhotos = 0;
    public FirebaseAppConnection(Context context) {
        this.context = context;
        this.database = new Database(context);
    }
    public void getAllPhotos(){
        database.removeAllPhotos();
        List<UserAccount> userAccounts = database.getAllUserAccounts();
        String authName;
        for (UserAccount userAccount : userAccounts){
            authName = userAccount.getUserID();
            mDatabase = FirebaseDatabase.getInstance().getReference("Photos/"+authName);
            String finalAuthName = authName;
            mDatabase .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // dataSnapshot is the "issue" node with all children with id 0
                        for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                            PhotoFirebaseDatabase photoFirebaseDatabase = messageSnapshot.getValue(PhotoFirebaseDatabase.class);
                            //photosFirebaseDatabase.add(photoFirebaseDatabase);
                            onlineDatabasePhotos =  onlineDatabasePhotos + 1;
                            getPhotoImages(finalAuthName,photoFirebaseDatabase);
                        }
//                        String meessage = Integer.toString(onlineDatabasePhotos)+ " photos downloaded";
//                        Toast.makeText(context,meessage,Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
    public void getPhotoImages(String authName,PhotoFirebaseDatabase photoFirebaseDatabase){
        sReference =  FirebaseStorage.getInstance().getReference().child("photos/" + authName + "/" + photoFirebaseDatabase.getUniqueID() + ".jpg");
        final long ONE_MEGABYTE = 1024 * 1024;
        sReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
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
