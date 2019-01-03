package com.moyacs.canary.main.market.mina;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

/**
 * 作者：luoshen on 2018/3/12 0012 16:03
 * 邮箱：rsf411613593@gmail.com
 * 说明：
 */

public class FrameDecoder2 extends CumulativeProtocolDecoder {
    @Override
    protected boolean doDecode(IoSession ioSession, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        int packHeadLenth = 8;  //包头长度(int 的长度) 根据自定义协议的包头的长度
        if(in.remaining() > packHeadLenth){  //说明缓冲区中有数据
            in.mark();//标记当前position，以便后继的reset操作能恢复position位置

            //获取数据包长度
            int len = in.getInt();

            //上面的get会改变remaining()的值

            if(in.remaining() <len - packHeadLenth) {
                //内容不够， 重置position到操作前，进行下一轮接受新数据
                in.reset();
                return false;
            }else{
                //内容足够
                in.reset(); //重置回复position位置到操作前
                byte[] packArray = new byte[len];
                in.get(packArray, 0, len); //获取整条报文

                //根据自己需要解析接收到的东西  我的例子 把收到的报文转成String
                String str = new String(packArray);
                out.write(str); //发送出去 就算完成了

                if(in.remaining() > 0){//如果读取一个完整包内容后还粘了包，就让父类再调用一次，进行下一次解析
                    return true;
                }
            }
        }
        return false;  //处理成功
    }
}
