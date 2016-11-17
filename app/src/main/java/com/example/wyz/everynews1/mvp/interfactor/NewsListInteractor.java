package com.example.wyz.everynews1.mvp.interfactor;

import com.example.wyz.everynews1.listener.RequestCallBack;

import rx.Subscription;

/**
 * Created by Wyz on 2016/11/7.
 * 新闻列表的联系者
 */
public interface NewsListInteractor<T> {
    Subscription loadNews(RequestCallBack<T> listener ,String type, String id, int startPage);
}
