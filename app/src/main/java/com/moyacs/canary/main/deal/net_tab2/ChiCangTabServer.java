package com.moyacs.canary.main.deal.net_tab2;

import com.moyacs.canary.common.AppConstans;
import com.moyacs.canary.login.net.MT4Users;
import com.moyacs.canary.network.HttpResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 作者：luoshen on 2018/4/16 0016 20:28
 * 邮箱：rsf411613593@gmail.com
 * 说明：tab2 持仓页面接口封装
 */

public interface ChiCangTabServer {

    /**
     * 获取持仓列表 ，
     * @param mt4id
     * @param server
     * @param startDate 为了获取交易记录所增加的字段，已无用，可以删除
     * @param endDate 为了获取交易记录所增加的字段，已无用，可以删除
     * @return
     */
    @GET("trading/orders")
    Observable<Response<HttpResult<List<ChiCangDateBean>>>> getChiCangList(@Query("mt4id") int mt4id,
                                                                           @Query("server") String server,
                                                                           @Query("startDate") String startDate,
                                                                           @Query("endDate") String endDate

    );
}
