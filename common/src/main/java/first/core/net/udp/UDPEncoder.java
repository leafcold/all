package first.core.net.udp;

import first.bean.Protocal;
import first.bean.UDPProtocal;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.ReferenceCountUtil;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * LogEvent 的编码器
 */
public class UDPEncoder extends MessageToMessageEncoder<UDPProtocal> {


    @Override
    protected void encode(ChannelHandlerContext ctx, UDPProtocal protocal, List<Object> out) throws Exception {
        ByteBuf byteBuf = protocal.toArray();
        out.add(new DatagramPacket(byteBuf, protocal.getTarget()));
    }
}