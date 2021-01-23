package com.dfsdk.ocr.scan.network.callback;


public abstract class DFNetworkCallback<T> {

    public abstract void completed(T response);

    public abstract void failed(int httpStatusCode, T response);
}