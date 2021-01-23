package com.lirongyuns.happyrupees.requester;

import okhttp3.FormBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
public interface BindCardRequester {

    @POST("app/index/bank_card_display")
    Call<ResponseBody> getBankCardDataFromServer(@Body FormBody bodyContent);
    
    
    @POST("app/mwithdraw/checkbank")
    Call<ResponseBody> bindBankCardToServer(@Body FormBody bodyContent);

    @POST("app/mwithdraw/getIfcs")
    Call<ResponseBody> getBankIfscCodeFromServer(@Body FormBody bodyContent);
}



