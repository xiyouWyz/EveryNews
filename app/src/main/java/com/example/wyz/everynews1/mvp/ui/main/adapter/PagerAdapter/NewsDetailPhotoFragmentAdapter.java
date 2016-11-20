package com.example.wyz.everynews1.mvp.ui.main.adapter.PagerAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.wyz.everynews1.mvp.ui.main.fragment.NewsDetailPhotoFragment;

import java.util.List;

/**
 * Created by Wyz on 2016/11/18.
 */
public class NewsDetailPhotoFragmentAdapter  extends FragmentStatePagerAdapter{

    private List<NewsDetailPhotoFragment> mNewsDetailPhotoFragmentList;

    public NewsDetailPhotoFragmentAdapter(FragmentManager fm, List<NewsDetailPhotoFragment> mNewsDetailPhotoFragmentList) {
        super(fm);
        this.mNewsDetailPhotoFragmentList = mNewsDetailPhotoFragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return mNewsDetailPhotoFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mNewsDetailPhotoFragmentList.size();
    }
}
