package com.ucash_test.lirongyunindialoan.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ucash_test.lirongyunindialoan.MainActivity;
import com.ucash_test.lirongyunindialoan.R;
import com.ucash_test.lirongyunindialoan.annonation.Click;
import com.ucash_test.lirongyunindialoan.annonation.SetActivity;
import com.ucash_test.lirongyunindialoan.myosotisutils.PermissionRequestUtils;
import com.ucash_test.lirongyunindialoan.myosotisutils.ShowTextUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SetActivity(R.layout.activity_login)
public class LoginActivity extends BasicActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
        Runnable task =()->{
            requestPermission();
        };
        fixedThreadPool.execute(task);
        fixedThreadPool.shutdown();
    }

    @Click(R.id.tv_login)
    private void click(){
        Message msg = mHandler.obtainMessage();
        msg.what = 0;
        mHandler.sendMessage(msg);
    }

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:

                    break;
                case 1:
                    startActivity(new Intent(getCtx(),MainActivity.class));
                    finish();
                    break;
                default:
                    ShowTextUtils.show(getCtx(),getClass()+" Unexpected value: " + msg.what);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler = null;
    }

    /*
    * 请求权限
    * */
    private void requestPermission(){
        PermissionRequestUtils.checkAndRequestPermission(this,new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_SMS,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_PHONE_STATE
        });
    }
}
