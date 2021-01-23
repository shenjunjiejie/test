package com.ucash_test.lirongyunindialoan.presenter;

import com.ucash_test.lirongyunindialoan.bean.UploadAppJson;
import com.ucash_test.lirongyunindialoan.view.ToastView;

/**
 * HomePresenter Interface
 * @author Krear 2020/03/12
 */
public interface HomePresenter {
    
    void getUserInfoFromServer(String memberId);

    
    void verifyPanCardToServer(String memberId);
    
    
    void userWithdrawToServer(String memberId, String productId, String billId);
    
    
    void getAppVersionFromServer(String memberId);
    
    
    
    void uploadUserLoginState(String memberId);
    
    
     interface View extends ToastView {

        
         
         void dismissLoadingDialog();
    
    
         void setHomePageDisplay(String app_setting);
         
    
         void setAuditStatusData(String auditStatus);
    
         void setHomeButtonEnable();
    
         void setAppUploadData(UploadAppJson.DataBean data);
         

//         void finishReferesh();
         
     }
}
