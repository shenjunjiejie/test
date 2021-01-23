package com.dfsdk.ocr.scan.base;

import android.app.Activity;
import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

import com.dfsdk.ocr.scan.view.DFOCRScanViewUtils;

public abstract class DFOCRBaseActivity extends Activity {
    protected void initTitle(int titleResId) {
        TextView tvTitle = findViewById(com.dfsdk.ocr.scan.R.id.id_tv_title);
        DFOCRScanViewUtils.refreshText(tvTitle, getString(titleResId));
        View vBack = findViewById(com.dfsdk.ocr.scan.R.id.id_ll_back);
        vBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    protected int getColorByResId(int colorResId) {
        Resources resources = getResources();
        int color = -1;
        if (resources != null) {
            color = resources.getColor(colorResId);
        }
        return color;
    }
}
