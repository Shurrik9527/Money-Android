package com.moyacs.canary.pay.tixian.net;

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

public interface WithdrawServer {




    /**
     *  提现接口
     * @param mt4id
     * @param type  WireTransfer 暂时写死
     * @param amount 金额
     * @param accountNumber 账号
     * @param accountName   账户名
     * @param bankName  发卡行
     * @param bankAddress   开户地址
     * @return
     */
    @FormUrlEncoded
    @POST("withdraw/apply")
    Observable<Response<HttpResult<Object>>> withdraw(@Field("mt4id") int mt4id,
                                                      @Field("type") String type,
                                                      @Field("amount") double amount,
                                                      @Field("accountNumber") String accountNumber,
                                                      @Field("accountName") String accountName,
                                                      @Field("bankName") String bankName,
                                                      @Field("bankAddress") String bankAddress
    );
}
