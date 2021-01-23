package com.lirongyuns.happyrupees.presenter;

import com.google.gson.Gson;
import com.lirongyuns.happyrupees.bean.HttpBean;
import com.lirongyuns.happyrupees.bean.UserInfoJson;
import com.lirongyuns.happyrupees.dialog.LoadingDialog;
import com.lirongyuns.happyrupees.internet.JsonCreator;
import com.lirongyuns.happyrupees.internet.RetrofitCreator;
import com.lirongyuns.happyrupees.requester.AuthorizeRequester;
import com.lirongyuns.happyrupees.utils.ConstantUtils;
import com.lirongyuns.happyrupees.view.WaitView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import okhttp3.FormBody;

public class AuditUcashSystemResultPresenterImpl implements AuditUcashSystemResultPresenter {
    private WaitView view;
    AuthorizeRequester mRequester;

    public AuditUcashSystemResultPresenterImpl(WaitView view) {
        this.view = view;
        mRequester = RetrofitCreator.create(AuthorizeRequester.class);
    }
    
    @Override
    public void getAuditStateFromServer(String memberId, String productId,String RiskId) {
        JsonCreator jsonCreator = new JsonCreator();
        jsonCreator.put("memberId", memberId);
        jsonCreator.put("productId", productId);
        mRequester.getAuditStateFromServer(RetrofitCreator.getFormBody(jsonCreator)).enqueue(new RetrofitCreator.JsonCallback() {
            @Override
            public void onSuccess(JSONObject jsonObject)  {
                Gson gson = new Gson();
                HttpBean httpBean = gson.fromJson(jsonObject.toString(), HttpBean.class);
               
                if(200 == httpBean.getCode()){
                    view.onCommitSuccess();
                }else{
                    LoadingDialog.close();
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
    
    
    
    @Override
    public void getUserInfoFromServer(String memberId) {
        JsonCreator jsonCreator = new JsonCreator();
        jsonCreator.put("memberId",memberId);
        FormBody formBody = RetrofitCreator.getFormBody(jsonCreator);
        mRequester.getUserInfoFromServer(formBody)
                .enqueue(new RetrofitCreator.JsonCallback() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        LoadingDialog.close();
                        Gson gson = new Gson();
                        UserInfoJson userInfoJson = gson.fromJson(jsonObject.toString(), UserInfoJson.class);
                        UserInfoJson.DataBean data = userInfoJson.getData();
                        int code = userInfoJson.getCode();
                        if(200 == code && null != data){
                            UserInfoJson.DataBean.LoanMemberBean loanMember = data.getLoanMember();
                            ConstantUtils.APP_SETTING = data.getAppSeting();
                            ConstantUtils.IS_ALL_UPLOAD = data.getUploadSuccessful();
                            ConstantUtils.OCR_TYPE = data.getOcrType();
                            ConstantUtils.CARD_CHECK_MSG = data.getCardCheckingMsg();


                            if(null == loanMember){
                                return;
                            }
//                            SystemConstantUtils.AADHAAR_ID = loanMember.getAadhaarId();
//                            SystemConstantUtils.PAN_ID = loanMember.getPanId();
                            ConstantUtils.LOAN_STATUS = loanMember.getLoanStatus();
                            ConstantUtils.IS_UPLOAD_APP = loanMember.getUploadApp();
                            ConstantUtils.IS_UPLOAD_ALBUM = loanMember.getUploadAlbum();
                            ConstantUtils.IS_UPLOAD_SMS = loanMember.getUploadSms();
                            ConstantUtils.IS_UPLOAD_CONTACT = loanMember.getUploadAddress();
    
                            ConstantUtils.IS_UPLOAD_INFORMATION = loanMember.getUploadInformation();
                            ConstantUtils.IS_UPLOAD_PAN = loanMember.getOcrInspect();
                            ConstantUtils.IS_UPLOAD_FACE = loanMember.getLiveInspect();
                            ConstantUtils.PHONE_NUM = loanMember.getMobile();
                            ConstantUtils.IS_CAN_REPEAT = loanMember.getResetState();
                            ConstantUtils.REPEATED_BORROWING = loanMember.getResetState();
//                            efinancing();
                            view.setAuditResultPageUIDisplay();
                        }else {
                            view.showToast(userInfoJson.getMsg());
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
