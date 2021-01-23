package com.dfsdk.ocr.scan.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.dfsdk.ocr.scan.R;
import com.dfsdk.ocr.scan.util.DFOCRScanUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DFScanOverlayView extends SurfaceView implements SurfaceHolder.Callback, Runnable, DFScanLineModel.DFScanLineModelCallback {
    private static final String TAG = "DFScanOverlayView";
    private ExecutorService mDetectorExecutor;
    private DFScanLineModel mScanLineModel;
    private float mMarginTop;
    private float mRoundRadius;
    private float mScanLineHeight;
    private float mScanRectHeight;
    private int mBackgroundColor;
    private int mBorderColor;
    private boolean mReDraw;

    public DFScanOverlayView(Context context) {
        super(context);
        init(null);
    }

    public DFScanOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public DFScanOverlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        this.setZOrderMediaOverlay(true);
        this.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        initAttributeSet(attrs);
        this.getHolder().addCallback(this);
        mReDraw = false;
    }

    private void initAttributeSet(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray attrArray = getContext().obtainStyledAttributes(attrs,
                    R.styleable.DFScanOverlayView);
            mMarginTop = attrArray.getDimension(R.styleable.DFScanOverlayView_scan_overlay_margin_top, 0);
            mRoundRadius = attrArray.getDimension(R.styleable.DFScanOverlayView_scan_overlay_round_radius, 0);
            mScanLineHeight = attrArray.getDimension(R.styleable.DFScanOverlayView_scan_overlay_scan_line_height, 0);
            mScanRectHeight = attrArray.getDimension(R.styleable.DFScanOverlayView_scan_overlay_scan_rect_height, 0);
            mBackgroundColor = attrArray.getColor(R.styleable.DFScanOverlayView_scan_overlay_bg_color, Color.TRANSPARENT);
            attrArray.recycle();

            initScanLineModel();
        }
    }

    private void initScanLineModel() {
        if (mScanLineModel == null) {
            mScanLineModel = new DFScanLineModel(this);
        }
        mScanLineModel.setBackgroundColor(mBackgroundColor)
                .setMarginTop(mMarginTop)
                .setRoundRadius(mRoundRadius)
                .setScannerLineHeight((int) mScanLineHeight)
                .setScanRectHeight(mScanRectHeight)
                .setBorderColor(mBorderColor);
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        super.layout(l, t, r, b);
        mReDraw = true;
    }

    private void startScanAnimation() {
        if (mDetectorExecutor == null) {
            mDetectorExecutor = Executors.newSingleThreadExecutor();
        }
        mDetectorExecutor.execute(this);
    }

    private void destroyScanAnimation() {
        if (mDetectorExecutor != null) {
            mDetectorExecutor.shutdownNow();
            try {
                mDetectorExecutor.awaitTermination(100, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mDetectorExecutor = null;
        }
    }

    private long mFrameTime = 30;

    @Override
    public void run() {
        while (mDetectorExecutor != null) {
            DFOCRScanUtils.logI(TAG, "run", "start");
            long runStartTime = System.currentTimeMillis();
            Canvas canvas = this.getHolder().lockCanvas();
            if (canvas != null) {
                if (mReDraw) {
                    if (mScanLineModel != null) {
                        mScanLineModel.invalidate();
                    }
                    mReDraw = false;
                }
                if (mScanLineModel != null) {
                    mScanLineModel.onDraw(canvas);
                }
                this.getHolder().unlockCanvasAndPost(canvas);
            }

            long drawSpaceTime = System.currentTimeMillis() - runStartTime;
            long sleepTime = mFrameTime - drawSpaceTime;
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
//            long runSpaceTime = System.currentTimeMillis() - runStartTime;
//            DFOCRScanUtils.logI(TAG, "run---------------------------", "time_statistical", "runSpaceTime", runSpaceTime);

        }
        destroyScanLineModel();
    }

    public void setBorderColor(int borderColor) {
        mBorderColor = borderColor;
        if (mScanLineModel != null) {
            mScanLineModel.setBorderColor(borderColor);
        }
    }

    public void showSuccessView() {
        if (mScanLineModel != null) {
            mScanLineModel.showSuccessView();
        }
    }

    public void stopScan() {
        destroyScanAnimation();
    }

    public void setScanOverlayCallback(DFScanOverlayView.DFScanOverlayCallback scanOverlayCallback) {
        if (mScanLineModel != null) {
            mScanLineModel.setScanOverlayCallback(scanOverlayCallback);
        }
    }

    private void destroyScanLineModel() {
        if (mScanLineModel != null) {
            mScanLineModel.destroy();
            mScanLineModel = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        initScanLineModel();
        startScanAnimation();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        destroyScanAnimation();
    }

    @Override
    public int getViewWidth() {
        return getWidth();
    }

    @Override
    public int getViewHeight() {
        return getHeight();
    }

    @Override
    public Context getActivityContext() {
        return getContext();
    }

    public Rect getScanRectF(float transScale) {
        return mScanLineModel != null ? mScanLineModel.getScanRectF(transScale) : null;
    }

    public interface DFScanOverlayCallback {
        void showSuccessViewEnd();
    }
}
