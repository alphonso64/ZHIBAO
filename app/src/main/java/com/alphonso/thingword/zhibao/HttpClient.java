package com.alphonso.thingword.zhibao;

/**
 * Created by thingword-A on 2016/8/23.
 */

import android.util.Log;

import com.litesuits.http.LiteHttp;
import com.litesuits.http.impl.huc.HttpUrlClient;
import com.litesuits.http.listener.HttpListener;
import com.litesuits.http.request.FileRequest;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.content.FileBody;
import com.litesuits.http.request.content.JsonBody;
import com.litesuits.http.request.content.multi.FilePart;
import com.litesuits.http.request.content.multi.MultipartBody;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by alphonso on 2016/7/28.
 */
public class HttpClient {
    private LiteHttp liteHttp;
    private static HttpClient single = null;
    private static String DOMAIN_NAME = "http://192.168.1.7:8089/";

    //登陆判断
    public static final String LOGIN_URL =  "ZHIBAO/rest/hello/uploadStoreKeeperList";

    private HttpClient() {
        liteHttp = LiteHttp.build(null)
                .setHttpClient(new HttpUrlClient())      // http
                .setUserAgent("Mozilla/5.0 (...)")  // set custom User-Agent
                .setSocketTimeout(3000)           // socket timeout: 10s
                .setConnectTimeout(3000)// connect timeout: 10s
                .create();
    }


    public static HttpClient getInstance() {
        if (single == null) {
            single = new HttpClient();
        }
        return single;
    }

    public void checkLogin(HttpListener<String> listener,String path) {

//        JSONObject object = new JSONObject();
//        try {
//            object.put("name", "213213");
//        } catch (JSONException e) {
//            return;
//        }
        Log.e("aa",path);

        MultipartBody body = new MultipartBody();
        body.addPart(new FilePart("filepath", new File(path), "image/jpeg"));


        StringRequest stringRequest = new StringRequest(DOMAIN_NAME+LOGIN_URL)
                .setMethod(HttpMethods.Post).setHttpBody(body).setHttpListener(listener);


        liteHttp.executeAsync(stringRequest);
    }


}
