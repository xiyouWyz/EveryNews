package com.example.wyz.everynews1.mvp.interfactor.impl;
import com.example.wyz.everynews1.MyApp;
import com.example.wyz.everynews1.R;
import com.example.wyz.everynews1.common.ApiConstants;
import com.example.wyz.everynews1.greendao.NewsChannelTable;
import com.example.wyz.everynews1.listener.RequestCallBack;
import com.example.wyz.everynews1.mvp.interfactor.NewsInteractor;
import com.example.wyz.everynews1.repository.db.NewsChannelTableManager;
import com.example.wyz.everynews1.utils.TransformUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by Wyz on 2016/11/8.
 */
public class NewsInteractorImpl implements NewsInteractor<List<NewsChannelTable>>{

    @Inject
    public NewsInteractorImpl() {
    }

    @Override
    public Subscription lodeNewsChannels(final  RequestCallBack<List<NewsChannelTable>> callback) {
        return rx.Observable.create(new rx.Observable.OnSubscribe<List<NewsChannelTable>>() {
                    @Override
                    public void call(Subscriber<? super List<NewsChannelTable>> subscriber) {
                        NewsChannelTableManager.initDB();
                        subscriber.onNext(NewsChannelTableManager.loadNewsChannelsMine());
                        //subscriber.onNext(loadNewcChannelData());
                        subscriber.onCompleted();
                    }
                })
               .compose(TransformUtils.<List<NewsChannelTable>>defaultSchedulers())
                .subscribe(new Subscriber<List<NewsChannelTable>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(MyApp.getAppContext().getString(R.string.db_error));
                    }

                    @Override
                    public void onNext(List<NewsChannelTable> newsChannelTables) {
                        callback.success(newsChannelTables);
                    }
                });
    }

/*
    public  List<NewsChannelTable> loadNewcChannelData() {

        List<NewsChannelTable> newsChannelTables=new ArrayList<>();

        List<String> channelName = Arrays.asList(MyApp.getAppContext().getResources()
                .getStringArray(R.array.news_channel_choose_name));
        List<String> channelId = Arrays.asList(MyApp.getAppContext().getResources()
                .getStringArray(R.array.news_channel_choose_id));
        for (int i = 0; i < channelName.size(); i++) {
            NewsChannelTable entity = new NewsChannelTable(channelName.get(i), channelId.get(i)
                    , ApiConstants.getType(channelId.get(i)), i <= 5, i, i == 0);
            newsChannelTables.add(entity);
        }
        return  newsChannelTables;

    }*/
}
