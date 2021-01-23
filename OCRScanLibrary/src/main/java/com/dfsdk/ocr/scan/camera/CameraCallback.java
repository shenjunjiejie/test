package com.dfsdk.ocr.scan.camera;

public interface CameraCallback {
    int getActivityOrientation();

    void onPreviewFrame(byte[] data, CameraInterface cameraInterface);

    void openEnd();
}
