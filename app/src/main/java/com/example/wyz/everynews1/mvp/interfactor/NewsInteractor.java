package com.example.wyz.everynews1.mvp.interfactor;

import com.example.wyz.everynews1.listener.RequestCallBack;

import rx.Subscription;

/**
 * Created by Wyz on 2016/11/8.
 */
public interface NewsInteractor<T> {
    Subscription lodeNewsChannels(RequestCallBack<T> callback);
}
