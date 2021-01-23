package com.lirongyuns.happyrupees.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lirongyuns.happyrupees.R;
import com.lirongyuns.happyrupees.annonation.Click;
import com.lirongyuns.happyrupees.annonation.SetActivity;
import com.lirongyuns.happyrupees.annonation.SetView;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

@SetActivity(R.layout.activity_authorization)
public class AuthorizationActivity extends BaseActivity {

    @SetView(R.id.permission_not_allowed_tip)
    private TextView tip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplyMultiplePermissions();
        Intent intent = getIntent();
        String msg = intent.getStringExtra("DialogMessage");
        tip.setText(msg);
    }

    @Click(R.id.authorization_ok)
    private void click(View v){
        switch (v.getId()){
            case R.id.authorization_ok:
                finish();
                break;
        }
    }

    @SuppressLint("CheckResult")
    private void ApplyMultiplePermissions(){
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.requestEach(
                Manifest.permission.INTERNET,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_SMS,
//                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.READ_PHONE_STATE)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted ) {
                            // 用户已经同意该权限
//                            Log.d(TAG, permission.name + " is granted.");


                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时。还会提示请求权限的对话框
                            String name = permission.name;
//                            Log.e(name, permission.granted+"");

                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                //申请被拒绝的权限
//                                applyAgain(name);
//                                ToastUtil.showShortToast(getBaseContext(), "The required permissions were not obtained, please restart");
                            }
//                            Log.e("权限1", permission.name +"====="+ permission.granted);
                        } else {
//                            Log.e("权限2", permission.name +"====="+ permission.granted);
                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                //申请被拒绝的权限
//                                applyAgain(name);
//                                ToastUtil.showShortToast(getBaseContext(), "The required permissions were not obtained, please restart");
                            }
                        }
                    }
                });

    }
}