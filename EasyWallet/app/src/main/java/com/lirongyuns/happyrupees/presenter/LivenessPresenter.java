package com.lirongyuns.happyrupees.presenter;

import com.lirongyuns.happyrupees.bean.LivenessInfo;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.http.Part;

public interface LivenessPresenter {

    void uploadVerify(String memberId, LivenessInfo livenessInfo) throws Exception;
    
//    void verifyFaceImageToServer(@Part List<MultipartBody.Part> files);
    
//    void getAuthorityProtocolFromServer(String memberId, String productId);
    
    void getUserInfoFromServer(String memberId);

}
