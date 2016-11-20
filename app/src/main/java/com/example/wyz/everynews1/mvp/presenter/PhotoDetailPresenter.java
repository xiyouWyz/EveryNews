package com.example.wyz.everynews1.mvp.presenter;

import com.example.wyz.everynews1.common.LoadPhotoType;
import com.example.wyz.everynews1.mvp.presenter.base.BasePresenter;

/**
 * Created by Wyz on 2016/11/19.
 */
public interface PhotoDetailPresenter extends BasePresenter {
    void handlePicture(String imageUrl, @LoadPhotoType.PhotoRequestTypeChecker int type);
}
