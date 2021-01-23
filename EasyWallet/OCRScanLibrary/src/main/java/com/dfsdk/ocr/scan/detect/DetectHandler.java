package com.dfsdk.ocr.scan.detect;

import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.dfsdk.ocr.scan.util.DFOCRScanUtils;

public class DetectHandler<T> extends Handler {
    private static final Object TAG = "DetectHandler";

    private static final int DETECT_INIT = 100;
    private static final int DETECT_PROCESS = 101;
    private static final int DETECT_RELEASE = 102;

    private DetectInterface<T> mDetector;
    private DetectCallback<T> mDetectCallback;
    private boolean mPause;

    public DetectHandler(Looper looper, @NonNull DetectInterface<T> detector,
                         @NonNull DetectCallback<T> detectCallback) {
        super(looper);
        mDetector = detector;
        mDetectCallback = detectCallback;
        sendEmptyMessage(DETECT_INIT);
    }

    public void detectFrame(byte[] data, int width, int height, Rect rect, int degree) {
        logI(TAG, "detect_process", "detectFrame");
        if (mPause) {
            return;
        }
        Message msg = obtainMessage(DETECT_PROCESS);
        DetectObject detectObject = new DetectObject();
        detectObject.data = data;
        detectObject.width = width;
        detectObject.height = height;
        detectObject.scanRect = rect;
        detectObject.degree = degree;
        msg.obj = detectObject;
        sendMessage(msg);
    }

    public void startDetect() {
        mPause = false;
    }

    public void stopDetect() {
        mPause = true;
    }

    public void releaseHandler() {
        sendEmptyMessage(DETECT_RELEASE);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case DETECT_INIT:
                initDetector();
                break;
            case DETECT_PROCESS:
                Object obj = msg.obj;
                detect((DetectObject) obj);
                break;
            case DETECT_RELEASE:
                releaseDetector();
                break;
        }
    }

    private void initDetector() {
        logI(TAG, "detect_process", "initDetector");
        if (mDetector != null) {
            int initResult = mDetector.init(mDetectCallback.getActivityContext());
            if (mDetectCallback != null) {
                mDetectCallback.initResult(initResult);
            }
        }
    }

    private void detect(DetectObject detectObject) {
        logI(TAG, "detect_process", "detect", "start");
        T detectResult = mDetector.detect(detectObject.data, detectObject.width, detectObject.height,
                detectObject.scanRect, detectObject.degree);
        logI(TAG, "detect_process", "detect", "end");
        T finishResult = mDetector.isFinish(detectResult);
        if (finishResult != null) {
            if (mDetectCallback != null) {
                mDetectCallback.detectEnd(finishResult);
            }
        } else {
            if (mDetectCallback != null) {
                mDetectCallback.detecting(detectResult);
            }
        }
    }

    private void releaseDetector() {
        logI(TAG, "detect_process", "detect", "releaseDetector");
        if (mDetector != null) {
            mDetector.release();
            mDetector = null;
        }
        removeCallbacksAndMessages(null);
    }

    private class DetectObject {
        public byte[] data;
        public int width;
        public int height;
        public Rect scanRect;
        public int degree;
    }

    private void logI(Object... logValue) {
        DFOCRScanUtils.logI(logValue);
    }
}
