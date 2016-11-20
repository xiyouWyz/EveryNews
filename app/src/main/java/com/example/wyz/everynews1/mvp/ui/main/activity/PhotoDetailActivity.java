package com.example.wyz.everynews1.mvp.ui.main.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import com.example.wyz.everynews1.R;
import com.example.wyz.everynews1.common.Constants;
import com.example.wyz.everynews1.di.scope.ContextLife;
import com.example.wyz.everynews1.mvp.presenter.impl.PhotoDetailPresenterImpl;
import com.example.wyz.everynews1.mvp.ui.main.activity.base.BaseActivity;
import com.example.wyz.everynews1.mvp.view.PhotoDetailView;
import com.example.wyz.everynews1.utils.MyUtils;
import com.example.wyz.everynews1.utils.SystemUIVisibilityUtil;
import com.squareup.picasso.Picasso;


import javax.inject.Inject;

import butterknife.BindView;
import dagger.Lazy;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Wyz on 2016/11/19.
 */
public class PhotoDetailActivity  extends BaseActivity implements PhotoDetailView{
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.photo_iv)
    PhotoView mPhotoView;

    @Inject
    Lazy<PhotoDetailPresenterImpl> mPhotoDetailPresenter;
    @Inject
    @ContextLife("Activity")
    Context mContext;

    private ColorDrawable mBackground;
    private boolean mIsToolBarHidden;
    private boolean mIsStatusBarHidden;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //showToolBarAndPhotoTouchView();
        //initLazyLoadView();
    }

    private void initLazyLoadView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getEnterTransition().addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {

                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    showToolBarAndPhotoTouchView();
                }

                @Override
                public void onTransitionCancel(Transition transition) {

                }

                @Override
                public void onTransitionPause(Transition transition) {

                }

                @Override
                public void onTransitionResume(Transition transition) {

                }
            });
        } else {
            showToolBarAndPhotoTouchView();
        }
    }

    private void showToolBarAndPhotoTouchView() {
        toolBarFadeIn();
        //loadPhotoTouchIv();
    }

    private void loadPhotoTouchIv() {
    }

    private void toolBarFadeIn() {
        mIsToolBarHidden = true;
        hideOrShowToolbar();
    }

    private void hideOrShowToolbar() {
        mToolbar.animate()
                .alpha(mIsToolBarHidden?1.0f:0.0f)
                .setInterpolator(new DecelerateInterpolator(2))
                .start();
        mIsToolBarHidden=!mIsStatusBarHidden;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_photo_detail;
    }

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    public void initViews() {
        initToolbar();
        initImageView();
        initBackground();
        setPhotoViewClickEvent();
    }




    private void initToolbar() {
        mToolbar.setTitle(getString(R.string.girl));
    }
    private void initImageView() {
        loadPhotoIv();
    }

    private void loadPhotoIv() {
        Picasso.with(this)
                .load(getIntent().getStringExtra(Constants.PHOTO_DETAIL))
                .into(mPhotoView);
    }

    private void initBackground() {
        mBackground=new ColorDrawable(Color.BLACK);
        MyUtils.getRootView(this).setBackgroundDrawable(mBackground);

    }
    private void setPhotoViewClickEvent() {
        mPhotoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float v, float v1) {
                hideOrShowToolbar();
                hideOrShowStatusBar();
            }

            @Override
            public void onOutsidePhotoTap() {
                hideOrShowToolbar();
                hideOrShowStatusBar();
            }
        });
    }

    private void hideOrShowStatusBar() {
        if (mIsStatusBarHidden) {
            SystemUIVisibilityUtil.enter(PhotoDetailActivity.this);
        } else {
            SystemUIVisibilityUtil.exit(PhotoDetailActivity.this);
        }
        mIsStatusBarHidden = !mIsStatusBarHidden;
    }



    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showMsg(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }
}
