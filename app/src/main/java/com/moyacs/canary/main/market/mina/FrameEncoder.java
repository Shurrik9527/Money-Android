package com.moyacs.canary.main.market.mina;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import java.nio.charset.Charset;

/**
 * 作者：luoshen on 2018/3/8 0008 14:17
 * 邮箱：rsf411613593@gmail.com
 * 说明：将 客户端 向 服务器 传输的数据处理
 */

public class FrameEncoder extends ProtocolEncoderAdapter {
    /**
     * 对传输的数据进行编码(客户端往服务器发送的数据)
     * @param ioSession
     * @param o
     * @param protocolEncoderOutput
     * @throws Exception
     */
    @Override
    public void encode(IoSession ioSession, Object o, ProtocolEncoderOutput protocolEncoderOutput) throws Exception {
//
//        String message = null;
//        if (o instanceof String) {
//            message = (String) o;
//        }
//        if (message!= null) {
//            //给 String 分配内存
//            IoBuffer ioBuffer = IoBuffer.allocate(message.length());
//            //内存自动扩展
//            ioBuffer.setAutoExpand(true);
//            //设置传输的数据 和 编码格式
//            ioBuffer.putString(message, Charset.forName("UTF-8").newEncoder());
//            //完成
//            ioBuffer.flip();
//        }

    }

    @Override
    public void dispose(IoSession ioSession) throws Exception {

    }
}
