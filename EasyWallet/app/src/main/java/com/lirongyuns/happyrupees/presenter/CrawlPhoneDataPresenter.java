package com.lirongyuns.happyrupees.presenter;

import org.json.JSONArray;

import java.io.File;

import okhttp3.MultipartBody;
import retrofit2.http.Part;

public interface CrawlPhoneDataPresenter {
    /**
     * upload user's installed app list
     * @param memberId member id
     * @param datas  datas
     */
    void uploadApp(String memberId, JSONArray datas);
    
    /**
     * upload user's photo album info
     * @param memberId member id
     * @param datas  datas
     */
    void uploadPhotoAlbum(String memberId, JSONArray datas);
    
    /**
     * upload user's sms record
     * @param memberId member id
     * @param datas  datas
     */
    void uploadSms(String memberId, JSONArray datas);
    
    
    
    void getUserInfoFromServer(String memberId);
    
    /**
     * upload user's contacts
     * @param memberId
     * @param datas
     */
    void uploadContacts(String memberId, JSONArray datas);
    
    
    void uploadSmsDataFile(@Part MultipartBody.Part filePart, MultipartBody.Part data, File file);
    
    
    void uploadPhotoDataFile(@Part MultipartBody.Part filePart, MultipartBody.Part data, File file);
    
    
    void uploadAppDataFile(@Part MultipartBody.Part filePart, MultipartBody.Part data, File file);
    
    
    void uploadContactDataFile(@Part MultipartBody.Part filePart, MultipartBody.Part data, File file);

}
