package com.moyacs.canary.product_fxbtg.net_kline;

import com.moyacs.canary.network.HttpResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 作者：luoshen on 2018/3/23 0023 16:31
 * 邮箱：rsf411613593@gmail.com
 * 说明：产品详情页面 数据请求接口
 */

public interface ProductServer {
    /**
     * 获取 品种走势图数据 ，K 线数据
     *
     * @param symbol    品种代码
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @param period    数据间隔，以分钟为单位，
     * @return
     */
    @GET("price/records")
    Observable<Response<HttpResult<List<KLineData>>>> getKLineData(@Query("symbol") String symbol,
                                                                   @Query("startDate") String startDate,
                                                                   @Query("endDate") String endDate,
                                                                   @Query("period") String period,
                                                                   @Query("server") String server);
}
