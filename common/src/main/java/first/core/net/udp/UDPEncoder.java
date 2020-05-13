package first.core.net.udp;

import first.bean.Protocal;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * LogEvent 的编码器
 */
public class UDPEncoder extends MessageToMessageEncoder<Protocal> {


    @Override
    protected void encode(ChannelHandlerContext ctx, Protocal protocal, List<Object> out) throws Exception {
        ByteBuf byteBuf = protocal.toArray();
        out.add(new DatagramPacket(byteBuf,UDPSenderCache.get(protocal.getPid())));
    }
}