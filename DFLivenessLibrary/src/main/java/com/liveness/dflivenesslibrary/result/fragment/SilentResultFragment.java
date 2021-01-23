package com.liveness.dflivenesslibrary.result.fragment;

import com.liveness.dflivenesslibrary.R;
import com.liveness.dflivenesslibrary.feature.FeatureProcessBase;
import com.liveness.dflivenesslibrary.feature.SilentLivenessProcess;


public class SilentResultFragment extends LivenessResultBaseFragment {
    private static final String TAG = "SilentResultFragment";

    @Override
    public int getTitleString() {
        return R.string.string_activity_bottom_silentliveness;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.layout_result_liveness_fragment_main;
    }

    @Override
    protected FeatureProcessBase getProcess() {
        return new SilentLivenessProcess();
    }
}
