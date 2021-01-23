package com.dfsdk.ocr.scan.util;


import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.dfsdk.ocr.scan.BuildConfig;

import java.util.ArrayList;
import java.util.List;

public class DFAppUtils {

    private static final String TAG = "app_ocr_scan";
    private static final boolean DEBUG = true;

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
            //Log.i(TAG, "logI*" + sb.toString());
        }
    }

//    public static String getIntegratedDocument() {
//        return BuildConfig.INTEGRATED_DOCUMENT;
//    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static <T> List<T> refreshData(List<T> destList, List<? extends T> srcList) {
        if (destList == null) {
            destList = new ArrayList<>();
        } else {
            destList.clear();
        }
        if (srcList != null) {
            destList.addAll(srcList);
        }
        return destList;
    }

    public static void refreshVisibility(View view, boolean show) {
        if (view != null) {
            view.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }
}
