package com.zrt.mybase.utils.support.http;


public abstract class NetCallback<T> {

    public abstract void onSuccess(int code, T t);

    public abstract void onFailure(int code, String msg);
}