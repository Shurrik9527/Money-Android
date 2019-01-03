package com.moyacs.canary.main.market.mina;


import com.blankj.utilcode.util.LogUtils;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;


/**
 * 作者：luoshen on 2018/3/8 0008 14:18
 * 邮箱：rsf411613593@gmail.com
 * 说明：对服务器传递过来的数据进行解码
 */

public class FrameDecoder implements ProtocolDecoder {

    private String message;


    @Override
    public void decode(IoSession ioSession, IoBuffer in, ProtocolDecoderOutput out) throws Exception {

////        将 iobuffer 转化为 String
//        while (in.hasRemaining()) {
//            byte[] bytes = new byte[in.limit()];
//            in.get(bytes);
//            message = new String(bytes);
//            //将处理好的数据传递出去
//            out.write(message);
//        }

        }

    @Override
    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

    }

    @Override
    public void dispose(IoSession ioSession) throws Exception {

    }
}
