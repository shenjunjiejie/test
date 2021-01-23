package com.dfsdk.ocr.scan.view;

import android.view.View;
import android.widget.TextView;

public class DFOCRScanViewUtils {
    public static void refreshText(TextView textView, CharSequence charSequence) {
        if (textView != null) {
            textView.setText(charSequence);
        }
    }

    public static void refreshVisibility(View view, boolean show) {
        if (view != null) {
            view.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }
}
