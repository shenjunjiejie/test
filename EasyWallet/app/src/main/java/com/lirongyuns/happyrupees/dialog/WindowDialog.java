package com.lirongyuns.happyrupees.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.lirongyuns.happyrupees.annonation.ViewInjector;

/**
 * 窗口对话框
 */
public abstract class WindowDialog extends Dialog {


    public WindowDialog(@NonNull Context context) {
        super(context);
        this.getContext().setTheme(android.R.style.Theme_DeviceDefault_Dialog_NoActionBar_MinWidth);
    }

    /**
     * 设置默认参数
     */
    private void setParams()
    {
        this.setCancelable(true);
        this.setCanceledOnTouchOutside(false);
        WindowManager windowManager = getWindow().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        // Dialog宽度
        lp.width = (int) (display.getWidth() * 0.55);
        Window window = getWindow();
        window.setAttributes(lp);
        window.getDecorView().getBackground().setAlpha(0);//背景透明
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(onViewCreated(), null);
        setContentView(view);
        setParams();
        ViewInjector.inject(this, view);
    }

    /**
     * 返回dialog的布局id
     * @return
     */
    protected abstract @LayoutRes int onViewCreated();

}
