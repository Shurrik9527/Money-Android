package com.moyacs.canary.network;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.JsonObject;
import com.moyacs.canary.util.SharePreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
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
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient().newBuilder()
                .retryOnConnectionFailure(true)//失败重试
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                //添加请求头
                .addInterceptor(new HeadersInterceptor());
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
           /* Response response = chain.proceed(chain.request());
            String header = response.header(HttpConstants.header_key);
            if (!TextUtils.isEmpty(header)) {
                SharePreferencesUtil.getInstance().setServerAuthor(header);
            }
            if(isAuthExpired(response)){
                getNewAuth();
            }
            Request request = chain.request().newBuilder()
                    .header(HttpConstants.header_key, SharePreferencesUtil.getInstance().getServerAuthor())
                    .build();
            Log.d("TAG",request.url()+"======请求头===" + SharePreferencesUtil.getInstance().getServerAuthor());
            return chain.proceed(request);*/


            Request request = chain.request().newBuilder()
                    .addHeader(HttpConstants.header_key, SharePreferencesUtil.getInstance().getServerAuthor()).build();
            Response response = chain.proceed(request); //前往网络请求
            String auth = response.header(HttpConstants.header_key); // 获取请求头的auth
            Log.d("TAG", request.url() + "======请求头===" + SharePreferencesUtil.getInstance().getServerAuthor());
            if (auth != null && !TextUtils.isEmpty(auth) && auth.contains("Bearer")) {
                //当前服务器返回auth不为空的话 更新auth
                SharePreferencesUtil.getInstance().setServerAuthor(auth);
            }

            String requestBody = response.body().string();
            String msgCode = "";
            try {
                JSONObject jsonObject = new JSONObject(requestBody);
                msgCode = jsonObject.getString("msgCode");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //判断auth是否过期
            if (TextUtils.equals("-111", msgCode) || TextUtils.equals("-999", msgCode)) {
                //更新auth
                getNewAuth();
                //重新配置
                Request newRequest = chain.request().newBuilder()
                        .addHeader(HttpConstants.header_key, SharePreferencesUtil.getInstance().getServerAuthor())
                        .build();
                Log.d("TAG", newRequest.url() + "======请求头===" + SharePreferencesUtil.getInstance().getServerAuthor());
                return chain.proceed(newRequest); //再次发起请求
            } else {
                return createNewResponse(response, requestBody, response.code());
            }
        }
    }


   /* private boolean isAuthExpired(Response response) {
        try {

            if (TextUtils.equals("-111", msgCode)) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }*/

    private void getNewAuth() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HttpConstants.SERVER_HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofit2.Response<JsonObject> tokenJson = null;
        try {
            tokenJson = retrofit.create(ServerApi.class).getAuth().execute();
            assert tokenJson.body() != null;
            String auth = tokenJson.body().get("data").toString();
            if (TextUtils.isEmpty(auth)) {
                Log.e("TAG", "========获取的auth为空======");
                return;
            }
            SharePreferencesUtil.getInstance().setServerAuthor(auth);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private Response createNewResponse(Response response, String content, int code) {
        return response.newBuilder().body(ResponseBody.create(response.body().contentType(), content)).code(code).build();
    }
}
