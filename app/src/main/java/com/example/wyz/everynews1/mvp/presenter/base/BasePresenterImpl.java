package com.example.wyz.everynews1.mvp.presenter.base;

import android.support.annotation.NonNull;

import com.example.wyz.everynews1.listener.RequestCallBack;
import com.example.wyz.everynews1.mvp.view.base.BaseView;
import com.example.wyz.everynews1.utils.MyUtils;

import rx.Subscription;

/**
 * Created by Wyz on 2016/11/7.
 */
public class BasePresenterImpl<T extends BaseView,E> implements  BasePresenter,RequestCallBack<E>{

    protected T mView;
    //订阅
    protected Subscription mSubscription;

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {
        MyUtils.cancelSubscription(mSubscription);
    }

    @Override
    public void attachView(@NonNull BaseView view) {
        // TODO?
        mView = (T) view;
    }

    @Override
    public void beforeRequest() {
        mView.showProgress();
    }

    @Override
    public void success(E data) {
        mView.hideProgress();
    }

    @Override
    public void onError(String errorMsg) {
        mView.hideProgress();
        mView.showMsg(errorMsg);
    }
}
