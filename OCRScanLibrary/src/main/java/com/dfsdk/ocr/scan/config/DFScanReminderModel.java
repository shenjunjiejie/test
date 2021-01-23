package com.dfsdk.ocr.scan.config;

public class DFScanReminderModel {
    private int scanOverlayBorderColor;
    private int audioResId;
    private String scanHint;

    public DFScanReminderModel(int scanOverlayBorderColor, int audioResId, String scanHint) {
        this.scanOverlayBorderColor = scanOverlayBorderColor;
        this.audioResId = audioResId;
        this.scanHint = scanHint;
    }

    public int getScanOverlayBorderColor() {
        return scanOverlayBorderColor;
    }

    public void setScanOverlayBorderColor(int scanOverlayBorderColor) {
        this.scanOverlayBorderColor = scanOverlayBorderColor;
    }

    public int getAudioResId() {
        return audioResId;
    }

    public void setAudioResId(int audioResId) {
        this.audioResId = audioResId;
    }

    public String getScanHint() {
        return scanHint;
    }

    public void setScanHint(String scanHint) {
        this.scanHint = scanHint;
    }
}
