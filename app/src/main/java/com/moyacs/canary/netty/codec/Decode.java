package com.moyacs.canary.netty.codec;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.apache.commons.lang3.StringUtils;


import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.List;


public class Decode  extends MessageToMessageDecoder<ByteBuf>
{
    private final Charset charset;
    
    /**
     * Creates a new instance with the current system character set.
     */
    public Decode() {
        this(Charset.defaultCharset());
    }
    
    /**
     * Creates a new instance with the specified character set.
     */
    public Decode(Charset charset) {
        if (charset == null) {
            throw new NullPointerException("charset");
        }
        this.charset = charset;
    }
    
    
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception
    {
        if (byteBuf.readableBytes() > 4) {
            //顺序分别为：品种、日期、时间、bid(卖家)、ask(买价)
            Quotation q = new Quotation();
			String message = byteBuf.toString(charset);
            String[] array = StringUtils.split(message,"|");
            q.setSymbol(array[0]);
            q.setBid(Double.parseDouble(array[3]));
            q.setAsk(Double.parseDouble(array[4]));
            String time = StringUtils.replace(array[1],"/","-")+" "+array[2];
            q.setTime(time);
            q.setDateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time));
            q.setUnixTime(q.getDateTime().getTime());
            list.add(q);
		}
    }

    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis()%1000);
    }
}
