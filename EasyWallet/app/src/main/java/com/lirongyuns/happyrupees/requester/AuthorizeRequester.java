package com.lirongyuns.happyrupees.requester;

import okhttp3.FormBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthorizeRequester
{

    @POST("app/apply_credit/extension")
    Call<ResponseBody> getAuditStateFromServer(@Body FormBody bodyContent);
    
    
    @POST("app/login/getInformation")
    Call<ResponseBody> getUserInfoFromServer(@Body FormBody formBody);
    
}
