package com.dfsdk.ocr.scan.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.dfsdk.ocr.scan.BuildConfig;
import com.dfsdk.ocr.scan.R;
import com.dfsdk.ocr.scan.network.DFHttpManager;
import com.dfsdk.ocr.scan.network.callback.DFNetworkCallback;
import com.dfsdk.ocr.scan.network.model.DFAadhaarCardBackInfo;
import com.dfsdk.ocr.scan.network.model.DFAadhaarCardFrontInfo;
import com.dfsdk.ocr.scan.network.model.DFCardBaseInfo;
import com.dfsdk.ocr.scan.network.model.DFCardInfo;
import com.dfsdk.ocr.scan.network.model.DFPanCardInfo;
import com.dfsdk.ocr.scan.network.request.DFApiParameterList;
import com.dfsdk.ocr.scan.network.response.DFNetworkBaseModule;
import com.dfsdk.ocr.scan.util.DFOCRScanUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DFCardRecognizePresenter {
    private static final String TAG = "DFCardRecognizePresenter";

    private static final String CONTROL = "/ocr/";
    private DFRecognizeCallback mRecognizeCallback;
    private static final String API_ID = "305420bbe8f54b90b4ebc840d81feae4";
    private static final String API_SECRET = "0f95b3a100dc4976bb6ead969e3b19cb";

    public DFCardRecognizePresenter(@NonNull DFRecognizeCallback recognizeCallback) {
        this.mRecognizeCallback = recognizeCallback;
    }

    public void recognizeCard(final Bitmap bitmap) {
        mRecognizeCallback.starLoading();
        DFApiParameterList headerParameterList = DFApiParameterList.create();
        headerParameterList.add("X-DF-API-ID", API_ID);
        headerParameterList.add("X-DF-API-SECRET", API_SECRET);
        DFApiParameterList bodyParameterList = DFApiParameterList.create();
        bodyParameterList.add("file", bitmap);
        DFHttpManager.getInstance().postSyn(CONTROL, "indian_card", headerParameterList, bodyParameterList,
                new DFNetworkCallback<DFNetworkBaseModule>() {
                    @Override
                    public void completed(DFNetworkBaseModule response) {
                        String results = response.getResults();
                        DFOCRScanUtils.logI(TAG, "recognizeCard", "results", results);
                        DFCardInfo cardInfo = getCardInfo(results);
                        cardInfo.setDetectImage(DFOCRScanUtils.convertBmpToJpeg(bitmap));
                        mRecognizeCallback.endLoading();
                        mRecognizeCallback.recognizeCardResult(cardInfo);
                    }

                    @Override
                    public void failed(int httpStatusCode, DFNetworkBaseModule response) {
                        mRecognizeCallback.endLoading();
                        DFOCRScanUtils.logI(TAG, "recognizeCard", "httpStatusCode", httpStatusCode);
                        DFCardInfo cardInfo = new DFCardInfo();
                        cardInfo.setDetectImage(DFOCRScanUtils.convertBmpToJpeg(bitmap));
                        cardInfo.setResult(httpStatusCode);
                        if (response != null) {
                            cardInfo.setReason(response.getReason());
                        } else {
                            cardInfo.setReason(getString(com.dfsdk.ocr.scan.R.string.ocr_scan_network_error));
                        }
                        mRecognizeCallback.recognizeCardResult(cardInfo);
                    }
                });
    }

    private DFCardInfo getCardInfo(String results) {
        DFCardInfo cardInfo = new DFCardInfo();
        try {
            JSONArray jsonArray = new JSONArray(results);
            if (jsonArray.length() >= 1) {
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                String cardType = jsonObject.optString("card_type");
                String cardSide = jsonObject.optString("card_side");
                if (TextUtils.equals(cardType, "pan_card")) {
                    DFPanCardInfo panCardInfo = new DFPanCardInfo();
                    JSONObject cardInfoJsonObject = jsonObject.getJSONObject("card_info");
                    panCardInfo.setResult(DFCardBaseInfo.DF_OK);
                    panCardInfo.setCardNo(cardInfoJsonObject.optString("card_no"));
                    panCardInfo.setName(cardInfoJsonObject.optString("name"));
                    panCardInfo.setFatherName(cardInfoJsonObject.optString("father_name"));
                    panCardInfo.setDateType(cardInfoJsonObject.optString("date_type"));
                    panCardInfo.setDateInfo(cardInfoJsonObject.optString("date_info"));
                    panCardInfo.setIssueDate(cardInfoJsonObject.optString("issue_date"));
                    cardInfo.setCardType(DFCardInfo.CARD_TYPE_PAN_CARD);
                    cardInfo.setCardInfo(panCardInfo);
                } else if (TextUtils.equals(cardType, "aadhaar_card")) {
                    if (TextUtils.equals(cardSide, "front")) {
                        DFAadhaarCardFrontInfo aadhaarCardFrontInfo = new DFAadhaarCardFrontInfo();
                        JSONObject cardInfoJsonObject = jsonObject.getJSONObject("card_info");
                        aadhaarCardFrontInfo.setResult(DFCardBaseInfo.DF_OK);
                        aadhaarCardFrontInfo.setCardNo(cardInfoJsonObject.optString("card_no"));
                        aadhaarCardFrontInfo.setName(cardInfoJsonObject.optString("name"));
                        aadhaarCardFrontInfo.setGender(cardInfoJsonObject.optString("gender"));
                        aadhaarCardFrontInfo.setMotherName(cardInfoJsonObject.optString("mother_name"));
                        aadhaarCardFrontInfo.setFatherName(cardInfoJsonObject.optString("father_name"));
                        aadhaarCardFrontInfo.setDateType(cardInfoJsonObject.optString("date_type"));
                        aadhaarCardFrontInfo.setDateInfo(cardInfoJsonObject.optString("date_info"));
                        aadhaarCardFrontInfo.setPhoneNumber(cardInfoJsonObject.optString("phone_number"));
                        aadhaarCardFrontInfo.setVid(cardInfoJsonObject.optString("vid"));
                        cardInfo.setCardType(DFCardInfo.CARD_TYPE_AADHAAR_CARD_FRONT);
                        cardInfo.setCardInfo(aadhaarCardFrontInfo);
                    } else if (TextUtils.equals(cardSide, "back")) {
                        DFAadhaarCardBackInfo aadhaarCardbackInfo = new DFAadhaarCardBackInfo();
                        JSONObject cardInfoJsonObject = jsonObject.getJSONObject("card_info");
                        aadhaarCardbackInfo.setResult(DFCardBaseInfo.DF_OK);
                        aadhaarCardbackInfo.setCardNo(cardInfoJsonObject.optString("card_no"));
                        aadhaarCardbackInfo.setVid(cardInfoJsonObject.optString("vid"));
                        aadhaarCardbackInfo.setSonOf(cardInfoJsonObject.optString("son_of"));
                        aadhaarCardbackInfo.setDaughterOf(cardInfoJsonObject.optString("daughter_of"));
                        aadhaarCardbackInfo.setWifeOf(cardInfoJsonObject.optString("wife_of"));
                        aadhaarCardbackInfo.setCareOf(cardInfoJsonObject.optString("care_of"));
                        aadhaarCardbackInfo.setHusbandOf(cardInfoJsonObject.optString("husband_of"));
                        aadhaarCardbackInfo.setCity(cardInfoJsonObject.optString("city"));
                        aadhaarCardbackInfo.setState(cardInfoJsonObject.optString("state"));
                        aadhaarCardbackInfo.setPin(cardInfoJsonObject.optString("pin"));
                        aadhaarCardbackInfo.setAddress(cardInfoJsonObject.optString("address"));
                        aadhaarCardbackInfo.setAddressLineOne(cardInfoJsonObject.optString("address_line_one"));
                        aadhaarCardbackInfo.setAddressLineTwo(cardInfoJsonObject.optString("address_line_two"));
                        cardInfo.setCardType(DFCardInfo.CARD_TYPE_AADHAAR_CARD_BACK);
                        cardInfo.setCardInfo(aadhaarCardbackInfo);
                    } else {
                        cardInfo.setResult(DFCardBaseInfo.DF_DETECTION_FAILED);
                        cardInfo.setReason(getString(R.string.ocr_scan_detect_image_failed));
                    }
                } else {
                    cardInfo.setResult(DFCardBaseInfo.DF_DETECTION_FAILED);
                    cardInfo.setReason(getString(R.string.ocr_scan_detect_image_failed));
                }
            } else {
                cardInfo.setResult(DFCardBaseInfo.DF_DETECTION_FAILED);
                cardInfo.setReason(getString(R.string.ocr_scan_detect_image_failed));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            cardInfo.setResult(DFCardBaseInfo.DF_DETECTION_FAILED);
            cardInfo.setReason(getString(R.string.ocr_scan_detect_image_failed));
        }
        return cardInfo;
    }

    private String getString(int stringResId) {
        Context activityContext = mRecognizeCallback.getActivityContext();
        String value = null;
        if (activityContext != null) {
            value = activityContext.getString(stringResId);
        }
        return value;
    }


    public interface DFRecognizeCallback {
        Context getActivityContext();

        void starLoading();

        void endLoading();

        void recognizeCardResult(DFCardInfo cardInfo);
    }
}
