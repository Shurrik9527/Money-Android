package com.moyacs.canary.network;

import com.moyacs.canary.bean.BannerJsons;
import com.moyacs.canary.bean.HoldOrderDateBean;
import com.moyacs.canary.bean.HomeDealChanceVo;
import com.moyacs.canary.bean.MarketDataBean;
import com.moyacs.canary.bean.PaymentDateBean;
import com.moyacs.canary.bean.TradeVo;
import com.moyacs.canary.bean.TransactionRecordVo;
import com.moyacs.canary.bean.DealChanceBean;
import com.moyacs.canary.bean.UserAmountVo;
import com.moyacs.canary.bean.UserInfoVo;
import com.moyacs.canary.bean.WithdrawalDataBean;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
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
    Observable<ServerResult<BannerJsons>> getBannerList(@Field("page") String size, @Field("pageSize") String pageSize);

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
    Observable<ServerResult<String>> updateNickName(@Field("nickname") String nickname, @Field("userImg") String userImg);

    /**
     * 平仓
     */
    @FormUrlEncoded
    @POST("transaction/sell")
    Observable<ServerResult<String>> transactionSell(@FieldMap Map<String, Object> map);

    /**
     * 获取我的自选列表
     */
    @POST("userSymbol/getList")
    Observable<ServerResult<TradeVo>> getMyChoiceList();

    /**
     * 添加自选
     */
    @POST("userSymbol/save")
    @FormUrlEncoded
    Observable<ServerResult<String>> saveOptional(@Field("symbolCode") String symbolCode);

    /**
     * 删除自选
     */
    @POST("userSymbol/delete")
    @FormUrlEncoded
    Observable<ServerResult<String>> deleteOptional(@Field("symbolCode") String symbolCode);

    /**
     * 刷新auth
     */
    @POST("login/refreshJWT")
    Call<ServerResult<String>> getAuth();

    /**
     * 获取webSocket地址
     */
    @GET("symbolInfo/getWebSocketAddress")
    Observable<ServerResult<String>> getWebSocketAddress();

    /**
     * 设置为不过夜
     */
    @FormUrlEncoded
    @POST("transactionRecord/updateOvernight")
    Observable<ServerResult<String>> updateOverNight(@Field("id") String id);

    /**
     * 上传图片
     */
    @POST("file/upload")
    @Multipart
    Observable<ServerResult<String>> uploadFile(@PartMap Map<String, RequestBody> body, @Part List<MultipartBody.Part> file);



    //heguogui


    /**
     * 首页 交易机会列表
     */
    @POST("chance")
    Observable<HttpResult<List<DealChanceBean>>> getDealChanceList();


    /**
     * 首页 交易机会列表
     */
    @POST("tradingOpportunities/getList")
    Observable<HttpResponse<HomeDealChanceVo>> tradingOpportunitiesList();


    /**
     * 获取全部行情列表
     *
     * @return
     */
    @GET("price/symbols")
    Observable<HttpResult<List<MarketDataBean>>> getMarketList(@Query(HttpConstants.server) String server,
                                                               @Query(HttpConstants.type) String type);




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
    Observable<Response<HttpResult<List<HoldOrderDateBean>>>> getTradingRecords(@Query("mt4id") int mt4id,
                                                                                @Query("server") String server,
                                                                                @Query("startDate") String startDate,
                                                                                @Query("endDate") String endDate

    );

    /**
     * 获取持仓列表 ，
     * @param mt4id
     * @param server
     * @param startDate 为了获取交易记录所增加的字段，已无用，可以删除
     * @param endDate 为了获取交易记录所增加的字段，已无用，可以删除
     * @return
     */
    @GET("trading/orders")
    Observable<Response<HttpResult<List<HoldOrderDateBean>>>> getChiCangList(@Query("mt4id") int mt4id,
                                                                           @Query("server") String server,
                                                                           @Query("startDate") String startDate,
                                                                           @Query("endDate") String endDate

    );


    /**
     * 获取融云 token
     * @param userId 用户ID
     * @param name 用户呢城
     * @param portraitUri 头像路径
     * @return
     */
    @POST("getToken")
    Observable<Response<Object>> getRongToken(@Query("userId") String userId,
                                                                             @Query("name") String name,
                                                                             @Query("portraitUri") String portraitUri);


    /**
     * 属性用户信息
     * @param userId 用户ID
     * @param name 用户呢城
     * @param portraitUri 头像路径
     * @return
     */
    @POST("refresh")
    Observable<Response<Object>> getRefreshRongUserInform(@Query("userId") String userId,
                                                                           @Query("name") String name,
                                                                           @Query("portraitUri") String portraitUri);



}
