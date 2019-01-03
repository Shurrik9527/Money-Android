package com.moyacs.canary.main.deal.net_tab3;

import com.moyacs.canary.main.deal.net_tab2.ChiCangDateBean;
import com.moyacs.canary.network.HttpConstants;
import com.moyacs.canary.network.HttpResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 作者：luoshen on 2018/4/18 0018 11:41
 * 邮箱：rsf411613593@gmail.com
 * 说明：资金 页面 网络请求接口封装
 */

public interface FundServer {
    /**
     * 获取 资金相关数据
     *
     * @return
     */
    @GET("/userAmount/get")
    Observable<Response<HttpResult<FundDataBean>>> getFund();


    /**
     * 入金记录
     * @param mt4id
     * @param startDate
     * @param endDate
     * @param pageSize 每页几个数据
     * @param pageNumber 第几页
     * @return
     */
    @GET("payment/records")
    Observable<Response<HttpResult<PaymentDateBean>>> getPayment(@Query("mt4id") int mt4id,
                                                                 @Query("startDate") String startDate,
                                                                 @Query("endDate") String endDate,
                                                                 @Query("pageSize") int pageSize,
                                                                 @Query("pageNumber") int pageNumber
    );

    /**
     * 出金记录
     * @param mt4id
     * @param startDate
     * @param endDate
     * @param pageSize 每页几个数据
     * @param pageNumber 第几页
     * @return
     */
    @GET("withdraw/records")
    Observable<Response<HttpResult<WithdrawalDataBean>>> getWithdrawal(@Query("mt4id") int mt4id,
                                                                       @Query("startDate") String startDate,
                                                                       @Query("endDate") String endDate,
                                                                       @Query("pageSize") int pageSize,
                                                                       @Query("pageNumber") int pageNumber
                                                                       );

    /**
     * 获取交易记录
     * @param mt4id
     * @param server
     * @param startDate
     * @param endDate
     * @return
     */
    @GET("trading/records")
    Observable<Response<HttpResult<List<ChiCangDateBean>>>> getTradingRecords(@Query("mt4id") int mt4id,
                                                                           @Query("server") String server,
                                                                           @Query("startDate") String startDate,
                                                                           @Query("endDate") String endDate

    );
}
