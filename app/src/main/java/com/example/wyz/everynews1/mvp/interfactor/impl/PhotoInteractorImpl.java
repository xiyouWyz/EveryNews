package com.example.wyz.everynews1.mvp.interfactor.impl;

import com.example.wyz.everynews1.MyApp;
import com.example.wyz.everynews1.R;
import com.example.wyz.everynews1.common.HostType;
import com.example.wyz.everynews1.listener.RequestCallBack;
import com.example.wyz.everynews1.mvp.entity.GirlData;
import com.example.wyz.everynews1.mvp.entity.PhotoGirl;
import com.example.wyz.everynews1.mvp.interfactor.PhotoInteractor;
import com.example.wyz.everynews1.repository.network.RetrofitManager;
import com.example.wyz.everynews1.utils.TransformUtils;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;

/**
 * Created by Wyz on 2016/11/19.
 */
public class PhotoInteractorImpl implements PhotoInteractor<List<PhotoGirl>> {

    @Inject
    public PhotoInteractorImpl() {
    }



    @Override
    public Subscription loadPhotos(final RequestCallBack<List<PhotoGirl>> listener, int size, int page) {
        return RetrofitManager.getInstance(HostType.GANK_GIRL_PHOTO)
                .getPhotoListObservable(size,page)
                .map(new Func1<GirlData, List<PhotoGirl>>() {

                    @Override
                    public List<PhotoGirl> call(GirlData girlData) {
                        return girlData.getResults();
                    }
                })
                .compose(TransformUtils.<List<PhotoGirl>>defaultSchedulers())
                .subscribe(new Subscriber<List<PhotoGirl>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(MyApp.getAppContext().getString(R.string.load_error));
                    }

                    @Override
                    public void onNext(List<PhotoGirl> photoGirls) {
                        listener.success(photoGirls);
                    }
                });
    }
}
