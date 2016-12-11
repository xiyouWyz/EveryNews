package com.example.wyz.everynews1.widget;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.widget.TextView;

import com.example.wyz.everynews1.MyApp;
import com.example.wyz.everynews1.R;
import com.example.wyz.everynews1.common.HostType;
import com.example.wyz.everynews1.repository.network.RetrofitManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Wyz on 2016/11/17.
 */
public class URLImageGetter implements Html.ImageGetter {
    private TextView mTextView;
    private  int mPicWidth;
    private  String mNewsbody;
    private  int mPicCount;
    private  int mPicTotal;
    private  static  final  String mFilePath= MyApp.getAppContext().getCacheDir().getAbsolutePath();
    public Subscription mSubscription;

    public URLImageGetter(TextView mTextView, String mNewsbody, int mPicTotal) {
        this.mTextView = mTextView;
        this.mNewsbody = mNewsbody;
        this.mPicTotal = mPicTotal;
        this.mPicWidth=mTextView.getWidth();
    }

    @Override
    public Drawable getDrawable(String source) {
        Drawable drawable;
        File file=new File(mFilePath,source.hashCode()+"");
        if(file.exists()){
            mPicCount++;
            drawable=getDrawableFromDisk(file);
        }else{
            drawable=getDrawableFromNet(source);
        }
        return drawable;
    }

    private Drawable getDrawableFromNet(final String source) {
         mSubscription= RetrofitManager.getInstance(HostType.NEWS_DETAIL_HTML_PHOTO)
                 .getNewsBodyHtmlPhoto(source)
                 .unsubscribeOn(Schedulers.io())
                 .subscribeOn(Schedulers.io())
                 .observeOn(AndroidSchedulers.mainThread())
                 .map(new Func1<ResponseBody, Boolean>() {

                     @Override
                     public Boolean call(ResponseBody responseBody) {
                         return  WritePicToDisk(responseBody,source);
                     }
                 })
                 .subscribe(new Subscriber<Boolean>() {
                     @Override
                     public void onCompleted() {

                     }

                     @Override
                     public void onError(Throwable e) {

                     }

                     @Override
                     public void onNext(Boolean isLoadSuccess) {
                        mPicCount++;
                         if(isLoadSuccess&&mPicCount==mPicTotal-1){
                             mTextView.setText(Html.fromHtml(mNewsbody,URLImageGetter.this,null));
                         }
                     }
                 });
        return  createPicPlaceholder();
    }
    private Boolean WritePicToDisk(ResponseBody responseBody, String source) {
        File file=new File(mFilePath,source.hashCode()+"");
        InputStream in=null;
        FileOutputStream out=null;
        int len;
        try {
            in=responseBody.byteStream();
            out=new FileOutputStream(file);
            byte[] buffer=new byte[1024];
            while ((len=in.read(buffer))!=-1){
                out.write(buffer,0,len);
            }
            return  true;
        }catch (Exception ex){
            return  false;
        }finally {
            try{
                if(in!=null)
                    in.close();
                if(out!=null)
                    out.close();
            }catch (IOException ignored){

            }

        }
    }
    private Drawable createPicPlaceholder() {
        Drawable drawable;
        int color= R.color.image_place_holder;
        drawable=new ColorDrawable(MyApp.getAppContext().getResources().getColor(color));
        drawable.setBounds(0,0,mPicWidth,mPicWidth/3);
        return  drawable;
    }



    private Drawable getDrawableFromDisk(File file) {
        Drawable drawable=Drawable.createFromPath(file.getAbsolutePath());
        if(drawable!=null){
            int picHeight=calculatePicHeight(drawable);
            drawable.setBounds(0,0,mPicWidth,picHeight);
        }
        return  drawable;
    }
    private int calculatePicHeight(Drawable drawable) {
        float imgWidth = drawable.getIntrinsicWidth();
        float imgHeight = drawable.getIntrinsicHeight();
        float rate = imgHeight / imgWidth;
        return (int) (mPicWidth * rate);
    }
}
