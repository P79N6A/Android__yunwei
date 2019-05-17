package com.zhongzhi.grid.GDactivity.activity.util;

import android.util.Log;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtil {
    public static void sendOkHttpRequest(String address, okhttp3.Callback callack) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callack);
    }

    private static OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(10000, TimeUnit.MILLISECONDS)
            .readTimeout(10000, TimeUnit.MILLISECONDS)
            .writeTimeout(10000, TimeUnit.MILLISECONDS).build();

  /*  public static void postFile(String url, final ProgressListener listener, okhttp3.Callback callback, File[] files){

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        Log.i("huang","files[0].getName()=="+files[0].getName());
        //第一个参数要与Servlet中的一致
        builder.addFormDataPart("file",files[0].getName(), RequestBody.create(MediaType.parse("application/octet-stream"),files[0]));

        MultipartBody multipartBody = builder.build();

        Request request = new Request.Builder().url(url).post(new ProgressRequestBody(multipartBody, listener)).build();
        okHttpClient.newCall(request).enqueue(callback);
    }*/

    public static void postFile2(String url, okhttp3.Callback callback, File[] files){

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        Log.i("huang","files[0].getName()=="+files[0].getName());
        //第一个参数要与Servlet中的一致
        builder.addFormDataPart("file",files[0].getName(), RequestBody.create(MediaType.parse("application/octet-stream"),files[0]));

        MultipartBody multipartBody = builder.build();

        Request request = new Request.Builder().url(url).post(multipartBody).build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    /*public static void postFile3(String url, final ProgressListener listener, okhttp3.Callback callback, File[] files, Map<String, String> map){

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        Log.i("huang","files[0].getName()=="+files[0].getName());
        //第一个参数要与Servlet中的一致
        builder.addFormDataPart("file",files[0].getName(), RequestBody.create(MediaType.parse("application/octet-stream"),files[0]));

        if (map != null) {
            for (String key : map.keySet()) {
                builder.addFormDataPart(key, map.get(key));
            }
        }

        MultipartBody multipartBody = builder.build();

        Request request = new Request.Builder().url(url).post(new ProgressRequestBody(multipartBody, listener)).build();
        okHttpClient.newCall(request).enqueue(callback);
    }*/
}
