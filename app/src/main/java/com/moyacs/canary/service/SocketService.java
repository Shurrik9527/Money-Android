package com.moyacs.canary.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import com.moyacs.canary.network.BaseObservable;
import com.moyacs.canary.network.RxUtils;
import com.moyacs.canary.network.ServerManger;
import com.moyacs.canary.network.ServerResult;
import com.moyacs.canary.util.WebSocketManger;
import io.reactivex.disposables.CompositeDisposable;

/**
 * 作者：luoshen on 2018/3/8 0008 17:46
 * 邮箱：rsf411613593@gmail.com
 * 说明：Socket 获取当前可交易信息后台服务
 */

public class SocketService extends Service {

    private CompositeDisposable disposables;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        disposables = new CompositeDisposable();
        getWebSocketAddress();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposables.clear();
        WebSocketManger.getInstance().closeConnect();
    }

    /**
     * 获取WebSocket 服务地址
     */
    private void getWebSocketAddress() {
        disposables.add(ServerManger.getInstance().getServer().getWebSocketAddress()
                .compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(new BaseObservable<ServerResult<String>>() {
                    @Override
                    protected void requestSuccess(ServerResult<String> data) {
                        WebSocketManger.getInstance().connectWebSocket(data.getData());
                    }
                }));
    }
}
