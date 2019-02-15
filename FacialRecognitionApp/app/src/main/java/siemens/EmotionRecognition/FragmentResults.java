package siemens.EmotionRecognition;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


import com.affectiva.android.affdex.sdk.Frame;
import com.affectiva.android.affdex.sdk.decoder.FrameDecoder;
import com.affectiva.android.affdex.sdk.detector.Detector;
import com.affectiva.android.affdex.sdk.detector.Face;
import com.affectiva.android.affdex.sdk.detector.PhotoDetector;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import siemens.EmotionRecognition.helpfulStructures.ArrayAdapterResult;
import siemens.EmotionRecognition.helpfulStructures.Result;
import siemens.EmotionRecognition.helpfulStructures.ResultsDatabase;

import static android.app.Activity.RESULT_OK;


public class FragmentResults extends Fragment implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener,Detector.ImageListener {
    private FloatingActionButton fab;
    private ListView listView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ResultsDatabase resultsDatabase;
    private List<Result> results;
    private  PhotoDetector detector;
    public static int SELECT_IMAGE_GALLERY = 12445;
    public static int SELECT_IMAGE_CAMERA = 124451;
    public static String RESULTJSON = "RESULTJSON";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Inflate the layout
        View view = inflater.inflate(R.layout.fragment_results, container, false);
        resultsDatabase = new ResultsDatabase(getActivity());
        listView = view.findViewById(R.id.result_list);
        listView.setOnItemClickListener(this::onItemClick);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentResults.this.onItemLongClick(parent,view,position,id);
                return true;
            }
        });
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        //Get reference to FloatingActionButton, set correct icon and register OnClickListener
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(this::onButtonPressed);

        detector = new PhotoDetector(getActivity(),1,Detector.FaceDetectorMode.LARGE_FACES);
        detector.setImageListener(this);
        detector.setImageListener(this);
        detector.setDetectAllEmotions(true);
        detector.setDetectAllAppearances(true);
        updateList();
        return view;
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Result result = results.get(position);
        Gson gson = new Gson();
        String resultString = gson.toJson(result);
        Intent intent = new Intent(getActivity(),ResultDetails.class);
        intent.putExtra(RESULTJSON,resultString);
        startActivity(intent);
    }

    public void onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getActivity());
        }
        builder.setTitle("Delete Result")
                .setMessage("Do you want to delete this result :")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        resultsDatabase.delete(results.get(position).getId());
                        updateList();
                    }
                })
                .show();
    }

    @Override
    public void onRefresh() {updateList();  }

    public boolean checkPicturesPermission() {
        if ((ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                )
                ) {
            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            return false;

        } else {
            return true;
            // Permission has already been granted
        }
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
                            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, SELECT_IMAGE_CAMERA);
                        }
                    })
                    .setNeutralButton("From gallery", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(intent, SELECT_IMAGE_GALLERY);
                        }
                    })
                    .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
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
                Result result = new Result(bitmap);
                resultsDatabase.addResult(result);
                updateList();

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

                Result result = new Result(getResizedBitmap(rotateBitmap(thumbnail,-90.0f),1000));
                resultsDatabase.addResult(result);
                updateList();

            }

        }
    }
    public void updateList(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int apiType =  Integer.valueOf(sp.getString("pref_list","1"));
        results = resultsDatabase.getAllResults();
        for (Result result : results){
            if (result.getAPI_Type() != apiType){
                updateImageFromDifferentAPI(result,apiType);
                resultsDatabase.delete(result.getId());
            }
        }
        results = resultsDatabase.getAllResults();
        ArrayAdapterResult arrayAdapterResult = new ArrayAdapterResult
                (getActivity(), R.layout.result_list_row, results);
        listView.setAdapter(arrayAdapterResult);
        swipeRefreshLayout.setRefreshing(false);
    }
    public void updateImageFromDifferentAPI(Result result,int apiType){
        detector.start();
        switch (apiType){
            case 1:
                //affectiva api
                Frame frame = new Frame.BitmapFrame(result.getImageBitMap(),Frame.COLOR_FORMAT.RGBA);
                detector.process(frame);
                break;
            default:
                //no api;
                break;
        }
    }
    @Override
    public void onImageResults(List<Face> faces, Frame image, float v) {
        if (faces == null){
            resultsDatabase.addResult(new Result(getBitmapFromFrame(image)));
            return; //frame was not processed
        }
        if (faces.size() == 0){
            resultsDatabase.addResult(new Result(getBitmapFromFrame(image)));
            return; //no face found
        }
        //For each face found
        for (int i = 0; i < faces.size(); i++) {
            Face face = faces.get(i);
            int faceId = face.getId();
            //Appearance
            Face.GENDER genderValue = face.appearance.getGender();
            //Face.GLASSES glassesValue = face.appearance.getGlasses();
            Face.AGE ageValue = face.appearance.getAge();
            Face.ETHNICITY ethnicityValue = face.appearance.getEthnicity();
            Result result = new Result(getBitmapFromFrame(image));
            List<String> emotionString = new ArrayList<>();
            emotionString.add("anger");
            emotionString.add("contempt");
            emotionString.add("disgust");
            emotionString.add("engagement");
            emotionString.add("fear");
            emotionString.add("joy");
            emotionString.add("sadness");
            emotionString.add("surprise");
            emotionString.add("valence");
            List<Float> emotionValues = new ArrayList<>();
            emotionValues.add(face.emotions.getAnger());
            emotionValues.add(face.emotions.getContempt());
            emotionValues.add(face.emotions.getDisgust());
            emotionValues.add(face.emotions.getEngagement());
            emotionValues.add(face.emotions.getFear());
            emotionValues.add(face.emotions.getJoy());
            emotionValues.add(face.emotions.getSadness());
            emotionValues.add(face.emotions.getSurprise());
            emotionValues.add(face.emotions.getValence());
            result.setEmotionString(emotionString);
            result.setEmotionValues(emotionValues);
            result.setAge(ageValue.name());
            result.setGender(genderValue.name());
            result.setEthinicity(ethnicityValue.name());
            result.setAPI_Type(1);// affectivia is the API 1 in the list
            resultsDatabase.addResult(result);
        }
        detector.stop();
    }
    public static Bitmap getBitmapFromFrame(@NonNull  Frame frame) {
        Bitmap bitmap;

        if (frame instanceof Frame.BitmapFrame) {
            bitmap = ((Frame.BitmapFrame) frame).getBitmap();
        } else { //frame is ByteArrayFrame
            switch (frame.getColorFormat()) {
                case RGBA:
                    bitmap = getBitmapFromRGBFrame(frame);
                    break;
                case YUV_NV21:
                    bitmap = getBitmapFromYuvFrame(frame);
                    break;
                case UNKNOWN_TYPE:
                default:
                    Log.e("LOG_TAG", "Unable to get bitmap from unknown frame type");
                    return null;
            }
        }

        if (bitmap == null || frame.getTargetRotation().toDouble() == 0.0) {
            return bitmap;
        } else {
            return rotateBitmap(bitmap, (float) frame.getTargetRotation().toDouble());
        }
    }

    public static Bitmap getBitmapFromRGBFrame(@NonNull final Frame frame) {
        byte[] pixels = ((Frame.ByteArrayFrame) frame).getByteArray();
        Bitmap bitmap = Bitmap.createBitmap(frame.getWidth(), frame.getHeight(), Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(ByteBuffer.wrap(pixels));
        return bitmap;
    }

    public static Bitmap getBitmapFromYuvFrame(@NonNull final Frame frame) {
        byte[] pixels = ((Frame.ByteArrayFrame) frame).getByteArray();
        YuvImage yuvImage = new YuvImage(pixels, ImageFormat.NV21, frame.getWidth(), frame.getHeight(), null);
        return convertYuvImageToBitmap(yuvImage);
    }

    /**
     * Note: This conversion procedure is sloppy and may result in JPEG compression artifacts
     *
     * @param yuvImage - The YuvImage to convert
     * @return - The converted Bitmap
     */
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
