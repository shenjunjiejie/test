package com.dfsdk.ocr.scan;

import android.app.Activity;
import android.content.Intent;

public class DFCardModel extends DFOCRScanModel {

    public DFCardModel(DFOCRScan ocrScan) {
        super(ocrScan);
    }

    @Override
    public void forResult(int requestCode) {
        Activity activity = mOCRScan.getActivity();
        if (activity == null || mOCRScanConfig == null) {
            return;
        }
        Intent intent = new Intent(activity, DFCardScanActivity.class);

        activity.startActivityForResult(intent, requestCode);
    }
}
