package com.example.wyz.everynews1.mvp.presenter.impl;

import com.example.wyz.everynews1.mvp.entity.NewsDetail;
import com.example.wyz.everynews1.mvp.interfactor.NewDetailInteractor;
import com.example.wyz.everynews1.mvp.interfactor.impl.NewDetailInteractorImpl;
import com.example.wyz.everynews1.mvp.presenter.NewsDetailPresenter;
import com.example.wyz.everynews1.mvp.presenter.base.BasePresenterImpl;
import com.example.wyz.everynews1.mvp.view.NewsDetailView;

import javax.inject.Inject;

/**
 * Created by Wyz on 2016/11/17.
 */
public class NewDetailPresenterImpl extends BasePresenterImpl<NewsDetailView,NewsDetail>
      implements NewsDetailPresenter{
      private NewDetailInteractor<NewsDetail> mNewsDetailInteractor;
      private  String mPostId;
      @Inject
      public NewDetailPresenterImpl(NewDetailInteractorImpl  mNewsDetailInteractor) {
            this.mNewsDetailInteractor=mNewsDetailInteractor;
      }

      @Override
      public void onCreate() {
            super.onCreate();
            mSubscription=mNewsDetailInteractor.loadNewsDetail(this,mPostId);
      }

      @Override
      public void success(NewsDetail data) {
            super.success(data);
            mView.setNewsDetail(data);
      }

      @Override
      public void setpostId(String postId) {
            mPostId=postId;
      }
}
