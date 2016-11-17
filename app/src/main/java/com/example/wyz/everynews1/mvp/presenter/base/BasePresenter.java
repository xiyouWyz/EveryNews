package com.example.wyz.everynews1.mvp.presenter.base;

import android.support.annotation.NonNull;

import com.example.wyz.everynews1.mvp.view.base.BaseView;

/**
 * Created by Wyz on 2016/11/7.
 */
public interface BasePresenter {
    void onCreate();

    void attachView(@NonNull BaseView view);

    void  onDestroy();
}
