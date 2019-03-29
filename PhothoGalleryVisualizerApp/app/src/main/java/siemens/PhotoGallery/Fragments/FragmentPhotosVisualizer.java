package siemens.PhotoGallery.Fragments;

import android.Manifest;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import siemens.PhotoGallery.R;
import siemens.PhotoGallery.helpfulStructures.FirebaseAppConnection;
import siemens.PhotoGallery.helpfulStructures.Photo;
import siemens.PhotoGallery.helpfulStructures.Database;


public class FragmentPhotosVisualizer extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final int NUMBER_OF_TIMES_TO_DOWNLOAD_DATA = 5;
    private Database database;
    private ImageView imageView;
    private int currentlyPhotoNumber;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Button nextPhoto;
    private Button previousPhoto;
    List<Photo> photos = new ArrayList<>();
    Timer t;
    private FirebaseAppConnection firebaseAppConnection;
    private int refreshCounter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Inflate the layout
        View view = inflater.inflate(R.layout.fragment_photo_preview, container, false);
        database = new Database(getActivity());
        //Get reference to FloatingActionButton, set correct icon and register OnClickListener
        imageView = view.findViewById(R.id.GalleryPreviewImg);
        currentlyPhotoNumber = 0;// stores the photo in the list corresponding to the showed image
        firebaseAppConnection = new FirebaseAppConnection(getActivity());
        // initialize the buttons
        nextPhoto = view.findViewById(R.id.nextPhotto);
        nextPhoto.setOnClickListener(this::onNextButtonPressed);
        previousPhoto = view.findViewById(R.id.previousPhoto);
        previousPhoto.setOnClickListener(this::onPreviousButtonPressed);
        // initialize the timer  that is going to pass the pictures automatically
        t = new Timer();
        initializeTimer();
        // initialize the refresher
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(this::onRefresh);
        updateView();
        return view;
    }

    public void onNextButtonPressed(View v) {// when the next buttorn is pressed show the next photo in the list
        currentlyPhotoNumber = currentlyPhotoNumber + 1;
        if (currentlyPhotoNumber >= photos.size()) currentlyPhotoNumber = 0;
        updateImage();// update photo
        initializeTimer();// reset timer to pass the photos automatically
    }

    public void onPreviousButtonPressed(View v) {// when the previous buttorn is pressed show the previous photo in the list
        if (photos.size() == 0) currentlyPhotoNumber = 0;
        else {
            currentlyPhotoNumber = currentlyPhotoNumber - 1;
            if (currentlyPhotoNumber < 0) currentlyPhotoNumber = photos.size() - 1;
        }
        updateImage();// update photo
        initializeTimer();// reset timer to pass the photos automatically
    }

    public boolean checkPicturesPermission() { // check for the needed permissions to execute the application, if any permission is missing ask for the permissions
        if (
                (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) ||
                        (ContextCompat.checkSelfPermission(getActivity(),
                                Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) ||
                        (ContextCompat.checkSelfPermission(getActivity(),
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) ||
                        (ContextCompat.checkSelfPermission(getActivity(),
                                Manifest.permission.INTERNET)
                                != PackageManager.PERMISSION_GRANTED)
                ) {
            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission
                            .WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET}, 0);
            return false;

        } else {
            return true;
            // Permission has already been granted
        }
    }

    public void updateView() { // update the view
        if (checkPicturesPermission()) {
            refreshCounter = refreshCounter + 1;
            if (refreshCounter == NUMBER_OF_TIMES_TO_DOWNLOAD_DATA) { // if the number of refresh pass the treshhold, get the pictures from the online database
                firebaseAppConnection.getAllPhotos();
                refreshCounter = 0;
            }
            photos = database.getAllPhotos();
            if (currentlyPhotoNumber >= photos.size()) currentlyPhotoNumber = 0;
            updateImage();
            initializeTimer();
            swipeRefreshLayout.setRefreshing(false);
        }
    }
    public void updateImage(){
        if (photos.size() > 0)
            imageView.setImageBitmap(photos.get(currentlyPhotoNumber).getImageBitMap());
        else
            imageView.setImageDrawable(null);

    }
    @Override
    public void onRefresh() {
        updateView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        t.cancel();// cancel timer
    }

    public void initializeTimer() {
        t.cancel();// reset time to pass the images automatically
        t = new Timer();

        t.scheduleAtFixedRate(
                new TimerTask() {
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                currentlyPhotoNumber = currentlyPhotoNumber + 1;
                                if (currentlyPhotoNumber >= photos.size()) currentlyPhotoNumber = 0;
                                updateImage();
                            }
                        });

                    }
                },
                5 * 1000,      // run first occurrence immediatetly
                5 * 1000); // run every minute

    }

}



