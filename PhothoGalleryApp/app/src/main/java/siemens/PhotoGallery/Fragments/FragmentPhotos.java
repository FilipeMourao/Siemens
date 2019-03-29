package siemens.PhotoGallery.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import siemens.PhotoGallery.PhotoDetails;
import siemens.PhotoGallery.R;
import siemens.PhotoGallery.helpfulStructures.Photo;
import siemens.PhotoGallery.helpfulStructures.PhotoDatabase;
import siemens.PhotoGallery.helpfulStructures.SingleAlbumAdapter;

import static android.app.Activity.RESULT_OK;


public class FragmentPhotos extends Fragment implements  SwipeRefreshLayout.OnRefreshListener  {

    private FloatingActionButton fab;
    private PhotoDatabase photosDatabase;
    public static int SELECT_IMAGE_GALLERY = 12445;
    public static int SELECT_IMAGE_CAMERA = 124451;
    public GridView galleryGridView;
    public SwipeRefreshLayout swipeRefreshLayout;
    List<Photo> photos = new ArrayList<>();
    public static String RESULTJSON = "RESULTJSON";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Inflate the layout
        View view = inflater.inflate(R.layout.fragment_photos, container, false);
        galleryGridView = view.findViewById(R.id.galleryGridView);
        int iDisplayWidth = getResources().getDisplayMetrics().widthPixels ;
        Resources resources = view.getResources();
        // design the layout to show all the images
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = iDisplayWidth / (metrics.densityDpi / 160f);

        if(dp < 360)
        {
            dp = (dp - 17) / 2;
            float px = convertDpToPixel(dp, view.getContext());
            galleryGridView.setColumnWidth(Math.round(px));
        }

        photosDatabase = new PhotoDatabase(getActivity());
        //Get reference to FloatingActionButton, set correct icon and register OnClickListener
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(this::onButtonPressed);
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(this::onRefresh);

        updateView();
        return view;
    }

    public boolean checkPicturesPermission() { // check for the permissions
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

    public void onButtonPressed(View v) { // if the plus button was pressed show possibilities to add a picture
        if (checkPicturesPermission()) {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(getActivity());
            }
            builder.setTitle("Get a new picture")
                    .setMessage("Take the picture from:")
                    .setNegativeButton("From camera", new DialogInterface.OnClickListener() {// if the user clicks on camera, open the camera via intent
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, SELECT_IMAGE_CAMERA);
                        }
                    })
                    .setPositiveButton("From gallery", new DialogInterface.OnClickListener() {// if the user clicks on galley, open the gallery via intent
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {// get the image from the result of the last intent
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_IMAGE_CAMERA) { // if it was from the camera get the image and convert to bitmap
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                Photo photo= new Photo(bitmap);
                photosDatabase.addPhoto(photo);
                updateView();
                // update the view


            } else if (requestCode == SELECT_IMAGE_GALLERY) { // if the image was from the gallery, get the image rotate it to the right format and save in the database
                Uri selectedImage = data.getData();
                // h=1;
                //imgui = selectedImage;
                String[] filePath = {MediaStore.Images.Media.DATA};
                try {
                    Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    String picturePath = c.getString(columnIndex);
                    c.close();
                    ExifInterface exif = new ExifInterface(picturePath);
                    int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    int rotationInDegrees = exifToDegrees(rotation);
                    Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                    long fileSize = getImageLength(picturePath);
                    //Photo photo= new Photo(getResizedBitmap(rotateBitmap(thumbnail,-90.0f),1000));
                    if (fileSize < 1000000){ // file size less then 1MB
                        Photo photo= new Photo(getResizedBitmap(rotateBitmap(thumbnail,rotationInDegrees),1000));
                        photosDatabase.addPhoto(photo);
                        //update the view
                        updateView();
                    }else {
                        Toast.makeText(getActivity(), "This image is to big to be saved!",Toast.LENGTH_LONG).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
    public static Bitmap rotateBitmap(@NonNull final Bitmap source, final float angle) {
        Matrix matrix = new Matrix();
//        matrix.postRotate(angle);
        matrix.preRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {  return 180; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {  return 270; }
        return 0;
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



   // adapted from http://androstock.com/tutorials/create-a-photo-gallery-app-in-android-android-studio.html

    public  float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }
    public void updateView(){
        photos = photosDatabase.getAllPhotos();
        SingleAlbumAdapter adapter = new SingleAlbumAdapter(getActivity(), photos);
        galleryGridView.setAdapter(adapter);
        galleryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                Intent intent = new Intent(getActivity(), PhotoDetails.class);
                Photo photo = photos.get(position);
                Gson gson = new Gson();
                intent.putExtra("photo", gson.toJson(photo));
                startActivity(intent);
            }
        });
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        updateView();
    }

    public long getImageLength(String absFileName)
    {
        File file = new File(absFileName);
        return file.length();
    }
}



