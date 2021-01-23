package com.lirongyuns.happyrupees.presenter;

public interface WithdrawPresenter {
    
    void getLoanDetailsFromServer(String memberId, String productId);
    
    void userWithdrawToServer(String memberId, String productId, String billId);
    
    void confirWithdrawToServer(String memberId, String productId, String billId);
    
    void getUserInfoFromServer(String memberId);

}
