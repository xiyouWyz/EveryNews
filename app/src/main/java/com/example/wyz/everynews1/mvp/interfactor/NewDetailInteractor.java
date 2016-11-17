package com.example.wyz.everynews1.mvp.interfactor;

import android.view.animation.BaseInterpolator;

import com.example.wyz.everynews1.listener.RequestCallBack;

import rx.Subscription;

/**
 * Created by Wyz on 2016/11/17.
 */
public interface NewDetailInteractor<T> {
    Subscription loadNewsDetail(RequestCallBack<T> callBack,String postId);
}
