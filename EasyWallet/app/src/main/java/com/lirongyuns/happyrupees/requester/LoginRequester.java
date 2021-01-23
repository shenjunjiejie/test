package com.lirongyuns.happyrupees.requester;

import okhttp3.FormBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Login Internet Requester
 */
public interface LoginRequester
{

    @POST("app/login/code")
    Call<ResponseBody> check(@Body FormBody bodyContent);

    @POST("app/login")
    Call<ResponseBody> login(@Body FormBody bodyContent);

    @POST("app/login/code")
    Call<ResponseBody> getLoginCode(@Body FormBody bodyContent);

    @POST("app/SRXDMember/register")
    Call<ResponseBody> register(@Body FormBody bodyContent);

    @POST("app/SRXDMember/register/code")
    Call<ResponseBody> getRegisterCode(@Body FormBody bodyContent);
}
