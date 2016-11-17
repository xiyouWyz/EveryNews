package com.example.wyz.everynews1.mvp.view;

import com.example.wyz.everynews1.common.LoadNewsType;
import com.example.wyz.everynews1.mvp.entity.NewsSummary;
import com.example.wyz.everynews1.mvp.view.base.BaseView;

import java.util.List;

/**
 * Created by Wyz on 2016/11/7.
 */
public interface NewsListView  extends BaseView{
    void setNewsList(List<NewsSummary> newsSummary, @LoadNewsType.checker int loadType);
}
