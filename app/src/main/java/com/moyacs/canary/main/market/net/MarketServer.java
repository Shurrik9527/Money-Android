package com.moyacs.canary.main.market.net;

import android.support.v4.media.session.PlaybackStateCompat;

import com.moyacs.canary.common.AppConstans;
import com.moyacs.canary.network.HttpConstants;
import com.moyacs.canary.network.HttpResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 作者：luoshen on 2018/3/7 0007 10:03
 * 邮箱：rsf411613593@gmail.com
 * 说明：行情列表 ，网络请求接口
 */

public interface MarketServer {


//    @FormUrlEncoded
//    @POST("/price/symbols")
//    Observable<Response<HttpResult<List<MarketDataBean>>>> getMarketList();


    /**
     * 获取自选列表
     *
     * @param userName 账号
     * @param server   环境
     * @return
     */
    @FormUrlEncoded
    @POST("price/private")
    Observable<Response<HttpResult<List<MarketDataBean>>>> getMarketList_optional(@Field("username") String userName,
                                                                                  @Field(HttpConstants.server) String server);

    /**
     * 获取行情列表
     *
     * @return
     */
    @GET("price/symbols")
    Observable<Response<HttpResult<List<MarketDataBean>>>> getMarketList_type(@Query(HttpConstants.server) String server,
                                                                         @Query(HttpConstants.type) String type);
}
