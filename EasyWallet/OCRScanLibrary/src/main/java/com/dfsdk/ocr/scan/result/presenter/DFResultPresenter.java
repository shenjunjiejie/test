package com.dfsdk.ocr.scan.result.presenter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.dfsdk.ocr.scan.R;
import com.dfsdk.ocr.scan.network.model.DFAadhaarCardBackInfo;
import com.dfsdk.ocr.scan.network.model.DFAadhaarCardFrontInfo;
import com.dfsdk.ocr.scan.network.model.DFCardBaseInfo;
import com.dfsdk.ocr.scan.network.model.DFCardInfo;
import com.dfsdk.ocr.scan.network.model.DFPanCardInfo;
import com.dfsdk.ocr.scan.result.adapter.model.DFResultInfoItem;

import java.util.ArrayList;
import java.util.List;

public class DFResultPresenter {

    private DFResultView mResultView;

    public DFResultPresenter(@NonNull DFResultView resultView) {
        this.mResultView = resultView;
    }

    public void dealCardInfo(DFCardInfo cardInfo) {
        List<DFResultInfoItem> resultInfoItemList = new ArrayList<>();

        boolean textExtractCheck = false;
        String textExtractedStr = getString(R.string.app_text_extracted);
        DFAadhaarCardFrontInfo aadhaarCardFrontInfo = null;
        DFAadhaarCardBackInfo aadhaarCardBackInfo = null;
        DFPanCardInfo panCardInfo = null;
        DFResultInfoItem singleImageItem = new DFResultInfoItem();
        singleImageItem.setInfoItemType(DFResultInfoItem.DFResultInfoItemType.TYPE_CARD_IMAGE_SINGLE);
        if (cardInfo != null) {
            textExtractCheck = cardInfo.getResult() == DFCardBaseInfo.DF_OK;
            if (!textExtractCheck) {
                textExtractedStr = cardInfo.getReason();
            }

            byte[] detectImage = cardInfo.getDetectImage();
            singleImageItem.setImage(BitmapFactory.decodeByteArray(detectImage, 0, detectImage.length));
            singleImageItem.setChecked(textExtractCheck);

            String cardType = cardInfo.getCardType();
            if (TextUtils.equals(cardType, DFCardInfo.CARD_TYPE_AADHAAR_CARD_FRONT)) {
                aadhaarCardFrontInfo = (DFAadhaarCardFrontInfo) cardInfo.getCardInfo();
            } else if (TextUtils.equals(cardType, DFCardInfo.CARD_TYPE_AADHAAR_CARD_BACK)) {
                aadhaarCardBackInfo = (DFAadhaarCardBackInfo) cardInfo.getCardInfo();
            } else if (TextUtils.equals(cardType, DFCardInfo.CARD_TYPE_PAN_CARD)) {
                panCardInfo = (DFPanCardInfo) cardInfo.getCardInfo();
            }
        }
        resultInfoItemList.add(singleImageItem);

        DFResultInfoItem textExtracted = new DFResultInfoItem();
        if (textExtractCheck) {
            textExtracted.setInfoItemType(DFResultInfoItem.DFResultInfoItemType.TYPE_EXTRACT_SUCCESS);
        } else {
            textExtracted.setInfoItemType(DFResultInfoItem.DFResultInfoItemType.TYPE_EXTRACT_FAIL);
        }
        textExtracted.setContent(textExtractedStr);
        textExtracted.setChecked(textExtractCheck);
        resultInfoItemList.add(textExtracted);

        if (aadhaarCardFrontInfo != null) {
            resultInfoItemList = addResultItem(resultInfoItemList, getString(R.string.app_card_type), getString(R.string.app_card_aadhaar));
            resultInfoItemList = addResultItem(resultInfoItemList, getString(R.string.app_card_side), getString(R.string.app_card_side_front));
            resultInfoItemList = addResultItem(resultInfoItemList, getString(R.string.app_aadhar_number), aadhaarCardFrontInfo.getCardNo());
            resultInfoItemList = addResultItem(resultInfoItemList, getString(R.string.app_aadhar_name), aadhaarCardFrontInfo.getName());
            resultInfoItemList = addResultItem(resultInfoItemList, getString(R.string.app_aadhar_mother_name), aadhaarCardFrontInfo.getMotherName());
            resultInfoItemList = addResultItem(resultInfoItemList, getString(R.string.app_aadhar_father_name), aadhaarCardFrontInfo.getFatherName());
            resultInfoItemList = addResultItem(resultInfoItemList, getString(R.string.app_aadhar_gender), aadhaarCardFrontInfo.getGender());
            resultInfoItemList = addResultItem(resultInfoItemList, aadhaarCardFrontInfo.getDateType(), aadhaarCardFrontInfo.getDateInfo());
            resultInfoItemList = addResultItem(resultInfoItemList, getString(R.string.app_aadhar_phone_number), aadhaarCardFrontInfo.getPhoneNumber());
            resultInfoItemList = addResultItem(resultInfoItemList, getString(R.string.app_aadhar_vid), aadhaarCardFrontInfo.getVid());
        }
        if (aadhaarCardBackInfo != null) {
            resultInfoItemList = addResultItem(resultInfoItemList, getString(R.string.app_card_type), getString(R.string.app_card_aadhaar));
            resultInfoItemList = addResultItem(resultInfoItemList, getString(R.string.app_card_side), getString(R.string.app_card_side_back));
            resultInfoItemList = addResultItem(resultInfoItemList, getString(R.string.app_aadhar_number), aadhaarCardBackInfo.getCardNo());
            resultInfoItemList = addResultItem(resultInfoItemList, getString(R.string.app_aadhar_vid), aadhaarCardBackInfo.getVid());
            resultInfoItemList = addResultItem(resultInfoItemList, getString(R.string.app_aadhar_s_o), aadhaarCardBackInfo.getSonOf());
            resultInfoItemList = addResultItem(resultInfoItemList, getString(R.string.app_aadhar_d_o), aadhaarCardBackInfo.getDaughterOf());
            resultInfoItemList = addResultItem(resultInfoItemList, getString(R.string.app_aadhar_c_o), aadhaarCardBackInfo.getCareOf());
            resultInfoItemList = addResultItem(resultInfoItemList, getString(R.string.app_aadhar_w_o), aadhaarCardBackInfo.getWifeOf());
            resultInfoItemList = addResultItem(resultInfoItemList, getString(R.string.app_aadhar_h_o), aadhaarCardBackInfo.getHusbandOf());
            resultInfoItemList = addResultItem(resultInfoItemList, getString(R.string.app_aadhar_city), aadhaarCardBackInfo.getCity());
            resultInfoItemList = addResultItem(resultInfoItemList, getString(R.string.app_aadhar_state), aadhaarCardBackInfo.getState());
            resultInfoItemList = addResultItem(resultInfoItemList, getString(R.string.app_aadhar_pin), aadhaarCardBackInfo.getPin());
            resultInfoItemList = addResultItem(resultInfoItemList, getString(R.string.app_aadhar_address), aadhaarCardBackInfo.getAddress());
            resultInfoItemList = addResultItem(resultInfoItemList, getString(R.string.app_aadhar_line_1), aadhaarCardBackInfo.getAddressLineOne());
            resultInfoItemList = addResultItem(resultInfoItemList, getString(R.string.app_aadhar_line_2), aadhaarCardBackInfo.getAddressLineTwo());
        }
        if (panCardInfo != null) {
            resultInfoItemList = addResultItem(resultInfoItemList, getString(R.string.app_card_type),
                    getString(R.string.app_card_pan));
            resultInfoItemList = addResultItem(resultInfoItemList, getString(R.string.app_pan_number),
                    panCardInfo.getCardNo());
            resultInfoItemList = addResultItem(resultInfoItemList, getString(R.string.app_pan_name),
                    panCardInfo.getName());
            resultInfoItemList = addResultItem(resultInfoItemList, getString(R.string.app_pan_father_name),
                    panCardInfo.getFatherName());
            resultInfoItemList = addResultItem(resultInfoItemList, panCardInfo.getDateType(),
                    panCardInfo.getDateInfo());
            resultInfoItemList = addResultItem(resultInfoItemList, getString(R.string.app_pan_date_of_issue),
                    panCardInfo.getIssueDate());
        }
        DFResultInfoItem tryAgainItem = new DFResultInfoItem();
        tryAgainItem.setInfoItemType(DFResultInfoItem.DFResultInfoItemType.TYPE_TRY_AGAIN);
        tryAgainItem.setContent(getString(R.string.app_try_again));
        resultInfoItemList.add(tryAgainItem);
        mResultView.returnResult(resultInfoItemList);
    }

    protected List<DFResultInfoItem> addResultItem(List<DFResultInfoItem> resultInfoItemList, String title, String content) {
        if (!TextUtils.isEmpty(content)) {
            DFResultInfoItem resultInfoItem = getUserInfoResult();
            resultInfoItem.setTitle(title);
            resultInfoItem.setContent(content);
            resultInfoItemList.add(resultInfoItem);
        }
        return resultInfoItemList;
    }

    protected DFResultInfoItem getUserInfoResult() {
        DFResultInfoItem resultItem = new DFResultInfoItem();
        resultItem.setInfoItemType(DFResultInfoItem.DFResultInfoItemType.TYPE_USER_INFO);
        return resultItem;
    }

    private String getString(int stringResId) {
        Context activityContext = mResultView.getActivityContext();
        String value = null;
        if (activityContext != null) {
            value = activityContext.getString(stringResId);
        }
        return value;
    }

    public interface DFResultView {
        Context getActivityContext();

        void returnResult(List<DFResultInfoItem> resultInfoItemList);
    }
}
