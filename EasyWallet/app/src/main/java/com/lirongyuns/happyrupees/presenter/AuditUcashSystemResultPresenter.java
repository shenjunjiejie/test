package com.lirongyuns.happyrupees.presenter;

public interface AuditUcashSystemResultPresenter {

    void getAuditStateFromServer(String memberId, String productId,String RiskId);
    
    void getUserInfoFromServer(String memberId);

}
