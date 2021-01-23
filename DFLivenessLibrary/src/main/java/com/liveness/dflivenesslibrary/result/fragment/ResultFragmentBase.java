package com.liveness.dflivenesslibrary.result.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.liveness.dflivenesslibrary.DFProductResult;
import com.liveness.dflivenesslibrary.DFTransferResultInterface;
import com.liveness.dflivenesslibrary.R;
import com.liveness.dflivenesslibrary.feature.FeatureProcessBase;
import com.liveness.dflivenesslibrary.result.ResultActivity;


public abstract class ResultFragmentBase extends Fragment {

    public static final String KEY_ANTI_HACK = "key_anti_hack";

    protected View mRootView;
    protected DFProductResult mResult;
    private long mMainThreadId;

    private boolean mAntiHack;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayoutResourceId(), container,
                false);
        mResult = ((DFTransferResultInterface) getActivity().getApplication()).getResult();
        initIntentData();
        mMainThreadId = Thread.currentThread().getId();

        processInputImage();

        return mRootView;
    }

    private void initIntentData() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            mAntiHack = arguments.getBoolean(KEY_ANTI_HACK, true);
        }
    }

    private void processInputImage() {
        initialize();
    }

    protected void initialize() {
    }

    public abstract int getTitleString();

    protected abstract int getLayoutResourceId();

    public void onClickTryAgain() {
        getProcess().gotoActivity(getActivity());
    }

    protected void setRootViewViewVisibility(final int visibility) {
        if (Thread.currentThread().getId() == mMainThreadId) {
            mRootView.setVisibility(visibility);
        } else {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mRootView.setVisibility(visibility);
                }
            });
        }
    }

    protected abstract FeatureProcessBase getProcess();

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            mResult = ((DFTransferResultInterface) getActivity().getApplication()).getResult();
            processInputImage();
        } else {
            ((ResultActivity) getActivity()).progressDialogDismiss();
        }
    }

    public boolean onBackPressed() {
        return false;
    }

    protected void updateAntiHackResult(final DFProductResult result) {
        if (result == null) {
            return;
        }
        int icnId = R.drawable.ic_result_success;
        String livenessResult = getString(R.string.string_result_liveness_success);
        if (mAntiHack) {
            if (!result.isAntiHackPass()) {
                icnId = R.drawable.ic_result_failed;
                if (TextUtils.isEmpty(result.getErrorMessage())) {
                    livenessResult = getString(R.string.string_result_liveness_failed);
                } else {
                    livenessResult = result.getErrorMessage();
                }
            }
        } else {
            icnId = R.drawable.ic_result_failed;
            livenessResult = getString(R.string.string_result_liveness_no_anti_hack);
        }
        ImageView resultIcon = mRootView.findViewById(R.id.id_iv_livenss_result_icon);
        TextView resulthint = mRootView.findViewById(R.id.id_tv_liveness_result_hint);
        resultIcon.setBackgroundResource(icnId);
        resulthint.setText(livenessResult);
    }
}
