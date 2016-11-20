package com.example.wyz.everynews1.mvp.interfactor;

import com.example.wyz.everynews1.listener.RequestCallBack;

import rx.Subscription;

/**
 * Created by Wyz on 2016/11/19.
 */
public interface PhotoDetailInteractor<T> {
    Subscription loadPicture(RequestCallBack<T> callBack,String url);
}
