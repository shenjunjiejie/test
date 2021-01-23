package com.dfsdk.ocr.scan.util;


import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (c) 2018-2019 DEEPFINCH Corporation. All rights reserved.
 */
public class DFIntentTransport {

    private static DFIntentTransport mInstance;

    private Map<String, Object> mData;

    public DFIntentTransport() {
        mData = new HashMap<>();
    }

    public static DFIntentTransport getInstance() {
        if (mInstance == null) {
            synchronized (DFIntentTransport.class) {
                if (mInstance == null) {
                    mInstance = new DFIntentTransport();
                }
            }
        }
        return mInstance;
    }

    public void putData(String key, Object value) {
        mData.put(key, value);
    }

    public Object getData(String key) {
        return mData.get(key);
    }

    public Object popData(String key) {
        return mData.remove(key);
    }

    public void clear() {
        if (mData != null) {
            mData.clear();
        }
    }
}
