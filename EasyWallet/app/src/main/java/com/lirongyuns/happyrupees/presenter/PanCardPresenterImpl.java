package com.lirongyuns.happyrupees.presenter;


import com.google.gson.Gson;
import com.lirongyuns.happyrupees.bean.HttpBean;
import com.lirongyuns.happyrupees.bean.OCRImageJson;
import com.lirongyuns.happyrupees.dialog.LoadingDialog;
import com.lirongyuns.happyrupees.internet.JsonCreator;
import com.lirongyuns.happyrupees.internet.RetrofitCreator;
import com.lirongyuns.happyrupees.requester.PanCardRequester;
import com.lirongyuns.happyrupees.utils.ConstantUtils;
import com.lirongyuns.happyrupees.view.PanCardView;

import org.json.JSONObject;

import java.util.List;

import okhttp3.MultipartBody;

public class PanCardPresenterImpl  implements PanCardPresenter {
    private PanCardView view;
    PanCardRequester mRequester;
    public PanCardPresenterImpl(PanCardView view) {
        this.view = view;
        mRequester = RetrofitCreator.create(PanCardRequester.class);
    }

    @Override
    public void commitPanImageToServer(MultipartBody.Part part, MultipartBody.Part data, String type) {
        mRequester.commitPanImageToServer(part, data).enqueue(new RetrofitCreator.JsonCallback() {
            @Override
            public void onSuccess(JSONObject jsonObject){
                LoadingDialog.close();
                Gson gson = new Gson();
                OCRImageJson ocrImageJson = gson.fromJson(jsonObject.toString(), OCRImageJson.class);
                if (null == ocrImageJson) {
                    return;
                }
                int code = ocrImageJson.getCode();

                if (200 == code) {
                    OCRImageJson.DataBean dataBean = ocrImageJson.getData();
                    if (null == dataBean) {
                        return;
                    }
                    String fileName = dataBean.getFileName();
                    view.setOCRImagePath(fileName, type);
                }else{
                    view.showToast(ocrImageJson.getMsg());
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
    public void commitOcrImagePathToServer(String member_id, String pan_positive, String aadhaar_positive, String aadhaar_back) {
        JsonCreator jsonCreator = new JsonCreator();
        jsonCreator.put("memberId", member_id);
        jsonCreator.put("pan_positive", pan_positive);
        jsonCreator.put("aadhaar_positive", aadhaar_positive);
        jsonCreator.put("aadhaar_back", aadhaar_back);
        mRequester.commitOcrImagePathToServer(RetrofitCreator.getFormBody(jsonCreator)).enqueue(new RetrofitCreator.JsonCallback() {
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
                    view.success(httpBean.getMsg());
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
