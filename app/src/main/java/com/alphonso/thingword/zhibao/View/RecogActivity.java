package com.alphonso.thingword.zhibao.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.alphonso.thingword.zhibao.HttpClient;
import com.alphonso.thingword.zhibao.R;
import com.alphonso.thingword.zhibao.util.PlantType;
import com.google.gson.Gson;
import com.litesuits.http.exception.HttpException;
import com.litesuits.http.listener.HttpListener;
import com.litesuits.http.response.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class RecogActivity extends AppCompatActivity {

    Toolbar toolbar;
    String [] images;
    ImageView img2;
    AssetManager am;
    List<String> resStr;
    int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recog);

        toolbar = findViewById(R.id.recog_toolbar);
        toolbar.setTitle("识别结果");
        setSupportActionBar(toolbar);


        am = getAssets();
        try {
            images = am.list("0");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Resources res =getResources();
        resStr  = Arrays.asList(res.getStringArray(R.array.结果));


        ImageView img= (ImageView) findViewById(R.id.image_View);
        img2= (ImageView) findViewById(R.id.image_View2);
        Intent intent=getIntent();
        if(intent !=null)
        {
            String path = intent.getStringExtra("bitmap1");
            Bitmap bitmap= BitmapFactory.decodeFile(path);
            img.setImageBitmap(bitmap);

//            path = intent.getStringExtra("bitmap2");
//            bitmap= BitmapFactory.decodeFile(path);
//            img2.setImageBitmap(bitmap);

            index = 0;
            InputStream assetfile = null;
            Log.e("aaa","path "+images[index]);
            try{
                //打开指定资源的输入流
                assetfile = am.open("0"+"/"+images[index]);
            }catch(IOException e){
                e.printStackTrace();
            }
            img2.setImageBitmap(BitmapFactory.decodeStream(assetfile));
            toolbar.setTitle(resStr.get(index));

            SharedPreferences sharedPreferences = getSharedPreferences(this.getPackageName(), this.MODE_PRIVATE);
            PlantType plant = new PlantType();
            plant.setType(sharedPreferences.getString("type", ""));
            plant.setDetail(sharedPreferences.getString("detail", ""));
            plant.setType_index(sharedPreferences.getInt("type_index",0));
            plant.setDetail_index(sharedPreferences.getInt("detail_index",0));

            HttpListener listener = new HttpListener<String>() {
                @Override
                public void onSuccess(String s, Response<String> response) {
                    Log.e("test","success "+s);
                }

                @Override
                public void onFailure(HttpException e, Response<String> response) {

                }
            };
            HttpClient.getInstance().checkLogin(listener,path,plant);

            Button btn2 =findViewById(R.id.button2);
            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(index>0)
                    {
                        index--;

                        InputStream assetfile = null;
                        Log.e("aaa","path "+images[index]);
                        try{
                            //打开指定资源的输入流
                            assetfile = am.open("0"+"/"+images[index]);
                            img2.setImageBitmap(BitmapFactory.decodeStream(assetfile));
                            toolbar.setTitle(resStr.get(index));
                        }catch(IOException e){
                            e.printStackTrace();
                        }


                    }
                }
            });

            Button btn =findViewById(R.id.button);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(index<images.length-1)
                    {
                        index++;
                        InputStream assetfile = null;
                        Log.e("aaa","path "+images[index]);
                        try{
                            //打开指定资源的输入流
                            assetfile = am.open("0"+"/"+images[index]);
                            img2.setImageBitmap(BitmapFactory.decodeStream(assetfile));
                            toolbar.setTitle(resStr.get(index));
                        }catch(IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            });


        }


    }
}
