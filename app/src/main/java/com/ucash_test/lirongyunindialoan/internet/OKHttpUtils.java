package com.ucash_test.lirongyunindialoan.internet;

import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.util.SparseArray;

import com.dfsdk.ocr.scan.DFOCRScan;
import com.ucash_test.lirongyunindialoan.BuildConfig;
import com.ucash_test.lirongyunindialoan.R;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OKHttpUtils {
//    private static boolean openSSL= true;
//    public static void httpGet(@NotNull String url, JsonParser parser){
//        OkHttpClient okHttpClient=new OkHttpClient();
//        final Request request=new Request.Builder()
//                .url(url)
//                .build();
//        final Call call = okHttpClient.newCall(request);
//        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
//        Runnable task = new Runnable(){
//            public void run(){
//                try {
//                    String html = null;
//                    Response response = call.execute();
//                    if(BuildConfig.IS_TEST){
//                        Log.e("request_headers",response.message().toString()+"sss");
//
//                    }
//                    html = response.body().string();
//                    if (html != null)
//                        Log.e("同步结果----   ",html+"");
//
//                } catch (IOException e) {
//
//                    e.printStackTrace();
//
//                }
//            }
//        };
//        fixedThreadPool.execute(task);
//        fixedThreadPool.shutdown();
//    }

//    public static void httpPostwithFiles(@NotNull String url, MultipartBody.Part part,MultipartBody.Part body,Callback callback){
////        RequestBody requestBody = getFormBody(parser);
//        OkHttpClient okHttpClient=new OkHttpClient();
////        final Request request=new Request.Builder()
////                .url(url)
////                .post(requestBody)
////                .build();
////        okHttpClient.newCall(request).enqueue(callback);
//    }

//    public static void httpPost(@NotNull String url, String body,Callback callback) {
//        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//        RequestBody requestBody = RequestBody.create(JSON, body);
//        OkHttpClient okHttpClient = new OkHttpClient();
//        final Request request = new Request.Builder()
//                .url(url)
//                .post(requestBody)
//                .build();
//        okHttpClient.newCall(request).enqueue(callback);
//    }

    public static void httpPost(@NotNull String url, JsonParser parser,Callback callback){
//        RequestBody requestBody = getFormBody(parser);
        FormBody requestBody = getFormBody(parser);
        OkHttpClient okHttpClient = null;
//        if(openSSL){
//            okHttpClient = CustomTrust.getInstance().getClient();
//        }
//        else{
            okHttpClient=new OkHttpClient();
//        }
        final Request request=new Request.Builder()
                .url(url)
//                .addHeader("Content-Type","application/json")
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);

    }


    public static FormBody getFormBody(JsonParser creator) {
        String jsonString = creator.toString();
        if(BuildConfig.IS_TEST) {
            if (creator.size() > 0) {
                Log.e("jsondata", JsonFormat.formatJson(jsonString));
            }
        }

        String encryptString = "";
        //分段加密
        try {
            byte[] bytes = RSA.encryptByPublicKey(jsonString.getBytes(), RSA.getPUBLICKEY());
            encryptString = Base64.encodeToString(bytes, Base64.DEFAULT);
//            Log.e("kmsg",encryptString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new FormBody.Builder().add("data", encryptString).build();
    }

}
