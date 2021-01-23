package com.dfsdk.ocr.scan.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import com.dfsdk.ocr.scan.R;

public class DFLoadingDialog {
    private Dialog mDialog;

    public DFLoadingDialog(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View rooterView = layoutInflater.inflate(R.layout.ocr_scan_dialog_loading, null);
        View loadingView = rooterView.findViewById(R.id.id_iv_progress_spinner);
        initView(loadingView);

        mDialog = new Dialog(context, R.style.DFOCRScanLoadingDialog);
        mDialog.setContentView(rooterView);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
        setTranslucentStatus();
        Window window = mDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(R.color.ocr_scan_loading_bg);
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        }
    }

    private void initView(View view) {
        RotateAnimation rotateAnimation = new RotateAnimation(0f, 359, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(700);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        view.startAnimation(rotateAnimation);
    }

    public void show() {
        if (mDialog != null) {
            mDialog.show();
        }
    }

    public void hide() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    private void setTranslucentStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0
            if (mDialog != null) {
                Window window = mDialog.getWindow();
                if (window != null) {
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(Color.TRANSPARENT);
                }
            }
        } else {//4.4
            if (mDialog != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Window window = mDialog.getWindow();
                    if (window != null) {
                        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    }
                }
            }
        }
    }
}
