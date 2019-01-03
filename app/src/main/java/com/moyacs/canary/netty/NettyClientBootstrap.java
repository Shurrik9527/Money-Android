package com.moyacs.canary.netty;

import android.util.Log;

import com.moyacs.canary.netty.codec.Decode;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;


import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;


/**
 * netty 客户端配置
 */
public class NettyClientBootstrap {

	private NettyClientHandler clientHandler;
	private Bootstrap bootstrap = new Bootstrap();

	private SocketChannel socketChannel;
	private ChannelFuture channelFuture;
	private EventLoopGroup eventLoopGroup;

	public void init() throws InterruptedException {
		clientHandler = new NettyClientHandler(this);
		eventLoopGroup = new NioEventLoopGroup();
		bootstrap.channel(NioSocketChannel.class);
		bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
		bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000);
		bootstrap.group(eventLoopGroup);
		bootstrap.remoteAddress("quotation.moamarkets.com", 1234);
		bootstrap.handler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel socketChannel) throws Exception {

				socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, Unpooled.copiedBuffer(">".getBytes())));
				socketChannel.pipeline().addLast(new StringEncoder(Charset.forName("utf-8")));
				socketChannel.pipeline().addLast(new Decode(Charset.forName("UTF-8")));
				socketChannel.pipeline().addLast("idleStateHandler", new IdleStateHandler(60, 0, 0));
				socketChannel.pipeline().addLast("timeOutHandler", new TimeOutHandler());
				socketChannel.pipeline().addLast(clientHandler);

			}
		});

		connect();

	}


	public void connect() {
		//防止客户端创建 俩个链接以上
		if (socketChannel != null && socketChannel.isActive()) {
			return;
		}
		channelFuture = bootstrap.connect();
		//添加自动重连的 任务
		channelFuture.addListener(new ChannelFutureListener() {
			public void operationComplete(ChannelFuture f) throws Exception {
				if (f.isSuccess()) {
					System.out.println("Started Tcp Client:{}" + getServerInfo());
				} else {
					f.channel().eventLoop().schedule(new Runnable() {
						@Override
						public void run() {
							connect();
						}
					}, 1, TimeUnit.SECONDS);
				}
			}
		});

	}


	private String getServerInfo() {
		return String.format("RemoteHost=%s RemotePort=%d","quotation.moamarkets.com", 1234);
	}

	public SocketChannel getSocketChannel() {
		return socketChannel;
	}

	/**
	 * 断开连接
	 */
	public void disConnect(){
		try {
			if (socketChannel != null
					&& socketChannel.isActive()) {
				socketChannel.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
