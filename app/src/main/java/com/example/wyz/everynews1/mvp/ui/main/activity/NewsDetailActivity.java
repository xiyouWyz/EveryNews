package com.example.wyz.everynews1.mvp.ui.main.activity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.HideReturnsTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.wyz.everynews1.MyApp;
import com.example.wyz.everynews1.R;
import com.example.wyz.everynews1.common.Constants;
import com.example.wyz.everynews1.greendao.NewsChannelTable;
import com.example.wyz.everynews1.mvp.entity.NewsDetail;
import com.example.wyz.everynews1.mvp.presenter.impl.NewDetailPresenterImpl;
import com.example.wyz.everynews1.mvp.ui.main.activity.base.BaseActivity;
import com.example.wyz.everynews1.mvp.view.NewsDetailView;
import com.example.wyz.everynews1.mvp.view.NewsView;
import com.example.wyz.everynews1.utils.LogUtil;
import com.example.wyz.everynews1.utils.MyUtils;
import com.example.wyz.everynews1.utils.NetUtil;
import com.example.wyz.everynews1.utils.TransformUtils;
import com.example.wyz.everynews1.widget.URLImageGetter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by Wyz on 2016/11/13.
 */
public class NewsDetailActivity extends BaseActivity  implements NewsDetailView{
    final  String TAG="NewsDetailActivity";
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.news_detail_from_tv)
    TextView mNewsDeatilFromTv;
    @BindView(R.id.news_detail_body_tv)
    TextView mNewsDeatilBodyTv;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.news_detail_photo_iv)
    ImageView mNewsDetailPhotoIv;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout mToolbarLayout;
    @BindView(R.id.mask_view)
    View mMaskView;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBar;

    @Inject
    NewDetailPresenterImpl mNewDetailPresenterImpl;

    private  String mNewsTitle;
    private URLImageGetter mURLImageGetter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_news_detail;
    }

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    public void initViews() {
        String postId=getIntent().getStringExtra(Constants.NEWS_POST_ID);
        mNewDetailPresenterImpl.setpostId(postId);
        mPresenter=mNewDetailPresenterImpl;
        mPresenter.attachView(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case    R.id.action_settings:
                break;
            case  R.id.action_settings1:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("deprecation")
    @Override
    public void setNewsDetail(NewsDetail newsDetail) {
        mNewsTitle=newsDetail.getTitle();
        String newsSource=newsDetail.getSource();
        String newspTime= MyUtils.formatDate(newsDetail.getPtime());
        String newsBody=newsDetail.getBody();
        String newsImgSrc=getImgSrc(newsDetail);

        setToolBarLayout(mNewsTitle);
        mNewsDeatilFromTv.setText(getString(R.string.news_from,newsSource,newspTime));

        setNewsDetailPhotoIv(newsImgSrc);
        setNewsDetailBodyTv(newsDetail,newsBody);

    }

    private void setNewsDetailPhotoIv(String newsImgSrc) {
        Glide.with(this).load(newsImgSrc).asBitmap()
                .placeholder(R.mipmap.ic_loading)
                .format(DecodeFormat.PREFER_ARGB_8888)
                .error(R.mipmap.ic_load_fail)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mNewsDetailPhotoIv);
    }

    private void setNewsDetailBodyTv(final NewsDetail newsDetail, final String newsBody) {
        mSubscription= Observable.timer(500, TimeUnit.MILLISECONDS)
                .compose(TransformUtils.<Long>defaultSchedulers())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        mProgressBar.setVisibility(View.GONE);
                        //mFab.setVisibility(View.VISIBLE);
                        //YoYo.with(Techniques.BounceIn).playOn(mFab);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        setBody(newsDetail,newsBody);
                    }
                });
    }

    private void setBody(NewsDetail newsDetail, String newsBody) {
        int imgTotal=newsDetail.getImg().size();
        if(isShowBody(newsBody,imgTotal)){
            mURLImageGetter=new URLImageGetter(mNewsDeatilBodyTv,newsBody,imgTotal);
            mNewsDeatilBodyTv.setText(Html.fromHtml(newsBody,mURLImageGetter,null));

        }else{
            //mNewsDeatilBodyTv.setText(newsBody);
            mNewsDeatilBodyTv.setText(Html.fromHtml(newsBody));
        }
    }

    private boolean isShowBody(String newsBody, int imgTotal) {
        return MyApp.isHavePhoto()&&imgTotal>=2&&newsBody!=null;
    }

    public void setToolBarLayout(String newsTitle) {
        mToolbarLayout.setTitle(newsTitle);
        mToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this,R.color.white));
        mToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(this,R.color.white));
    }
    //获取CollapsingToolbarLayout顶部的图片
    private String getImgSrc(NewsDetail newsDetail) {
        List<NewsDetail.ImgBean> imgSrcs=newsDetail.getImg();
        String imgSrc;
        if(imgSrcs!=null&&imgSrcs.size()>0){
            imgSrc=imgSrcs.get(0).getSrc();
        }else{
            imgSrc=getIntent().getStringExtra(Constants.NEWS_IMG_RES);
        }
        return  imgSrc;
    }

    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        //mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showMsg(String message) {
        mProgressBar.setVisibility(View.GONE);
        if(NetUtil.isNetworkAvailable()){
            Snackbar.make(mAppBar,message,Snackbar.LENGTH_SHORT).show();
        }
    }
    //注解点击事件
    @OnClick(R.id.fab)
    public  void onClick(){share();}

    private void share() {
        Snackbar.make(mAppBar,"分享",Snackbar.LENGTH_SHORT).show();
    }
    @Override
    protected void onDestroy() {
        cancelUrlImageGetterSubscription();
        super.onDestroy();

    }

    private void cancelUrlImageGetterSubscription() {
        try {
            if (mURLImageGetter != null && mURLImageGetter.mSubscription != null
                    && !mURLImageGetter.mSubscription.isUnsubscribed()) {
                mURLImageGetter.mSubscription.unsubscribe();
                LogUtil.d(TAG,"UrlImageGetter unsubscribe");
            }
        } catch (Exception e) {
            LogUtil.d(TAG,"取消UrlImageGetter Subscription 出现异常： " + e.toString());
        }
    }

}
