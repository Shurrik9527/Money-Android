package com.moyacs.canary.main.market.mina;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * 作者：luoshen on 2018/3/8 0008 14:17
 * 邮箱：rsf411613593@gmail.com
 * 说明：mina 自定义 编解码
 */

public class FrameCodecFactory implements ProtocolCodecFactory {

    @Override
    public ProtocolEncoder getEncoder(IoSession ioSession) throws Exception {
        return new FrameEncoder();
    }

    @Override
    public ProtocolDecoder getDecoder(IoSession ioSession) throws Exception {
        return new FrameDecoder();
    }
}
