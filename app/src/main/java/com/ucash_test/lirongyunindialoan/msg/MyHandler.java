package com.ucash_test.lirongyunindialoan.msg;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

public class MyHandler {
    private Handler handler;
    private MyHandler(){

    }
    private static class Holder{
        final static MyHandler HANDLER = new MyHandler();
    }
    public static Handler get(){
        return Holder.HANDLER.buildHandler();
    }

    private Handler buildHandler(){
        if(handler == null){
            handler = new Handler(Looper.getMainLooper()){
                @Override
                public void handleMessage(@NonNull Message msg) {
                    

                }
            };
        }
        return handler;
    }
}
