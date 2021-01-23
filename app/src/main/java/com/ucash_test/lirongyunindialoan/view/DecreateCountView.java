package com.ucash_test.lirongyunindialoan.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class DecreateCountView extends View {
    public DecreateCountView(Context context) {
        super(context);
    }

    public DecreateCountView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DecreateCountView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DecreateCountView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 分别获取期望的宽度和高度，并取其中较小的尺寸作为该控件的宽和高,并且不超过屏幕宽高
        int widthPixels = this.getResources().getDisplayMetrics().widthPixels;//获取屏幕宽
        int heightPixels = this.getResources().getDisplayMetrics().heightPixels;//获取屏幕高
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int hedight = MeasureSpec.getSize(heightMeasureSpec);
        int minWidth = Math.min(widthPixels, width);
        int minHedight = Math.min(heightPixels, hedight);
        setMeasuredDimension(Math.min(minWidth, minHedight), Math.min(minWidth, minHedight));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int center = this.getWidth() / 2;
        int radius = center - circleWidth / 2;

//        drawCircle(canvas, center, radius); // 绘制进度圆弧
        drawText(canvas, center);
    }
    private int duration;
    private void drawText(Canvas canvas, int center) {
        int result = ((maxValue - currentValue) * (duration / 1000) / maxValue); // 计算进度
        String percent;
        if (maxValue == currentValue) {
            percent = "Finish";
            textPaint.setTextSize(centerTextSize); // 设置要绘制的文字大小
        } else {
            percent = (result / 60 < 10 ? "0" + result / 60 : result / 60) + ":" + (result % 60 < 10 ? "0" + result % 60 : result % 60);
//            percent = result+"秒";
        }
        textPaint.setTextSize(centerTextSize); // 设置要绘制的文字大小
        textPaint.setTextAlign(Paint.Align.CENTER); // 设置文字居中，文字的x坐标要注意
        textPaint.setColor(getResources().getColor(Color.GRAY)); // 设置文字颜色
        textPaint.setStrokeWidth(0); // 注意此处一定要重新设置宽度为0,否则绘制的文字会重叠
        //根据路径得到Typeface
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/haipai_qingxia.ttf");
        textPaint.setTypeface(typeface);
        Rect bounds = new Rect(); // 文字边框
        textPaint.getTextBounds(percent, 0, percent.length(), bounds); // 获得绘制文字的边界矩形
        Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt(); // 获取绘制Text时的四条线
        int baseline = center + (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom; // 计算文字的基线    canvas.drawText(percent, center, baseline, textPaint); // 绘制表示进度的文字
        canvas.drawText(percent,canvas.getWidth()/2, baseline, textPaint);
    }

    /**
     * 进度条最大值
     */
    private int maxValue = 200;

    /**
     * 当前进度值
     */
    private int currentValue ;

    /**
     * 每次扫过的角度，用来设置进度条圆弧所对应的圆心角，alphaAngle=(currentValue/maxValue)*360
     */
    private float alphaAngle;

    /**
     * 底部圆弧的颜色，默认为Color.LTGRAY
     */
    private int firstColor;

    /**
     * 进度条圆弧块的颜色
     */
    private int secondColor;
    /**
     * 中间文字颜色(默认蓝色)
     */
    private int centerTextColor = Color.GRAY;
    /**
     * 中间文字的字体大小(默认40dp)
     */
    private int centerTextSize;

    /**
     * 圆环的宽度
     */
    private int circleWidth;

    /**
     * 画圆弧的画笔
     */
    private Paint circlePaint;

    /**
     * 画文字的画笔
     */
    private Paint textPaint;
    /**
     * 是否使用渐变色
     */
    private boolean isShowGradient = false;
}
