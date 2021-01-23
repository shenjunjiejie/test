package com.dfsdk.ocr.scan.camera;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;

import com.dfsdk.ocr.scan.R;
import com.dfsdk.ocr.scan.util.DFOCRScanUtils;


public class FixedAspectRatioFrameLayout extends FrameLayout {
    private static final String TAG = "FixedAspectRatioFrameLayout";

    public FixedAspectRatioFrameLayout(Context context) {
        super(context);
    }

    public FixedAspectRatioFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FixedAspectRatioFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        SurfaceView svCamera = findViewById(R.id.id_sv_camera);
        View svScan = findViewById(R.id.id_scan_view);

        int previewWidth = DFCameraPreviewSize.getInstance().getPreviewWidth();
        int previewHeight = DFCameraPreviewSize.getInstance().getPreviewHeight();

        DFOCRScanUtils.logI(TAG, "screen_info", "onLayout", previewWidth);

        DFOCRScanUtils.logI(TAG, "onLayout,", "screen_info", l, t, r, b);
        if (svCamera != null && svScan != null) {
            int h = b - t;
            int w = r - l;
            int newW = (int) ((float) previewHeight / previewWidth * h);

            DFOCRScanUtils.logI(TAG, "screen_info", "onLayout", "w", w);
            DFOCRScanUtils.logI(TAG, "screen_info", "onLayout", "newW", newW);

            if (newW > w) {
                int newL = -(newW - w) / 2;
                int newR = newL + newW;
                svCamera.layout(newL, 0, newR, h);
                svScan.layout(newL, 0, newR, h);
                DFOCRScanUtils.logI(TAG, "screen_info", "onLayout", "111", newL, newR);
            } else {
                int newH = (int) ((float) previewWidth / previewHeight * w);
                int newT = 0;
                int newB = newT + newH;
                svCamera.layout(0, newT, w, newB);
                svScan.layout(0, newT, w, newB);
            }
        }
    }
}
