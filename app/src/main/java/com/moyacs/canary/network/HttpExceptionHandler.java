package com.moyacs.canary.network;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.google.gson.JsonSyntaxException;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;

/**
 * 作者：luoshen on 2017/12/18 0018 14:20
 * 邮箱：rsf411613593@gmail.com
 * <p>
 * 网络请求异常处理类
 */

public class HttpExceptionHandler {
    /**
     * 根据 Throwable 判断具体的异常
     *
     * @param e Throwable
     * @return 具体异常
     */
    public static String getThrowable(Throwable e) {
        String error = e.toString();
        LogUtils.d("网络请求异常信息：：" + error);
        if (e instanceof UnknownHostException || e instanceof HttpException) {
            if (NetworkUtils.isConnected()) {//判断网络是否可用
                //服务器挂了
                return HttpConstants.Exception_server;
            } else {
                //网络错误
                return HttpConstants.Exception_http;

            }
        } else if (e instanceof ConnectException || e instanceof SocketTimeoutException || e instanceof SocketException) {
            //网络连接超时
            return HttpConstants.Exception_http_connect;

        } else if (e instanceof NumberFormatException || e instanceof  IllegalArgumentException || e instanceof JsonSyntaxException) {
            //json 解析异常
            return HttpConstants.Exception_json;

        } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
            //证书错误
            return HttpConstants.Exception_SSL;
        } else {
            //未知错误
            return HttpConstants.Exception_other;
        }

    }
}
