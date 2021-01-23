package com.lirongyuns.happyrupees.presenter;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Base64;

import com.google.gson.Gson;
import com.lirongyuns.happyrupees.bean.HttpBean;
import com.lirongyuns.happyrupees.bean.LivenessInfo;
import com.lirongyuns.happyrupees.bean.UserInfoJson;
import com.lirongyuns.happyrupees.conf.NetWorkConf;
import com.lirongyuns.happyrupees.dialog.LoadingDialog;
import com.lirongyuns.happyrupees.internet.JsonCreator;
import com.lirongyuns.happyrupees.internet.RSA;
import com.lirongyuns.happyrupees.internet.RetrofitCreator;
import com.lirongyuns.happyrupees.requester.LivenessRequester;
import com.lirongyuns.happyrupees.utils.ConstantUtils;
import com.lirongyuns.happyrupees.view.LivenessView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class LivenessPresenterImpl  implements LivenessPresenter {

    private LivenessView view;

    private LivenessRequester mRequester;

    public LivenessPresenterImpl(LivenessView view) {
        this.view = view;
        
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
    public void uploadVerify(String memberId, LivenessInfo livenessInfo) throws Exception {
        JsonCreator creator = new JsonCreator();
        mRequester = RetrofitCreator.create(LivenessRequester.class);
        creator.put("memberId", memberId);
        creator.put("liveScore","100");//人脸活体分数
        Map<String, RequestBody> datas = new HashMap<>();
        
        datas.put("img1\"; filename=\"img1", RetrofitCreator.getImageBody(livenessInfo.getLiveness()));
        byte[] bytes = RSA.encryptByPublicKey(creator.toString().getBytes(), RSA.PUBLICKEY);
        String encodeData = Base64.encodeToString(bytes, Base64.DEFAULT);
        MultipartBody.Part part = MultipartBody.Part.createFormData("data", encodeData);
        mRequester.uploadVerify(datas, part).enqueue(new RetrofitCreator.JsonCallback() {
    
            @Override
            public void onSuccess(JSONObject jsonObject) {
                LoadingDialog.close();
                if(null != livenessInfo){
                    livenessInfo.release();
                }
                Gson gson = new Gson();
                HttpBean httpBean = gson.fromJson(jsonObject.toString(), HttpBean.class);
                int code = httpBean.getCode();
                if(200 == code) {
                    view.onGetUploadResult(true);
                }else {
                    view.showToast(httpBean.getMsg());
                }
                
            }
    
            @Override
            public void onFailure(int code, String msg) {
                LoadingDialog.close();
                if(null != livenessInfo){
                    livenessInfo.release();
                }
                view.showToast(msg);
            }
        });

    }



//    @Override
//    public void getAuthorityProtocolFromServer(String memberId, String productId) {
//        mRequester = RetrofitCreator.create(LivenessRequester.class);
//        JsonCreator jsonCreator = new JsonCreator();
//        jsonCreator.put("memberId", memberId);
//        jsonCreator.put("productId", productId);
//
//        mRequester.getAuthorityProtocolFromServer(RetrofitCreator.getFormBody(jsonCreator)).enqueue(new RetrofitCreator.JsonCallback() {
//            @Override
//            public void onSuccess(JSONObject jsonObject) {
//                Gson gson = new Gson();
//                HttpBean httpBean = gson.fromJson(jsonObject.toString(), HttpBean.class);
//                String msg = httpBean.getMsg();
//                if(!TextUtils.isEmpty(msg) && msg.startsWith("/")){
//                    msg = msg.substring(1);
//                }
//                msg = NetWorkConf.getInstance().getHttpHost() + msg;
//                view.setAuthorityProtocolUrl(msg);
//
//            }
//
//
//            @Override
//            public void onFailure(int code, String msg) {
//                super.onFailure(code, msg);
//                view.showToast(msg);
//            }
//        });
//    }
    
    
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
                            ConstantUtils.PHONE_NUM = loanMember.getMobile();
                        }else{
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
