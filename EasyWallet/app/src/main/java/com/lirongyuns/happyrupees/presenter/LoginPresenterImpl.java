package com.lirongyuns.happyrupees.presenter;

import com.google.gson.Gson;
import com.lirongyuns.happyrupees.BuildConfig;
import com.lirongyuns.happyrupees.bean.HttpBean;
import com.lirongyuns.happyrupees.bean.LoginJson;
import com.lirongyuns.happyrupees.internet.JsonCreator;
import com.lirongyuns.happyrupees.internet.RetrofitCreator;
import com.lirongyuns.happyrupees.requester.LoginRequester;
import com.lirongyuns.happyrupees.view.BaseView;
import com.lirongyuns.happyrupees.view.LoginView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class LoginPresenterImpl implements LoginPresenter {
    private LoginView view;

    private LoginRequester mRequester;
    private HashMap<String, Integer> mChannel = new HashMap<>();
    

    public LoginPresenterImpl(LoginView view) {
        this.view = view;
        mRequester = RetrofitCreator.create(LoginRequester.class);
        mChannel.put("lry_1", 1);
        mChannel.put("lry_2", 2);
        mChannel.put("lry_3", 3);
        mChannel.put("lry_4", 4);
        mChannel.put("lry_5", 5);
        mChannel.put("lry_6", 6);
        mChannel.put("lry_7", 7);
        mChannel.put("lry_8", 8);
        mChannel.put("lry_9", 9);
        mChannel.put("lry_10", 10);
        mChannel.put("lry_11", 11);
        mChannel.put("lry_12", 12);
        mChannel.put("lry_13", 13);
        mChannel.put("lry_14", 14);
        mChannel.put("lry_15", 15);
    }

    @Override
    public void login(String phoneNumber, String smsCode) {
      
        JsonCreator creator = new JsonCreator();
        String channelName = BuildConfig.FLAVOR;
        if(phoneNumber.startsWith("91")){
            creator.put("phone", phoneNumber);
        }else{
            creator.put("phone", "91"+phoneNumber);
        }
        creator.put("IDcode", smsCode);
        creator.put("channel", mChannel.get(channelName));
        
        mRequester.login(RetrofitCreator.getFormBody(creator)).enqueue(new RetrofitCreator.JsonCallback() {
            @Override
            public void onSuccess(JSONObject jsonObject){
                Gson gson = new Gson();
                LoginJson loginJson = gson.fromJson(jsonObject.toString(), LoginJson.class);
                if (null == loginJson) {
                    return;
                }
                int code = loginJson.getCode();
                LoginJson.DataBean data = loginJson.getData();
                if(200 == code && null != data){
//                    view.loginSuccess(data,"Login success.");
                    view.loginResult(loginJson.getMsg());
                    view.loginSuccess(data);
                }else{
                    view.loginResult(loginJson.getMsg());
                }
            
            }

            @Override
            public void onFailure(int code, String msg) {
                view.loginResult(msg);
            }
        });
    }

    @Override
    public void getLoginCode(String phoneNumber) {
        JsonCreator creator = new JsonCreator();
        if(phoneNumber.startsWith("91")){
            creator.put("phone", phoneNumber);
        }else{
            creator.put("phone", "91"+phoneNumber);
        }
        
        mRequester.getLoginCode(RetrofitCreator.getFormBody(creator)).enqueue(new RetrofitCreator.JsonCallback() {
            @Override
            public void onSuccess(JSONObject jsonObject){
                Gson gson = new Gson();
                HttpBean httpBean = gson.fromJson(jsonObject.toString(), HttpBean.class);
                if (null == httpBean) {
                    return;
                }
                int code = httpBean.getCode();
                if (200 != code) {
                    view.getSMSCodeResult(httpBean.getMsg());
                }else{
                    view.getSMSCodeResult(httpBean.getMsg());
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                view.getSMSCodeResult(msg);
            }
        });
    }
}
