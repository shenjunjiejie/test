package com.dfsdk.ocr.scan.camera;

import android.hardware.Camera;
import android.view.SurfaceHolder;

import androidx.annotation.NonNull;

public interface CameraInterface {
    void initCamera(@NonNull CameraCallback cameraCallback);

    void openCamera(@NonNull SurfaceHolder holder);

    void startPreview();

    void stopPreview();

    void releaseCamera();

    void addCallbackBuffer();

    Camera.CameraInfo getCameraInfo();

    Camera.Size getPreviewSize();
}
