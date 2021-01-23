package com.lirongyuns.happyrupees.view;

import com.lirongyuns.happyrupees.bean.LoginJson;

public interface LoginView extends BaseView{
    void loginSuccess(LoginJson.DataBean data);
    void loginResult(String msg);
    void getSMSCodeResult(String msg);
    void failed();
}
