package com.example.wyz.everynews1.mvp.view;

import com.example.wyz.everynews1.common.LoadNewsType;
import com.example.wyz.everynews1.mvp.entity.PhotoGirl;
import com.example.wyz.everynews1.mvp.view.base.BaseView;

import java.util.List;

/**
 * Created by Wyz on 2016/11/19.
 */
public interface PhotoView extends BaseView {
    void  setPhotoList(List<PhotoGirl> photoGirls,@LoadNewsType.checker  int loadType);
}
