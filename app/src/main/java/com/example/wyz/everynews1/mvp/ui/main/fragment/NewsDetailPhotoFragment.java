package com.example.wyz.everynews1.mvp.ui.main.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.wyz.everynews1.R;
import com.example.wyz.everynews1.common.Constants;
import com.example.wyz.everynews1.event.NewsDetailPhotoOnClickEvent;
import com.example.wyz.everynews1.mvp.ui.main.fragment.base.BaseFragment;
import com.example.wyz.everynews1.utils.RxBus;
import com.example.wyz.everynews1.utils.TransformUtils;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Wyz on 2016/11/18.
 */
public class NewsDetailPhotoFragment extends BaseFragment{

    @BindView(R.id.photo_view)
    PhotoView mPhotoView;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    private  String mImgSrc;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            mImgSrc=getArguments().getString(Constants.PHOTO_DETAIL_IMGSRC);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(getLayoutId(),container,false);
        ButterKnife.bind(this,view);
        initViews(view);
        return  view;
    }

    @Override
    public void initInjector() {

    }

    @Override
    public void initViews(View view) {
        mProgressBar.setVisibility(View.VISIBLE);
        initPhotoView();
        setPhotoViewClickEvent();
    }


    private void initPhotoView() {
        mSubscription= Observable.timer(100, TimeUnit.MILLISECONDS)// 直接使用glide加载的话，activity切换动画时背景短暂为默认背景色
                .compose(TransformUtils.<Long>defaultSchedulers())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        Glide.with(NewsDetailPhotoFragment.this).load(mImgSrc).asBitmap()
                                .format(DecodeFormat.PREFER_ARGB_8888)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .error(R.mipmap.ic_load_fail)
                                .into(mPhotoView);
                        mProgressBar.setVisibility(View.GONE);
                    }
                });
    }
    private void setPhotoViewClickEvent() {
        mPhotoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float v, float v1) {
                NewsDetailPhotoOnClickEvent.flag="inSide";
                handleOnTabEvent();
            }

            @Override
            public void onOutsidePhotoTap() {
                NewsDetailPhotoOnClickEvent.flag="outSide";
                handleOnTabEvent();
            }
        });
    }

    private void handleOnTabEvent() {
        RxBus.getInstance().post(new NewsDetailPhotoOnClickEvent());
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_news_photo_detail;
    }
}
