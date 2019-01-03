package com.moyacs.canary.kchart.data;


import android.text.TextUtils;
import android.util.Log;


import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class OkHttpFactory {

    public static String TAG = OkHttpFactory.class.getSimpleName();

    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType MEDIA_TYPE_STR = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

    /*上传指定的文件类型  图片*/
    public static final MediaType CONTENT_TYPE_IMG = MediaType.parse("image/png");

    public OkHttpClient singleClient;
    private static OkHttpFactory okHttpUtils = null;

    private OkHttpFactory() {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        /* 超时设置 */
        okHttpClientBuilder.connectTimeout(20, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS);
        /*okHttpClientBuilder.addInterceptor(new OkhttpLoggingInterceptor());
        if (!ServerConnect.getInstance().isProModel()) {
            //非产线环境信任服务器证书
            okHttpClientBuilder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            //非产线环境可以使用ssl3.0
            ConnectionSpec MODEN_TLS_WITH_SSL3 = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                    .tlsVersions(TlsVersion.TLS_1_2, TlsVersion.TLS_1_1, TlsVersion.TLS_1_0, TlsVersion.SSL_3_0)
                    .build();
            List<ConnectionSpec> DEFAULT_CONNECTION_SPECS_YQB = Util.immutableList(MODEN_TLS_WITH_SSL3, ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.CLEARTEXT);
            okHttpClientBuilder.connectionSpecs(DEFAULT_CONNECTION_SPECS_YQB);
        }*/
        singleClient = okHttpClientBuilder.build();
    }

    public static OkHttpFactory getInstance() {
        if (okHttpUtils == null) {
            synchronized (OkHttpFactory.class) {
                if (okHttpUtils == null) {
                    okHttpUtils = new OkHttpFactory();
                }
            }
        }
        return okHttpUtils;
    }

    public static Call postAsync(String url, String requestJson, Map<String, String> header, Callback callback) {
        OkHttpClient okHttpClient = OkHttpFactory.getInstance().singleClient;
        Request.Builder request = new Request.Builder()
                .url(url);
        addCommonHeaders(request, header);
        Log.e(TAG, url+"?"+requestJson);
        request.post(RequestBody.create(MEDIA_TYPE_STR, requestJson));
        Call call = okHttpClient.newCall(request.build());
        call.enqueue(callback);
        return call;
    }

    public static String postSync(String url, String requestJson, Map<String, String> header) {
        OkHttpClient okHttpClient = OkHttpFactory.getInstance().singleClient;
        Request.Builder request = new Request.Builder()
                .url(url);
        addCommonHeaders(request, header);
        Log.e(TAG, url+"?"+requestJson);
        request.post(RequestBody.create(MEDIA_TYPE_STR, requestJson));
        try {
            Response response = okHttpClient.newCall(request.build()).execute();
            String res = response.body().string();
            Log.e(TAG, res);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "okhttp postSync error:" + e.getMessage());
        }
        return null;
    }

    public static Call getAsync(String url, Callback callback) {
        OkHttpClient okHttpClient = OkHttpFactory.getInstance().singleClient;
        Request.Builder request = new Request.Builder()
                .url(url).get();
        request.addHeader("User-Agent", "Android");
        Log.e(TAG, url);
        Call call = okHttpClient.newCall(request.build());
        call.enqueue(callback);
        return call;

    }

    public static String getSync(String url) {
        OkHttpClient okHttpClient = OkHttpFactory.getInstance().singleClient;
        Request.Builder request = new Request.Builder()
                .url(url).get();
        request.addHeader("User-Agent", "Android");
        Log.e(TAG, url);
        try {
            Response response = okHttpClient.newCall(request.build()).execute();
            String res = response.body().string();
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "okhttp getSync error:" + e.getMessage());
        }
        return null;
    }

    /**
     * 上传图片
     *
     * @param url
     * @param requestMap
     * @param mediaType content type
     * @param fileName
     * @return
     */
    public static String uploadFile(String url,
                                    Map<String, Object> requestMap,
                                    MediaType mediaType,
                                    String fileName) {

//        Log.v(TAG, "requestMap="+ NetWorkUtils.getRequestData(requestMap));
        MultipartBody.Builder builder = new MultipartBody.Builder();
        //设置类型
        builder.setType(MultipartBody.FORM);

        //追加参数
        for (String key : requestMap.keySet()) {
            Object object = requestMap.get(key);
            if (!(object instanceof File)) {
                builder.addFormDataPart(key, object.toString());
            } else {
                File file = (File) object;
                builder.addFormDataPart(key, fileName, RequestBody.create(mediaType, file));
            }
        }
        //创建RequestBody
        RequestBody body = builder.build();


        OkHttpClient okHttpClient = OkHttpFactory.getInstance().singleClient;
        Request.Builder request = new Request.Builder()
                .url(url);
        Log.v(TAG, "url="+url);

        request.post(body);
        try {
            Response response = okHttpClient.newCall(request.build()).execute();
            String res = response.body().string();
            Log.v(TAG, "res="+res);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "okhttp postSync error:" + e.getMessage());
        }
        return null;
    }

    /**
     * 添加请求头部
     *
     * @param request
     * @param header
     */
    private static void addCommonHeaders(Request.Builder request, Map<String, String> header) {
        if (!TextUtils.isEmpty(cookie)) {
            request.addHeader("Cookie", cookie);
        }
        request.addHeader("User-Agent", "Android");
        if (header != null) {
            Iterator<String> iterator = header.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = header.get(key);
                if (value == null)
                    value = "";
                request.addHeader(key, value);
            }
        }
    }

    static String cookie;


    public void setCookie(String cookie) {
        this.cookie = cookie;
    }
}