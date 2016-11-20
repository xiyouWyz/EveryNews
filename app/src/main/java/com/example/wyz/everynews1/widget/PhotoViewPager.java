package com.example.wyz.everynews1.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Wyz on 2016/11/17.
 */
public class PhotoViewPager extends ViewPager {
    public PhotoViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PhotoViewPager(Context context) {
        super(context);
    }

    //外部拦截法
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        return super.onInterceptTouchEvent(ev);
    }

    //单指触摸
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }
}
