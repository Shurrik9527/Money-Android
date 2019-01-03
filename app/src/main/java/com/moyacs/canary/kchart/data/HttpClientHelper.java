package com.moyacs.canary.kchart.data;

import android.content.Context;



import java.io.IOException;
import java.io.InputStream;
import java.net.URL;



public class HttpClientHelper {
    static final int CONNECTTIMEOUT = 10 * 1000;
    static final int READTIMEOUT = 10 * 1000;
    static String sCookie; //设置cookie服务端当成是同一个请求
    private static String TAG = "HttpClientHelper";

    public static final int REQUEST_SUCCESS =1;// 访问成功
    public static final int REQUEST_FAILED =2;// 访问失败

    private HttpClientHelper() {
    }

    public static InputStream getStreamFromGet(Context context, String url) {
        try {
            if (url == null || url.trim().length() == 0) {
                return null;
            }
            URL u = new URL(url);
            return (InputStream) u.getContent();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getStringFromGet(Context context, String path) {
        return OkHttpFactory.getInstance().getSync(NetWorkUtils.setParam4get(path, null)).toString();
    }




}
