package com.moyacs.canary.util;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * @Author: Akame
 * @Date: 2019/1/21
 * @Description:
 */
public class WebSocketManger {
    private static final WebSocketManger ourInstance = new WebSocketManger();
    private OkHttpClient client;
    private final String WEB_SOCKET_URL = "ws://www.zhangstz.com/royal/websocket/";

    public static WebSocketManger getInstance() {
        return ourInstance;
    }

    private WebSocketManger() {
        initOkHttpClient();
    }

    private void initOkHttpClient() {
        client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true) //失败自动重连
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    /**
     * 开启Socket
     *
     * @param url
     */
    public void connectWebSocket(String url) {
        String webSocketUrL = WEB_SOCKET_URL + url;
        Log.d("===webSocket地址==", webSocketUrL);
        Request request = new Request.Builder()
                .url(webSocketUrL).build();
        client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                Log.e("WebSocket", "======webSocket 连接成功========");
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                Log.e("===WebSocket==", "===数据======" + text);
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                Log.e("===WebSocket==", "=====数据====" + bytes.hex());
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                super.onClosing(webSocket, code, reason);
                Log.e("===WebSocket===", "=====关闭中======");
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket,code,reason);
                Log.e("===WebSocket===", "=====关闭====="+reason);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                super.onFailure(webSocket, t, response);
                Log.e("==webSocketError==", t.getMessage());
            }
        });
    }


    /**
     * 关闭Socket连接
     */
    public void closeConnect() {
        client.dispatcher().executorService().shutdown();
    }
}
