package com.cretin.www.cretinautoupdatelibrary.activity;

import android.content.Context;
import android.os.Bundle;
import androidx.core.content.FileProvider;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cretin.www.cretinautoupdatelibrary.utils.AppUtils;
import com.cretin.www.cretinautoupdatelibrary.utils.LogUtils;
import com.cretin.www.cretinautoupdatelibrary.R;
import com.cretin.www.cretinautoupdatelibrary.interfaces.AppDownloadListener;
import com.cretin.www.cretinautoupdatelibrary.model.DownloadInfo;
import com.cretin.www.cretinautoupdatelibrary.utils.ResUtils;
import com.cretin.www.cretinautoupdatelibrary.utils.RootActivity;

import java.io.File;

public class UpdateType5Activity extends RootActivity {

    private TextView tvMsg;
    private TextView tvBtn2;
    private ImageView ivClose;
    private TextView tvVersion;
    private TextView tvUploadTip;
    private long mExitTime;
    
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_type5);

        findView();

        setDataAndListener();
    }

    private void setDataAndListener() {
//        tvMsg.setText(downloadInfo.getUpdateLog());
        tvMsg.setMovementMethod(ScrollingMovementMethod.getInstance());
        tvVersion.setText("v"+downloadInfo.getProdVersionName());
    
        ivClose.setVisibility(View.VISIBLE);
        int updateFlag = downloadInfo.getForceUpdateFlag();
        if(1 == updateFlag){
            ivClose.setVisibility(View.GONE);
        }else{
            ivClose.setVisibility(View.VISIBLE);
        }
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            
//                if(1 == updateFlag){
//                    if ((System.currentTimeMillis() - mExitTime) > 2000) {
//                        Toast.makeText(getApplicationContext(), getString(R.string.upload_cancel), Toast.LENGTH_LONG).show();
//                        mExitTime = System.currentTimeMillis();
//                    } else {
//                        System.exit(0);
//                        finish();
//                    }
//                }else{
//                    finish();
//                }
                finish();
               
                
            }
        });

        tvBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //右边的按钮
                download();
            }
        });
    }

    private void findView() {
        tvMsg = findViewById(R.id.tv_content);
        tvBtn2 = findViewById(R.id.tv_update);
        ivClose = findViewById(R.id.iv_close);
        tvVersion = findViewById(R.id.tv_version);
        tvUploadTip = findViewById(R.id.tv_upload_tip);
    }

    @Override
    public AppDownloadListener obtainDownloadListener() {
        return new AppDownloadListener() {
            @Override
            public void downloading(int progress) {
                tvUploadTip.setVisibility(View.VISIBLE);
                tvMsg.setText(ResUtils.getString(R.string.downloading)+progress+"%"+"");
//                tvBtn2.setText(ResUtils.getString(R.string.downloading)+progress+"%");
            }

            @Override
            public void downloadFail(String msg) {
                tvBtn2.setText(ResUtils.getString(R.string.btn_update_now));
                Toast.makeText(UpdateType5Activity.this, ResUtils.getString(R.string.apk_file_download_fail), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void downloadComplete(String path) {
                tvBtn2.setText(ResUtils.getString(R.string.btn_update_now));
                File newFile = new File(path);
                AppUtils.installApkFile(getBaseContext(), newFile);
//                SystemUtils.installApk(getApplicationContext(), path);
                
            }

            @Override
            public void downloadStart() {
                tvBtn2.setText(ResUtils.getString(R.string.downloading));
            }

            @Override
            public void reDownload() {
                LogUtils.log("下载失败后点击重试");
            }

            @Override
            public void pause() {

            }
        };
    }

    /**
     * 启动Activity
     *
     * @param context
     * @param info
     */
    public static void launch(Context context, DownloadInfo info) {
        launchActivity(context, info, UpdateType5Activity.class);
    }
    
    
    

}
