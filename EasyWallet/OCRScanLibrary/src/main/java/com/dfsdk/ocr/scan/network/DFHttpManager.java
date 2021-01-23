package com.dfsdk.ocr.scan.network;


import com.dfsdk.ocr.scan.network.callback.DFNetworkCallback;
import com.dfsdk.ocr.scan.network.request.DFApiParameterList;
import com.dfsdk.ocr.scan.network.response.DFNetworkBaseModule;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DFHttpManager {
    private ExecutorService mThreadPool;

    private static volatile DFHttpManager mInstance;

    public static DFHttpManager getInstance() {
        if (mInstance == null) {
            synchronized (DFHttpManager.class) {
                if (mInstance == null) {
                    mInstance = new DFHttpManager();
                }
            }
        }
        return mInstance;
    }

    public DFHttpManager() {
        mThreadPool = Executors.newFixedThreadPool(3);
    }

    public void postSyn(final String control, final String action, final DFApiParameterList headerParameterList,
                        final DFApiParameterList bodyParameterList, final DFNetworkCallback<DFNetworkBaseModule> callback) {
        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                DFApiManager apiManager = new DFApiManager();
                apiManager.postSyn(control, action, headerParameterList, bodyParameterList, callback);
            }
        });
    }

}
