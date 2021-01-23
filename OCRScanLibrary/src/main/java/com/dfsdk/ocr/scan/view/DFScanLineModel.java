package com.dfsdk.ocr.scan.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;

import com.dfsdk.ocr.scan.R;
import com.dfsdk.ocr.scan.util.DFOCRScanUtils;

public class DFScanLineModel {
    private static final Object TAG = "DFScanLineModel";

    private static final int SCANNER_LINE_HEIGHT = 100;  //scan line width
    private static final int SCANNER_LINE_MOVE_DISTANCE = 12;  //scan move distance

    private DFScanLineModelCallback mScanLineModelCallback;
    private DFScanOverlayView.DFScanOverlayCallback mScanOverlayCallback;

    /**
     * region of background
     */
    private Path mLockedBackgroundPath;
    private Path mScanBorderPath;
    /**
     * background's mLaserPaint
     */
    private Paint mLockedBackgroundPaint;

    private Paint mLaserPaint;
    private Paint mBorderPaint;
    private Paint mBitmapPaint;
    private Paint mSuccessBgPaint;

    private RectF mHollowRectF;

    private int mBackgroundColor;
    private int mBorderColor;
    private float mMarginTop;
    private float mRoundRadius;
    private int mScannerLineHeight;
    private float mScanRectHeight;

    private int mCurrentScanner;
    private Path mClipScanRectPath;

    private Bitmap mSuccessIcon;
    private boolean mShowSuccessIcon;
    private float mSuccessIconWidth;
    private float mCurrentSuccessIconWidth;
    private float mEveryChangeWidth;
    private Handler mMainHandler;
    private Bitmap mLaserBitmap;
    private Matrix mLaserMatrix;

    public DFScanLineModel(@NonNull DFScanLineModelCallback scanLineModelCallback) {
        this.mScanLineModelCallback = scanLineModelCallback;
        mScannerLineHeight = SCANNER_LINE_HEIGHT;
        mSuccessIcon = BitmapFactory.decodeResource(scanLineModelCallback.getActivityContext().getResources(),
                R.mipmap.ocr_scan_group);
        mLaserBitmap = BitmapFactory.decodeResource(scanLineModelCallback.getActivityContext().getResources(),
                R.mipmap.ocr_scan_laser);
        mCurrentSuccessIconWidth = 0;
        mSuccessIconWidth = 80;
        mEveryChangeWidth = 6;
    }

    public void invalidate() {
        releaseDrawPath();
    }

    private void initPaint() {
        if (mLockedBackgroundPaint == null) {
            mLockedBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mLockedBackgroundPaint.clearShadowLayer();
            mLockedBackgroundPaint.setStyle(Paint.Style.FILL);
            mLockedBackgroundPaint.setColor(mBackgroundColor);
        }

        if (mLaserPaint == null) {
            mLaserPaint = new Paint();
            mLaserPaint.setAntiAlias(true);
        }
        if (mBorderPaint == null) {
            mBorderPaint = new Paint();
            mBorderPaint.setAntiAlias(true);
            mBorderPaint.setColor(Color.WHITE);
            mBorderPaint.setStyle(Paint.Style.STROKE);
            mBorderPaint.setStrokeWidth(dip2px(6));
        }
        if (mBitmapPaint == null) {
            mBitmapPaint = new Paint();
            mBitmapPaint.setAntiAlias(true);
        }
        if (mSuccessBgPaint == null) {
            mSuccessBgPaint = new Paint();
            mSuccessBgPaint.setAntiAlias(true);
            mSuccessBgPaint.setStyle(Paint.Style.FILL);
        }
    }

    private void initBackgroundPath() {
        if (mLockedBackgroundPath == null) {
            mLockedBackgroundPath = new Path();
            int width = mScanLineModelCallback.getViewWidth();
            int height = mScanLineModelCallback.getViewHeight();
            mLockedBackgroundPath.addRect(new RectF(0, 0, width, height), Path.Direction.CCW);
            DFOCRScanUtils.logI(TAG, "initBackgroundPath,mMarginTop", mMarginTop);
            DisplayMetrics displayMetrics = mScanLineModelCallback.getActivityContext().getResources().getDisplayMetrics();
            float scanHalfWidth = displayMetrics.widthPixels * 0.47f;
            float centerX = width / 2;
            mHollowRectF = new RectF(centerX - scanHalfWidth, mMarginTop,
                    centerX + scanHalfWidth, mMarginTop + mScanRectHeight);
            DFOCRScanUtils.logI(TAG, "initBackgroundPath.l,t,r,b", mHollowRectF.left, mHollowRectF.top, mHollowRectF.right, mHollowRectF.bottom);
            mLockedBackgroundPath.addRoundRect(mHollowRectF, mRoundRadius, mRoundRadius, Path.Direction.CW);
        }
        if (mClipScanRectPath == null) {
            mClipScanRectPath = new Path();
            mClipScanRectPath.addRoundRect(mHollowRectF, mRoundRadius, mRoundRadius, Path.Direction.CW);

            mCurrentScanner = mScannerLineHeight;
        }
        if (mScanBorderPath == null) {
            mScanBorderPath = new Path();
            mScanBorderPath.addRoundRect(mHollowRectF, mRoundRadius, mRoundRadius, Path.Direction.CCW);
        }
    }

//    private int mBackgroundDrawCount;

    public void onDraw(Canvas canvas) {
        initPaint();
        initBackgroundPath();

//        long detectStartTime = System.currentTimeMillis();
//        if (mBackgroundDrawCount <= 10) {
//        DFOCRScanUtils.logI(TAG, "time_statistical", "mBackgroundDrawCount", mBackgroundDrawCount);
//        mBackgroundDrawCount++;
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        canvas.drawPath(mLockedBackgroundPath, mLockedBackgroundPaint);
//        }
//        long detectSpaceTime = System.currentTimeMillis() - detectStartTime;
//        DFOCRScanUtils.logI(TAG, "time_statistical", "mLockedBackgroundPath", detectSpaceTime / 1000.0f);
        if (mShowSuccessIcon) {
            drawSuccessView(canvas);
        } else {
//            detectStartTime = System.currentTimeMillis();
            drawLaserScanner(canvas);
//            detectSpaceTime = System.currentTimeMillis() - detectStartTime;
//            DFOCRScanUtils.logI(TAG, "time_statistical", "drawLaserScanner", detectSpaceTime / 1000.0f);
        }
        canvas.drawPath(mScanBorderPath, mBorderPaint);
    }

    //draw laser scanner
    private void drawLaserScanner(Canvas canvas) {
        canvas.save();
        if (mLaserMatrix == null) {
            mLaserMatrix = new Matrix();
            int width = mLaserBitmap.getWidth();
            int height = mLaserBitmap.getHeight();
            float scanRectWidth = mHollowRectF.width();
            float scaleX = scanRectWidth / width;
            DFOCRScanUtils.logI(TAG, "drawLaserScanner", "scanRectWidth", scanRectWidth, "width", width);
            DFOCRScanUtils.logI(TAG, "drawLaserScanner", "scaleX", scaleX);
            mLaserMatrix.postScale(scaleX, scaleX);
            mLaserMatrix.postTranslate(mHollowRectF.left, mHollowRectF.top);
            mScannerLineHeight = (int) (height * scaleX);
        }

        canvas.clipPath(mClipScanRectPath);
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        canvas.translate(0, mCurrentScanner);
        float height = mHollowRectF.height();
        if (mCurrentScanner >= height + dip2px(6)) {
            mCurrentScanner = -mScannerLineHeight - dip2px(6);
        }
        canvas.drawBitmap(mLaserBitmap, mLaserMatrix, mLaserPaint);
        mCurrentScanner += SCANNER_LINE_MOVE_DISTANCE;
        canvas.restore();
    }

    private void drawSuccessView(Canvas canvas) {
        Matrix matrix = new Matrix();
        int width = mSuccessIcon.getWidth();
        int height = mSuccessIcon.getHeight();
        int showWidth = dip2px(mCurrentSuccessIconWidth);
        float sx = (showWidth + 0.0f) / width;
        float sy = (showWidth + 0.0f) / height;
        matrix.postScale(sx, sy);
        float left = mHollowRectF.left + mHollowRectF.width() / 2 - showWidth / 2;
        float top = mHollowRectF.top + mHollowRectF.height() / 2 - showWidth / 2;
        matrix.postTranslate(left, top);

        float currentScale = mCurrentSuccessIconWidth / mSuccessIconWidth;
        float successBgRadius = mRoundRadius * currentScale;
        int successBgColor = getSuccessBgColor(currentScale * 0.7f);
        mSuccessBgPaint.setColor(successBgColor);

        canvas.drawRoundRect(mHollowRectF, successBgRadius, successBgRadius, mSuccessBgPaint);
        canvas.drawBitmap(mSuccessIcon, matrix, mBitmapPaint);

        mCurrentSuccessIconWidth += mEveryChangeWidth;
        if (mCurrentSuccessIconWidth >= mSuccessIconWidth) {
            mCurrentSuccessIconWidth = mSuccessIconWidth;
            if (mMainHandler == null) {
                mMainHandler = new Handler(Looper.getMainLooper());
                mMainHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mEveryChangeWidth = -mEveryChangeWidth;
                    }
                }, 1000);
            }
        }
        if (mCurrentSuccessIconWidth <= 0) {
            mShowSuccessIcon = false;
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mScanOverlayCallback != null) {
                        mScanOverlayCallback.showSuccessViewEnd();
                    }
                }
            });

        }
    }

    public int shadeColor(int color) {
        String hax = Integer.toHexString(color);
        String result = "10" + hax.substring(2);
        return Integer.valueOf(result, 16);
    }

    private int getSuccessBgColor(float percent) {
        int bgColor = Color.WHITE;
        String hax = Integer.toHexString(bgColor);
        int alpha = (int) (percent * 255);
        if (alpha > 255) {
            alpha = 255;
        }
        if (alpha < 0) {
            alpha = 0;
        }
        String alphaHax = Integer.toHexString(alpha);
        if (alphaHax.length() == 1) {
            alphaHax = "0" + alphaHax;
        }
        DFOCRScanUtils.logI(TAG, "getSuccessBgColor", "alphaHax", alphaHax);
        String result = "#" + alphaHax + hax.substring(2);

        DFOCRScanUtils.logI(TAG, "getSuccessBgColor", "result", result);
        return Color.parseColor(result);
    }

    public DFScanLineModel setBackgroundColor(int backgroundColor) {
        this.mBackgroundColor = backgroundColor;
        return this;
    }

    public DFScanLineModel setMarginTop(float marginTop) {
        this.mMarginTop = marginTop;
        return this;
    }

    public DFScanLineModel setRoundRadius(float roundRadius) {
        this.mRoundRadius = roundRadius;
        return this;
    }

    public DFScanLineModel setScannerLineHeight(int scannerLineHeight) {
        this.mScannerLineHeight = scannerLineHeight;
        return this;
    }

    public DFScanLineModel setScanRectHeight(float scanRectHeight) {
        this.mScanRectHeight = scanRectHeight;
        return this;
    }

    public DFScanLineModel setBorderColor(int borderColor) {
        this.mBorderColor = borderColor;
        if (mBorderPaint != null) {
            mBorderPaint.setColor(mBorderColor);
        }
        return this;
    }

    public Rect getScanRectF(float transScale) {
        float scanRectWidth = mHollowRectF.width();
        float scanRectHeight = mHollowRectF.height();
        int width = mScanLineModelCallback.getViewWidth();
        int height = mScanLineModelCallback.getViewHeight();
        float expandX = scanRectWidth * 0.05f;
        float expandY = scanRectHeight * 0.05f;
        float left = mHollowRectF.left - expandX;
        if (left < 0) {
            left = 0;
        }
        float right = mHollowRectF.right + expandX;
        if (right > width) {
            right = width;
        }
        float top = mHollowRectF.top - expandY;
        if (top < 0) {
            top = 0;
        }
        float bottom = mHollowRectF.bottom + expandY;
        if (bottom > height) {
            bottom = height;
        }

        Rect rect = new Rect();
        rect.left = (int) (left * transScale);
        rect.top = (int) (top * transScale);
        rect.right = (int) (right * transScale);
        rect.bottom = (int) (bottom * transScale);
        return rect;
    }

    public void showSuccessView() {
        mShowSuccessIcon = true;
    }

    public void setScanOverlayCallback(DFScanOverlayView.DFScanOverlayCallback scanOverlayCallback) {
        this.mScanOverlayCallback = scanOverlayCallback;
    }

    public void destroy() {
        DFOCRScanUtils.logI(TAG, "destroy");
        if (mMainHandler != null) {
            mMainHandler.removeCallbacksAndMessages(null);
            mMainHandler = null;
        }
        if (mLaserBitmap != null) {
            mLaserBitmap.recycle();
            mLaserBitmap = null;
        }
        if (mSuccessIcon != null) {
            mSuccessIcon.recycle();
            mSuccessIcon = null;
        }
    }

    private void releaseDrawPath() {
        if (mLockedBackgroundPath != null) {
            mLockedBackgroundPath.reset();
            mLockedBackgroundPath = null;
        }
        if (mClipScanRectPath != null) {
            mClipScanRectPath.reset();
            mClipScanRectPath = null;
        }
        if (mScanBorderPath != null) {
            mScanBorderPath.reset();
            mScanBorderPath = null;
        }
        if (mLaserMatrix != null) {
            mLaserMatrix = null;
        }
    }

    public int dip2px(float dpValue) {
        int densityDpi = mScanLineModelCallback.getActivityContext().getResources().getDisplayMetrics().densityDpi;
        return (int) (dpValue * (densityDpi / 160));
    }

    public interface DFScanLineModelCallback {
        int getViewWidth();

        int getViewHeight();

        Context getActivityContext();
    }
}