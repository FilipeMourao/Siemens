package siemens.PhotoGallery.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import siemens.PhotoGallery.R;
import siemens.PhotoGallery.helpfulStructures.FirebaseAppConnection;
import siemens.PhotoGallery.helpfulStructures.Photo;
import siemens.PhotoGallery.helpfulStructures.PhotoDatabase;

import static android.app.Activity.RESULT_OK;


public class FragmentCloud extends Fragment {
    private FloatingActionButton fab;
    private Button uploadButton;
    private Button downloaddButton;
    private Button deleteAll;
    private PhotoDatabase photosDatabase;
    private List<Photo> results;
    public static int SELECT_IMAGE_GALLERY = 12445;
    public static int SELECT_IMAGE_CAMERA = 124451;
    public static String RESULTJSON = "RESULTJSON";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Inflate the layout
        View view = inflater.inflate(R.layout.fragment_cloud, container, false);
        photosDatabase = new PhotoDatabase(getActivity());
        //Get reference to FloatingActionButton, set correct icon and register OnClickListener
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(this::onButtonPressed);
        uploadButton  = view.findViewById(R.id.upload_button);
        uploadButton.setOnClickListener(this::onUploadButtonPressed);
        downloaddButton = view.findViewById(R.id.download_button);
        downloaddButton.setOnClickListener(this::onDownloadButtonPressed);
        deleteAll = view.findViewById(R.id.deleteAll_button);
        deleteAll.setOnClickListener(this::onDeleteAllButtonPressed);
        return view;
    }

    public boolean checkPicturesPermission() {
        if ((ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED )||
                (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED)||
                (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED)||
                (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.INTERNET)
                        != PackageManager.PERMISSION_GRANTED)
                )
        {
            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission
                    .WRITE_EXTERNAL_STORAGE,Manifest.permission.INTERNET}, 0);
            return false;

        } else {
            return true;
            // Permission has already been granted
        }
    }
    public void onUploadButtonPressed(View v){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getActivity());
        }
        builder.setTitle("Do you want to upload your photos to the database?")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // add the update of results
                        FirebaseAppConnection firebaseAppConnection = new FirebaseAppConnection(getActivity());
                        firebaseAppConnection.saveCurrentlyUserPhotos();
                    }
                })
                .show();
    }

    public void onDownloadButtonPressed(View v){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getActivity());
        }
        builder.setTitle("Do you want to download your photos from the database?")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // dowload the results via firebase
                        FirebaseAppConnection firebaseAppConnection = new FirebaseAppConnection(getActivity());
                        firebaseAppConnection.getAllPhotos();

                    }
                })
                .show();
    }
    public void onDeleteAllButtonPressed(View v){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getActivity());
        }
        builder.setTitle("Do you want to delete all photos also from the online database?")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAppConnection  firebaseAppConnection = new FirebaseAppConnection(getActivity());
                        firebaseAppConnection.deleteAllPhotos(false);
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAppConnection  firebaseAppConnection = new FirebaseAppConnection(getActivity());
                        firebaseAppConnection.deleteAllPhotos(true);
                    }
                })
                .show();
    }
    public void onButtonPressed(View v) {
        if (checkPicturesPermission()) {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(getActivity());
            }
            builder.setTitle("Get a new picture")
                    .setMessage("Take the picture from:")
                    .setNegativeButton("From camera", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, SELECT_IMAGE_CAMERA);
                        }
                    })
                    .setPositiveButton("From gallery", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new   Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(intent, SELECT_IMAGE_GALLERY);
                        }
                    })
                    .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_IMAGE_CAMERA) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                Photo photo= new Photo(bitmap);
                photosDatabase.addPhoto(photo);
                // update the view


            } else if (requestCode == SELECT_IMAGE_GALLERY) {
                Uri selectedImage = data.getData();
                // h=1;
                //imgui = selectedImage;
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));

                Photo photo= new Photo(getResizedBitmap(rotateBitmap(thumbnail,-90.0f),1000));
                photosDatabase.addPhoto(photo);
                //update the view
            }
        }
    }
    public static Bitmap convertYuvImageToBitmap(@NonNull final YuvImage yuvImage) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, yuvImage.getWidth(), yuvImage.getHeight()), 100, out);
        byte[] imageBytes = out.toByteArray();
        try {
            out.close();
        } catch (IOException e) {
            Log.e("LOG_TAG", "Exception while closing output stream", e);
        }
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    public static Bitmap rotateBitmap(@NonNull final Bitmap source, final float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
    //The follow code sample shows an example of how to retrieve metric values from the Face object
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}
