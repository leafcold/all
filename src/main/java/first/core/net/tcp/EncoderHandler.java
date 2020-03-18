package first.core.net.tcp;/*
 *创建者: zsq
 *创建时间:2020/3/18 16:17
 */

import first.bean.Protocal;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class EncoderHandler extends MessageToByteEncoder<Protocal> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Protocal msg, ByteBuf out) {
        out.writeBytes(msg.toString().getBytes());
    }
}
