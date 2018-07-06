package com.alphonso.thingword.zhibao.View;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.Toast;

import com.alphonso.thingword.zhibao.HttpClient;
import com.alphonso.thingword.zhibao.R;
import com.google.gson.Gson;
import com.litesuits.http.exception.HttpException;
import com.litesuits.http.listener.HttpListener;
import com.litesuits.http.response.Response;

public class RecogActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recog);

        toolbar = findViewById(R.id.recog_toolbar);
        toolbar.setTitle("识别结果");
        setSupportActionBar(toolbar);





        ImageView img= (ImageView) findViewById(R.id.image_View);
        ImageView img2= (ImageView) findViewById(R.id.image_View2);
        Intent intent=getIntent();
        if(intent !=null)
        {
            String path = intent.getStringExtra("bitmap1");
            Bitmap bitmap= BitmapFactory.decodeFile(path);
            img.setImageBitmap(bitmap);


            path = intent.getStringExtra("bitmap2");
            bitmap= BitmapFactory.decodeFile(path);
            img2.setImageBitmap(bitmap);


            HttpListener listener = new HttpListener<String>() {
                @Override
                public void onSuccess(String s, Response<String> response) {
                    Log.e("test","success "+s);
                }

                @Override
                public void onFailure(HttpException e, Response<String> response) {

                }
            };
            HttpClient.getInstance().checkLogin(listener,path);

        }


    }
}
