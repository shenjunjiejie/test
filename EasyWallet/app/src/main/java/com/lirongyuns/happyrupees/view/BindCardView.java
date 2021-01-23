package com.lirongyuns.happyrupees.view;

import com.lirongyuns.happyrupees.bean.MyBankJson;

import java.util.ArrayList;

public interface BindCardView {
    void startAuditReusltPage(String msg);

    void setMyBankDisplay(MyBankJson.DataBean data);

    void setBankCardButtonEnable();

    void setBankIFSCData(ArrayList<String> data);

    void showToast(String msg);
}
