package com.moyacs.canary.netty;


import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.moyacs.canary.netty.codec.Quotation;
import com.moyacs.canary.service.SocketService;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * netty 客户端 handler ，服务端返回的数据在此类中接收
 */
@ChannelHandler.Sharable
public class NettyClientHandler extends SimpleChannelInboundHandler<Quotation> {

    private NettyClientBootstrap createBootstrap;

    public NettyClientHandler(NettyClientBootstrap createBootstrap) {
        this.createBootstrap = createBootstrap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Quotation q) throws Exception {
        try {
            //在这里处理报价
            EventBus.getDefault().post(q);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("断开重连.");
        //super.exceptionCaught(ctx, cause);
    }

    /**
     * 连接成功
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LogUtils.d("channelActive");
        super.channelInactive(ctx);
    }

    /**
     * 断开连接函数，客户端主动close或者服务器断开连接的时候会回调此函数
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LogUtils.d("channelInactive");
        final EventLoop eventLoop = ctx.channel().eventLoop();
        eventLoop.schedule(new Runnable() {
            @Override
            public void run() {
                createBootstrap.connect();
            }
        }, 1L, TimeUnit.SECONDS);
        super.channelInactive(ctx);
    }
}
