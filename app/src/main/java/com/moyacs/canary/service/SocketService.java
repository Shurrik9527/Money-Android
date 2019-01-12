package com.moyacs.canary.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.moyacs.canary.netty.NettyClientBootstrap;
import com.moyacs.canary.util.LogUtils;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 作者：luoshen on 2018/3/8 0008 17:46
 * 邮箱：rsf411613593@gmail.com
 * 说明：Socket 后台服务
 */

public class SocketService extends Service {
    private IoSession session;
    private NioSocketConnector nioSocketConnector;
    private ConnectFuture future;
    private SocketThread socketThread;
    private NettyClientBootstrap nettyClientBootstrap;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.d("SocketService  :  onCreate");
        try {
            nettyClientBootstrap = new NettyClientBootstrap();
            nettyClientBootstrap.init();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.d("SocketService  :  onDestroy");
//        socketThread.interrupt();
//        nettyClientMT4.diconnect();
        nettyClientBootstrap.disConnect();
    }


    private class SocketThread extends Thread {
        private Socket socket;

        @Override
        public void run() {
            /**
             * mina
             */
//            if (nioSocketConnector == null) {
//
//                nioSocketConnector = new NioSocketConnector();
//                //设置 handler，处理消息收发
//                nioSocketConnector.setHandler(new MinaClientHandler());
//
//                nioSocketConnector.getFilterChain().addLast("codec",
//                        new ProtocolCodecFilter(new FrameCodecFactory()));
//
//
//                // 创建心跳工厂
//                HeartbeatMessageFactory heartBeatFactory = new HeartbeatMessageFactory();
//                // 当读操作空闲时发送心跳
//                KeepAliveFilter heartBeat = new KeepAliveFilter(heartBeatFactory, IdleStatus.READER_IDLE);
//                // 设置是否将事件继续往下传递
//                heartBeat.setForwardEvent(true);
//                // 设置心跳包请求后超时无反馈情况下的处理机制，默认为关闭连接,在此处设置为输出日志提醒
//                heartBeat.setRequestTimeoutHandler(KeepAliveRequestTimeoutHandler.LOG);
//                //设置心跳频率
//                heartBeat.setRequestInterval(1800);
//                //该过滤器加入到整个通信的过滤链中
//                nioSocketConnector.getFilterChain().addLast("keepAlive", heartBeat);
//            }
//            if (future == null) {
//                //连接服务器
//                future = nioSocketConnector.connect(new InetSocketAddress("quotation.moamarkets.com", 1234));
//                //一直阻塞，等待客户端连接成功
//                future.awaitUninterruptibly();
//            }
//            session = future.getSession();

            /**
             * 原生 socket
             */
            while (true) {
                try {
                    if (socket == null || socket.isClosed()) {
                        LogUtils.d("---------------------------------------------");

                        socket = new Socket("quotation.moamarkets.com", 1234);
                    }
                    Thread.sleep(200);
                    //1.创建监听指定服务器地址以及指定服务器监听的端口号

                    boolean connected = socket.isConnected();

                    //拿到socket的输入流，这里存储的是服务器返回的数据
                    InputStream is = socket.getInputStream();
                    //解析服务器返回的数据

                    byte[] bytes;
                    bytes = new byte[is.available()];
                    is.read(bytes);
                    String str = new String(bytes, "utf-8");
                    LogUtils.d("socket 从服务器获取的数据：" + str);

                    EventBus.getDefault().post(str);

                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
