package siemens.PhotoGallery;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import siemens.PhotoGallery.helpfulStructures.FirebaseAppConnection;
import siemens.PhotoGallery.helpfulStructures.Photo;
import siemens.PhotoGallery.helpfulStructures.PhotoDatabase;

public class PhotoDetails extends AppCompatActivity {
    ImageView galleryPreviewImg;
    Button deleteButton;
    Button addNameButton;
    Photo photo;
    PhotoDatabase photoDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.images_gallery_preview);
        Intent intent = getIntent();
        String jsonClass = intent.getExtras().getString("photo");
        Gson gson = new Gson();
         photo = gson.fromJson(jsonClass, Photo.class);
        galleryPreviewImg = (ImageView) findViewById(R.id.GalleryPreviewImg);
        galleryPreviewImg.setImageBitmap(photo.getImageBitMap());
        deleteButton = findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(this::onDeleteButtonPressed);
        addNameButton = findViewById(R.id.add_name);
        if (photo.getName()!= null && !photo.getName().isEmpty()){
            String name = "Name: " + photo.getName();
            addNameButton.setText(name);
        }
        addNameButton.setOnClickListener(this::onAddNameButtonPressed);
        photoDatabase = new PhotoDatabase(getApplicationContext());

    }
    void onDeleteButtonPressed(View v){// if the delete button was pressed ask if the user want to delete the element only in the local database or also in the global database
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(PhotoDetails.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(PhotoDetails.this);
        }
        builder.setTitle("Do you want to delete the image also from the online database?")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAppConnection  firebaseAppConnection = new FirebaseAppConnection(getBaseContext());
                        firebaseAppConnection.deletePhoto(photo,false);
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAppConnection  firebaseAppConnection = new FirebaseAppConnection(getBaseContext());
                        firebaseAppConnection.deletePhoto(photo,true);
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                    }
                })
                .show();

    }
    void onAddNameButtonPressed(View v){// if the add name button was pressed ask if the user to add the name to the photo
        AlertDialog.Builder builder = new AlertDialog.Builder(PhotoDetails.this);
// Set up the input
        final EditText input = new EditText(getApplicationContext());
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        builder.setView(input);
        builder.setTitle("Please add the new name of the photo");
// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                photo.setName(input.getText().toString());
                photoDatabase.addPhoto(photo);
                String name = "Name: " + photo.getName();
                addNameButton.setText(name);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);

    }
}
