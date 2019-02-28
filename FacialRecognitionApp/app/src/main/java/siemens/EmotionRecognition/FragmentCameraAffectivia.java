package siemens.EmotionRecognition;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.affectiva.android.affdex.sdk.Frame;
import com.affectiva.android.affdex.sdk.detector.CameraDetector;
import com.affectiva.android.affdex.sdk.detector.CameraDetector.CameraType;
import com.affectiva.android.affdex.sdk.detector.Detector;
import com.affectiva.android.affdex.sdk.detector.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.sql.Time;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import siemens.EmotionRecognition.helpfulStructures.Result;
import siemens.EmotionRecognition.helpfulStructures.ResultsDatabase;

//public class FragmentCameraAffectivia extends Fragment implements CameraDetector.CameraEventListener,Detector.ImageListener {
public class FragmentCameraAffectivia extends Fragment implements Detector.ImageListener {
    public SurfaceHolder  mSurfaceHolder;
    public SurfaceView mSurfaceView;
    public TextView emotionText;
//    public Camera mCamera;
    public Camera mCamera;
    public boolean mPreviewRunning;
    public Timer t ;
    public CameraType currrentCameraType =  CameraType.CAMERA_FRONT;
    public ImageButton switchCamera;
    public CameraDetector detector;
    public int width,height;
    ResultsDatabase resultsDatabase;
    static int FRAMES_PER_SECOND = 20;
    List<Result> resultList = new ArrayList<>();
//    public ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        resultsDatabase = new ResultsDatabase(getActivity());;
        mSurfaceView = (SurfaceView) view.findViewById(R.id.surfaceCamera);
        emotionText = view.findViewById(R.id.emotionText);
        switchCamera = view.findViewById(R.id.switchCamera);
        switchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currrentCameraType ==  CameraType.CAMERA_FRONT){
                    currrentCameraType =  CameraType.CAMERA_BACK;
                }
                else {
                    currrentCameraType =  CameraType.CAMERA_FRONT;
                }
                detector.stop();
                detector.reset();
                detector = new CameraDetector(getActivity(),currrentCameraType,mSurfaceView,1,Detector.FaceDetectorMode.LARGE_FACES);
                detector.start();

            }
        });
        detector = new CameraDetector(getActivity(),currrentCameraType,mSurfaceView,1,Detector.FaceDetectorMode.LARGE_FACES);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        detector.setMaxProcessRate(FRAMES_PER_SECOND);
        detector.setImageListener(this);
        detector.setDetectAllEmotions(true);
        detector.setDetectAllAppearances(true);
        detector.start();

    }

    public void onImageResults(List<Face> faces, Frame image, float timestamp) {

        if (faces == null)
            return; //frame was not processed

        if (faces.size() == 0)
            return; //no face found

        //For each face found
        for (int i = 0 ; i < faces.size() ; i++) {
            Face face = faces.get(i);

            int faceId = face.getId();

            //Appearance
            Face.GENDER genderValue = face.appearance.getGender();
            //Face.GLASSES glassesValue = face.appearance.getGlasses();
            Face.AGE ageValue = face.appearance.getAge();
            Face.ETHNICITY ethnicityValue = face.appearance.getEthnicity();
            Result result = new Result(getBitmapFromFrame(image));
            List<String> emotionString = new ArrayList<>();
            emotionString.add("anger");emotionString.add("contempt");emotionString.add("disgust");emotionString.add("engagement");
            emotionString.add("fear");emotionString.add("joy");emotionString.add("sadness");emotionString.add("surprise");
            emotionString.add("valence");
            List<Float> emotionValues = new ArrayList<>();
            emotionValues.add(face.emotions.getAnger());emotionValues.add(face.emotions.getContempt());emotionValues.add(face.emotions.getDisgust());
            emotionValues.add(face.emotions.getEngagement());emotionValues.add(face.emotions.getFear());emotionValues.add(face.emotions.getJoy());
            emotionValues.add(face.emotions.getSadness());emotionValues.add(face.emotions.getSurprise());emotionValues.add(face.emotions.getValence());
            result.setEmotionString(emotionString);result.setEmotionValues(emotionValues);
            result.setAge(ageValue.name());
            result.setGender(genderValue.name());
            result.setEthinicity(ethnicityValue.name());
            result.setAPI_Type(1);// affectivia is the API 1 in the list
            resultList.add(result);
            emotionText.setText(result.getMostPossibleEmotionName());
            if (resultList.size()>FRAMES_PER_SECOND*2) {
                Random random = new Random();
                int randomNumber = random.nextInt(resultList.size());
                Result result1 = resultList.get(randomNumber);
                resultsDatabase.addResult(result1);
                resultList = new ArrayList<>();
            }
    }
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

}
