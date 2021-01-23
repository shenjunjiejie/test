package com.dfsdk.ocr.scan.detect;

import android.content.Context;
import android.graphics.Rect;

public interface DetectInterface<T> {
    int init(Context context);

    T detect(byte[] data, int width, int height, Rect scanRect, int degree);

    T isFinish(T t);

    void release();

}
