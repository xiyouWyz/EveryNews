package com.example.wyz.everynews1.mvp.ui.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.example.wyz.everynews1.R;
import com.example.wyz.everynews1.annotation.BindValues;
import com.example.wyz.everynews1.common.Constants;
import com.example.wyz.everynews1.event.ChannelChangeEvent;
import com.example.wyz.everynews1.event.ScrollToTopEvent;
import com.example.wyz.everynews1.greendao.NewsChannelTable;
import com.example.wyz.everynews1.mvp.presenter.impl.NewPresenterImpl;
import com.example.wyz.everynews1.mvp.ui.main.activity.base.BaseActivity;
import com.example.wyz.everynews1.mvp.ui.main.adapter.PagerAdapter.NewsFragmentPagerAdapter;
import com.example.wyz.everynews1.mvp.ui.main.fragment.NewsListFragment;
import com.example.wyz.everynews1.mvp.view.NewsView;
import com.example.wyz.everynews1.utils.MyUtils;
import com.example.wyz.everynews1.utils.RxBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by Wyz on 2016/11/13.
 */
@BindValues(mIsHasNavigationView = true)
public class NewsActivity extends BaseActivity
        implements NewsView {
    private String mCurrentViewPagerName;
    private List<String> mChannelNames;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tabs)
    TabLayout mTabs;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.nav_view)
    NavigationView mNavView;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.drawLayout)
    DrawerLayout mDrawerLayout;

    @Inject
    NewPresenterImpl mNewsPresenter;

    private List<Fragment> mNewsFragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSubscription = RxBus.getInstance().toObservable(ChannelChangeEvent.class)
                .subscribe(new Action1<ChannelChangeEvent>() {
                    @Override
                    public void call(ChannelChangeEvent channelChangeEvent) {
                        mNewsPresenter.onChannelDbChanged();

                        Intent intent=new Intent(NewsActivity.this,NewsActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                    }
                });
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    public void initViews() {
//        mIsHasNavigationView = true;
        mBaseNavView = mNavView;

        mPresenter = mNewsPresenter;
        mPresenter.attachView(this);
    }

    @OnClick({R.id.fab,R.id.add_channel_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                RxBus.getInstance().post(new ScrollToTopEvent());
                break;
            case  R.id.add_channel_iv:
                Intent intent=new Intent(this,NewsChannelActivity.class);
                startActivity(intent);
                break;
        }

    }


    @Override
    public void initViewPager(List<NewsChannelTable> newsChannels) {
        final List<String> channelNames = new ArrayList<>();
        if (newsChannels != null) {
            setNewsList(newsChannels, channelNames);
            setViewPager(channelNames);
        }
    }

    private void setNewsList(List<NewsChannelTable> newsChannels, List<String> channelNames) {
        mNewsFragmentList.clear();
        for (NewsChannelTable newsChannel : newsChannels) {
            NewsListFragment newsListFragment = createListFragments(newsChannel);
            mNewsFragmentList.add(newsListFragment);
            channelNames.add(newsChannel.getNewsChannelName());
        }
    }

    private NewsListFragment createListFragments(NewsChannelTable newsChannel) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.NEWS_ID, newsChannel.getNewsChannelId());
        bundle.putString(Constants.NEWS_TYPE, newsChannel.getNewsChannelType());
        bundle.putInt(Constants.CHANNEL_POSITION, newsChannel.getNewsChannelIndex());
        fragment.setArguments(bundle);
        return fragment;
    }

    private void setViewPager(List<String> channelNames) {
        NewsFragmentPagerAdapter adapter = new NewsFragmentPagerAdapter(
                getSupportFragmentManager(), channelNames, mNewsFragmentList);
        mViewPager.setAdapter(adapter);
        mTabs.setupWithViewPager(mViewPager);
        MyUtils.dynamicSetTabLayoutMode(mTabs);
//        mTabs.setTabsFromPagerAdapter(adapter);
        setPageChangeListener();

        mChannelNames = channelNames;
        int currentViewPagerPosition = getCurrentViewPagerPosition();
        mViewPager.setCurrentItem(currentViewPagerPosition, false);
    }

    private int getCurrentViewPagerPosition() {
        int position = 0;
        if (mCurrentViewPagerName != null) {
            for (int i = 0; i < mChannelNames.size(); i++) {
                if (mCurrentViewPagerName.equals(mChannelNames.get(i))) {
                    position = i;
                }
            }
        }
        return position;
    }

    private void setPageChangeListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentViewPagerName = mChannelNames.get(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showMsg(String message) {
        Snackbar.make(mFab, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==event.KEYCODE_BACK){
            exitByTwoClick();
        }
        return  false;
    }
    //是否点击了退出按钮
    private static Boolean isExit = false;
    private void exitByTwoClick() {
        Timer tExit;
        if(isExit){
            //连续第二次点击退出程序
            finish();
            System.exit(0);
        }else{
            //第一次点击返回
            isExit=true;
            Toast.makeText(NewsActivity.this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
            tExit=new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    //将isExit设置为false，表示取消退出
                    isExit=false;
                }
            },2000 );//如果两秒内没有按下返回键，则启动定时器取消刚才执行的任务
        }
    }
}
