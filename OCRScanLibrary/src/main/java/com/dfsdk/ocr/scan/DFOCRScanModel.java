package com.dfsdk.ocr.scan;

import com.dfsdk.ocr.scan.config.DFOCRScanConfig;

public abstract class DFOCRScanModel {
    protected DFOCRScan mOCRScan;
    protected DFOCRScanConfig mOCRScanConfig;

    public DFOCRScanModel(DFOCRScan ocrScan) {
        this.mOCRScan = ocrScan;
        mOCRScanConfig = DFOCRScanConfig.getCleanInstance();
    }


    public abstract void forResult(int requestCode);
}
