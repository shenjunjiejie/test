package com.dfsdk.ocr.scan.network.model;

import java.io.Serializable;

public class DFAadhaarCardInfo implements Serializable {
    private DFAadhaarCardFrontInfo frontInfo;
    private DFAadhaarCardBackInfo backInfo;

    public DFAadhaarCardFrontInfo getFrontInfo() {
        return frontInfo;
    }

    public void setFrontInfo(DFAadhaarCardFrontInfo frontInfo) {
        this.frontInfo = frontInfo;
    }

    public DFAadhaarCardBackInfo getBackInfo() {
        return backInfo;
    }

    public void setBackInfo(DFAadhaarCardBackInfo backInfo) {
        this.backInfo = backInfo;
    }
}
