package com.example.wyz.everynews1.listener;

/**
 * Created by Wyz on 2016/11/7.
 * 网络请求回调事件
 */
public interface RequestCallBack<T> {
    void beforeRequest();

    void success(T data);

    void onError(String errorMsg);
}

