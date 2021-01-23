package com.lirongyuns.happyrupees.internet;

import com.google.gson.Gson;
import com.lirongyuns.happyrupees.bean.HttpBean;
import com.lirongyuns.happyrupees.bean.HttpJsonBean;

import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.observers.DisposableObserver;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * 网络json数据解析Observable
 * 用于rxjava的异步请求
 * @param <T>
 */
public abstract class HttpObservable<T extends HttpBean> implements ObservableOnSubscribe<T> {

    @Override
    public void subscribe(ObservableEmitter<T> emitter) throws Exception {
        Response<ResponseBody> response = onRequest();
        if (response.isSuccessful()) {
            String body = response.body().string();
            T t = new Gson().fromJson(body, getType());
            response.body().close();

            if (t instanceof HttpJsonBean) {
                ((HttpJsonBean) t).setJSONObject(new JSONObject(body));
            }
            emitter.onNext(t);
        } else {
            emitter.onNext(null);
        }
    }

    /**
     * 泛型类型
     */
    Type getType()
    {
        return ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * 请求获得响应数据
     * 运行在子线程
     * @return
     */
    public abstract Response<ResponseBody> onRequest() throws Exception;

    /**
     * 成功返回实体数据
     * 运行在主线程
     * @param data
     * @param msg
     */
    public abstract void onSuccess(T data, String msg);

    /**
     * 失败返回响应码,与错误信息
     * 运行在主线程
     * @param code
     * @param msg
     */
    public void onFailure(int code, String msg) { }

    /**
     * 获得Observer
     * @return
     */
    public Observer<T> getObserver() {
        return new DisposableObserver<T>() {

            @Override
            public void onNext(T bean) {
                int code = bean.getCode();
                String msg = bean.getMsg();
                if (code == RetrofitCreator.SUCCESS) {
                    onSuccess(bean, msg);
                } else {
                    onFailure(code, msg);
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (e instanceof NullPointerException) {
                    onFailure(RetrofitCreator.ERROR, "Server error");
                } else {
                    onFailure(RetrofitCreator.ERROR, "Data error");
                }
            }

            @Override
            public void onComplete() { }
        };
    }

    /**
     * 不带有数据的回调
     */
    public static abstract class Msg extends HttpObservable<HttpBean> {

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
}
