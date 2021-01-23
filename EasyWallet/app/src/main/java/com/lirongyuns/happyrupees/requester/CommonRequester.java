package com.lirongyuns.happyrupees.requester;

import okhttp3.FormBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Common onRequest
 */
public interface CommonRequester
{
    /**
     * get user information
     * @param bodyContent {phone: String}
     * @return
     */
    @POST("app/SRXDMember/login")
    Call<ResponseBody> getUserInfo(@Body FormBody bodyContent);
}
