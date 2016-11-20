package com.example.wyz.everynews1.mvp.interfactor.impl;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import com.example.wyz.everynews1.MyApp;
import com.example.wyz.everynews1.listener.RequestCallBack;
import com.example.wyz.everynews1.mvp.interfactor.PhotoDetailInteractor;
import com.example.wyz.everynews1.utils.TransformUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;

/**
 * Created by Wyz on 2016/11/19.
 */
public class PhotoDetailInteractorImpl implements PhotoDetailInteractor<Uri> {


    @Inject
    public PhotoDetailInteractorImpl() {
    }

    @Override
    public Subscription loadPicture( final  RequestCallBack<Uri> callBack, final String url) {
        return Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                Bitmap bitmap=null;
                try {
                    bitmap= Picasso.with(MyApp.getAppContext())
                            .load(url)
                            .get();
                } catch (IOException e) {
                    subscriber.onError(e);
                }
                if(bitmap==null){
                    subscriber.onError(new Exception("下载图片失败"));
                }
                subscriber.onNext(bitmap);
                subscriber.onCompleted();
            }
        })
                .flatMap(new Func1<Bitmap, Observable<Uri>>() {
                    @Override
                    public Observable<Uri> call(Bitmap bitmap) {
                        return getUriObservable(bitmap,url);
                    }
                })
                .compose(TransformUtils.<Uri>defaultSchedulers())
                .subscribe(new Subscriber<Uri>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.onError("错误，请重试");
                    }

                    @Override
                    public void onNext(Uri uri) {
                        callBack.success(uri);
                    }
                });
    }

    private Observable<Uri> getUriObservable(Bitmap bitmap, String url) {
        File file=getImageFile(bitmap,url);
        if(file==null){
            return  Observable.error(new NullPointerException("Save Image file failed!"));
        }
        Uri uri=Uri.fromFile(file);
        // 通知图库更新 //Update the System --> MediaStore.Images.Media --> 更新ContentUri
        Intent scannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
        MyApp.getAppContext().sendBroadcast(scannerIntent);
        return  Observable.just(uri);
    }

    private File getImageFile(Bitmap bitmap, String url) {
        String fileName="/every_news/photo/"+url.hashCode()+".jpg";
        File file=new File(Environment.getExternalStorageDirectory(),fileName);
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        FileOutputStream outputStream=null;
        try {
            outputStream=new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        }catch (Exception ex){
            ex.printStackTrace();
            return  null;
        }finally {
            try {
                if(outputStream!=null){
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return  file;
    }
}
