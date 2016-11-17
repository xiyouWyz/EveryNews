package com.example.wyz.everynews1.mvp.presenter;

/**
 * Created by Wyz on 2016/11/7.
 */
public interface NewsListPresenter {
    void setNewTypeAndId(String type,String newsId);

    void refreshData();

    void loadMore();
}
