package com.example.wyz.everynews1.mvp.presenter.impl;

import android.support.annotation.NonNull;

import com.example.wyz.everynews1.common.LoadNewsType;
import com.example.wyz.everynews1.listener.RequestCallBack;
import com.example.wyz.everynews1.mvp.entity.PhotoGirl;
import com.example.wyz.everynews1.mvp.interfactor.impl.PhotoInteractorImpl;
import com.example.wyz.everynews1.mvp.presenter.PhotoPresenter;
import com.example.wyz.everynews1.mvp.presenter.base.BasePresenterImpl;
import com.example.wyz.everynews1.mvp.view.PhotoView;
import com.example.wyz.everynews1.mvp.view.base.BaseView;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Wyz on 2016/11/19.
 */
public class PhotoPresenterImpl extends BasePresenterImpl<PhotoView,List<PhotoGirl>>
    implements  PhotoPresenter,RequestCallBack<List<PhotoGirl>>{

    private PhotoInteractorImpl mPhotoInteractior;
    private static int SIZE = 20;
    private int mStartPage = 1;
    private boolean misFirstLoad;
    private boolean mIsRefresh = true;

    @Inject
    public PhotoPresenterImpl(PhotoInteractorImpl mPhotoInteractior) {
        this.mPhotoInteractior = mPhotoInteractior;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        loadPhotoData();
    }
    private void loadPhotoData() {
        mPhotoInteractior.loadPhotos(this,SIZE,mStartPage);
    }
    @Override
    public void beforeRequest() {
        if(!misFirstLoad){
            mView.showProgress();
        }
    }

    @Override
    public void success(List<PhotoGirl> data) {
        super.success(data);
        misFirstLoad=true;
        if(data!=null){
            mStartPage+=1;
        }
        int loadType=mIsRefresh? LoadNewsType.TYPE_REFRESH_SUCCESS:LoadNewsType.TYPE_LOAD_MORE_SUCCESS;
        if(mView!=null){
            mView.setPhotoList(data,loadType);
            mView.hideProgress();
        }

    }

    @Override
    public void onError(String errorMsg) {
        super.onError(errorMsg);
        int loadType=mIsRefresh? LoadNewsType.TYPE_REFRESH_ERROR:LoadNewsType.TYPE_LOAD_MORE_ERROR;
        mView.setPhotoList(null,loadType);
    }



    @Override
    public void refreshData() {
        mStartPage=1;
        mIsRefresh=true;
        loadPhotoData();
    }

    @Override
    public void loadMore() {
        mIsRefresh=false;
        loadPhotoData();
    }
}
