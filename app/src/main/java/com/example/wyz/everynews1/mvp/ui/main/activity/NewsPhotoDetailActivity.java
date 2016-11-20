package com.example.wyz.everynews1.mvp.ui.main.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.wyz.everynews1.R;
import com.example.wyz.everynews1.common.Constants;
import com.example.wyz.everynews1.event.NewsDetailPhotoOnClickEvent;
import com.example.wyz.everynews1.mvp.entity.NewsPhotoDetail;
import com.example.wyz.everynews1.mvp.ui.main.activity.base.BaseActivity;
import com.example.wyz.everynews1.mvp.ui.main.adapter.PagerAdapter.NewsDetailPhotoFragmentAdapter;
import com.example.wyz.everynews1.mvp.ui.main.fragment.NewsDetailPhotoFragment;
import com.example.wyz.everynews1.utils.RxBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by Wyz on 2016/11/18.
 */
public class NewsPhotoDetailActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolBar;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.photo_detail_title_tv)
    TextView mPhotoDetailTitleTv;

    private List<NewsDetailPhotoFragment> mNewsDetailPhotoFragmentList=new ArrayList<>();
    private NewsPhotoDetail mNewsPhotoDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSubscription= RxBus.getInstance().toObservable(NewsDetailPhotoOnClickEvent.class)
                .subscribe(new Action1<NewsDetailPhotoOnClickEvent>() {
                    @Override
                    public void call(NewsDetailPhotoOnClickEvent newsPhotoDetailOnClickEvent) {
                        if(mPhotoDetailTitleTv.getVisibility()== View.VISIBLE){
                            if(Objects.equals(NewsDetailPhotoOnClickEvent.flag, "outSide"))
                                startAnimation(View.GONE, 0.9f, 0.5f);
                        }else{
                            if(NewsDetailPhotoOnClickEvent.flag.equals("inSide"))
                            {
                                mPhotoDetailTitleTv.setVisibility(View.VISIBLE);
                                startAnimation(View.VISIBLE, 0.5f, 0.9f);
                            }
                        }
                    }
                });
    }

    private void startAnimation(final int endState, float startValue, float endValue) {
        ObjectAnimator animator=ObjectAnimator
                .ofFloat(mPhotoDetailTitleTv,"alpha",startValue,endValue)
                .setDuration(200);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mPhotoDetailTitleTv.setVisibility(endState);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_news_photo_detail;
    }

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    public void initViews() {
        mNewsPhotoDetail=getIntent().getParcelableExtra(Constants.PHOTO_DETAIL);
        createFragment(mNewsPhotoDetail);
        initViewPager();
        setPhotoDetailTitle(0);
    }




    private void createFragment(NewsPhotoDetail mNewsPhotoDetail) {
        mNewsDetailPhotoFragmentList.clear();
        for(NewsPhotoDetail.Picture picture:mNewsPhotoDetail.getPictures()){
            NewsDetailPhotoFragment photoFragment=new NewsDetailPhotoFragment();
            Bundle bundle=new Bundle();
            bundle.putString(Constants.PHOTO_DETAIL_IMGSRC,picture.getImgSrc());
            photoFragment.setArguments(bundle);
            mNewsDetailPhotoFragmentList.add(photoFragment);
        }
    }
    private void initViewPager() {
        NewsDetailPhotoFragmentAdapter adapter=new NewsDetailPhotoFragmentAdapter(getSupportFragmentManager(),mNewsDetailPhotoFragmentList);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setPhotoDetailTitle(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    private void setPhotoDetailTitle(int position) {
        String title=getTitle(position);
        mPhotoDetailTitleTv.setText(getString(R.string.photo_detail_title,position+1,mNewsDetailPhotoFragmentList.size(),title));

    }
    private  String getTitle(int position){
        String title=mNewsPhotoDetail.getPictures().get(position).getTitle();
        if(title==null){
            title=mNewsPhotoDetail.getTitle();
        }
        return  title;
    }


}
