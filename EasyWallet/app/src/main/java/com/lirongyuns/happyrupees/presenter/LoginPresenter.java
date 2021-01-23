package com.lirongyuns.happyrupees.presenter;


public interface LoginPresenter {


    void login(String phoneNumber, String smsCode);

    void getLoginCode(String phoneNumber);

//    void register(String phoneNumber, String smsCode);
//
//    void getRegisterCode(String phoneNumber);

}
