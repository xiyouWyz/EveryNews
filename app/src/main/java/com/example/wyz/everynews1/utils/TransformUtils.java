package com.example.wyz.everynews1.utils;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by Wyz on 2016/11/13.
 */
public class TransformUtils {
    public static <T> rx.Observable.Transformer<T, T> defaultSchedulers() {
        return new rx.Observable.Transformer<T, T>() {

            @Override
            public rx.Observable<T> call(rx.Observable<T> tObservable) {
                return tObservable
                        .unsubscribeOn(Schedulers.io())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
