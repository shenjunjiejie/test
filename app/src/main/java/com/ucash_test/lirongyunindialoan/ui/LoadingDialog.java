package com.ucash_test.lirongyunindialoan.ui;

import android.content.Context;

import com.ucash_test.lirongyunindialoan.R;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * 加载对话框
 */
public class LoadingDialog extends WindowDialog {

    private static LoadingDialog loadDialog;

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

    public static void open(Context context) {
        if (loadDialog==null)
            loadDialog = new LoadingDialog(context);
        else
        {
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
        return R.layout.dialog_loading;
    }

}
