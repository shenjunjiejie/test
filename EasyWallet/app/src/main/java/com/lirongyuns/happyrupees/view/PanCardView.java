package com.lirongyuns.happyrupees.view;

public interface PanCardView {
    void success(String msg);
    void onUpLoadSuccess(String msg);
    void setOCRImagePath(String fileName, String ocrType);
    void showToast(String msg);
}
