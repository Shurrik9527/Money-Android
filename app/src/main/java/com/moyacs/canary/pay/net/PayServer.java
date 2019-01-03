package com.moyacs.canary.pay.net;

import com.moyacs.canary.network.HttpConstants;
import com.moyacs.canary.network.HttpResult;


import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 作者：luoshen on 2018/4/15 0015 10:34
 * 邮箱：rsf411613593@gmail.com
 * 说明：下单接口
 */

public interface PayServer {

    /**
     * expiredDate 有效期
     *
     * 买涨限价： BUY_LIMIT
     * 买涨止损： BUY_STOP
     * 买跌限价： SELL_LIMIT
     * 买跌止损： SELL_STOP
     */

    /**
     *  挂单，买涨，买跌，通用接口
     *
     * @param server  交易环境  DEMO 或者 live
     * @param mt4id     MT4ID
     * @param symbol    品种代码
     * @param type      交易类型
     * @param volume    手数
     * @param sl        止损
     * @param tp        止盈
     * @param ticket    单号
     * @param price    挂单价
     * @param expiredDate    有效期
     * @return
     */
    @FormUrlEncoded
    @POST("trading/execute")
    Observable<Response<HttpResult<Object>>> submitOrder(@Field(HttpConstants.server) String server,
                                                         @Field("mt4id") int mt4id,
                                                         @Field("symbol") String symbol,
                                                         @Field("type") String type,
                                                         @Field("volume") int volume,
                                                         @Field("sl") double sl,
                                                         @Field("tp") double tp,
                                                         @Field("ticket") String ticket,
                                                         @Field("price") double price,
                                                         @Field("expiredDate") String expiredDate
                                                         );


}
