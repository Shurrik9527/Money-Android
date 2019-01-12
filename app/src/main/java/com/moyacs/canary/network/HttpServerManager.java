package com.moyacs.canary.network;


import android.content.Context;

import com.moyacs.canary.util.LogUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 作者：luoshen on 2017/12/13 0013 15:09
 * 邮箱：rsf411613593@gmail.com
 * <p>
 * okhttp + retrofit 参数配置，单例模式
 */

public class HttpServerManager {

    private static HttpServerManager retrofitServiceManager;
    private final Retrofit mRetrofit;

    private static Context context;

    public static void setHeaderValue(String headerValue) {
        HttpServerManager.headerValue = headerValue;
    }

    private static String headerValue;


    private HttpServerManager() {
        mRetrofit = initRetrofit();
    }

    public static HttpServerManager getInstance() {
//        headerValue = SPUtils.getInstance().getString(AppConstans.token);
//        LogUtils.d("getInstance  :  headerValue::::" + headerValue);
        if (retrofitServiceManager == null) {
            synchronized (HttpServerManager.class) {
                if (retrofitServiceManager == null) {
                    retrofitServiceManager = new HttpServerManager();
                }
            }
        }
        return retrofitServiceManager;
    }

    public static void setContext(Context context) {
        HttpServerManager.context = context;

    }

    /**
     * 配置 OkHttpClient 相关参数
     *
     * @return
     */
    private OkHttpClient initOkHttpClient() {


//        File cacheFile = new File(context.getExternalCacheDir().getAbsolutePath(), "mycache");
//        Cache cache = new Cache(cacheFile, 1024 * 1024 * 20);
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient().newBuilder()
                .retryOnConnectionFailure(true)//失败重试
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                //添加请求头
                .addInterceptor(new HeadersInterceptor());
//                .cache(cache)//缓存目录
//                .addNetworkInterceptor(new CacheInterceptorOnNet())//在线缓存
//                .addInterceptor(new CacheForceInterceptorNoNet())//离线缓存


//        client.interceptors().add(new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                // 获取 Cookie
//                Response resp = chain.proceed(chain.request());
//                List<String> cookies = resp.headers("Set-Cookie");
//                String cookieStr = "";
//                if (cookies != null && cookies.size() > 0) {
//                    for (int i = 0; i < cookies.size(); i++) {
//                        cookieStr += cookies.get(i);
//                    }
//                }
//                return resp;
//            }
//        });
        return client.build();
    }

    /**
     * retrofit 基础配置
     *
     * @return
     */
    private Retrofit initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HttpConstants.Base_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(initOkHttpClient())
                .build();
//        RetrofitCache.getInstance().addRetrofit(retrofit);
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
        return mRetrofit.create(service);
    }

    /**
     * 请求拦截器，修改请求header
     */
    private class HeadersInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request()
                    .newBuilder()
//                    .header(HttpConstants.header_key, HttpConstants.header_value)
                    .header(HttpConstants.header_key, HttpConstants.header_value + headerValue)
                    //注册的 请求头
                    .header("Referer", "android")
                    .build();
            LogUtils.d("请求头的 key ：" + HttpConstants.header_key);
//            LogUtils.d("本次请求头：" + header);
            return chain.proceed(request);
        }
    }

}
