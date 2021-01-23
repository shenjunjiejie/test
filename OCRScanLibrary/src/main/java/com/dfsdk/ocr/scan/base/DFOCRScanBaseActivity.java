package com.dfsdk.ocr.scan.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.HandlerThread;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.dfsdk.ocr.classify.sdk.DFOCRClassifyResult;
import com.dfsdk.ocr.classify.sdk.DFOCRClassifySDK;
import com.dfsdk.ocr.scan.R;
import com.dfsdk.ocr.scan.camera.CameraCallback;
import com.dfsdk.ocr.scan.camera.CameraInterface;
import com.dfsdk.ocr.scan.camera.CameraV1;
import com.dfsdk.ocr.scan.camera.FixedAspectRatioFrameLayout;
import com.dfsdk.ocr.scan.classify.OCRClassifySDKDetector;
import com.dfsdk.ocr.scan.config.DFSDKInitErrorModel;
import com.dfsdk.ocr.scan.config.DFScanReminderModel;
import com.dfsdk.ocr.scan.detect.DetectCallback;
import com.dfsdk.ocr.scan.detect.DetectHandler;
import com.dfsdk.ocr.scan.util.DFMediaPlayer;
import com.dfsdk.ocr.scan.util.DFOCRScanUtils;
import com.dfsdk.ocr.scan.util.DFStatusBarCompat;
import com.dfsdk.ocr.scan.view.DFOCRScanViewUtils;
import com.dfsdk.ocr.scan.view.DFScanOverlayView;

import java.util.HashMap;
import java.util.Map;

public class DFOCRScanBaseActivity extends DFOCRBaseActivity implements SurfaceHolder.Callback, CameraCallback, DetectCallback<DFOCRClassifyResult>, DFScanOverlayView.DFScanOverlayCallback {
    private static final String TAG = "DFOCRScanBaseActivity";

    public static final String KEY_RESULT_DATA = "key_result_data";
    public static final String KEY_INIT_ERROR_CODE = "key_init_error_code";

    private DFScanOverlayView mScanOverlayView;
    private TextView mTvReminder;
    protected View mVLoading;

    private CameraInterface mCamera;
    private DetectHandler<DFOCRClassifyResult> mDetectHandler;
    private HandlerThread mHandlerThread;
    private DFMediaPlayer mMediaPlayer;
    private float mTransScale;
    private int mDetectResult = -1;

    private Map<String, DFScanReminderModel> mScanReminderMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ocr_scan_activity_scan_base);

        initView();
        initPresenter();
        initReminderMap();
        DFStatusBarCompat.translucentStatusBar(this, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DFOCRScanUtils.logI(TAG, "life_style", "onResume");
        mDetectResult = -1;
        initDetect();
        initAudioPlay();
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseDetect();
        releaseGuideAudio();
        DFOCRScanUtils.logI(TAG, "life_style", "onPause");
    }

    protected void initView() {
        initTitle(getTitleResId());
        SurfaceView svCamera = findViewById(R.id.id_sv_camera);
        mScanOverlayView = findViewById(R.id.id_scan_view);
        mTvReminder = findViewById(R.id.id_tv_scan_reminder);
        mVLoading = findViewById(R.id.id_include_loading);
        svCamera.getHolder().addCallback(this);
    }

    protected int getTitleResId() {
        return R.string.ocr_scan_activity_title;
    }

    protected void initPresenter() {

    }

    private void initReminderMap() {
        if (mScanReminderMap == null) {
            mScanReminderMap = new HashMap<>();
            mScanReminderMap.put("0", new DFScanReminderModel(getColorByResId(R.color.ocr_scan_border_ok),
                    -1, getString(R.string.ocr_scan_reminder_ok)));
            mScanReminderMap.put("1", new DFScanReminderModel(getColorByResId(R.color.ocr_scan_border_error),
                    R.raw.audio_no_card, getString(R.string.ocr_scan_reminder_no_card)));
            mScanReminderMap.put("2", new DFScanReminderModel(getColorByResId(R.color.ocr_scan_border_not_clear),
                    -1, getString(R.string.ocr_scan_reminder_no_clear)));
            mScanReminderMap.put("3", new DFScanReminderModel(getColorByResId(R.color.ocr_scan_border_error),
                    -1, getString(R.string.ocr_scan_reminder_scan_back)));
        }
    }

    private void initDetect() {
        if (mHandlerThread == null) {
            mHandlerThread = new HandlerThread("DetectHandler");
            mHandlerThread.start();
        }
        if (mDetectHandler == null) {
            OCRClassifySDKDetector classifySDKDetector = new OCRClassifySDKDetector(DFOCRScanUtils.MEET_COUNT,
                    DFOCRScanUtils.MEET_THRESHOLD);
            mDetectHandler = new DetectHandler<DFOCRClassifyResult>(mHandlerThread.getLooper(),
                    classifySDKDetector, this);
        }
    }

    protected void startDetect() {
        if (mDetectHandler != null) {
            mDetectHandler.startDetect();
        }
        addCallbackBuffer();
    }

    protected void stopDetect() {
        if (mDetectHandler != null) {
            mDetectHandler.stopDetect();
        }
    }

    private void releaseDetect() {
        if (mDetectHandler != null) {
            mDetectHandler.releaseHandler();
            mDetectHandler = null;
        }
        if (mHandlerThread != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                mHandlerThread.quitSafely();
            } else {
                mHandlerThread.quit();
            }
            mHandlerThread = null;
        }
    }

    private void initCamera() {
        mCamera = new CameraV1();
        mCamera.initCamera(this);
    }

    private void openCamera(SurfaceHolder holder) {
        if (mCamera != null) {
            mCamera.openCamera(holder);
        }
    }

    protected void startPreview() {
        if (mCamera != null) {
            mCamera.startPreview();
        }
    }

    protected void addCallbackBuffer() {
        if (mCamera != null) {
            mCamera.addCallbackBuffer();
        }
    }

    protected void stopPreview() {
        if (mCamera != null) {
            mCamera.stopPreview();
        }
    }

    protected void releaseCamera() {
        if (mCamera != null) {
            mCamera.releaseCamera();
        }
    }

    protected void removeSurfaceCallback() {
        SurfaceView svCamera = findViewById(R.id.id_sv_camera);
        svCamera.getHolder().removeCallback(this);
    }

    private void initAudioPlay() {
        if (mMediaPlayer == null) {
            mMediaPlayer = new DFMediaPlayer();
        }
    }

    private void startAudio(int audioResId) {
        if (mMediaPlayer != null) {
            DFOCRScanUtils.logI(TAG, "startAudio", "audioResId", audioResId);
            if (audioResId != -1) {
                mMediaPlayer.setMediaSource(this, audioResId);
            } else {
                mMediaPlayer.removeRepeatPlay();
            }
        }
    }

    protected void stopGuideAudio() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
        }
    }

    protected void releaseGuideAudio() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    protected void showLoadingDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DFOCRScanViewUtils.refreshVisibility(mVLoading, true);
                initLoadingView(mVLoading);
            }
        });
    }

    private void initLoadingView(View view) {
        View vProgress = view.findViewById(R.id.id_iv_progress_spinner);
        RotateAnimation rotateAnimation = new RotateAnimation(0f, 359, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(700);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        vProgress.startAnimation(rotateAnimation);
    }

    protected void hideLoadingDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DFOCRScanViewUtils.refreshVisibility(mVLoading, false);
            }
        });
    }

    private void reDetectFrame() {
        startPreview();
    }

    protected void showSuccessView() {
        if (mScanOverlayView != null) {
            mScanOverlayView.showSuccessView();
            mScanOverlayView.setScanOverlayCallback(this);
        }
    }

    protected void stopScan() {
        if (mScanOverlayView != null) {
            mScanOverlayView.stopScan();
        }
    }

    protected void selectImageEnd(DFOCRClassifyResult ocrClassifyResult) {
        int classifyResult = ocrClassifyResult.getClassifyResult();
        float classifyScore = ocrClassifyResult.getClassifyScore();
        Bitmap detectImage = ocrClassifyResult.getDetectImage();
        DFOCRScanUtils.logI(TAG, "showClassifyView", "classifyResult,classifyScore", classifyResult, classifyScore);
//        mIvDebug.setImageBitmap(detectImage);
    }

    protected void detectItem(DFOCRClassifyResult ocrClassifyResult) {
//        debugPreviewImage(ocrClassifyResult);
        int classifyResult = ocrClassifyResult.getClassifyResult();
        float classifyScore = ocrClassifyResult.getClassifyScore();
        DFOCRScanUtils.logI(TAG, "detectItem", "classifyResult,classifyScore", classifyResult, classifyScore);
        int detectResult = getDetectResult(ocrClassifyResult);
        refreshReminderView(detectResult);
        addCallbackBuffer();
//        debugPreviewImage(ocrClassifyResult);
    }

    private void debugPreviewImage(DFOCRClassifyResult ocrClassifyResult) {
        ImageView ivDebug = findViewById(R.id.id_iv_debug);
        ivDebug.setImageBitmap(ocrClassifyResult.getDetectImage());
    }

    protected void refreshReminderView(int detectResult) {
        DFOCRScanUtils.logI(TAG, "detectItem", "refreshReminderView", detectResult);
        if (mDetectResult != detectResult) {
            mDetectResult = detectResult;
            DFScanReminderModel scanReminderModel = mScanReminderMap.get(String.valueOf(detectResult));
            if (scanReminderModel != null) {
                int scanOverlayBorderColor = scanReminderModel.getScanOverlayBorderColor();
                if (mScanOverlayView != null) {
                    mScanOverlayView.setBorderColor(scanOverlayBorderColor);
                }
                int audioResId = scanReminderModel.getAudioResId();
                startAudio(audioResId);
                String scanHint = scanReminderModel.getScanHint();
                DFOCRScanUtils.logI(TAG, "detectItem", "detectResult,audioResId,scanHint", detectResult, audioResId, scanHint);
                DFOCRScanViewUtils.refreshText(mTvReminder, scanHint);
                mTvReminder.setTextColor(scanOverlayBorderColor);
            }
        }
    }

    private int getDetectResult(DFOCRClassifyResult ocrClassifyResult) {
        int classifyResult = ocrClassifyResult.getClassifyResult();
        float classifyScore = ocrClassifyResult.getClassifyScore();
        int detectResult = classifyResult;
        if (classifyResult == DFOCRClassifySDK.IMAGE_QUALIFIED) {
            detectResult = 0;
        } else if (classifyResult == DFOCRClassifySDK.IMAGE_BACKGROUND) {
            detectResult = 1;
        } else {
            detectResult = 2;
        }
        return detectResult;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        DFOCRScanUtils.logI(TAG, "surfaceCreated");
        initCamera();
        openCamera(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        DFOCRScanUtils.logI(TAG, "surfaceChanged", "format,width,height", format, width, height);
        Camera.Size previewSize = mCamera.getPreviewSize();
        DFOCRScanUtils.logI(TAG, "surfaceChanged", "previewSize,width,height", previewSize.width, previewSize.height);
        mTransScale = (previewSize.height + 0.0f) / width;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        DFOCRScanUtils.logI(TAG, "surfaceDestroyed");
        releaseCamera();
    }

    @Override
    public void initResult(int result) {
        DFOCRScanUtils.logI(TAG, "initResult", result);
        if (result == DFOCRClassifySDK.DF_OK) {
            addCallbackBuffer();
        } else {
            initDetectError(result);
        }
    }

    private void initDetectError(int result) {
        DFSDKInitErrorModel sdkInitErrorModel = DFSDKInitErrorModel.getSDKInitErrorModel(result);
        if (sdkInitErrorModel != null) {
            int errorHintResId = sdkInitErrorModel.getErrorHintResId();
            DFOCRScanUtils.showToast(this, getString(errorHintResId));
        }

        Intent data = new Intent();
        data.putExtra(KEY_INIT_ERROR_CODE, result);
        setResult(RESULT_CANCELED, data);
        finish();
    }

    @Override
    public void detecting(final DFOCRClassifyResult ocrClassifyResult) {
        DFOCRScanUtils.logI(TAG, "detecting", ocrClassifyResult.getClassifyScore());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                detectItem(ocrClassifyResult);
            }
        });
    }

    @Override
    public int getActivityOrientation() {
        return getResources().getConfiguration().orientation;
    }

    @Override
    public Context getActivityContext() {
        return this;
    }

    @Override
    public void onPreviewFrame(byte[] data, CameraInterface cameraInterface) {
        DFOCRScanUtils.logI(TAG, "preview_info", "detect_process", "onPreviewFrame");
        if (mDetectHandler != null) {
            Camera.Size previewSize = cameraInterface.getPreviewSize();
            Camera.CameraInfo cameraInfo = cameraInterface.getCameraInfo();
            int orientation = cameraInfo.orientation;
            DFOCRScanUtils.logI(TAG, "preview_info", "detect_process", "orientation", orientation, "mTransScale", mTransScale);
            Rect scanRect = mScanOverlayView.getScanRectF(mTransScale);
            if (scanRect != null) {
                mDetectHandler.detectFrame(data, previewSize.width, previewSize.height, scanRect, orientation);
            } else {
                addCallbackBuffer();
            }
        }
    }

    @Override
    public void openEnd() {
        DFOCRScanUtils.logI(TAG, "screen_info", "openEnd");
        FixedAspectRatioFrameLayout previewLayout = findViewById(R.id.id_frfl_preview);
        previewLayout.requestLayout();
    }

    @Override
    public void detectEnd(DFOCRClassifyResult ocrClassifyResult) {
        DFOCRScanUtils.logI(TAG, "classifyEnd", ocrClassifyResult.getClassifyScore());
        selectImageEnd(ocrClassifyResult);
    }

    private void save(DFOCRClassifyResult saveClassifyResult) {
//        DFOCRScanUtils.showToast(this, "save image");

        Bitmap detectImage = saveClassifyResult.getDetectImage();
        byte[] jpeg = DFOCRScanUtils.convertBmpToJpeg(detectImage);
    }

    @Override
    public void showSuccessViewEnd() {

    }
}
