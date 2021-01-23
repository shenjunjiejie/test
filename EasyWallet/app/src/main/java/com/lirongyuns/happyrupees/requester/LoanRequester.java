package com.lirongyuns.happyrupees.requester;

import okhttp3.FormBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface LoanRequester
{
    @GET("app/SRXDRiskIndia/getparam")
    Call<ResponseBody> getBigDataConfig();

    @POST("app/SRXDPersona/getPersona")
    Call<ResponseBody> uploadPersonalInfo(@Body FormBody bodyContent);

    @POST("/app/pick_up_information/appdata")
    Call<ResponseBody> uploadAppInfo(@Body FormBody bodyContent);

    @POST("app/pick_up_information/album")
    Call<ResponseBody> uploadPhotoInfo(@Body FormBody bodyContent);

    @POST("app/pick_up_information/note")
    Call<ResponseBody> uploadSmsInfo(@Body FormBody bodyContent);

    @POST("app/pick_up_information/address")
    Call<ResponseBody> uploadContactsInfo(@Body FormBody bodyContent);
    
    
    
    @POST("app/pick_up_information/getpersona")
    Call<ResponseBody> commitPersonalInfoToServer(@Body FormBody bodyContent);

    @POST("app/SRXDMember/material/refer")
    Call<ResponseBody> creditProduct7(@Body FormBody bodyContent);

    @POST("app/SRXDMember/material/sc_refer")
    Call<ResponseBody> creditProduct14(@Body FormBody bodyContent);
}
