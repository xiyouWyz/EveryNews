package com.example.wyz.everynews1.mvp.presenter.impl;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.example.wyz.everynews1.common.LoadPhotoType;
import com.example.wyz.everynews1.listener.RequestCallBack;
import com.example.wyz.everynews1.mvp.interfactor.impl.PhotoDetailInteractorImpl;
import com.example.wyz.everynews1.mvp.interfactor.impl.PhotoInteractorImpl;
import com.example.wyz.everynews1.mvp.presenter.PhotoDetailPresenter;
import com.example.wyz.everynews1.mvp.presenter.base.BasePresenterImpl;
import com.example.wyz.everynews1.mvp.view.PhotoDetailView;
import com.example.wyz.everynews1.mvp.view.base.BaseView;

import javax.inject.Inject;

/**
 * Created by Wyz on 2016/11/19.
 */
public class PhotoDetailPresenterImpl  extends BasePresenterImpl<PhotoDetailView,Uri>
    implements  PhotoDetailPresenter,RequestCallBack<Uri>{

    private PhotoDetailInteractorImpl mPhotoInteractor;
    private Activity mActivity;
    private  int mLoadType=-1;
    @Inject
    public PhotoDetailPresenterImpl(PhotoDetailInteractorImpl photoDetailInteractor,Activity activity) {
        mPhotoInteractor=photoDetailInteractor;
        mActivity=activity;
    }


    @Override
    public void handlePicture(String imageUrl, @LoadPhotoType.PhotoRequestTypeChecker int type) {
        mLoadType=type;
        mPhotoInteractor.loadPicture(this,imageUrl);
    }
}
