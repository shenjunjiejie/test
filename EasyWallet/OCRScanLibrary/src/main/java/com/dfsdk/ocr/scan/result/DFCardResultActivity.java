package com.dfsdk.ocr.scan.result;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.dfsdk.ocr.scan.R;
import com.dfsdk.ocr.scan.network.model.DFCardInfo;

public class DFCardResultActivity extends DFResultActivity {
    @Override
    protected String getActivityTitle() {
        return getString(R.string.app_activity_result_auto_scan_title);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        DFCardInfo cardInfo = (DFCardInfo) intent.getSerializableExtra(KEY_RESULT_DATA);
        mResultPresenter.dealCardInfo(cardInfo);
    }
}
