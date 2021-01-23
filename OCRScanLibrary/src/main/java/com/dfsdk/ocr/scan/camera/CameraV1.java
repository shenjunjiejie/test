package com.dfsdk.ocr.scan.camera;

import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.view.SurfaceHolder;

import androidx.annotation.NonNull;

import com.dfsdk.ocr.scan.util.DFOCRScanUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CameraV1 implements CameraInterface, Camera.PreviewCallback {
    private static final String TAG = "CameraV1";

    private Camera mCamera;
    private Camera.CameraInfo mCameraInfo;
    private CameraCallback mCameraCallback;
    private int mCameraId;
    private byte[] mPreviewCallbackData;

    @Override
    public void initCamera(@NonNull CameraCallback cameraCallback) {
        mCameraCallback = cameraCallback;
        mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
    }

    @Override
    public void openCamera(@NonNull SurfaceHolder holder) {
        releaseCamera();
        openCamera(mCameraId);
        setPreviewDisplay(holder);
        initCameraParameters();
        startPreview();
        initPreviewCallbackData();
        addCallbackBuffer();
        if (mCameraCallback != null){
            mCameraCallback.openEnd();
        }
    }

    @Override
    public void startPreview() {
        if (mCamera != null) {
            mCamera.startPreview();
        }
        setPreviewCallbackWithBuffer();
    }

    @Override
    public void stopPreview() {
        DFOCRScanUtils.logI(TAG, "preview_info", "stopPreview");
        if (mCamera != null) {
            mCamera.stopPreview();
        }
    }

    @Override
    public void releaseCamera() {
        stopPreview();
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
        releasePreviewCallbackData();
    }

    @Override
    public void addCallbackBuffer() {
        if (mCamera != null) {
            DFOCRScanUtils.logI(TAG, "preview_info", "addCallbackBuffer", "mPreviewCallbackData", (mPreviewCallbackData == null));
            mCamera.addCallbackBuffer(mPreviewCallbackData);
        }
    }

    @Override
    public Camera.CameraInfo getCameraInfo() {
        return mCameraInfo;
    }

    @Override
    public Camera.Size getPreviewSize() {
        Camera.Size size = null;
        if (mCamera != null) {
            Camera.Parameters parameters = mCamera.getParameters();
            size = parameters.getPreviewSize();
        }
        return size;
    }

    private void openCamera(int cameraId) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
            Camera.getCameraInfo(i, info);
            try {
                if (info.facing == cameraId) {
                    mCamera = Camera.open(i);
                    mCameraInfo = info;
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
                releaseCamera();
                continue;
            }
            break;
        }
    }

    private void setPreviewCallbackWithBuffer() {
        if (mCamera != null) {
            mCamera.setPreviewCallbackWithBuffer(this);
        }
    }

    private void setPreviewDisplay(SurfaceHolder holder) {
        if (mCamera != null) {
            try {
                mCamera.setPreviewDisplay(holder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initPreviewCallbackData() {
        if (mPreviewCallbackData == null) {
            Camera.Size previewSize = getPreviewSize();
            mPreviewCallbackData = new byte[previewSize.width * previewSize.height * 3 / 2];
        }
    }

    private void releasePreviewCallbackData() {
        mPreviewCallbackData = null;
    }

    protected void initCameraParameters() {
        if (mCamera == null) {
            logI(TAG, "initCameraParameters", "mCamera == null");
            return;
        }
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPreviewFormat(ImageFormat.NV21);
        int previewWidth = DFCameraPreviewSize.getInstance().getPreviewWidth();
        int previewHeight = DFCameraPreviewSize.getInstance().getPreviewHeight();
        List<Camera.Size> supportedPreviewSizes = getSupportPreviewList(parameters);
        if (supportedPreviewSizes.size() > 0) {
            Camera.Size previewSize = supportedPreviewSizes.get(0);
            previewWidth = previewSize.width;
            previewHeight = previewSize.height;
            DFCameraPreviewSize.getInstance().setPreviewWidth(previewWidth);
            DFCameraPreviewSize.getInstance().setPreviewHeight(previewHeight);
        }
        logI(TAG, "screen_info", "initCameraParameters");
        parameters.setPreviewSize(previewWidth, previewHeight);
        if (parameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        }
        parameters.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);
        if (mCameraCallback.getActivityOrientation() == Configuration.ORIENTATION_PORTRAIT) {
            parameters.set("orientation", "portrait");
            parameters.set("rotation", 90);
            if (mCameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT && mCameraInfo.orientation == 90) {
                mCamera.setDisplayOrientation(270);
            } else {
                mCamera.setDisplayOrientation(90);
            }
        } else {
            parameters.set("orientation", "landscape");
            mCamera.setDisplayOrientation(0);
        }
        mCamera.setParameters(parameters);
    }

    private List<Camera.Size> getSupportPreviewList(Camera.Parameters parameters) {
        List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
        Collections.sort(supportedPreviewSizes, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size o1, Camera.Size o2) {
                return o2.width - o1.width;
            }
        });
        return supportedPreviewSizes;
    }

    private boolean isSupportPreviewSize(Camera.Parameters parameters, int previewWidth, int previewHeight) {
        boolean isSupport = false;
        if (parameters != null) {
            List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
            if (supportedPreviewSizes != null) {
                int size = supportedPreviewSizes.size();
                for (int i = 0; i < size; i++) {
                    Camera.Size previewSize = supportedPreviewSizes.get(i);
                    if (previewSize != null) {
                        if (previewSize.width == previewWidth && previewSize.height == previewHeight) {
                            isSupport = true;
                        }
                    }
                }
            }
        }
        return isSupport;
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (mCameraCallback != null) {
            mCameraCallback.onPreviewFrame(data, this);
        }
    }

    private void logI(Object... logValue) {
        DFOCRScanUtils.logI(logValue);
    }
}
