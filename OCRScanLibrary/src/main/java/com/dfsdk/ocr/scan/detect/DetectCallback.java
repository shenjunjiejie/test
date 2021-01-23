package com.dfsdk.ocr.scan.detect;

import android.content.Context;

public interface DetectCallback<T> {
    void initResult(int result);

    void detecting(T t);

    void detectEnd(T t);

    Context getActivityContext();
}
