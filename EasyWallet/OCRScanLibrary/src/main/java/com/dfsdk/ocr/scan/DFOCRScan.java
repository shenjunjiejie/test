package com.dfsdk.ocr.scan;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.dfsdk.ocr.scan.base.DFOCRScanBaseActivity;
import com.dfsdk.ocr.scan.network.model.DFCardInfo;

import java.lang.ref.WeakReference;

public class DFOCRScan {
    private final WeakReference<Activity> mActivity;

    private DFOCRScan(Activity activity) {
        mActivity = new WeakReference<>(activity);
    }

    /**
     * Start card detect for Activity.
     *
     * @param activity
     * @return DFOCRScan instance.
     */
    public static DFOCRScan create(Activity activity) {
        return new DFOCRScan(activity);
    }

    public DFOCRScanModel startCardDetect() {
        return new DFCardModel(this);
    }

    public static DFCardInfo obtainCardResult(Intent data) {
        DFCardInfo cardInfo = new DFCardInfo();
        if (data != null) {
            cardInfo = (DFCardInfo) data.getSerializableExtra(DFCardScanActivity.KEY_RESULT_DATA);
        }
        return cardInfo;
    }

    public static int obtainErrorCode(Intent data) {
        int errorCode = -1;
        if (data != null) {
            errorCode = data.getIntExtra(DFOCRScanBaseActivity.KEY_INIT_ERROR_CODE, -1);
        }
        return errorCode;
    }

    /**
     * @return Activity.
     */
    @Nullable
    Activity getActivity() {
        return mActivity.get();
    }
}
