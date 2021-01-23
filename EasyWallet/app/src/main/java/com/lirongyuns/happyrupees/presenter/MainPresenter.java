package com.lirongyuns.happyrupees.presenter;

public interface MainPresenter {
    
    void getUserInfoFromServer(String memberId);
    
    void verifyPanCardToServer(String memberId);
    
    
    void userWithdrawToServer(String memberId, String productId, String billId);
    
    
    void getAppVersionFromServer(String memberId);
    
    
    
    void uploadUserLoginState(String memberId);

}
