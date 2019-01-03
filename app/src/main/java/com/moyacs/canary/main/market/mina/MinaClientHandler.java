package com.moyacs.canary.main.market.mina;

import com.blankj.utilcode.util.LogUtils;

import org.apache.mina.core.buffer.SimpleBufferAllocator;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.greenrobot.eventbus.EventBus;

/**
 * 作者：luoshen on 2018/3/8 0008 10:52
 * 邮箱：rsf411613593@gmail.com
 * 说明：Mina 框架 消息处理 handler
 */

public class MinaClientHandler extends IoHandlerAdapter {
    /**
     * 连接出现异常的时候调用
     * @param session
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        super.exceptionCaught(session, cause);
        LogUtils.d(" Mina ---   exceptionCaught:::" + cause.getMessage());
    }

    /**
     * 收到消息的时候调用
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        super.messageReceived(session, message);

        //将获取到的数据 回调出去
        EventBus.getDefault().post(message.toString());

    }

    /**
     * 发出消息的时候调用
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        super.messageSent(session, message);

        LogUtils.d(" Mina ---   messageSent:  " + message.toString());
    }

    /**
     * 客户端与服务器的会话关闭的时候调用
     * @param session
     * @throws Exception
     */
    @Override
    public void sessionClosed(IoSession session) throws Exception {
        super.sessionClosed(session);
        LogUtils.d(" Mina ---   sessionClosed");
    }

    /**
     * 会话打开的时候调用
     * @param session
     * @throws Exception
     */
    @Override
    public void sessionOpened(IoSession session) throws Exception {
        super.sessionOpened(session);
        LogUtils.d(" Mina ---   sessionOpened");
    }

    /**
     * 会话进入空闲状态的时候调用
     * @param session
     * @param status
     * @throws Exception
     */
    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        super.sessionIdle(session, status);
        LogUtils.d(" Mina ---   sessionIdle");
    }

    /**
     * 会话创建的时候调用
     * @param session
     * @throws Exception
     */
    @Override
    public void sessionCreated(IoSession session) throws Exception {
        super.sessionCreated(session);
        LogUtils.d(" Mina ---   sessionCreated");
    }
}
