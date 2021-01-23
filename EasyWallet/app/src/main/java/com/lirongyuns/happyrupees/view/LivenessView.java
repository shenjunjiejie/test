package com.lirongyuns.happyrupees.view;

public interface LivenessView {
    void onGetUploadResult(boolean isSuccess);
//    void setOCRImagePath(String fileName, String ocrType);
    void showToast(String msg);
}
