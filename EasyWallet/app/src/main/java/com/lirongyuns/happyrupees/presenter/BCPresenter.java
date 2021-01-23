package com.lirongyuns.happyrupees.presenter;

public interface BCPresenter {

    void bindBankCardToServer(String beneAccno, String beneIfsc, String memberId);
    
    void getBankCardDataFromServer(String memberId);
    
    void getBankIFSCCodeFromServer(String state, String city, String bank_name, String bank_branch, String bank_ifsc);

}
