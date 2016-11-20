package com.example.wyz.everynews1.di.component;

import android.app.Activity;
import android.content.Context;

import com.example.wyz.everynews1.di.module.ActivityModule;
import com.example.wyz.everynews1.di.scope.ContextLife;
import com.example.wyz.everynews1.di.scope.PerActivity;
import com.example.wyz.everynews1.mvp.ui.main.activity.NewsActivity;
import com.example.wyz.everynews1.mvp.ui.main.activity.NewsDetailActivity;
import com.example.wyz.everynews1.mvp.ui.main.activity.NewsPhotoDetailActivity;
import com.example.wyz.everynews1.mvp.ui.main.activity.PhotoActivity;
import com.example.wyz.everynews1.mvp.ui.main.activity.PhotoDetailActivity;

import dagger.Component;

/**
 * Created by Wyz on 2016/11/13.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    @ContextLife("Activity")
    Context getActivityContext();

    @ContextLife("Application")
    Context getApplicationContext();

    Activity getActivity();

    //注入
    void inject(NewsActivity newsActivity);

    void inject(NewsDetailActivity newsDetailActivity);

    void inject(NewsPhotoDetailActivity newsPhotoDetailActivity);

    void inject(PhotoActivity photoActivity);

    void inject(PhotoDetailActivity photoDetailActivity);
}
