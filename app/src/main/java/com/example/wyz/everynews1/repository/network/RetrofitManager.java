package com.example.wyz.everynews1.repository.network;
import android.support.annotation.NonNull;
import android.util.SparseArray;

import com.example.wyz.everynews1.common.ApiConstants;
import com.example.wyz.everynews1.common.HostType;
import com.example.wyz.everynews1.mvp.entity.GirlData;
import com.example.wyz.everynews1.mvp.entity.NewsDetail;
import com.example.wyz.everynews1.mvp.entity.NewsSummary;
import com.example.wyz.everynews1.mvp.entity.PhotoGirl;
import com.example.wyz.everynews1.utils.NetUtil;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by Wyz on 2016/11/4.
 */

//网络请求，加载数据
public class RetrofitManager {
    private NewsService newsService;
    /**
     * 设缓存有效期为两天
     */
    private static final long CACHE_STALE_SEC = 60 * 60 * 24 * 2;
    /**
     * 查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
     * max-stale 指示客户机可以接收超出超时期间的响应消息。如果指定max-stale消息的值，那么客户机可接收超出超时期指定值之内的响应消息。
     */
    private static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;

    /**
     * 查询网络的Cache-Control设置，头部Cache-Control设为max-age=0
     * (假如请求了服务器并在a时刻返回响应结果，则在max-age规定的秒数内，浏览器将不会发送对应的请求到服务器，数据由缓存直接返回)时则不会使用缓存而请求服务器
     */
    private static final String CACHE_CONTROL_AGE = "max-age=0";

    private static volatile OkHttpClient okHttpClient;
    private static SparseArray<RetrofitManager> sRetrofitManager = new SparseArray<>(HostType.TYPE_COUNT);


    private OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            synchronized (RetrofitManager.class) {
                if (okHttpClient == null) {
                    okHttpClient = new OkHttpClient.Builder()
                            .connectTimeout(6, TimeUnit.SECONDS)
                            .readTimeout(6, TimeUnit.SECONDS)
                            .writeTimeout(6, TimeUnit.SECONDS)
                            .build();
                           /* .addInterceptor(mRewriteCacheControlInterceptor)
                            .addNetworkInterceptor(mRewriteCacheControlInterceptor)
                            .addInterceptor(mLoggingInterceptor)*/
                }
            }
        }
        return okHttpClient;
    }

    public RetrofitManager(@HostType.HostTypeChecker int hostType) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiConstants.getHost(hostType))
                .client(getOkHttpClient()).addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        newsService = retrofit.create(NewsService.class);
    }

    /**
     * @param hostType NETEASE_NEWS_VIDEO：1 （新闻，视频），GANK_GIRL_PHOTO：2（图片新闻）;
     *                 EWS_DETAIL_HTML_PHOTO:3新闻详情html图片)
     */
    public static RetrofitManager getInstance(int hostType) {
        RetrofitManager retrofitManager = sRetrofitManager.get(hostType);
        if (retrofitManager == null) {
            retrofitManager = new RetrofitManager(hostType);
            sRetrofitManager.put(hostType, retrofitManager);
            return retrofitManager;
        }
        return retrofitManager;
    }
    /**
     * 根据网络状况获取缓存的策略
     */
    @NonNull
    private String getCacheControl() {
        return NetUtil.isNetworkAvailable() ? CACHE_CONTROL_AGE : CACHE_CONTROL_CACHE;
    }
    /**
     * example：http://c.m.163.com/nc/article/headline/T1348647909107/0-20.html
     *
     * @param newsType  ：headline为头条,house为房产，list为其他
     * @param newId     ：T13458647909107
     * @param startPage ：0-20
     */
    public rx.Observable<Map<String, List<NewsSummary>>> getNewsListObservable(String newsType, String newId, int startPage) {
        return newsService.getNewsList(getCacheControl(), newsType, newId, startPage);
    }
    public  rx.Observable<Map<String,NewsDetail>> getNewDetailObservable(String postId) {
        return  newsService.getNewsDetail(getCacheControl(),postId);
    }
    public Observable<ResponseBody> getNewsBodyHtmlPhoto(String photoPath){
        return  newsService.getNewsBodyHtmlPhoto(photoPath);
    }
    public Observable<GirlData> getPhotoListObservable(int size, int page){
        return  newsService.getPhotoList(size,page);
    }



}

















    /*
    public  static  void getNewDetail(String postId)
    {
        StringBuilder stringBuilder=new StringBuilder("http://c.m.163.com/nc/article/");
        stringBuilder.append(postId).append("/").append("full.html");
        getHtmlFromUrl(stringBuilder.toString());
    }

    private  static  String getHtmlFromUrl(String url)
    {
        StringBuilder stringBuilder1=null;
        getOkHttpClient();
        Request request=new Request.Builder().url(url).build();
        try{
            Response response=okHttpClient.newCall(request).execute();
            BufferedReader bufferedReader=new BufferedReader(response.body().charStream());
            stringBuilder1 =new StringBuilder();
            String line;
            while (((line=bufferedReader.readLine())!=null))
            {
                stringBuilder1.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder1 != null ? stringBuilder1.toString() : null;
    }*/

