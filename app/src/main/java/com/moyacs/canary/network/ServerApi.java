package com.moyacs.canary.network;

import com.moyacs.canary.main.deal.net_tab3.TransactionRecordVo;
import com.moyacs.canary.main.deal.net_tab3.UserAmountVo;
import com.moyacs.canary.main.homepage.net.BannerDate;
import com.moyacs.canary.main.market.net.TradeVo;
import com.moyacs.canary.main.me.UserInfoVo;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ServerApi {
    /**
     * 登录
     *
     * @param userName
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST("login/passwordLogin")
    Observable<ServerResult<String>> doLogin(@Field("loginName") String userName, @Field("password") String password);

    /**
     * 获取验证码
     *
     * @param mobile
     * @return
     */
    @GET("login/getCode")
    Observable<ServerResult<String>> getCode(@Query("loginName") String mobile);

    /**
     * 注册
     *
     * @param mobile   电话号码
     * @param password 密码
     * @param fullname 全名
     * @param vcode    验证码
     * @return
     */
    @FormUrlEncoded
    @POST("login/register")
    Observable<ServerResult<String>> register(@Field("id") String id,
                                                        @Field("loginName") String mobile,
                                                        @Field("password") String password,
                                                        @Field("userName") String fullname,
                                                        @Field("verificationCode") String vcode);

    /**
     * 上传公钥
     *
     * @param publicKey 公钥
     */
    @POST("login/uploadPublicKey")
    @FormUrlEncoded
    Observable<ServerResult<String>> uploadPubKey(@Field("publicKey") String publicKey);


    /**
     * 首页 banner 列表
     *
     * @param size 要获取的 banner 数量
     */
    @FormUrlEncoded
    @POST("banner/getList")
    Observable<Response<ServerResult<BannerDate>>> getBannerList(@Field("page") String size, @Field("pageSize") String pageSize);

    /**
     * 获取可以交易品种
     */
    @FormUrlEncoded
    @POST("symbolInfo/getList")
    Observable<ServerResult<TradeVo>> getTradList(@Field("page") String page, @Field("pageSize") String pageSize);

    /**
     * 买入
     */
    @FormUrlEncoded
    @POST("transaction/buy")
    Observable<ServerResult<String>> transactionBuy(@FieldMap Map<String, Object> map);

    /**
     * 获取资金状况
     */
    @POST("userAmount/get")
    Observable<ServerResult<UserAmountVo>> getUserAmountInfo();

    /**
     * 交易记录
     *
     * @param transactionStatus 0除挂单外所有 1持仓 3挂单（已成功的则归到持仓）
     */
    @FormUrlEncoded
    @POST("transactionRecord/getList")
    Observable<ServerResult<TransactionRecordVo>> getTransactionRecordList(@Field("loginName") String loginName,
                                                                           @Field("transactionStatus") String transactionStatus);

    /**
     * 忘记密码
     */
    @FormUrlEncoded
    @POST("login/forgetPassword")
    Observable<ServerResult<String>> forgetPassWord(@FieldMap Map<String, Object> map);

    /**
     * 挂单
     */
    @FormUrlEncoded
    @POST("transaction/reserve")
    Observable<ServerResult> reserveTransaction(@FieldMap Map<String, Object> map);

    /**
     * 获取用户信息
     */
    @POST("user/getUser")
    Observable<ServerResult<UserInfoVo>> getUserInfo();

    /**
     * 修改用户昵称
     */
    @FormUrlEncoded
    @POST("user/edit")
    Observable<ServerResult<String>> updateNickName(@Field("nickname") String nickname);

    /**
     * 平仓
     */
    @FormUrlEncoded
    @POST("transaction/sell")
    Observable<ServerResult<String>> transactionSell(@FieldMap Map<String, Object> map);

}
