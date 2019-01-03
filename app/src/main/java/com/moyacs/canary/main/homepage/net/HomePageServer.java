package com.moyacs.canary.main.homepage.net;

import com.moyacs.canary.main.market.net.MarketDataBean;
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
 * 作者：luoshen on 2018/4/8 0008 14:33
 * 邮箱：rsf411613593@gmail.com
 * 说明：首页网络请求接口封装
 */

public interface HomePageServer {
    /**
     * 首页 banner 列表
     *
     * @param size 要获取的 banner 数量
     * @return
     */
    @FormUrlEncoded
    @POST("banner")
    Observable<Response<HttpResult<List<BannerDate>>>> getBannerList(@Field("size") int size);

    /**
     * 首页 交易机会列表
     *
     * @param size 每页数据数量
     * @param page 第几页
     * @return
     */
    @FormUrlEncoded
    @POST("chance")
    Observable<Response<HttpResult<List<DealChanceDate>>>> getDealChanceList(@Field("size") int size,
                                                                             @Field("page") int page);

    /**
     * 获取全部行情列表
     *
     * @return
     */
    @GET("price/symbols")
    Observable<Response<HttpResult<List<MarketDataBean>>>> getMarketList(@Query(HttpConstants.server) String server,
                                                                         @Query(HttpConstants.type) String type);
}
