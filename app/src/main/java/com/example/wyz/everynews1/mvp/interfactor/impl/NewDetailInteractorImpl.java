package com.example.wyz.everynews1.mvp.interfactor.impl;

import com.example.wyz.everynews1.MyApp;
import com.example.wyz.everynews1.common.HostType;
import com.example.wyz.everynews1.listener.RequestCallBack;
import com.example.wyz.everynews1.mvp.entity.NewsDetail;
import com.example.wyz.everynews1.mvp.entity.NewsSummary;
import com.example.wyz.everynews1.mvp.interfactor.NewDetailInteractor;
import com.example.wyz.everynews1.repository.network.RetrofitManager;
import com.example.wyz.everynews1.utils.MyUtils;
import com.example.wyz.everynews1.utils.TransformUtils;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.functions.Func1;

/**
 * Created by Wyz on 2016/11/17.
 */
public class NewDetailInteractorImpl implements NewDetailInteractor<NewsDetail>{
    @Inject
    public NewDetailInteractorImpl() {
    }

    @Override
    public Subscription loadNewsDetail(final  RequestCallBack<NewsDetail> callBack, final String postId) {
       return RetrofitManager.getInstance(HostType.NETEASE_NEWS_VIDEO).getNewDetailObserable(postId)
                   .map(new Func1<Map<String,NewsDetail>,NewsDetail>() {

                       @Override
                       public NewsDetail call(Map<String,NewsDetail> stringListMap) {
                           NewsDetail newsDetail= stringListMap.get(postId);
                           changeNewsDetail(newsDetail);
                           return  newsDetail;
                       }


                   })
               .compose(TransformUtils.<NewsDetail>defaultSchedulers())
               .subscribe(new Observer<NewsDetail>() {
                   @Override
                   public void onCompleted() {

                   }

                   @Override
                   public void onError(Throwable e) {
                        callBack.onError(MyUtils.analyzeNetworkError(e));
                   }

                   @Override
                   public void onNext(NewsDetail newsDetail) {
                        callBack.success(newsDetail);
                   }
               });
    }
    private void changeNewsDetail(NewsDetail newsDetail) {
        List<NewsDetail.ImgBean> imgSrcs=newsDetail.getImg();
        if(isChange(imgSrcs)) {
            String newsBody=newsDetail.getBody();
            newsBody=changeNewsBody(imgSrcs,newsBody);
            newsDetail.setBody(newsBody);
        }
    }
    private boolean isChange(List<NewsDetail.ImgBean> imgSrcs) {
        return imgSrcs != null && imgSrcs.size() >= 2 && MyApp.isHavePhoto();
    }
    private String changeNewsBody(List<NewsDetail.ImgBean> imgSrcs, String newsBody) {
        for (int i = 0; i < imgSrcs.size(); i++) {
            String oldChars = "<!--IMG#" + i + "-->";
            String newChars;
            if (i == 0) {
                newChars = "";
            } else {
                newChars = "<img src=\"" + imgSrcs.get(i).getSrc() + "\" />";
            }
            newsBody = newsBody.replace(oldChars, newChars);

        }
        return newsBody;
    }
}
