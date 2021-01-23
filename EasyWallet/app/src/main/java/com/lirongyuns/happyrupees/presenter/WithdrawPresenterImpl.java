package com.lirongyuns.happyrupees.presenter;

import com.google.gson.Gson;
import com.lirongyuns.happyrupees.bean.HttpBean;
import com.lirongyuns.happyrupees.bean.LoanDetailsJson;
import com.lirongyuns.happyrupees.bean.UserInfoJson;
import com.lirongyuns.happyrupees.dialog.LoadingDialog;
import com.lirongyuns.happyrupees.internet.JsonCreator;
import com.lirongyuns.happyrupees.internet.RetrofitCreator;
import com.lirongyuns.happyrupees.requester.LoanDetailsRequester;
import com.lirongyuns.happyrupees.utils.ConstantUtils;
import com.lirongyuns.happyrupees.view.WithdrawView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import okhttp3.FormBody;

public class WithdrawPresenterImpl implements WithdrawPresenter {
    private WithdrawView view;
    private LoanDetailsRequester mRequester;
    public WithdrawPresenterImpl(WithdrawView view) {
        this.view = view;
        mRequester = RetrofitCreator.create(LoanDetailsRequester.class);
    }

    @Override
    public void getLoanDetailsFromServer(String memberId, String productId) {
        JsonCreator jsonCreator = new JsonCreator();
        jsonCreator.put("memberId", memberId);
        jsonCreator.put("productId", productId);
        mRequester.getLoanDetailsFromServer(RetrofitCreator.getFormBody(jsonCreator))
                .enqueue(new RetrofitCreator.JsonCallback() {
                    @Override
                    public void onSuccess(JSONObject jsonObject){
                        LoadingDialog.close();
                        Gson gson = new Gson();

                        LoanDetailsJson httpBean = gson.fromJson(jsonObject.toString(), LoanDetailsJson.class);
                        LoanDetailsJson.DataBean data = httpBean.getData();
                        //Log.i("dsad",data.getId()+"");
//                        getView().showToast(httpBean.getMsg());
                        if(200 == httpBean.getCode()){
                           if(null == data){
                               return;
                           }
                           view.setLoanDetailsUIDisplay(data);
                        }else{
                            view.showToast(httpBean.getMsg());
                        }
                        
                    }
    
    
                    @Override
                    public void onFailure(int code, String msg) {
                        LoadingDialog.close();
                        view.showToast(msg);
                    }
                });
    }
    
    
    /**
     * 提现
     * @param memberId 用户id
     * @param productId 产品id
     * @param billId 借款账单id
     */
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
                        String msg = httpBean.getMsg();
                        view.startAuditResultPage();
                    }
                
                
                    @Override
                    public void onFailure(int code, String msg) {
                        LoadingDialog.close();
                    }
                });
        
    }
    
    
    
    /**
     * 提现
     * @param memberId 用户id
     * @param productId 产品id
     * @param billId 借款账单id
     */
    @Override
    public void confirWithdrawToServer(String memberId, String productId, String billId) {
        JsonCreator jsonCreator = new JsonCreator();
        jsonCreator.put("memberId", memberId);
        jsonCreator.put("productId", productId);
        jsonCreator.put("billId", billId);
        mRequester.confirWithdrawToServer(RetrofitCreator.getFormBody(jsonCreator))
                .enqueue(new RetrofitCreator.JsonCallback() {
                    @Override
                    public void onSuccess(JSONObject jsonObject){
                        LoadingDialog.close();
                        Gson gson = new Gson();
                        HttpBean httpBean = gson.fromJson(jsonObject.toString(), HttpBean.class);
                        String msg = httpBean.getMsg();
                        if(200 == httpBean.getCode()){
                            view.startNextPage(msg);
                        }else{
                            view.showToast(httpBean.getMsg());
                        }
                        
                    }
                    
                    
                    @Override
                    public void onFailure(int code, String msg) {
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
                            if(null == loanMember){
                                return;
                            }
//                            ConstantUtils.AADHAAR_ID = loanMember.getAadhaarId();
//                            ConstantUtils.PAN_ID = loanMember.getPanId();
                            ConstantUtils.LOAN_STATUS = loanMember.getLoanStatus();
                            ConstantUtils.IS_UPLOAD_APP = loanMember.getUploadApp();
                            ConstantUtils.IS_UPLOAD_ALBUM = loanMember.getUploadAlbum();
                            ConstantUtils.IS_UPLOAD_SMS= loanMember.getUploadSms();
                            ConstantUtils.IS_UPLOAD_CONTACT = loanMember.getUploadAddress();
                            
                            ConstantUtils.IS_UPLOAD_INFORMATION = loanMember.getUploadInformation();
                            ConstantUtils.IS_UPLOAD_PAN = loanMember.getOcrInspect();
                            ConstantUtils.IS_UPLOAD_FACE = loanMember.getLiveInspect();
                            ConstantUtils.APP_SETTING = data.getAppSeting();
                            ConstantUtils.PHONE_NUM = loanMember.getMobile();
                            ConstantUtils.IS_CAN_REPEAT = loanMember.getResetState();
                            ConstantUtils.IS_ALL_UPLOAD = data.getUploadSuccessful();
                            ConstantUtils.REPEATED_BORROWING = loanMember.getResetState();
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
