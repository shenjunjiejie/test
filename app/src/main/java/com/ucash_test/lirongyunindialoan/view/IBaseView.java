package com.ucash_test.lirongyunindialoan.view;

public interface IBaseView<Data>{
    /*
    * 进行耗时操作时调用的接口
    * */
    void showProgress(boolean isShow);

    /*
    * http请求错误时调用的接口
    * */
    void showOkHttpError(int errorCode,String errorDesc,String errorUrl);

    /*
    * http请求成功是调用的接口
    * */
    void showSuccess(boolean isSuccess);
}
