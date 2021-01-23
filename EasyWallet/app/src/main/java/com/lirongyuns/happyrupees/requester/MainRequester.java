package com.lirongyuns.happyrupees.requester;

import okhttp3.FormBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MainRequester {
    
    @POST("app/login/getInformation")
    Call<ResponseBody> getUserInfoFromServer(@Body FormBody formBody);
    
    
    @POST("app/mwithdraw")
    Call<ResponseBody> userWithdrawToServer(@Body FormBody bodyContent);
    
    
    /**
     * 校验P卡
     * @param requestBody requestBody
     * @return ResponseBody
     */
    @POST("app/big_data/verify_identity")
    Call<ResponseBody> verifyPanCardToServer(@Body FormBody requestBody);
    
    
    @POST("app/index")
    Call<ResponseBody> uploadUserLoginState(@Body FormBody requestBody);
    
    @POST("app/index/get_version")
    Call<ResponseBody> getAppVersionFromServer(@Body FormBody requestBody);
    
    
}
