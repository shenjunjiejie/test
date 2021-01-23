package com.dfsdk.ocr.scan.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;


import com.dfsdk.ocr.scan.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 2017-2019 DEEPFINCH Corporation. All rights reserved.
 **/
public class RoundRectImageView extends CircleImageView {
    private static final String TAG = "RoundRectImageView";
    private static final int ROUND_RADIUS = 30;

    private Paint mOverlayBgPaint;
    private Paint mOverlayTextPaint;
    private boolean mIsShowOverlay;
    private List<String> mShowTextList;
    private int mTextSize;

    public RoundRectImageView(Context context) {
        super(context);
        init(context, null);
    }

    public RoundRectImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RoundRectImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        initAttrs(attrs);
        initOverlayBgPaint();
        initOverlayTextPaint(context);
        initOverlayText(context);
    }

    private void initAttrs(AttributeSet attrs) {
        final TypedArray attrArray = getContext().obtainStyledAttributes(attrs, R.styleable.RoundRectImageView);
        int defaultTextSize = dp2px(16);
        mTextSize = (int) attrArray.getDimension(R.styleable.RoundRectImageView_round_rect_text_size, defaultTextSize);
        attrArray.recycle();
    }

    private void initOverlayBgPaint() {
        mOverlayBgPaint = new Paint();
        mOverlayBgPaint.setStyle(Paint.Style.FILL);
        mOverlayBgPaint.setAntiAlias(true);
        mOverlayBgPaint.setColor(Color.parseColor("#4D000000"));
    }

    private void initOverlayTextPaint(Context context) {
        mOverlayTextPaint = new Paint();
        mOverlayTextPaint.setStyle(Paint.Style.FILL);
        mOverlayTextPaint.setAntiAlias(true);
        mOverlayTextPaint.setColor(context.getResources().getColor(R.color.app_white));
        mOverlayTextPaint.setTextSize(mTextSize);
        mOverlayTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    private void initOverlayText(Context context) {
        mShowTextList = new ArrayList<>();
        addOverlayText(context, R.string.app_card_not_target);
        addOverlayText(context, R.string.app_card_not_target_document);
    }

    private void addOverlayText(Context context, int overlayTextId) {
        String overlayText = context.getResources().getString(overlayTextId);
        if (!TextUtils.isEmpty(overlayText)) {
            mShowTextList.add(overlayText);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mDisableCircularTransformation) {
            super.onDraw(canvas);
            return;
        }

        if (mBitmap == null) {
            return;
        }

        canvas.drawRoundRect(mDrawableRect, ROUND_RADIUS, ROUND_RADIUS, mBitmapPaint);
        if (mIsShowOverlay) {
            drawOverlay(canvas);
        }
    }

    private void drawOverlay(Canvas canvas) {
        canvas.drawRoundRect(mDrawableRect, ROUND_RADIUS, ROUND_RADIUS, mOverlayBgPaint);
        float middleHeight = getHeight() >> 1;
        if (mShowTextList != null) {
            float textX = getWidth() >> 1;

            Paint.FontMetrics fontMetrics = mOverlayTextPaint.getFontMetrics();
            float textOffset = (fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.descent;
            float textHeight = fontMetrics.bottom - fontMetrics.top;
            float halfTextHeight = textHeight / 2;

            int size = mShowTextList.size();
            float startTextYOffset = -(size - 1) * halfTextHeight;
            for (int i = 0; i < size; i++) {
                float positionOffset = startTextYOffset + i * textHeight;
                String drawText = mShowTextList.get(i);
                float textY = middleHeight + textOffset + positionOffset;
                canvas.drawText(drawText, textX, textY, mOverlayTextPaint);
            }
        }
    }

    protected RectF calculateBounds() {
        int availableWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int availableHeight = getHeight() - getPaddingTop() - getPaddingBottom();

        float left = getPaddingLeft() + (availableWidth - availableWidth) / 2f;
        float top = getPaddingTop() + (availableHeight - availableHeight) / 2f;

        return new RectF(left, top, left + availableWidth, top + availableHeight);
    }

    public void refreshMask(boolean isShowOverlay) {
        mIsShowOverlay = isShowOverlay;
        postInvalidate();
    }

    private int dp2px(float dpValue) {
        int densityDpi = this.getResources().getDisplayMetrics().densityDpi;
        return (int) (dpValue * (densityDpi / 160));
    }
}
