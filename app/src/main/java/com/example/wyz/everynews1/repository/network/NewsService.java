package com.example.wyz.everynews1.repository.network;


import com.example.wyz.everynews1.mvp.entity.NewsDetail;
import com.example.wyz.everynews1.mvp.entity.NewsSummary;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Wyz on 2016/11/4.
 */

//获取具体数据接口
public interface NewsService {
    @GET("nc/article/{type}/{id}/{startPage}-20.html")
    Observable<Map<String, List<NewsSummary>>> getNewsList(
            @Header("Cache_Control") String cacheControl,
            @Path("type") String type,
            @Path("id") String id,
            @Path("startPage") int startPage);

    @GET("nc/article/{postId}/full.html")
    Observable<Map<String,NewsDetail>> getNewsDetail(
            @Header("Cache_Control") String cacheControl,
            @Path("postId") String postId
    );
    @GET
    Observable<ResponseBody> getNewsBodyHtmlPhoto(
            @Url String photoPath
    );

}
