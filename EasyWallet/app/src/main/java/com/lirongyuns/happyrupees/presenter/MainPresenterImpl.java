package com.lirongyuns.happyrupees.presenter;

import com.google.gson.Gson;
import com.lirongyuns.happyrupees.bean.HttpBean;
import com.lirongyuns.happyrupees.bean.UploadAppJson;
import com.lirongyuns.happyrupees.bean.UserInfoJson;
import com.lirongyuns.happyrupees.dialog.LoadingDialog;
import com.lirongyuns.happyrupees.internet.JsonCreator;
import com.lirongyuns.happyrupees.internet.RetrofitCreator;
import com.lirongyuns.happyrupees.requester.MainRequester;
import com.lirongyuns.happyrupees.utils.ConstantUtils;
import com.lirongyuns.happyrupees.view.MainView;

import org.json.JSONObject;

import okhttp3.FormBody;

public class MainPresenterImpl implements MainPresenter {
    private MainView view;
    private MainRequester mRequester;

    public MainPresenterImpl(MainView view) {
        this.view = view;
        mRequester = RetrofitCreator.create(MainRequester.class);
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
                            if(null == loanMember){
                                return;
                            }
                            ConstantUtils.OCR_TYPE = data.getOcrType();
                            ConstantUtils.CARD_CHECK_MSG = data.getCardCheckingMsg();
                            ConstantUtils.LOAN_STATUS = loanMember.getLoanStatus();
                            ConstantUtils.IS_UPLOAD_APP = loanMember.getUploadApp();
                            ConstantUtils.IS_UPLOAD_ALBUM = loanMember.getUploadAlbum();
                            ConstantUtils.IS_UPLOAD_SMS = loanMember.getUploadSms();
                            ConstantUtils.IS_UPLOAD_CONTACT = loanMember.getUploadAddress();

                            ConstantUtils.IS_UPLOAD_INFORMATION = loanMember.getUploadInformation();
                            ConstantUtils.IS_UPLOAD_PAN = loanMember.getOcrInspect();
                            ConstantUtils.IS_UPLOAD_FACE = loanMember.getLiveInspect();
                            ConstantUtils.APP_SETTING = data.getAppSeting();
                            view.setHomePageDisplay(data.getAppSeting());
                            ConstantUtils.PHONE_NUM = loanMember.getMobile();
                            ConstantUtils.IS_CAN_REPEAT = loanMember.getResetState();
                            view.setAuditStatusData(data.getAuditStatus());
                            ConstantUtils.IS_ALL_UPLOAD = data.getUploadSuccessful();
                            ConstantUtils.REPEATED_BORROWING = loanMember.getResetState();
                            ConstantUtils.PAYMENT_CHANNEL = data.getPayType();
                        }else {
                            view.showToast(userInfoJson.getMsg());
                        }
                    }
    
                    @Override
                    public void onFailure(int code, String msg) {
                        super.onFailure(code, msg);
                        LoadingDialog.close();
                        view.showToast(msg);
                        view.setHomeButtonEnable();
                    }
                });
    }
    
    
    
    
    
    @Override
    public void verifyPanCardToServer(String memberId) {
        JsonCreator jsonCreator = new JsonCreator();
        jsonCreator.put("memberId",memberId);
        FormBody formBody = RetrofitCreator.getFormBody(jsonCreator);
        mRequester.verifyPanCardToServer(formBody).enqueue(new RetrofitCreator.JsonCallback() {
            @Override
            public void onSuccess(JSONObject jsonObject){
                LoadingDialog.close();
                Gson gson = new Gson();
                HttpBean httpBean = gson.fromJson(jsonObject.toString(), HttpBean.class);
                if (null == httpBean) {
                    return;
                }
                int code = httpBean.getCode();
                if (200 == code) {
//                    view.showToast(httpBean.getMsg());
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
    
    @Override
    public void uploadUserLoginState(String memberId) {
        JsonCreator jsonCreator = new JsonCreator();
        jsonCreator.put("memberId",memberId);
        FormBody formBody = RetrofitCreator.getFormBody(jsonCreator);
        mRequester.uploadUserLoginState(formBody).enqueue(new RetrofitCreator.MsgCallback() {
            @Override
            public void onSuccess(String msg) {
        
            }
    
    
            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
            }
        });
    }

    @Override
    public void userWithdrawToServer(String memberId, String productId, String billId) {
        JsonCreator jsonCreator = new JsonCreator();
        jsonCreator.put("memberId", memberId);
        jsonCreator.put("productId", productId);
        jsonCreator.put("billId", billId);
        mRequester.userWithdrawToServer(RetrofitCreator.getFormBody(jsonCreator))
                .enqueue(new RetrofitCreator.JsonCallback() {
                    @Override
                    public void onSuccess(JSONObject jsonObject){
                        LoadingDialog.close();
                        Gson gson = new Gson();
                        HttpBean httpBean = gson.fromJson(jsonObject.toString(), HttpBean.class);
//                        String msg = httpBean.getMsg();
                       
                    }
                    
                    
                    @Override
                    public void onFailure(int code, String msg) {
                        LoadingDialog.close();
                    }
                });
        
    }
    
    @Override
    public void getAppVersionFromServer(String memberId) {
        JsonCreator jsonCreator = new JsonCreator();
        jsonCreator.put("memberId", memberId);
        mRequester.getAppVersionFromServer(RetrofitCreator.getFormBody(jsonCreator))
                .enqueue(new RetrofitCreator.JsonCallback() {
                    @Override
                    public void onSuccess(JSONObject jsonObject){
                        LoadingDialog.close();
                        Gson gson = new Gson();
                        UploadAppJson uploadAppJson = gson.fromJson(jsonObject.toString(), UploadAppJson.class);
                        String msg = uploadAppJson.getMsg();
                        if (200 == uploadAppJson.getCode()) {
                            UploadAppJson.DataBean data = uploadAppJson.getData();
                            if (null == data) {
                                return;
                            }
                            
                            view.setAppUploadData(data);
                        }
                    
                    }
                
                
                    @Override
                    public void onFailure(int code, String msg) {
                        LoadingDialog.close();
                    }
                });
    }
    
}
