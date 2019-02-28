package siemens.EmotionRecognition;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
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
import android.widget.Toast;

import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;
import com.microsoft.projectoxford.face.contract.Face;
import com.microsoft.projectoxford.face.rest.ClientException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import siemens.EmotionRecognition.helpfulStructures.Result;
import siemens.EmotionRecognition.helpfulStructures.ResultsDatabase;


public class FragmentCameraMicrossoft extends Fragment implements SurfaceHolder.Callback{
    public SurfaceHolder  mSurfaceHolder;
    public SurfaceView mSurfaceView;
    public TextView emotionText;
//    public Camera mCamera;
    public  Camera mCamera;
    public boolean mPreviewRunning;
    public Timer t ;
    public int currrentCameraID = Camera.CameraInfo.CAMERA_FACING_FRONT;
    public ImageButton switchCamera;
    private ResultsDatabase resultsDatabase;
    private FaceServiceClient faceServiceClient;
    private CameraHandlerThread mThread = null;

//    public ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Inflate the layout
//        Intent i=new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//        startActivity(i);
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        final String apiEndpoint = "https://westcentralus.api.cognitive.microsoft.com/face/v1.0";
        final String subscriptionKey = "30a35bf0a63b40c482b5b83ab371e28c";
        faceServiceClient = new FaceServiceRestClient(apiEndpoint, subscriptionKey);

        mSurfaceView = (SurfaceView) view.findViewById(R.id.surfaceCamera);
        resultsDatabase = new ResultsDatabase(getActivity());
        emotionText = view.findViewById(R.id.emotionText);
        switchCamera = view.findViewById(R.id.switchCamera);
        switchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currrentCameraID == Camera.CameraInfo.CAMERA_FACING_BACK){
                    currrentCameraID = Camera.CameraInfo.CAMERA_FACING_FRONT;
                }
                else {
                    currrentCameraID = Camera.CameraInfo.CAMERA_FACING_BACK;
                }
                mCamera.stopPreview();
                mCamera.release();
                newOpenCamera();
                //mCamera = Camera.open(currrentCameraID);
                setCameraDisplayOrientation(getActivity(),currrentCameraID,mCamera);
                try {

                    mCamera.setPreviewDisplay(mSurfaceHolder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mCamera.startPreview();
            }
        });

        mSurfaceHolder = mSurfaceView.getHolder();

        mSurfaceHolder.addCallback(this);

        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//        imageView = view.findViewById(R.id.testImage);
        return view;
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        newOpenCamera();
        //mCamera = Camera.open(currrentCameraID);
        mPreviewRunning = true;

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mPreviewRunning) {

            mCamera.stopPreview();

        }
        Camera.Parameters param = mCamera.getParameters();
        int definedWidth = width;
        int definedHeight = height;
        setCameraDisplayOrientation(getActivity(),currrentCameraID,mCamera);
//        if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE){
//            param.set("orientation","portrait");
//            mCamera.setDisplayOrientation(90);
//            param.setRotation(90);
//
//        } else {
//            param.set("orientation","landscape");
//            mCamera.setDisplayOrientation(0);
//            param.setRotation(0);
//        }
        List<Camera.Size> allSizes = param.getSupportedPictureSizes();
        Collections.sort(allSizes,new Comparator<Camera.Size>(){
            @Override
            public int compare(final Camera.Size size1,Camera.Size size2) {
                return size1.width - size2.width;

            }
        });
        for (int i = 0; i < allSizes.size();i++ ){
            if (definedHeight < allSizes.get(i).height || definedWidth < allSizes.get(i).width){
                if (i == 0){
                    Toast.makeText(getActivity(),"Not possible to open camera",Toast.LENGTH_LONG);
                    return;
                }
                definedHeight = allSizes.get(i).height;
                definedWidth = allSizes.get(i).width;
                break;
            }
        }
        param.setPreviewSize(definedWidth, definedHeight);
        mCamera.setParameters(param);

        try {

            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
            mPreviewRunning = true;
            t = new Timer();
            TimerTask tt = new TimerTask() {
                @Override
                public void run() {
                    updatePictureOnTheScreen();
                }
            };
            t.schedule(tt, 0, 4000);

        } catch (IOException e) {

            e.printStackTrace();

        }



    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        t.cancel();
        t.purge();
        mPreviewRunning = false;
        mCamera.stopPreview();
        mCamera.release();

    }

    public void updatePictureOnTheScreen(){
        if (mPreviewRunning){
            mCamera.setOneShotPreviewCallback(new Camera.PreviewCallback() {
                @Override
                public void onPreviewFrame(byte[] data, Camera camera) {
                    Camera.Size previewSize = camera.getParameters().getPreviewSize();
                    YuvImage yuvimage=new YuvImage(data, ImageFormat.NV21, previewSize.width, previewSize.height, null);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    yuvimage.compressToJpeg(new Rect(0, 0, previewSize.width, previewSize.height), 100, baos);
                    byte[] jdata = baos.toByteArray();

//                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(jdata, 0, jdata.length);

                    Matrix matrix = new Matrix();
                    if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE){
                        matrix.postRotate(-90);

                    } else {
                        matrix.postRotate(0);
                    }

                    Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    rotatedBitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
                    AsyncTask<InputStream,String,Face[]> processAsync =
                            new AsyncTask<InputStream, String, com.microsoft.projectoxford.face.contract.Face[]>() {
                                @Override
                                protected com.microsoft.projectoxford.face.contract.Face[] doInBackground(InputStream... inputStreams) {
                                    com.microsoft.projectoxford.face.contract.Face[] result = null;
                                    try {
                                        result = faceServiceClient.detect(
                                                inputStreams[0],
                                                true,         // returnFaceId
                                                false,        // returnFaceLandmarks
                                                new FaceServiceClient.FaceAttributeType[] {
                                                        FaceServiceClient.FaceAttributeType.Age,
                                                        FaceServiceClient.FaceAttributeType.Gender,
                                                        FaceServiceClient.FaceAttributeType.Emotion}
                                        );
                                    } catch (ClientException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    return result;
                                }

                                @Override
                                protected void onPostExecute(com.microsoft.projectoxford.face.contract.Face[] recognizeResult) {
                                    Result newResult = new Result(getResizedBitmap(rotatedBitmap,1000));
                                    if (recognizeResult == null || recognizeResult.length == 0) {
                                        resultsDatabase.addResult(newResult);
                                    } else {
                                        com.microsoft.projectoxford.face.contract.Face face = recognizeResult[0];
                                        List<Float> emotionValues = new ArrayList<>();
                                        List<String> emotionNames = new ArrayList<>();
                                        emotionValues.add((float)face.faceAttributes.emotion.anger);
                                        emotionNames.add("anger");
                                        emotionValues.add((float)face.faceAttributes.emotion.contempt);
                                        emotionNames.add("contempt");
                                        emotionValues.add((float)face.faceAttributes.emotion.disgust);
                                        emotionNames.add("disgust");
                                        emotionValues.add((float)face.faceAttributes.emotion.fear);
                                        emotionNames.add("fear");
                                        emotionValues.add((float)face.faceAttributes.emotion.happiness);
                                        emotionNames.add("hapiness");
                                        emotionValues.add((float)face.faceAttributes.emotion.neutral);
                                        emotionNames.add("neutral");
                                        emotionValues.add((float)face.faceAttributes.emotion.sadness);
                                        emotionNames.add("sadness");
                                        emotionValues.add((float)face.faceAttributes.emotion.surprise);
                                        emotionNames.add("surprise");
                                        newResult.setAPI_Type(2);// microssoft api
                                        newResult.setGender(face.faceAttributes.gender);
                                        newResult.setAge(Double.toString(face.faceAttributes.age));
                                        newResult.setEmotionValues(emotionValues);
                                        newResult.setEmotionString(emotionNames);
                                        resultsDatabase.addResult(newResult);
                                        emotionText.setText(newResult.getMostPossibleEmotionName());

                                    }
                                }
                            };
                    processAsync.execute(inputStream);
                }
            });
        }
    }

    public static void setCameraDisplayOrientation(Activity activity, int cameraId, Camera camera) {
        Camera.CameraInfo info =
                new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
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
    private void newOpenCamera() {
        if (mThread == null) {
            mThread = new CameraHandlerThread();
        }

        synchronized (mThread) {
            mThread.openCamera();
        }
    }
    private  class CameraHandlerThread extends HandlerThread {
        Handler mHandler = null;

        CameraHandlerThread() {
            super("CameraHandlerThread");
            start();
            mHandler = new Handler(getLooper());
        }

        synchronized void notifyCameraOpened() {
            notify();
        }

        void openCamera() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    oldOpenCamera();
                    notifyCameraOpened();
                }
            });
            try {
                wait();
            }
            catch (InterruptedException e) {
                Log.w("Error", "wait was interrupted");
            }
        }
        private void oldOpenCamera() {
            try {
                mCamera = Camera.open(currrentCameraID);
            }
            catch (RuntimeException e) {
                Log.e("Error", "failed to open front camera");
            }
        }
    }

}
