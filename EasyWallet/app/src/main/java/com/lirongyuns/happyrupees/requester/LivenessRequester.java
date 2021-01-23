package com.lirongyuns.happyrupees.requester;

import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface LivenessRequester {
    @Multipart
    @POST("app/big_data/biopsy")
    Call<ResponseBody> uploadVerify(@PartMap Map<String, RequestBody> datas, @Part MultipartBody.Part part);
    
    
    @POST("app/big_data/getocr")
    Call<ResponseBody> uploadPanPicToServer(@PartMap Map<String, RequestBody> datas);
    
    
    @Multipart
    @POST("face-identity/v1/face-comparison")
    Call<ResponseBody> verifyFaceImageToServer(@Part List<MultipartBody.Part> files, @Header("X-ADVAI-KEY") String key);
    
    
    @POST("app/apply_credit/view_warrant")
    Call<ResponseBody> getAuthorityProtocolFromServer(@Body FormBody formBody);
    
    
    @POST("app/login/getInformation")
    Call<ResponseBody> getUserInfoFromServer(@Body FormBody formBody);
}
