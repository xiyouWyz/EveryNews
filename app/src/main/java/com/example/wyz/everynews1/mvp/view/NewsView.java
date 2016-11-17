package com.example.wyz.everynews1.mvp.view;

import com.example.wyz.everynews1.greendao.NewsChannelTable;
import com.example.wyz.everynews1.mvp.view.base.BaseView;

import java.util.List;

/**
 * Created by Wyz on 2016/11/8.
 */
public interface NewsView extends BaseView{
    void initViewPager(List<NewsChannelTable> newsChannels);
}
