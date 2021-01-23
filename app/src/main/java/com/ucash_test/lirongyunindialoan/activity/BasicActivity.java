package com.ucash_test.lirongyunindialoan.activity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ucash_test.lirongyunindialoan.App;
import com.ucash_test.lirongyunindialoan.myosotisutils.AnnotationUtils;

import java.util.HashMap;
import java.util.Map;

public abstract class BasicActivity extends AppCompatActivity {
    @CallSuper
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

        /* 隐藏标题栏 */
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();

        }
        /* 隐藏标题栏 */

        AnnotationUtils.inject(this);//引入注解


    }

    /**
     * 权限授权监听
     * @author prayer 2020/11/18
     */
    public interface PermissionGrantListener
    {
        void onGrantResult(boolean isGrant);
    }

    private Map<Integer, PermissionGrantListener> mPermissionlistenrs;

    private int mRequestCode;

    private boolean versionCheck(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * 检查权限
     * @param permissionGrantListener
     */
    public void checkPermission(String permission, PermissionGrantListener permissionGrantListener)
    {
        mRequestCode ++ ;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (mPermissionlistenrs==null)
                mPermissionlistenrs = new HashMap<>();

            mPermissionlistenrs.put(mRequestCode, permissionGrantListener);
            boolean isGrant = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
            if (isGrant&&permissionGrantListener!=null) {
                if(isGrant){
                    permissionGrantListener.onGrantResult(isGrant);
                }
            }
            else {
                ActivityCompat.requestPermissions(this, new String[]{permission}, mRequestCode);
            }
        }
        else
        {
            if (permissionGrantListener!=null)
                permissionGrantListener.onGrantResult(true);
        }
    }

    public Context getCtx() {
        return getApplicationContext();
    }

    public App getApp(){
        return (App) getApplication();
    }

    private int mGroupRequestCode = 998;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(versionCheck())
        {
            if (mPermissionlistenrs!=null&&mPermissionlistenrs.size()>0)
            {
                if (mPermissionlistenrs.containsKey(requestCode)) {
                    PermissionGrantListener listener = mPermissionlistenrs.get(requestCode);
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

}
