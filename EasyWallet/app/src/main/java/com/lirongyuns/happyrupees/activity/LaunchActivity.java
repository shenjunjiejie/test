package com.lirongyuns.happyrupees.activity;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Base64;
import android.util.Log;

import com.lirongyuns.happyrupees.R;
import com.lirongyuns.happyrupees.annonation.SetActivity;
import com.lirongyuns.happyrupees.utils.ToastUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@SetActivity(R.layout.activity_launch)
public class LaunchActivity extends BaseActivity {
    private ScheduledExecutorService mThreadPool;
    private AtomicInteger time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mThreadPool = Executors.newScheduledThreadPool(1);
        time = new AtomicInteger(1);
        mThreadPool.scheduleAtFixedRate(()->{
            int i = time.getAndDecrement();
            Message msg = mHandler.obtainMessage();
            if (time.get() == 0){
                msg.what = 1;
                mHandler.sendMessage(msg);
            }

        },1,1, TimeUnit.SECONDS);
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.lirongyuns.happyrupees",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mThreadPool.shutdown();
    }

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    startActivity(new Intent(LaunchActivity.this, PermissionDescActivity.class));
                    mThreadPool.shutdownNow();
                    finish();
                    break;
                default:
                    ToastUtil.show(LaunchActivity.this,getClass()+" Unexpected value: " + msg.what);
            }
        }
    };
}