package com.liveness.dflivenesslibrary.result.fragment.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;


public class RoundRectImageView extends CircleImageView {
    private static final int ROUND_RADIUS = 30;

    public RoundRectImageView(Context context) {
        super(context);
    }

    public RoundRectImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundRectImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
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
    }

    protected RectF calculateBounds() {
        int availableWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int availableHeight = getHeight() - getPaddingTop() - getPaddingBottom();

        float left = getPaddingLeft() + (0) / 2f;
        float top = getPaddingTop() + (0) / 2f;

        return new RectF(left, top, left + availableWidth, top + availableHeight);
    }
}
