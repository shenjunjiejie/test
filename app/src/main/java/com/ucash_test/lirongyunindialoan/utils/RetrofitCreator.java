package com.ucash_test.lirongyunindialoan.utils;

import android.graphics.Bitmap;
import android.util.Base64;

import com.google.gson.Gson;
import com.ucash_test.lirongyunindialoan.BuildConfig;
import com.ucash_test.lirongyunindialoan.bean.ContentBean;
import com.ucash_test.lirongyunindialoan.bean.HttpBean;
import com.ucash_test.lirongyunindialoan.bean.HttpJsonBean;
import com.ucash_test.lirongyunindialoan.config.NetWorkConf;
import com.ucash_test.lirongyunindialoan.internet.CustomTrust;
import com.ucash_test.lirongyunindialoan.internet.JsonParser;
import com.ucash_test.lirongyunindialoan.internet.RSA;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * 网络请求者
 * @author Krear 2018/8/15
 */
public class RetrofitCreator
{
//    public static final String HEAD = "http://192.168.3.17:8080";
//    public static final String BASE_URL = HEAD + "/yindu_xianjindai/";

    //    public static final String HEAD = "http://test.renbank.cn:10492";
//    public static final String HEAD = BuildConfig.HTTP_HOST;
    public static final String BASE_URL = NetWorkConf.getInstance().getHttpHost();

    public static final int SUCCESS = 200;
    public static final int ERROR = -1;

    private static Gson gson = new Gson();
//    private static Interceptor mInterceptor;

    /**
     * 数据回调对象
     * @param <T>
     */
    public static abstract class HttpCallback<T extends HttpBean> implements Callback<ResponseBody> {

        @Override
        public final void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            try {
//                Log.e("kmsg",response.toString());
                if (response.isSuccessful()) {
                    String body = response.body().string();
//                    LogUtil.e(JsonFormat.formatJson(body));
                    T t = gson.fromJson(body, getType());
                    response.body().close();

                    if (t instanceof HttpJsonBean) {
                        ((HttpJsonBean) t).setJSONObject(new JSONObject(body));
                    }
                    int code = t.getCode();
                    String msg = t.getMsg();
                    if (code == SUCCESS) {
                        onSuccess(t, msg);
                    } else {
                        onFailure(code, msg);
                    }
                } else {
//                    LogUtil.e(response.errorBody().string());
                    onFailure(ERROR, "Server error");
                }
            } catch (Exception e) {
                e.printStackTrace();

                //抛出异常打印日志，关闭进度框，弹出提示
//                LogUtil.e(e.getMessage());
                onFailure(ERROR, "Server error");
            }

        }

        @Override
        public final void onFailure(Call<ResponseBody> call, Throwable t) {
//            LogUtil.i(call.request().url().toString());
//            LogUtil.e(t.toString());
            onFailure(ERROR, t.toString());
        }

        /**
         * 成功返回实体数据
         * @param data
         * @param msg
         */
        public abstract void onSuccess(T data, String msg);

        /**
         * 失败返回响应码,与错误信息
         * @param code
         * @param msg
         */
        public void onFailure(int code, String msg) { }

        /**
         * 泛型类型
         * @return
         */
        Type getType() {
            return ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        }
    }

    /**
     * 不带有数据的回调
     */
    public static abstract class MsgCallback extends HttpCallback<HttpBean> {

        @Override
        public void onSuccess(HttpBean data, String msg) {
            onSuccess(msg);
        }

        /**
         *
         * @param msg 成功提示
         */
        public abstract void onSuccess(String msg);

        @Override
        Type getType() {
            return HttpBean.class;
        }
    }

    /**
     * 将数据装换为json的返回
     */
    public static abstract class JsonCallback extends HttpCallback<HttpJsonBean> {

        @Override
        public void onSuccess(HttpJsonBean data, String msg) {
            try {
                onSuccess(data.getJSONObject());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public abstract void onSuccess(JSONObject jsonObject) throws JSONException;

        @Override
        Type getType() {
            return HttpJsonBean.class;
        }
    }

    /**
     * Content数据回调对象
     * @param <T>
     */
    public static abstract class ContentCallback<T> extends HttpCallback<ContentBean<T>>
    {
        @Override
        public final void onSuccess(ContentBean<T> data, String msg) {
            onSuccess(data.getObj(), msg);
        }

        public abstract void onSuccess(T obj, String msg);

        @Override
        Type getType() {
            return new ParameterizedType() {
                @Override
                public Type[] getActualTypeArguments() {
                    return new Type[]{ContentCallback.super.getType()};
                }

                @Override
                public Type getRawType() {
                    return ContentBean.class;
                }

                @Override
                public Type getOwnerType() {
                    //比如 Map<String,Person> map 这个 ParameterizedType 的 getOwnerType() 为 null，
                    //而 Map.Entry<String, String> entry 的 getOwnerType() 为 Map 所属于的 Type。
                    return null;
                }
            };
        }
    }

    /**
     * 创建请求对象
     * @param tClass 接口class
     * @param <T> 接口对象
     * @return 接口对象
     */
    public static <T> T create(Class<T> tClass) {
        return create(BASE_URL, tClass);
    }


    /**
     * 创建请求对象
     * @param tClass 接口class
     * @param <T> 接口对象
     * @return 接口对象
     */
    public static <T> T create(Class<T> tClass, String base_url) {
        return create(base_url, tClass);
    }

    /**
     * 创建请求对象
     * @param baseUrl url头部
     * @param tClass 接口class
     * @param <T> 接口对象
     * @return 接口对象
     */
    public static <T> T create(String baseUrl, Class<T> tClass) {
//        OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
//
//        if (BuildConfig.DEBUG) {
//            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(message -> {
//                if (message.length()==0) return;
//                if (String.valueOf(message.charAt(0)).equals("{")&&String.valueOf(message.charAt(message.length()-1)).equals("}")) {
////                    LogUtil.e(JsonFormat.formatJson(message));
//                } else {
////                    LogUtil.e(message);
//                }
//            });
//            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//            httpBuilder.addInterceptor(httpLoggingInterceptor);
//        }
//        OkHttpClient client = httpBuilder
//                .connectTimeout(120000, TimeUnit.MILLISECONDS)
//                .readTimeout(120000, TimeUnit.MILLISECONDS) //设置超时
//                .writeTimeout(120000, TimeUnit.MILLISECONDS)
//                .sslSocketFactory(RxUtils.createSSLSocketFactory())
//                .hostnameVerifier(new RxUtils.TrustAllHostnameVerifier())
//                .build();

//        if (null != mInterceptor) {
//            builder.addInterceptor(mInterceptor);
//        }
        //        OkHttpClient client = builder.build();
        OkHttpClient client = CustomTrust.getInstance().getClient();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                //.addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(tClass);
    }


//    public static void setInterceptor(Interceptor interceptor){
//        mInterceptor = interceptor;
//    }



    /**
     * 转换为FormBody对象
     * @return 表单对象
     */
    public static FormBody getFormBody()
    {
        return getFormBody(new JsonParser());
    }

    /**
     * 转换为FormBody对象
     * @param creator JSON 数据创建者
     * @return 表单对象
     */
    public static FormBody getFormBody(JsonParser creator) {
        String jsonString = creator.toString();
//        Log.e("kmsg",jsonString);
        if (creator.size()>0) {
//            LogUtil.i(JsonFormat.formatJson(jsonString));
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


    /**
     * 转换为FormBody对象
     * @param creator JSON 数据创建者
     * @return 表单对象
     */
    public static String getDatas(JsonParser creator) {
        String jsonString = creator.toString();
//        Log.e("kmsg",jsonString);
        if (creator.size()>0) {
//            LogUtil.i(JsonFormat.formatJson(jsonString));
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

        return encryptString;
    }

    /**
     * 获取RequestBody
     * @param value 值
     * @return 请求体
     */
    public static RequestBody getTextBody(String value) {
        String encryptString = "";
        //分段加密
        try {
            byte[] bytes = RSA.encryptByPublicKey(value.getBytes(), RSA.getPUBLICKEY());
            encryptString = Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), encryptString);
    }

    /**
     * 获取图片（Bitmap）Body
     * @param value 值
     * @return 请求体
     */
    public static RequestBody getImageBody(Bitmap value) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        value.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return RequestBody.create(MediaType.parse("image/jpg"), baos.toByteArray());
    }


}
