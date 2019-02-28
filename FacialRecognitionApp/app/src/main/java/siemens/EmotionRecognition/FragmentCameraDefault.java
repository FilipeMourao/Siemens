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
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import siemens.EmotionRecognition.helpfulStructures.Result;


public class FragmentCameraDefault extends Fragment implements SurfaceHolder.Callback{
    public SurfaceHolder  mSurfaceHolder;
    public SurfaceView mSurfaceView;
    public TextView emotionText;
//    public Camera mCamera;
    public Camera mCamera;
    public boolean mPreviewRunning;
    public Timer t ;
    public int currrentCameraID = Camera.CameraInfo.CAMERA_FACING_FRONT;
    public ImageButton switchCamera;

//    public ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Inflate the layout
//        Intent i=new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//        startActivity(i);
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        mSurfaceView = (SurfaceView) view.findViewById(R.id.surfaceCamera);
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
                mCamera = Camera.open(currrentCameraID);
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
        mCamera = Camera.open(currrentCameraID);
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
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    takePicture();
                }
            }, 1000, 5000);

        } catch (IOException e) {

            e.printStackTrace();

        }



    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        t.cancel();
        mPreviewRunning = false;
        mCamera.stopPreview();
        mCamera.release();

    }
    public void takePicture(){
        if (mCamera !=null){
            mCamera.setOneShotPreviewCallback(new Camera.PreviewCallback() {
                @Override
                public void onPreviewFrame(byte[] data, Camera camera) {
                    Camera.Size previewSize = camera.getParameters().getPreviewSize();
                    YuvImage yuvimage=new YuvImage(data, ImageFormat.NV21, previewSize.width, previewSize.height, null);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    yuvimage.compressToJpeg(new Rect(0, 0, previewSize.width, previewSize.height), 80, baos);
                    byte[] jdata = baos.toByteArray();

//                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(jdata, 0, jdata.length);

                    Matrix matrix = new Matrix();
                    if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE){
                        matrix.postRotate(90);

                    } else {
                        matrix.postRotate(0);
                    }

                    Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);


                    Result result = new Result(rotatedBitmap );
//                    imageView.setImageBitmap(rotatedBitmap );
                }
            });
        }
    }

    public static void setCameraDisplayOrientation(Activity activity, int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
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

}
