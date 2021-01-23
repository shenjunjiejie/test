package com.lirongyuns.happyrupees.activity;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.appsflyer.AppsFlyerLib;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.lirongyuns.happyrupees.BuildConfig;
import com.lirongyuns.happyrupees.MyApp;
import com.lirongyuns.happyrupees.utils.AnnotationUtils;

import java.util.HashMap;
import java.util.Map;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* 隐藏状态栏 */
        Window window = getWindow();
        if (versionCheck()) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            int option = window.getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            window.getDecorView().setSystemUiVisibility(option);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        /* 隐藏状态栏 */

        AnnotationUtils.inject(this);//引入自定义注解

    }

    /* 版本检查 */
    private boolean versionCheck(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public void back(View view){
        finish();
    }

    public interface PermissionListener
    {
        void onGrantResult(boolean isGrant);
    }

    private Map<Integer, PermissionListener> mPermissionlistenrs;

    private int mGroupRequestCode = 998;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(versionCheck())
        {
            if (mPermissionlistenrs!=null&&mPermissionlistenrs.size()>0)
            {
                if (mPermissionlistenrs.containsKey(requestCode)) {
                    PermissionListener listener = mPermissionlistenrs.get(requestCode);
                    boolean isGrant;
                    if (requestCode==mGroupRequestCode)
                    {
                        isGrant = true;
                        for (int i=0; i<grantResults.length; i++)
                        {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED)
                            {
                                isGrant = false;
                                break;
                            }
                        }
                    }
                    else
                    {
                        isGrant = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    }
                    if (listener!=null) listener.onGrantResult(isGrant);
                    mPermissionlistenrs.remove(requestCode);
                }
            }
        }
    }

    public void appsflyerEvent(String phone, String event_name) {
        Map<String, Object> params = new HashMap<>();
        params.put("channel", BuildConfig.FLAVOR);
        params.put("phone", phone);
        AppsFlyerLib.getInstance().trackEvent(getApplicationContext(), event_name, params);
    }

    public void setFirebaseEvent(String extra, String event_name){
        if(null == extra){
            extra = "";
        }
        Bundle bundle = new Bundle();
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
        bundle.putString("channel", BuildConfig.FLAVOR);
        bundle.putString("phone", getApp().getPhoneNum());
        bundle.putString("extra", extra);
        firebaseAnalytics.logEvent(event_name, bundle);
    }

    public void setFaceBookEvent(Bundle bundle, String event_name){
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        if(null == bundle){
            bundle = new Bundle();
        }
        bundle.putString("channel", BuildConfig.FLAVOR);
        bundle.putString("phone", getApp().getPhoneNum());

        logger.logEvent(event_name , bundle);
    }

    public MyApp getApp() {
        return (MyApp) getApplication();
    }

    private Map<Integer, PermissionListener> permissionListenerMap;
    private int requestCode;
    private int groupRequestCode = 998;

    public void checkPermission(String permission, PermissionListener listener)
    {
        requestCode++;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
            if (permissionListenerMap==null)
                permissionListenerMap = new HashMap<>();

            permissionListenerMap.put(requestCode, listener);
            boolean isGrant = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
            if (isGrant&&listener!=null) {
                listener.onGrantResult(isGrant);
            }
            else {
                ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            }
        }
        else
        {
            if (listener!=null)
                listener.onGrantResult(true);
        }
    }

    public void checkPermissions(String[] permissions, PermissionListener listener)
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
            if (permissionListenerMap==null)
                permissionListenerMap = new HashMap<>();

            permissionListenerMap.put(groupRequestCode, listener);
            boolean isGrant = true;
            for (String per : permissions)
            {
                boolean is = ContextCompat.checkSelfPermission(this, per) == PackageManager.PERMISSION_GRANTED;
                if (!is) {
                    isGrant = is;
                    break;
                }
            }
            if (isGrant&&listener!=null) {
                listener.onGrantResult(isGrant);
            }
            else {
                ActivityCompat.requestPermissions(this, permissions, groupRequestCode);
            }
        }
        else
        {
            if (listener!=null)
                listener.onGrantResult(true);
        }
    }
}
