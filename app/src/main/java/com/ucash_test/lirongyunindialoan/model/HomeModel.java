package com.ucash_test.lirongyunindialoan.model;

import com.ucash_test.lirongyunindialoan.activity.LaunchActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public interface HomeModel {
    abstract LaunchActivity launchActivity();
}
