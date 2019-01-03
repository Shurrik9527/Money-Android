package com.moyacs.canary.network;

import android.content.Context;
import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;

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

public class ServerManger {
    private static final ServerManger ourInstance = new ServerManger();

    public static ServerManger getInstance() {
        return ourInstance;
    }

    private final Retrofit mRetrofit;

    private ServerManger() {
        mRetrofit = initRetrofit();
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


//        File cacheFile = new File(context.getExternalCacheDir().getAbsolutePath(), "mycache");
//        Cache cache = new Cache(cacheFile, 1024 * 1024 * 20);
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient().newBuilder()
                .retryOnConnectionFailure(true)//失败重试
                .connectTimeout(HttpConstants.default_connectTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(HttpConstants.default_readTimeout, TimeUnit.MINUTES)
                .writeTimeout(HttpConstants.default_writeTimeout, TimeUnit.MINUTES)
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
                .baseUrl(HttpConstants.SERVER_HOST)
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
        return mRetrofit.create(service);
    }

    /**
     * 请求拦截器，修改请求header
     */
    private class HeadersInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response response = chain.proceed(chain.request());
            String header = response.header(HttpConstants.header_key);
            if (!TextUtils.isEmpty(header)) {
                HttpConstants.header_value = header;
            }
            Request request = chain.request()
                    .newBuilder()
                    .header(HttpConstants.header_key,  HttpConstants.header_value)
//                    .header(HttpConstants.header_key, HttpConstants.header_value + headerValue)
                    //注册的 请求头
//                    .header("Referer", "android")
                    .build();
//            LogUtils.d("请求头的 key ：" + HttpConstants.header_key);
            LogUtils.d("本次请求头：" + HttpConstants.header_value);
            return chain.proceed(request);
        }
    }
}
