package com.dfsdk.ocr.scan.result;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dfsdk.ocr.scan.R;
import com.dfsdk.ocr.scan.result.adapter.DFOnClickListener;
import com.dfsdk.ocr.scan.result.adapter.model.DFResultInfoItem;
import com.dfsdk.ocr.scan.result.presenter.DFResultPresenter;
import com.dfsdk.ocr.scan.util.DFIntentTransport;
import com.dfsdk.ocr.scan.util.DFStatusBarCompat;

import java.util.List;

public class DFResultActivity extends AppCompatActivity implements DFResultPresenter.DFResultView, DFOnClickListener<DFResultInfoItem> {

    public static final String KEY_RESULT_DATA = "key_result_data";

    public static final int RESULT_TRY_AGAIN = 100;

//    private DFResultUserInfoAdapter mResultAdapter;
    protected DFResultPresenter mResultPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_activity_result);

        initPresenter();
        initView();
        DFStatusBarCompat.translucentStatusBar(this, false);
    }

    private void initPresenter() {
        mResultPresenter = new DFResultPresenter(this);
    }

    protected void initView() {
        TextView tvTitle = findViewById(R.id.id_tv_result_title);
        tvTitle.setText(getActivityTitle());
        findViewById(R.id.id_iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RecyclerView rvResult = findViewById(R.id.id_rv_result);
        rvResult.setLayoutManager(new LinearLayoutManager(this));
//        mResultAdapter = new DFResultUserInfoAdapter(this);
//        rvResult.setAdapter(mResultAdapter);
//        mResultAdapter.setOnClickListener(this);
    }

    protected String getActivityTitle() {
        return "";
    }

    @Override
    public Context getActivityContext() {
        return this;
    }

    @Override
    public void returnResult(List<DFResultInfoItem> resultInfoItemList) {
//        if (mResultAdapter != null) {
//            mResultAdapter.refreshData(resultInfoItemList);
//        }
    }

    @Override
    public void onClick(DFResultInfoItem dfResultInfoItem) {
        if (dfResultInfoItem != null) {
            switch (dfResultInfoItem.getInfoItemType()) {
                case TYPE_TRY_AGAIN:
                    tryAgain();
                    break;
                case TYPE_CARD_IMAGE_SINGLE:
                    clickCardImage(dfResultInfoItem);
                    break;
                case TYPE_CARD_IMAGE_DOUBLE:
                    clickCardImage(dfResultInfoItem);
                    break;
            }
        }
    }

    private void clickCardImage(DFResultInfoItem resultInfoItem) {
//        Intent intent = new Intent(this, DFResultImagePreviewActivity.class);
//        DFIntentTransport.getInstance().putData(DFResultImagePreviewActivity.KEY_SHOW_IMAGE_DATA, resultInfoItem);
//        startActivity(intent);
    }

    private void tryAgain() {
        setResult(RESULT_TRY_AGAIN);
        finish();
    }
}
