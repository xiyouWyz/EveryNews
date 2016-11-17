package com.example.wyz.everynews1.di.module;

import android.app.Activity;
import android.content.Context;

import com.example.wyz.everynews1.di.scope.ContextLife;
import com.example.wyz.everynews1.di.scope.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Wyz on 2016/11/13.
 */
@Module
public class ActivityModule {

    private Activity mActivity;
    public  ActivityModule(Activity activity){ mActivity=activity;}

    @Provides
    @PerActivity
    @ContextLife("Activity")
    public Context ProvideActivityContext() {
        return mActivity;
    }

    @Provides
    @PerActivity
    public Activity ProvideActivity() {
        return mActivity;
    }
}
