package com.lirongyuns.happyrupees.requester;

import okhttp3.FormBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoanDetailsRequester {

    @POST("app/mwithdraw/getpreviewBill")
    Call<ResponseBody> getLoanDetailsFromServer(@Body FormBody bodyContent);
    
    @POST("app/mwithdraw")
    Call<ResponseBody> userWithdrawToServer(@Body FormBody bodyContent);
    
    
    @POST("app/mwithdraw/ConfirmWithdrawal")
    Call<ResponseBody> confirWithdrawToServer(@Body FormBody bodyContent);
    
    
    @POST("app/login/getInformation")
    Call<ResponseBody> getUserInfoFromServer(@Body FormBody formBody);

}
