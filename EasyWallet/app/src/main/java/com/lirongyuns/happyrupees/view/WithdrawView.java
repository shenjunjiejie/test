package com.lirongyuns.happyrupees.view;

import com.lirongyuns.happyrupees.bean.LoanDetailsJson;

public interface WithdrawView {
    void setLoanDetailsUIDisplay(LoanDetailsJson.DataBean data);
    void startAuditResultPage();
    void startNextPage(String msg);
    void showToast(String msg);
}
