package com.moyacs.canary.util;

import android.util.Log;

import com.google.gson.Gson;
import com.moyacs.canary.bean.event.EvenVo;
import com.moyacs.canary.service.SocketQuotation;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * 当前可交易Socket 管理
 * @Author: Akame
 * @Date: 2019/1/21
 * @Description:
 */
public class WebSocketManger {
    private static final WebSocketManger ourInstance = new WebSocketManger();
    private OkHttpClient client;
    private final String WEB_SOCKET_URL = "ws://www.zhangstz.com/royal/websocket/";
    private Gson mGson;
    private boolean isFirstLoad = true; //是不是第一条数据  第一条数据过滤

    public static WebSocketManger getInstance() {
        return ourInstance;
    }

    private WebSocketManger() {
    }

    private OkHttpClient getOkHttpClick() {
        client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true) //失败自动重连
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
        mGson = new Gson();
        return client;
    }

    /**
     * 开启Socket
     *
     * @param url
     */
    public void connectWebSocket(String url) {
        String webSocketUrL = WEB_SOCKET_URL + url;
        Request request = new Request.Builder()
                .url(webSocketUrL).build();
        getOkHttpClick().newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                Log.e("WebSocket", "======webSocket 连接成功========");
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                if (isFirstLoad) {
                    isFirstLoad = false;
                } else {
                    SocketQuotation socketQuotation = mGson.fromJson(text, SocketQuotation.class);
                    EventBus.getDefault().post(new EvenVo<SocketQuotation>(EvenVo.SOCKET_QUOTATION).setT(socketQuotation));
                }
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {

            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                super.onClosing(webSocket, code, reason);
                Log.e("===WebSocket===", "=====关闭中======");
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
                Log.e("===WebSocket===", "=====关闭=====" + reason);
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
