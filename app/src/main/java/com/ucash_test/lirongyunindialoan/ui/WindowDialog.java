package com.ucash_test.lirongyunindialoan.ui;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.ucash_test.lirongyunindialoan.myosotisutils.AnnotationUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * 窗口对话框
 */
public abstract class WindowDialog extends Dialog {


    public WindowDialog(@NonNull Context context) {
        super(context);
        this.getContext().setTheme(android.R.style.Theme_DeviceDefault_Dialog_NoActionBar_MinWidth);
    }

    public void defeatCoding(){
        //花指令
        BufferedReader br =null;
        try {
            br = new BufferedReader(new FileReader("fakeFile"));
            String line;
            while((line=br.readLine())!= null){
                String[] splited = line.split(" +");
                if(splited.length >= 0){
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                br.close();
            }
            catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }

    /**
     * 设置默认参数
     */
    private void setParams()
    {
        this.setCancelable(true);
        this.setCanceledOnTouchOutside(false);
//        WindowManager windowManager = getWindow().getWindowManager();
//        Display display = windowManager.getDefaultDisplay();
//        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
//        // Dialog宽度
//        lp.width = (int) (display.getWidth() * 0.7);
        Window window = getWindow();
        //window.setAttributes(lp);
        window.getDecorView().getBackground().setAlpha(0);//背景透明
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(onViewCreated(), null);
        setContentView(view);
        setParams();
//        AnnotationUtils.inject(this, view);
    }

    /**
     * 返回dialog的布局id
     * @return
     */
    protected abstract @LayoutRes
    int onViewCreated();

}
