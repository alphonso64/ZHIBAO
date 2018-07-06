package com.alphonso.thingword.zhibao.View;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.alphonso.thingword.zhibao.Fragment.ConfirmationDialogFragment;
import com.alphonso.thingword.zhibao.util.DisplayUtil;
import com.alphonso.thingword.zhibao.util.PhotoUtils;
import com.alphonso.thingword.zhibao.R;
import com.google.android.cameraview.AspectRatio;
import com.google.android.cameraview.CameraView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CameraActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{

    private CameraView mCameraView;
    private Handler mBackgroundHandler;

    private static final String TAG = "CameraActivity";

    private static final int REQUEST_CAMERA_PERMISSION = 1;

    private static final String FRAGMENT_PERMISSION = "permission";
    private static final String FRAGMENT_PICTURE = "picture";
    private static final int CODE_GALLERY_REQUEST = 0xa0;

    Toolbar toolbar;

    private int mCurrentFlash;

    private int leftRight;
    private int topBottom;
    private float ratio;
    private float cameraRatio;
    private Rect screenCenterRect;

    private static final int[] FLASH_OPTIONS = {
            CameraView.FLASH_AUTO,
            CameraView.FLASH_OFF,
            CameraView.FLASH_ON,
    };

    private static final int[] FLASH_ICONS = {
            R.drawable.ic_flash_auto,
            R.drawable.ic_flash_off,
            R.drawable.ic_flash_on,
    };

    private static final int[] FLASH_TITLES = {
            R.string.flash_auto,
            R.string.flash_off,
            R.string.flash_on,
    };

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.take_picture:
                    if (mCameraView != null) {
                        mCameraView.takePicture();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);



        mCameraView = (CameraView) findViewById(R.id.camera);
        if (mCameraView != null) {
            mCameraView.addCallback(mCallback);
        }

        SharedPreferences sharedPreferences = getSharedPreferences(this.getPackageName(), this.MODE_PRIVATE);
        String type = sharedPreferences.getString("type", "");
        String detail = sharedPreferences.getString("detail", "");
//        Log.e("camera",type+" "+detail);
//        Log.e("camera","camera");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.take_picture);
        if (fab != null) {
            fab.setOnClickListener(mOnClickListener);
        }

        toolbar = findViewById(R.id.camera_toolbar);
        toolbar.setTitle(type+"—"+detail);
        setSupportActionBar(toolbar);

        ratio = 1.0f;
        leftRight = 300;
        topBottom = 300;

        MaskView viewMask = (MaskView) findViewById(R.id.view_mask);


        int RECT_HEIGHT,RECT_WIDTH;
        AspectRatio currentRatio = mCameraView.getAspectRatio();
        cameraRatio = currentRatio.toFloat();
        int mCameraWidth = (int) DisplayUtil.getScreenWidth(this);
        int mCameraHeight = (int) (mCameraWidth * cameraRatio);

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.width = mCameraWidth;
        layoutParams.height = mCameraHeight;
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        viewMask.setLayoutParams(layoutParams);

        if (ratio > cameraRatio) {
            //如果传过来的ratio比屏幕的高宽比大，那么需要以屏幕高为标准
            RECT_HEIGHT = mCameraHeight - topBottom; //以宽为准，到CameraView上下保留一定的间距
            RECT_WIDTH = (int) (RECT_HEIGHT / ratio);
        } else {
            RECT_WIDTH = mCameraWidth - leftRight; //以宽为准，到CameraView两边保留一定的间距
            RECT_HEIGHT = (int) (RECT_WIDTH * ratio);
        }
        if (viewMask != null) {
            screenCenterRect = DisplayUtil.createCenterScreenRect(mCameraWidth, mCameraHeight, RECT_WIDTH, RECT_HEIGHT);
            viewMask.setCenterRect(screenCenterRect);
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
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.camera, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.switch_flash:
                if (mCameraView != null) {
                    mCurrentFlash = (mCurrentFlash + 1) % FLASH_OPTIONS.length;
                    item.setTitle(FLASH_TITLES[mCurrentFlash]);
                    item.setIcon(FLASH_ICONS[mCurrentFlash]);
                    mCameraView.setFlash(FLASH_OPTIONS[mCurrentFlash]);
                }
                return true;
            case R.id.switch_camera:
//                if (mCameraView != null) {
//                    int facing = mCameraView.getFacing();
//                    mCameraView.setFacing(facing == CameraView.FACING_FRONT ? CameraView.FACING_BACK : CameraView.FACING_FRONT);
//                }
                PhotoUtils.openPic(CameraActivity.this, CODE_GALLERY_REQUEST);
                return true;
        }
        return super.onOptionsItemSelected(item);
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
        public void onPictureTaken(final CameraView cameraView, final byte[] data) {
            Log.d(TAG, "onPictureTaken " + data.length);
            getBackgroundHandler().post(new Runnable() {
                @Override
                public void run() {
                    Log.e("aaaa","take picture "+data.length+" ");

                    Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                    boolean flag = true;
                    File file1,file2;
                    {
                        Bitmap rectBitmap = Bitmap.createBitmap(bitmap, 0, screenCenterRect.top, bitmap.getWidth(), screenCenterRect.height());
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        rectBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] datas = baos.toByteArray();

                        long time = System.currentTimeMillis();
                        file1 = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), String.format("picture1_%s.jpg", String.valueOf(time)));
                        OutputStream os = null;
                        try {
                            os = new FileOutputStream(file1);
                            os.write(datas);
                            os.close();

                        } catch (IOException e) {
                            Log.w(TAG, "Cannot write to " + file1, e);
                        } finally {
                            if (os != null) {
                                try {
                                    os.close();
                                } catch (IOException e) {
                                    flag = false;
                                }
                            }
                        }
                    }

                    {
                        Bitmap rectBitmap = Bitmap.createBitmap(bitmap, screenCenterRect.left, screenCenterRect.top, screenCenterRect.width(), screenCenterRect.height());
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        rectBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] datas = baos.toByteArray();

                        long time = System.currentTimeMillis();
                        file2 = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), String.format("picture2_%s.jpg", String.valueOf(time)));
                        OutputStream os = null;
                        try {
                            os = new FileOutputStream(file2);
                            os.write(datas);
                            os.close();

                        } catch (IOException e) {
                            Log.w(TAG, "Cannot write to " + file2, e);
                        } finally {
                            if (os != null) {
                                try {
                                    os.close();
                                } catch (IOException e) {
                                    flag = false;
                                }
                            }
                        }

                    }






                    Intent intent=new Intent(CameraActivity.this,RecogActivity.class);
                    intent.putExtra("bitmap1", file1.getAbsolutePath());
                    intent.putExtra("bitmap2", file2.getAbsolutePath());
                    startActivity(intent);


                }
            });
        }

    };
}
