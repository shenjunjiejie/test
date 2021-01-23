package com.dfsdk.ocr.scan.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DFOCRScanUtils {
    private static final boolean DEBUG = false;
    private static final String TAG = "df_ocr_scan";

    public static final int MEET_COUNT = 3;
    public static final float MEET_THRESHOLD = 0.5f;

    public static void logI(Object... logValue) {
        if (DEBUG) {
            StringBuffer sb = new StringBuffer();
            if (logValue != null) {
                for (Object value : logValue) {
                    if (value != null) {
                        sb.append("*")
                                .append(value.toString())
                                .append("*");
                    }
                }
            }
            Log.i(TAG, "logI*" + sb.toString());
        }
    }

    public static void logE(Object... logValue) {
        if (DEBUG) {
            StringBuffer sb = new StringBuffer();
            if (logValue != null) {
                for (Object value : logValue) {
                    if (value != null) {
                        sb.append("*")
                                .append(value.toString())
                                .append("*");
                    }
                }
            }
            Log.e(TAG, "logI*" + sb.toString());
        }
    }

    public static Rect rotate(Rect rect, int width, int height, int degree) {
        Rect rotateRect = rect;
        if (degree == 90) {
            rotateRect = rotate90(rect, width, height);
        } else if (degree == 180) {
            rotateRect = rotate180(rect, width, height);
        } else if (degree == 270) {
            rotateRect = rotate270(rect, width, height);
        }
        return rotateRect;
    }

    public static Rect rotate90(Rect rect, int width, int height) {
        Rect rotateRect = new Rect();
        rotateRect.left = height - rect.bottom;
        rotateRect.top = rect.left;
        rotateRect.right = height - rect.top;
        rotateRect.bottom = rect.right;
        return rotateRect;
    }

    public static Rect rotate180(Rect rect, int width, int height) {
        Rect rotateRect = new Rect();
        rotateRect.left = width - rect.right;
        rotateRect.top = height - rect.bottom;
        rotateRect.right = width - rect.left;
        rotateRect.bottom = height - rect.top;
        return rotateRect;
    }

    public static Rect rotate270(Rect rect, int width, int height) {
        Rect rotateRect = new Rect();
        rotateRect.left = rect.top;
        rotateRect.top = width - rect.right;
        rotateRect.right = rect.bottom;
        rotateRect.bottom = width - rect.left;
        return rotateRect;
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static byte[] convertBmpToJpeg(Bitmap result) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        result.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
        byte[] jpeg = byteArrayOutputStream.toByteArray();
        try {
            byteArrayOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jpeg;
    }
}
