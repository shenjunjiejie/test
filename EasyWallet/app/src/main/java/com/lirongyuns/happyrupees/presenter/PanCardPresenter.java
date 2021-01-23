package com.lirongyuns.happyrupees.presenter;


import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.http.Part;

public interface PanCardPresenter {

    void commitPanImageToServer(MultipartBody.Part part, MultipartBody.Part data, String type);

    void commitOcrImagePathToServer(String member_id, String pan_positive, String aadhaar_positive, String aadhaar_back);
}
