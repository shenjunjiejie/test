package com.ucash_test.lirongyunindialoan.component;

import com.ucash_test.lirongyunindialoan.model.HomeModel;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.support.AndroidSupportInjectionModule;

@Component(modules = {HomeModel.class, AndroidSupportInjectionModule.class, AndroidInjectionModule.class})
public class UserInfoComponent {

}
