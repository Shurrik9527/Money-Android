package com.moyacs.canary.netty;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;


@ChannelHandler.Sharable
public class TimeOutHandler extends ChannelDuplexHandler {

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent e = (IdleStateEvent) evt;
			if (e.state() == IdleState.READER_IDLE) {
				System.out.println("userEventTriggered reader_idle event");
				ctx.channel().writeAndFlush("-1");
				//checkQuotationTime();
			} else if (e.state() == IdleState.WRITER_IDLE) {
				System.out.println("userEventTriggered writer_idle event");
				ctx.channel().writeAndFlush("-1");
			} else if (e.state() == IdleState.ALL_IDLE) {
				System.out.println("userEventTriggered all_idle event");
				ctx.channel().writeAndFlush("-1");
			}
		}
		super.userEventTriggered(ctx, evt);
	}
	
	
	
}
