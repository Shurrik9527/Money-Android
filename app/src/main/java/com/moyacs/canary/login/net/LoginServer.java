package com.moyacs.canary.login.net;

import com.moyacs.canary.main.market.net.MarketDataBean;
import com.moyacs.canary.network.HttpConstants;
import com.moyacs.canary.network.HttpResult;
import com.moyacs.canary.network.ServerResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import io.reactivex.Observable;
import retrofit2.http.Query;

/**
 * 作者：luoshen on 2018/3/5 0005 15:46
 * 邮箱：rsf411613593@gmail.com
 * 说明：登录、注册、找回密码
 */

public interface LoginServer {

    /**
     * 登录
     *
     * @param userName
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST("login/passwordLogin")
    Observable<Response<ServerResult<String>>> doLogin(@Field("loginName") String userName, @Field("password") String password);

    /**
     * 获取用户所有的  MT4 账户信息
     *
     * @return
     */
    @GET("user")
    Observable<Response<ServerResult<MT4Users>>> getMT4Info();

    /**
     * 获取验证码
     *
     * @param mobile
     * @return
     */
//    @POST("sendvcode")
//    Observable<Response<HttpResult<String>>> getCode(@Field("mobile") String mobile);
    @GET("login/getCode")
    Observable<Response<ServerResult<String>>> getCode(@Query("loginName") String mobile);


    /**
     * 注册
     *
     * @param mobile   电话号码
     * @param password 密码
     * @param fullname 全名
     * @param vcode    验证码
     * @return
     */

/*    @FormUrlEncoded
    @POST("register")
    Observable<Response<HttpResult<String>>> register(@Field("mobile") String mobile,
                                                      @Field("password") String password,
                                                      @Field("fullname") String fullname,
                                                      @Field("vcode") String vcode);*/
    @FormUrlEncoded
    @POST("login/register")
    Observable<Response<ServerResult<String>>> register(@Field("id") String id,
                                                        @Field("loginName") String mobile,
                                                        @Field("password") String password,
                                                        @Field("userName") String fullname,
                                                        @Field("verificationCode") String vcode);

    //上传公钥
    @POST("login/uploadPublicKey")
    @FormUrlEncoded
    Observable<ServerResult<String>> uploadPubKey(@Field("publicKey") String publicKey);


}
