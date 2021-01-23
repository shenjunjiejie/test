package com.lirongyuns.happyrupees.view;

import com.lirongyuns.happyrupees.bean.UploadAppJson;

public interface MainView extends BaseView{
    void dismissLoadingDialog();


    void setHomePageDisplay(String app_setting);


    void setAuditStatusData(String auditStatus);

    void setHomeButtonEnable();

    void setAppUploadData(UploadAppJson.DataBean data);

    void showToast(String msg);
}
