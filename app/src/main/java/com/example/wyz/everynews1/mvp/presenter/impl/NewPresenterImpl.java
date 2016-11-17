package com.example.wyz.everynews1.mvp.presenter.impl;

import com.example.wyz.everynews1.greendao.NewsChannelTable;
import com.example.wyz.everynews1.mvp.interfactor.NewsInteractor;
import com.example.wyz.everynews1.mvp.interfactor.impl.NewsInteractorImpl;
import com.example.wyz.everynews1.mvp.presenter.NewsPresenter;
import com.example.wyz.everynews1.mvp.presenter.base.BasePresenterImpl;
import com.example.wyz.everynews1.mvp.view.NewsView;
import java.util.List;
import javax.inject.Inject;


/**
 * Created by Wyz on 2016/11/8.
 */
public class NewPresenterImpl extends BasePresenterImpl<NewsView, List<NewsChannelTable>>
        implements NewsPresenter {

    private NewsInteractor<List<NewsChannelTable>> mNewsInteractor;

    @Inject
    public NewPresenterImpl(NewsInteractorImpl newsInteractor) {
        mNewsInteractor = newsInteractor;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        loadNewsChannels();
    }

    private void loadNewsChannels() {
        mSubscription = mNewsInteractor.lodeNewsChannels(this);
    }

    @Override
    public void success(List<NewsChannelTable> data) {
        super.success(data);
        mView.initViewPager(data);
    }

    @Override
    public void onError(String errorMsg) {
        super.onError(errorMsg);
    }

    @Override
    public void onChannelDbChanged() {
        loadNewsChannels();
    }
}