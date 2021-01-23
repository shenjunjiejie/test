package com.dfsdk.ocr.scan;

import android.content.Intent;

import com.dfsdk.ocr.classify.sdk.DFOCRClassifyResult;
import com.dfsdk.ocr.scan.base.DFOCRScanBaseActivity;
import com.dfsdk.ocr.scan.network.model.DFCardInfo;
import com.dfsdk.ocr.scan.presenter.DFCardRecognizePresenter;
import com.dfsdk.ocr.scan.util.DFOCRScanUtils;

public class DFCardScanActivity extends DFOCRScanBaseActivity implements DFCardRecognizePresenter.DFRecognizeCallback {
    private static final Object TAG = "DFAadhaarCardScanActivity";

    private DFCardRecognizePresenter mCardRecognizePresenter;

    @Override
    protected int getTitleResId() {
        return R.string.ocr_scan_activity_title_card;
    }

    @Override
    protected void initPresenter() {
        super.initPresenter();
        if (mCardRecognizePresenter == null) {
            mCardRecognizePresenter = new DFCardRecognizePresenter(this);
        }
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void selectImageEnd(DFOCRClassifyResult ocrClassifyResult) {
        stopGuideAudio();
        stopPreview();
        stopDetect();
        recognizeCard(ocrClassifyResult);
    }

    private void recognizeCard(DFOCRClassifyResult ocrClassifyResult) {
        DFOCRScanUtils.logI(TAG, "recognizeAadhaarCard", "start recognize");
        stopScan();
        releaseCamera();
        removeSurfaceCallback();
        mCardRecognizePresenter.recognizeCard(ocrClassifyResult.getDetectImage());
    }

    @Override
    public void starLoading() {
        showLoadingDialog();
    }

    @Override
    public void endLoading() {
        hideLoadingDialog();
    }

    @Override
    public void recognizeCardResult(DFCardInfo cardInfo) {
        Intent intent = new Intent();
        intent.putExtra(KEY_RESULT_DATA, cardInfo);
        setResult(RESULT_OK, intent);
        finish();
    }
}
