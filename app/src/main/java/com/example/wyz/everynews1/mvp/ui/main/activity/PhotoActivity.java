package com.example.wyz.everynews1.mvp.ui.main.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.wyz.everynews1.MyApp;
import com.example.wyz.everynews1.R;
import com.example.wyz.everynews1.annotation.BindValues;
import com.example.wyz.everynews1.common.Constants;
import com.example.wyz.everynews1.common.LoadNewsType;
import com.example.wyz.everynews1.listener.OnItemClickListener;
import com.example.wyz.everynews1.mvp.entity.PhotoGirl;
import com.example.wyz.everynews1.mvp.interfactor.impl.PhotoInteractorImpl;
import com.example.wyz.everynews1.mvp.presenter.impl.PhotoPresenterImpl;
import com.example.wyz.everynews1.mvp.ui.main.activity.base.BaseActivity;
import com.example.wyz.everynews1.mvp.ui.main.adapter.PhotoListAdapter;
import com.example.wyz.everynews1.mvp.view.PhotoView;
import com.example.wyz.everynews1.utils.NetUtil;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Wyz on 2016/11/19.
 */
@BindValues(mIsHasNavigationView = true)
public class PhotoActivity extends BaseActivity implements PhotoView,SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.drawLayout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.nav_view)
    NavigationView mNavigationView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwiperRefreshLayout;
    @BindView(R.id.photo_rv)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_view)
    TextView mEmptyView;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.fab)
    FloatingActionButton mFab;

    @Inject
    PhotoListAdapter mPhotoListAdapter;
    @Inject
    PhotoPresenterImpl mPhotoPresenter;
    @Inject
    Activity mActivity;

    private boolean mIsAllLoaded;

    @Override
    public int getLayoutId() {
        return R.layout.activity_photo;
    }

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void initViews() {
        mBaseNavView=mNavigationView;
        initSwipeRefreshLayout();
        initRecyclerView();
        setAdapterItemClickEvent();
        initPresenter();
    }



    private void initSwipeRefreshLayout() {
        mSwiperRefreshLayout.setOnRefreshListener(this);
        mSwiperRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
    }
    private void initRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mPhotoListAdapter);
        setRvScrollEvent();

    }

    private void setRvScrollEvent() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                RecyclerView.LayoutManager layoutManager=recyclerView.getLayoutManager();
                int[] lastVisibleItemPosition=((StaggeredGridLayoutManager) layoutManager)
                        .findLastVisibleItemPositions(null);
                int visibleItemCount=layoutManager.getChildCount();
                int totalItemCount=layoutManager.getItemCount();
                if (!mIsAllLoaded && visibleItemCount > 0 &&
                        (newState == RecyclerView.SCROLL_STATE_IDLE) &&
                        ((lastVisibleItemPosition[0] >= totalItemCount - 1) ||
                                (lastVisibleItemPosition[1] >= totalItemCount - 1))) {
                    mPhotoPresenter.loadMore();
                    mPhotoListAdapter.showFooter();
                    mRecyclerView.scrollToPosition(mPhotoListAdapter.getItemCount() - 1);
                }
            }
        });
    }

    private void setAdapterItemClickEvent() {
        mPhotoListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(PhotoActivity.this, PhotoDetailActivity.class);
                intent.putExtra(Constants.PHOTO_DETAIL, mPhotoListAdapter.getList().get(position).getUrl());
                startActivity(intent);
            }
        });
    }

    private void initPresenter() {
        mPresenter=mPhotoPresenter;
        mPresenter.attachView(this);
        mPresenter.onCreate();
    }

    @Override
    public void setPhotoList(List<PhotoGirl> photoGirls, @LoadNewsType.checker int loadType) {
        switch (loadType){
            case LoadNewsType.TYPE_REFRESH_SUCCESS:
                mSwiperRefreshLayout.setRefreshing(false);
                mPhotoListAdapter.setList(photoGirls);
                mPhotoListAdapter.notifyDataSetChanged();
                checkIsEmpty(photoGirls);
                mIsAllLoaded=false;
                break;
            case  LoadNewsType.TYPE_REFRESH_ERROR:
                mSwiperRefreshLayout.setRefreshing(false);
                checkIsEmpty(photoGirls);
                break;
            case LoadNewsType.TYPE_LOAD_MORE_SUCCESS:
                mPhotoListAdapter.hideFooter();
                if(photoGirls==null||photoGirls.size()==0){
                    mIsAllLoaded=true;
                    Snackbar.make(mRecyclerView,R.string.no_more,Snackbar.LENGTH_SHORT).show();
                }else{
                    mPhotoListAdapter.addMore(photoGirls);
                }
                break;
             case LoadNewsType.TYPE_LOAD_MORE_ERROR:
                 mPhotoListAdapter.hideFooter();
                 break;

        }
    }

    private void checkIsEmpty(List<PhotoGirl> photoGirls) {
        if(photoGirls==null&&mPhotoListAdapter.getList()==null){
            mRecyclerView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        }else{
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showMsg(String message) {
        mProgressBar.setVisibility(View.GONE);
        if(NetUtil.isNetworkAvailable()){
            Snackbar.make(mRecyclerView, message, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRefresh() {
        mPhotoPresenter.refreshData();
    }

    @OnClick({R.id.fab,R.id.empty_view})
    public  void  onClick(View view){
        switch (view.getId()){
            case  R.id.fab:
                //立刻跳至第一个item
                mRecyclerView.getLayoutManager().scrollToPosition(0);
                break;
            case  R.id.empty_view:
                mSwiperRefreshLayout.setRefreshing(true);
                mPhotoPresenter.refreshData();
                break;
        }
    }
}
