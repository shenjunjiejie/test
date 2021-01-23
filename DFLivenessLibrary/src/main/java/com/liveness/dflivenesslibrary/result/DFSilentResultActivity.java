package com.liveness.dflivenesslibrary.result;


import com.liveness.dflivenesslibrary.result.fragment.ResultFragmentBase;
import com.liveness.dflivenesslibrary.result.fragment.SilentResultFragment;

public class DFSilentResultActivity extends ResultActivity {
    @Override
    protected ResultFragmentBase getShowFragment() {
        return new SilentResultFragment();
    }
}
