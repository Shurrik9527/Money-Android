package com.moyacs.canary.network;


import com.moyacs.canary.MyApplication;
import com.moyacs.canary.util.NetworkUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 作者：luoshen on 2018/1/8 0008 17:18
 * 邮箱：rsf411613593@gmail.com
 * 说明：okhttp 缓存策略
 */

public class CacheInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        // 对请求进行拦截：无网络是强制读取缓存
        Request request = chain.request();
        if (!NetworkUtils.isNetworkAvailable(MyApplication.instance)) {
            request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
        //对响应进行拦截
        //有网络时，移除header，设置缓存超时时间为1小时
        okhttp3.Response response = chain.proceed(request);
        if (NetworkUtils.isNetworkAvailable(MyApplication.instance)) {
            int maxAge = 60 * 60;             //1小时
            response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public,max-age=" + maxAge)
                    .build();
        } else {
            //无网络时，缓存时间为1周
            int maxScale = 60 * 60 * 24 * 7;  // 1周
            response.newBuilder().removeHeader("Pragma")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxScale)
                    .build();
        }
        return response;
    }
}
