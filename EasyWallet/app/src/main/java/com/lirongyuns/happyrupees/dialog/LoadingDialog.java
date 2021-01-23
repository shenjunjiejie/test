package com.lirongyuns.happyrupees.dialog;

import android.content.Context;

import com.lirongyuns.happyrupees.R;

/**
 * 加载对话框
 */
public class LoadingDialog extends WindowDialog {

    private static LoadingDialog loadDialog;

    public static void open(Context context) {
        if (loadDialog==null)
            loadDialog = new LoadingDialog(context);
        else {
            if (loadDialog.isShowing()) {
                return;
            }

            if (context!=loadDialog.getContext())
                loadDialog = new LoadingDialog(context);
        }
        loadDialog.setCanceledOnTouchOutside(false);
        loadDialog.show();
    }

    /**
     * 关闭对话框
     */
    public static void close()
    {
        if (loadDialog != null && loadDialog.isShowing()) {
            loadDialog.dismiss();
        }
    }


    private LoadingDialog(Context context) {
        super(context);
    }


    @Override
    protected int onViewCreated() {
        return R.layout.dialog_loading_lirongyun;
    }

}