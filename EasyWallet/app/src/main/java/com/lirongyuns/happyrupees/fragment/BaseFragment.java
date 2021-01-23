package com.lirongyuns.happyrupees.fragment;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;


public abstract class BaseFragment extends Fragment {
    public View baseRootView;
    protected AppCompatActivity activity;
    public Context baseContext;

    /**t
     *  查找view
     * @param savedInstanceState
     */
    protected abstract void initAllChildView(Bundle savedInstanceState);

    /**
     *  填充UI
     * @return
     */
    protected abstract int getContentViewId();

    /**
     *  请求网络
     */
    protected abstract void initData();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        baseRootView = inflater.inflate(getContentViewId(), container, false);
        initAllChildView(savedInstanceState);
        initData();
        return baseRootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

}

