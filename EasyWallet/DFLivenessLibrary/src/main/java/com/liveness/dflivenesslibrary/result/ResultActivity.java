package com.liveness.dflivenesslibrary.result;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.liveness.dflivenesslibrary.R;
import com.liveness.dflivenesslibrary.result.fragment.ResultFragmentBase;
import com.liveness.dflivenesslibrary.utils.StatusBarCompat;


public abstract class ResultActivity extends Activity {
    public static final String KEY_ANTI_HACK = "key_anti_hack";
    private ResultFragmentBase mFragment;
    private TextView mTextViewTryAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_result_activity_main);
        mFragment = getShowFragment();
        initFragmentArguments(mFragment);
        if (mFragment == null) {
            Toast.makeText(getApplicationContext(), R.string.string_no_match_result_found, Toast.LENGTH_SHORT).show();
            return;
        }
        findViewById(R.id.id_ll_result_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        ((TextView) findViewById(R.id.id_tv_result_title)).setText(mFragment.getTitleString());
        mTextViewTryAgain = findViewById(R.id.id_tv_result_try_again);
        mTextViewTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickTryAgain();
            }
        });
        getFragmentManager().beginTransaction()
                .add(R.id.result_fragment_container, mFragment)
                .commit();
        StatusBarCompat.translucentStatusBar(this, false);
    }

    protected abstract ResultFragmentBase getShowFragment();

    private void initFragmentArguments(Fragment fragment) {
        boolean antiHack = getIntent().getBooleanExtra(KEY_ANTI_HACK, true);
        Bundle bundle = new Bundle();
        bundle.putBoolean(ResultFragmentBase.KEY_ANTI_HACK, antiHack);
        fragment.setArguments(bundle);
    }

    public void progressDialogDismiss() {
        mTextViewTryAgain.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (mFragment != null && mFragment.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

    protected void onClickTryAgain() {
        mFragment.onClickTryAgain();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        findViewById(R.id.id_sv_root).scrollTo(0, 0);// scroll to the beginning
        mFragment.onActivityResult(resultCode, resultCode, data);
    }
}
