package com.lirongyuns.happyrupees.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.lirongyuns.happyrupees.BuildConfig;
import com.lirongyuns.happyrupees.R;
import com.lirongyuns.happyrupees.annonation.Click;
import com.lirongyuns.happyrupees.annonation.SetActivity;
import com.lirongyuns.happyrupees.annonation.SetView;
import com.lirongyuns.happyrupees.bean.LoginJson;
import com.lirongyuns.happyrupees.conf.EnviromentConf;
import com.lirongyuns.happyrupees.conf.NetWorkConf;
import com.lirongyuns.happyrupees.dialog.LoginDialog;
import com.lirongyuns.happyrupees.utils.ToastUtil;
import com.lirongyuns.happyrupees.view.LoginView;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

@SetActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity implements LoginView {
    private String TAG = "login_activity";

    @SetView(R.id.login_et)
    private EditText et;

    @SetView(R.id.login_cb)
    private CheckBox cb;

    @SetView(R.id.login_btn)
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        ApplyMultiplePermissions();
    }

    @Click({R.id.login_btn,R.id.login_protocol})
    private void click(View view){
        int id = view.getId();
        switch (id){
            case R.id.login_btn:
                /* 登录点击事件 */
                if(BuildConfig.LOG_DEBUG) {
                    Log.i(TAG, "login_btn click!");
                }
                if(cb.isChecked()){
                    if(et.length() == 10){
//                        ToastUtil.show(this,"We have send message to your mobile phone.");

                        LoginDialog.show(this,et.getText().toString());
                    }
                    else{
                        ToastUtil.show(this,"The length of your mobile number is less than 10.");
                    }
                }
                else{
                    ToastUtil.show(this,"If you allow the Terms of Source Privacy Policy.");
                }
                break;

        }
    }

    public void login_protocol(View view){
        Intent intent = new Intent(this, ProtocolDescActivity.class);
        String url = NetWorkConf.getInstance().getHttpHost() +"app/agreement/privacy_policy";
        intent.putExtra("jumpUrl", url);
        startActivity(intent);
        if(BuildConfig.LOG_DEBUG) {
            Log.i(TAG, "login_protocol click!");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void loginSuccess(LoginJson.DataBean data) {
        if(EnviromentConf.isOnlyPage){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        LoginJson.DataBean.LoanMemberBean loanMember = data.getLoanMember();
        if(null != loanMember){
            getApp().setPhoneNum(loanMember.getMobile());
            getApp().setLoginStatus(true);
            getApp().setMemberId(loanMember.getId()+"");
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void loginResult(String msg) {
        ToastUtil.show(this,msg);
    }

    @Override
    public void getSMSCodeResult(String msg) {
        ToastUtil.show(this,msg);
    }

    @Override
    public void failed() {

    }

    @SuppressLint("CheckResult")
    private void ApplyMultiplePermissions(){
        RxPermissions rxPermissions = new RxPermissions(LoginActivity.this);
        rxPermissions.requestEach(
                Manifest.permission.INTERNET,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_SMS,
                Manifest.permission.CALL_PHONE,
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
                            Log.e("kmsg", "name==="+name+"permission===="+permission.granted);
                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                //申请被拒绝的权限
//                                applyAgain(name);
//                                ToastUtil.showShortToast(getBaseContext(), "The required permissions were not obtained, please restart");
//                                mHandler.sendEmptyMessageDelayed(303, 2000);
                                if(Manifest.permission.READ_EXTERNAL_STORAGE.equals(permission.name) || Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permission.name)){
                                    //READ_EXTERNAL_STORAGE
                                    startHintPage("Read or Write External Storage");
                                }else if(Manifest.permission.CAMERA.equals(permission.name)){
                                    //Camera
//                                    startHintPage("Camera");
                                }else if(Manifest.permission.READ_CONTACTS.equals(permission.name)){
                                    //READ_CONTACTS
                                    startHintPage("Read Contacts");
                                }else if(Manifest.permission.READ_SMS.equals(permission.name)){
                                    //READ_SMS
                                    startHintPage("Read SMS");
                                }else if(Manifest.permission.READ_PHONE_STATE.equals(permission.name)){
                                    //READ_PHONE_STATE
                                    startHintPage("Read Phone State");
                                }else{
                                    startHintPage("Internet");
                                }

                            }
                        } else {
                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                //申请被拒绝的权限
                                if(Manifest.permission.READ_EXTERNAL_STORAGE.equals(permission.name) || Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permission.name)){
                                    //READ_EXTERNAL_STORAGE
                                    startHintPage("Read or Write External Storage");
                                }else if(Manifest.permission.CAMERA.equals(permission.name)){
                                    //CAMERA
//                                    startHintPage("Camera");
                                }else if(Manifest.permission.READ_CONTACTS.equals(permission.name)){
                                    //READ_CONTACTS
                                    startHintPage("Read Contacts");
                                }else if(Manifest.permission.READ_SMS.equals(permission.name)){
                                    //READ_SMS
                                    startHintPage("Read SMS");
                                }else if(Manifest.permission.READ_PHONE_STATE.equals(permission.name)){
                                    //READ_PHONE_STATE
                                    startHintPage("Read Phone State");
                                }else{
                                    startHintPage("Internet");
                                }
                            }
                        }
                    }
                });

    }

    private void startHintPage(String name) {
        Intent intent = new Intent(LoginActivity.this, AuthorizationActivity.class);
        intent.putExtra("DialogMessage", "No access to " + name +". Please check if permissions are enabled");
        startActivity(intent);
    }
}