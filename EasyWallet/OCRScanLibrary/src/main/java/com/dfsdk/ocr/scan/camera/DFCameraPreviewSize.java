package com.dfsdk.ocr.scan.camera;

public class DFCameraPreviewSize {
    private int previewWidth;
    private int previewHeight;
    private static volatile DFCameraPreviewSize mInstance;

    private DFCameraPreviewSize() {
        previewWidth = 1920;
        previewHeight = 1080;
    }

    public static DFCameraPreviewSize getInstance() {
        if (mInstance == null) {
            synchronized (DFCameraPreviewSize.class) {
                if (mInstance == null) {
                    mInstance = new DFCameraPreviewSize();
                }
            }
        }
        return mInstance;
    }

    public int getPreviewWidth() {
        return previewWidth;
    }

    public void setPreviewWidth(int previewWidth) {
        this.previewWidth = previewWidth;
    }

    public int getPreviewHeight() {
        return previewHeight;
    }

    public void setPreviewHeight(int previewHeight) {
        this.previewHeight = previewHeight;
    }
}
