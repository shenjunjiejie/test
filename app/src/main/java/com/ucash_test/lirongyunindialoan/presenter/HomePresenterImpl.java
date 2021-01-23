package com.ucash_test.lirongyunindialoan.presenter;

import com.google.gson.Gson;
import com.ucash_test.lirongyunindialoan.bean.HttpBean;
import com.ucash_test.lirongyunindialoan.bean.UploadAppJson;
import com.ucash_test.lirongyunindialoan.bean.UserInfoJson;
import com.ucash_test.lirongyunindialoan.config.AppStatusConf;
import com.ucash_test.lirongyunindialoan.internet.JsonParser;
import com.ucash_test.lirongyunindialoan.internet.requester.HomeRequester;
import com.ucash_test.lirongyunindialoan.ui.LoadingDialog;
import com.ucash_test.lirongyunindialoan.utils.RetrofitCreator;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import okhttp3.FormBody;

/**
 * HomePresenter
 * @author Krear 2020/03/12
 */
public class HomePresenterImpl extends BasePresenter<HomePresenter.View> implements HomePresenter {

    private HomeRequester mRequester;
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

    public HomePresenterImpl(View view) {
        super(view);
        mRequester = RetrofitCreator.create(HomeRequester.class);
    }
    
    @Override
    public void getUserInfoFromServer(String memberId) {
        JsonParser jsonCreator = new JsonParser();
        jsonCreator.put("memberId",memberId);
        FormBody formBody = RetrofitCreator.getFormBody(jsonCreator);
        mRequester.getUserInfoFromServer(formBody)
                .enqueue(new RetrofitCreator.JsonCallback() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        LoadingDialog.close();
                        Gson gson = new Gson();
                        UserInfoJson userInfoJson = gson.fromJson(jsonObject.toString(), UserInfoJson.class);
                        //Log."user_info",jsonObject.toString());
                        UserInfoJson.DataBean data = userInfoJson.getData();
                        int code = userInfoJson.getCode();
                       
                        if(200 == code && null != data){
                            UserInfoJson.DataBean.LoanMemberBean loanMember = data.getLoanMember();
                            AppStatusConf.APP_SETTING = data.getAppSeting();
                            AppStatusConf.ALL_UPLOAD_SUCCEED_STATUS = data.getUploadSuccessful();
                            getView().setAuditStatusData(data.getAuditStatus());
                            getView().setHomePageDisplay(data.getAppSeting());

                            AppStatusConf.CARD_CHECK_MSG = data.getCardCheckingMsg();
                            //Log."OCR1",data.getOcrType()+"");
                            //Log."PAY1",data.getPayType()+"");
                            AppStatusConf.OCR_TYPE = data.getOcrType();
                            AppStatusConf.PAYMENT_CHANNEL = data.getPayType();
                            if(null == loanMember){
                                return;
                            }
//                            AppStatusConf.AADHAAR_ID = loanMember.getAadhaarId();
//                            AppStatusConf.PAN_ID = loanMember.getPanId();
                            AppStatusConf.LOAN_STATUS = loanMember.getLoanStatus();
                            AppStatusConf.IS_UPLOAD_APP = loanMember.getUploadApp();
                            AppStatusConf.UPLOAD_ALBUM_STATUS = loanMember.getUploadAlbum();
                            AppStatusConf.UPLOAD_SMS_STATUS = loanMember.getUploadSms();
                            AppStatusConf.UPLOAD_CONTACT_STATUS = loanMember.getUploadAddress();
    
                            AppStatusConf.UPLOAD_INFORMATION_STATUS = loanMember.getUploadInformation();
                            AppStatusConf.UPLOAD_PAN_STATUS = loanMember.getOcrInspect();
                            AppStatusConf.UPLOAD_FACE_STATUS = loanMember.getLiveInspect();
                            AppStatusConf.PHONE_CODE = loanMember.getMobile();
                            AppStatusConf.SUPPORT_REPEAT_STATUS = loanMember.getResetState();
                            AppStatusConf.REPEATED_BORROWING = loanMember.getResetState();
//                            efinancing();
                        }else {
                            getView().showToast(userInfoJson.getMsg());
                        }
                    }
    
                    @Override
                    public void onFailure(int code, String msg) {
                        super.onFailure(code, msg);
                        LoadingDialog.close();
                        getView().showToast(msg);
                        getView().setHomeButtonEnable();
                    }
                });
    }
    
    
    
    
    
    @Override
    public void verifyPanCardToServer(String memberId) {
        JsonParser jsonCreator = new JsonParser();
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
                if (AppStatusConf.NETWORK_REQUEST_OK == code) {
//                    getView().onUpLoadSuccess(httpBean.getMsg());
                }else{
                    getView().showToast(httpBean.getMsg());
                }
            }
            
            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                LoadingDialog.close();
                getView().showToast(msg);
            }
        });
    }
    
    @Override
    public void uploadUserLoginState(String memberId) {
        JsonParser jsonCreator = new JsonParser();
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
    
    
    
    
    
    
    
    /**
     * 提现
     * @param memberId 用户id
     * @param productId 产品id
     * @param billId 借款账单id
     */
    @Override
    public void userWithdrawToServer(String memberId, String productId, String billId) {
        JsonParser jsonCreator = new JsonParser();
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
                       
                    }
                    
                    
                    @Override
                    public void onFailure(int code, String msg) {
                        LoadingDialog.close();
                    }
                });
        
    }
    
    @Override
    public void getAppVersionFromServer(String memberId) {
        JsonParser jsonCreator = new JsonParser();
        jsonCreator.put("memberId", memberId);
        mRequester.getAppVersionFromServer(RetrofitCreator.getFormBody(jsonCreator))
                .enqueue(new RetrofitCreator.JsonCallback() {
                    @Override
                    public void onSuccess(JSONObject jsonObject){
                        LoadingDialog.close();
                        Gson gson = new Gson();
                        UploadAppJson uploadAppJson = gson.fromJson(jsonObject.toString(), UploadAppJson.class);
                        String msg = uploadAppJson.getMsg();
                        if (AppStatusConf.NETWORK_REQUEST_OK == uploadAppJson.getCode()) {
                            UploadAppJson.DataBean data = uploadAppJson.getData();
                            if (null == data) {
                                return;
                            }
                            
                            getView().setAppUploadData(data);
                        }
                    
                    }
                
                
                    @Override
                    public void onFailure(int code, String msg) {
                        LoadingDialog.close();
                    }
                });
    }
    
}
