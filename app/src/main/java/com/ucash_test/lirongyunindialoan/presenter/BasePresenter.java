package com.ucash_test.lirongyunindialoan.presenter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * 主持者
 * @author Krear 2018/8/15
 */
public class BasePresenter<V>
{
    private V view;

    public BasePresenter(V view)
    {
        this.view = view;
    }

    /**
     * 返回View
     * @return
     */
    public V getView()
    {
        return view;
    }

}
