package com.ucash_test.lirongyunindialoan.myosotisutils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionRequestUtils {
    public static void checkAndRequestPermission(Activity activity, String[] permissions){
        if(activity == null){
            Log.i("error","传入的activity对象为空");
        }
        else{
            for(String permission : permissions){
                if(ContextCompat.checkSelfPermission(activity.getApplicationContext(),permission)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(activity, new String[]{permission},2);
                }
            }
        }
    }
}
