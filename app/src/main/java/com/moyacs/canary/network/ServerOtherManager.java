package com.moyacs.canary.network;

import android.text.TextUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 融云请求服务
 * @date 2019/3/3
 * @email 252774645@qq.com
 */
public class ServerOtherManager {

    private static final ServerOtherManager ourInstance = new ServerOtherManager();
    public static ServerOtherManager getInstance() {
        return ourInstance;
    }
    private  Retrofit mRetrofit;
    private String mUrl;

    public void initUrl(String url){
        this.mUrl =url;
        mRetrofit = initRetrofit();
    }
    private ServerOtherManager() {

    }

    public ServerApi getServer() {
        return create(ServerApi.class);
    }

    /**
     * 配置 OkHttpClient 相关参数
     *
     * @return
     */
    private OkHttpClient initOkHttpClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient().newBuilder()
                .retryOnConnectionFailure(true)//失败重试
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor);
        return client.build();
    }

    /**
     * retrofit 基础配置
     *
     * @return
     */
    private Retrofit initRetrofit() {

        if(TextUtils.isEmpty(mUrl)){
            return null;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(initOkHttpClient())
                .build();
        return retrofit;
    }

    /**
     * 创建不同的接口实例
     *
     * @param service
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> service) {
        if(mRetrofit==null){
            return null;
        }
        return mRetrofit.create(service);
    }


}
