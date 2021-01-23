package com.dfsdk.ocr.scan.network.model;

import java.io.Serializable;

public class DFCardBaseInfo implements Serializable {
    public static final int DF_OK = 0;
    public static final int DF_DETECTION_FAILED = 400;
    private int result;
    private String reason;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
