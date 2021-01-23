package com.lirongyuns.happyrupees.requester;


import java.util.List;

import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface PanCardRequester {
    @Multipart
    @POST("app/big_data/save_img")//app/big_data/getocr
    Call<ResponseBody> commitPanImageToServer(@Part MultipartBody.Part files, @Part MultipartBody.Part part);


    @POST("/app/big_data/get_new_ocr")//
    Call<ResponseBody> commitOcrImagePathToServer(@Body FormBody bodyContent);
}
