package com.lirongyuns.happyrupees.presenter;

import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.lirongyuns.happyrupees.bean.BigDataConfig;
import com.lirongyuns.happyrupees.bean.HttpBean;
import com.lirongyuns.happyrupees.dialog.LoadingDialog;
import com.lirongyuns.happyrupees.internet.JsonCreator;
import com.lirongyuns.happyrupees.internet.RetrofitCreator;
import com.lirongyuns.happyrupees.requester.LoanRequester;
import com.lirongyuns.happyrupees.utils.ConstantUtils;
import com.lirongyuns.happyrupees.view.LoanView;

import org.json.JSONObject;

public class LoanPresenterImpl implements LoanPresenter {

    private LoanView view;
    LoanRequester mRequester;

    public LoanPresenterImpl(LoanView view) {
        this.view = view;
        mRequester = RetrofitCreator.create(LoanRequester.class);
    }

    @Override
    public void getConfig() {
        mRequester.getBigDataConfig().enqueue(new RetrofitCreator.ContentCallback<BigDataConfig>() {
            @Override
            public void onSuccess(BigDataConfig obj, String msg) {
                view.onGetConfig(obj);
            }
        });
    }
    
    @Override
    public void submitCredit7(String memberId) {
        JsonCreator creator = new JsonCreator();
        creator.put("srxd_memberId", memberId);

        mRequester.creditProduct7(RetrofitCreator.getFormBody(creator)).enqueue(new RetrofitCreator.MsgCallback() {
            @Override
            public void onSuccess(String msg) {
                view.showToast(msg);
                view.submitCreditSuccess();
            }
        });
    }

    @Override
    public void submitCredit14(String memberId) {
        JsonCreator creator = new JsonCreator();
        creator.put("srxd_memberId", memberId);

        mRequester.creditProduct14(RetrofitCreator.getFormBody(creator)).enqueue(new RetrofitCreator.MsgCallback() {
            @Override
            public void onSuccess(String msg) {
                view.showToast(msg);
                view.submitCreditSuccess();
            }
        });
    }
    
    @Override
    public void commitPersonalInfoToServer(long id, long memberId, String serialNumber, int imeiIsSame,
                                           String brandModel, int age, int sex, String applyTime,
                                           int salaryRange, int marryState, int jobPosition, int education,
                                           String phoneNumber1, String phoneNumber2, String contact1,
                                           String contact2, String email, String phoneTelNum, String language, int religion) {
        
        if(!TextUtils.isEmpty(ConstantUtils.PHONE_NUM) && !ConstantUtils.PHONE_NUM.startsWith("91")){
            ConstantUtils.PHONE_NUM = "91"+ConstantUtils.PHONE_NUM;
        }
        
        JsonCreator jsonCreator = new JsonCreator();
        jsonCreator.put("id",id);
        jsonCreator.put("memberId",memberId);
        jsonCreator.put("serialNumber",serialNumber);
        jsonCreator.put("imeiIsSame",imeiIsSame);
        jsonCreator.put("salaryRange",salaryRange);
        jsonCreator.put("brandModel",brandModel);
        jsonCreator.put("age",age);
        jsonCreator.put("sex",sex);
        jsonCreator.put("marryState",marryState);
        jsonCreator.put("jobPosition",jobPosition);
        jsonCreator.put("id",id);
        jsonCreator.put("applyTime",applyTime);
        jsonCreator.put("education",education);
        jsonCreator.put("phoneNumber1",ConstantUtils.PHONE_NUM);
        jsonCreator.put("phoneNumber2",phoneTelNum);
        jsonCreator.put("contact1",phoneNumber1);
        jsonCreator.put("contact2",phoneNumber2);
        jsonCreator.put("contactName1",contact1);
        jsonCreator.put("contactName2",contact2);
        jsonCreator.put("email", email);
        jsonCreator.put("religiouBelief", religion);
        jsonCreator.put("language", language);
        mRequester.commitPersonalInfoToServer(RetrofitCreator.getFormBody(jsonCreator)).enqueue(new RetrofitCreator.JsonCallback() {
            @Override
            public void onSuccess(JSONObject jsonObject){
                LoadingDialog.close();
                Gson gson = new Gson();
                HttpBean httpBean = gson.fromJson(jsonObject.toString(), HttpBean.class);
                int code = httpBean.getCode();
                if(200 == code){
                    view.commitPersonInfoSuccess();
                }else{
                    view.showToast(httpBean.getMsg());
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
