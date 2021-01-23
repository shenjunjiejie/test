package com.lirongyuns.happyrupees.presenter;

import com.google.gson.Gson;
import com.lirongyuns.happyrupees.bean.BankIfscJson;
import com.lirongyuns.happyrupees.bean.HttpBean;
import com.lirongyuns.happyrupees.bean.MyBankJson;
import com.lirongyuns.happyrupees.dialog.LoadingDialog;
import com.lirongyuns.happyrupees.internet.JsonCreator;
import com.lirongyuns.happyrupees.internet.RetrofitCreator;
import com.lirongyuns.happyrupees.requester.BindCardRequester;
import com.lirongyuns.happyrupees.view.BindCardView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class BCPresenterImpl implements BCPresenter {
    private BindCardView view;
    private BindCardRequester mRequester;
    public BCPresenterImpl(BindCardView view) {
        this.view = view;
        mRequester = RetrofitCreator.create(BindCardRequester.class);
    }

    public void defeatCoding(){
        //花指令
        BufferedReader br =null;
        try {
            br = new BufferedReader(new FileReader("fakeFile"));
            String line;
            while((line=br.readLine())!= null){
                String[] splited = line.split(" +");
                if(splited.length >= 0){
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                br.close();
            }
            catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void bindBankCardToServer(String beneAccno,String beneIfsc, String memberId) {
        JsonCreator jsonCreator = new JsonCreator();
        jsonCreator.put("beneAccno", beneAccno);
        jsonCreator.put("beneIfsc", beneIfsc);
        jsonCreator.put("memberId", memberId);
        mRequester.bindBankCardToServer(RetrofitCreator.getFormBody(jsonCreator)).enqueue(new RetrofitCreator.JsonCallback() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                LoadingDialog.close();
                Gson gson = new Gson();
                HttpBean httpBean = gson.fromJson(jsonObject.toString(), HttpBean.class);
                String msg = httpBean.getMsg();
                int code = httpBean.getCode();
               
                if (200 == code) {
                    view.startAuditReusltPage(msg);
                }else{
                    view.setBankCardButtonEnable();
                    view.showToast(msg);
                }
            }
    
            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                LoadingDialog.close();
                view.showToast(msg);
            }
        });
    }
    
    @Override
    public void getBankCardDataFromServer(String memberId) {
        JsonCreator jsonCreator = new JsonCreator();
        jsonCreator.put("memberId", memberId);
        mRequester.getBankCardDataFromServer(RetrofitCreator.getFormBody(jsonCreator)).enqueue(new RetrofitCreator.JsonCallback() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                LoadingDialog.close();
                Gson gson = new Gson();
                MyBankJson httpBean = gson.fromJson(jsonObject.toString(), MyBankJson.class);
                String msg = httpBean.getMsg();
                int code = httpBean.getCode();
                if (200 == code) {
                    MyBankJson.DataBean data = httpBean.getData();
                    if (null ==data) {
                        return;
                    }
                    view.setMyBankDisplay(data);
                }else{
                    view.showToast(msg);
                }
    
                
            }
    
            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                LoadingDialog.close();
                view.showToast(msg);
            }
        });
        
    }
    
    @Override
    public void getBankIFSCCodeFromServer(String state, String city, String bank_name, String bank_branch, String bank_ifsc) {
        JsonCreator jsonCreator = new JsonCreator();
        jsonCreator.put("state", state);
        jsonCreator.put("city", city);
        jsonCreator.put("bank_name", bank_name);
        jsonCreator.put("branch", bank_branch);
//        jsonCreator.put("ifsc", bank_ifsc);
        mRequester.getBankIfscCodeFromServer(RetrofitCreator.getFormBody(jsonCreator)).enqueue(new RetrofitCreator.JsonCallback() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                LoadingDialog.close();
                Gson gson = new Gson();
                BankIfscJson bankIfscJson = gson.fromJson(jsonObject.toString(), BankIfscJson.class);
                if (null == bankIfscJson) {
                    return;
                }
    
                if(200 == bankIfscJson.getCode()){
                    ArrayList<String> data = bankIfscJson.getData();
                    view.setBankIFSCData(data);
                }else{
                    view.showToast(bankIfscJson.getMsg());
                }
                
               
            }
    
    
            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                LoadingDialog.close();
                view.showToast(msg);
            }
        });
    }
    
    
}
