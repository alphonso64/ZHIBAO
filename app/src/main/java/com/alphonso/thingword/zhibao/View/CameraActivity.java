package com.alphonso.thingword.zhibao.View;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.alphonso.thingword.zhibao.Fragment.ConfirmationDialogFragment;
import com.alphonso.thingword.zhibao.R;
import com.google.android.cameraview.CameraView;

public class CameraActivity extends AppCompatActivity {

    private CameraView mCameraView;
    private Handler mBackgroundHandler;

    private static final String TAG = "CameraActivity";

    private static final int REQUEST_CAMERA_PERMISSION = 1;

    private static final String FRAGMENT_PERMISSION = "permission";
    private static final String FRAGMENT_PICTURE = "picture";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        mCameraView = (CameraView) findViewById(R.id.camera);
        if (mCameraView != null) {
            mCameraView.addCallback(mCallback);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            try {
                mCameraView.start();
            } catch (Exception e) {
                Log.e(TAG, "start camera fail", e);
            }
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            ConfirmationDialogFragment.newInstance(R.string.camera_permission_confirmation,
                    new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION, R.string.camera_permission_not_granted)
                    .show(getSupportFragmentManager(), FRAGMENT_PERMISSION);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
    }

    @Override
    protected void onPause() {
        try {
            mCameraView.stop();
        } catch (Exception e) {
            Log.e(TAG, "stop camera fail", e);
        }

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBackgroundHandler != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                mBackgroundHandler.getLooper().quitSafely();
            } else {
                mBackgroundHandler.getLooper().quit();
            }
            mBackgroundHandler = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:
                if (permissions.length != 1 || grantResults.length != 1) {
                    throw new RuntimeException("Error on requesting camera permission.");
                }
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, R.string.camera_permission_not_granted, Toast.LENGTH_SHORT).show();
                }
                // No need to start camera here; it is handled by onResume
                break;
        }
    }

    private Handler getBackgroundHandler() {
        if (mBackgroundHandler == null) {
            HandlerThread thread = new HandlerThread("background");
            thread.start();
            mBackgroundHandler = new Handler(thread.getLooper());
        }
        return mBackgroundHandler;
    }

    private CameraView.Callback mCallback = new CameraView.Callback() {

        @Override
        public void onCameraError(CameraView cameraView) {
            Log.e(TAG, "onCameraError");
        }

        @Override
        public void onCameraOpened(CameraView cameraView) {
            Log.d(TAG, "onCameraOpened");
        }

        @Override
        public void onCameraClosed(CameraView cameraView) {
            Log.d(TAG, "onCameraClosed");
        }

        @Override
        public void onPictureTaken(CameraView cameraView, final byte[] data) {
            Log.d(TAG, "onPictureTaken " + data.length);
            getBackgroundHandler().post(new Runnable() {
                @Override
                public void run() {
//                    long time = System.currentTimeMillis();
//                    File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), String.format("picture_%s.jpg", String.valueOf(time)));
//                    Log.e("test",Environment.DIRECTORY_PICTURES);
//                    OutputStream os = null;
//                    try {
//                        os = new FileOutputStream(file);
//                        os.write(data);
//                        os.close();
//
//                        PictureDialogFragment.newInstance(file.getAbsolutePath()).show(getSupportFragmentManager(), FRAGMENT_PICTURE);
//                    } catch (IOException e) {
//                        Log.w(TAG, "Cannot write to " + file, e);
//                    } finally {
//                        if (os != null) {
//                            try {
//                                os.close();
//                            } catch (IOException e) {
//                                // Ignore
//                            }
//                        }
//                    }
                }
            });
        }

    };
}
