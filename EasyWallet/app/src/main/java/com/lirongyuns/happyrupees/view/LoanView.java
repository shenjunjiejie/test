package com.lirongyuns.happyrupees.view;

import com.lirongyuns.happyrupees.bean.BigDataConfig;

public interface LoanView {
    void onGetConfig(BigDataConfig config);

    void submitCreditSuccess();

    void onUploadSuccess();

    void commitPersonInfoSuccess();

    void showToast(String msg);
}
