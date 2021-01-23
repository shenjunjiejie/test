package com.lirongyuns.happyrupees.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lirongyuns.happyrupees.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CertificationFragment extends BaseFragment {
    @Override
    protected void initAllChildView(Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.certification_fragment;
    }

    @Override
    protected void initData() {

    }

    private Unbinder mUnbinder;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView =super.onCreateView(inflater,container,savedInstanceState);
        mUnbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
