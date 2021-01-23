package com.dfsdk.ocr.scan.config;

import com.dfsdk.ocr.classify.sdk.DFOCRClassifySDK;
import com.dfsdk.ocr.scan.R;

public enum DFSDKInitErrorModel {
    DF_LICENSE_OUT_OF_DATE(DFOCRClassifySDK.DF_LICENSE_OUT_OF_DATE, R.string.ocr_scan_license_expired),
    DF_LICENSE_INVALID_BUNDLE(DFOCRClassifySDK.DF_LICENSE_INVALID_BUNDLE, R.string.ocr_scan_bundle_application_id_error),
    DF_LICENSE_INVALID(DFOCRClassifySDK.DF_LICENSE_INVALID, R.string.ocr_scan_license_invalid);
    private int errorCode;
    private int errorHintResId;

    DFSDKInitErrorModel(int errorCode, int errorHintResId) {
        this.errorCode = errorCode;
        this.errorHintResId = errorHintResId;
    }

    public static DFSDKInitErrorModel getSDKInitErrorModel(int errorCode) {
        for (DFSDKInitErrorModel initErrorModel : DFSDKInitErrorModel.values()) {
            if (errorCode == initErrorModel.errorCode) {
                return initErrorModel;
            }
        }
        return null;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public int getErrorHintResId() {
        return errorHintResId;
    }
}
