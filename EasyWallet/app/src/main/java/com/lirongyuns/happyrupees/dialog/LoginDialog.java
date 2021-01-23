package com.lirongyuns.happyrupees.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lirongyuns.happyrupees.R;
import com.lirongyuns.happyrupees.activity.LoginActivity;
import com.lirongyuns.happyrupees.conf.EnviromentConf;
import com.lirongyuns.happyrupees.presenter.LoginPresenter;
import com.lirongyuns.happyrupees.presenter.LoginPresenterImpl;
import com.lirongyuns.happyrupees.utils.ToastUtil;

public class LoginDialog extends Dialog {
    private Button submit;

    private EditText login_code_et;

    private LoginActivity activity;
    private LoginPresenter mPresenter;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_dialog);
        //按空白处取消动画
        setCanceledOnTouchOutside(true);
        //初始化界面控件
        initView();
        //初始化界面控件的事件
        initEvent();

    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        submit = findViewById(R.id.login_submit);
        login_code_et = findViewById(R.id.login_code_et);
        mPresenter = new LoginPresenterImpl(this.activity);
        if(!"".equals(phone) && null != phone){
            if(!EnviromentConf.isOnlyPage)
                mPresenter.getLoginCode(phone);
        }
        else{
            ToastUtil.show(activity,"The phone number should not be null.");
            cancel();
        }
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(EnviromentConf.isOnlyPage) {
                    ToastUtil.show(activity,login_code_et.getText().toString());
                }
                if(login_code_et.length() < 6){
                    ToastUtil.show(LoginDialog.this.activity,"The length of OTP is less than 6.");
                }
                else{
                    if(!EnviromentConf.isOnlyPage){
                        mPresenter.login(phone,login_code_et.getText().toString());
                        cancel();
                    }
                    else{
                        activity.loginSuccess(null);
                    }
                }
            }
        });
    }

    public LoginDialog(@NonNull LoginActivity activity,String phone) {
        super(activity);
        this.activity = activity;
    }


    public static void show(LoginActivity activity,String phone){
        LoginDialog loginDialog = new LoginDialog(activity,phone);
        if(EnviromentConf.isOnlyPage) {
            ToastUtil.show(activity, phone);
        }
        loginDialog.setPhone(phone);
        loginDialog.show();
    }

}
