package com.example.wyz.everynews1.mvp.presenter.impl;

import android.widget.TableRow;

import com.example.wyz.everynews1.common.LoadNewsType;
import com.example.wyz.everynews1.listener.RequestCallBack;
import com.example.wyz.everynews1.mvp.entity.NewsSummary;
import com.example.wyz.everynews1.mvp.interfactor.NewsListInteractor;
import com.example.wyz.everynews1.mvp.interfactor.impl.NewsListInteractorImpl;
import com.example.wyz.everynews1.mvp.presenter.NewsListPresenter;
import com.example.wyz.everynews1.mvp.presenter.base.BasePresenterImpl;
import com.example.wyz.everynews1.mvp.view.NewsListView;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Wyz on 2016/11/7.
 */
public class NewsListPresenterImpl extends BasePresenterImpl<NewsListView,List<NewsSummary>>
    implements NewsListPresenter,RequestCallBack<List<NewsSummary>> {
    private NewsListInteractor<List<NewsSummary>> mNewsListInteractor;
    private  String mNewsType;
    private  String mNewId;
    private int mStartPage;
    private  boolean misFirstLoad;
    private  boolean mIsRefresh=true;
    @Inject
    public NewsListPresenterImpl(NewsListInteractorImpl newsListInteractor) {
        mNewsListInteractor=newsListInteractor;
    }



    @Override
    public void onCreate() {
        if(mView!=null)
        {
            loadNewsData();
        }
    }


    @Override
    public void beforeRequest() {
       if(!misFirstLoad)
       {
           mView.showProgress();
       }
    }
    @Override
    public void onError(String errorMsg) {
        super.onError(errorMsg);
        if(mView!=null)
        {
            int loadType=mIsRefresh? LoadNewsType.TYPE_REFRESH_ERROR:LoadNewsType.TYPE_LOAD_MORE_ERROR;
            mView.setNewsList(null,loadType);
        }
    }

    @Override
    public void success(List<NewsSummary> data) {
        misFirstLoad = true;
        if (data != null) {
            mStartPage += 20;
        }

        int loadType = mIsRefresh ? LoadNewsType.TYPE_REFRESH_SUCCESS : LoadNewsType.TYPE_LOAD_MORE_SUCCESS;
        if (mView != null) {
            mView.setNewsList(data, loadType);
            mView.hideProgress();
        }
    }

    @Override
    public void setNewTypeAndId(String type, String newsId) {
        mNewsType=type;
        mNewId=newsId;
    }

    @Override
    public void refreshData() {
        mStartPage=0;
        mIsRefresh= true;
        loadNewsData();
    }

    @Override
    public void loadMore() {
        mIsRefresh=false;
        loadNewsData();
    }
    private void loadNewsData() {
        mSubscription=mNewsListInteractor.loadNews(this,mNewsType,mNewId,mStartPage);
    }
}
