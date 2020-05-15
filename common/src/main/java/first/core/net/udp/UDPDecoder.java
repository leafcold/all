package first.core.net.udp;

import first.bean.Protocal;
import first.com.model.Player;
import first.core.invoke.Code;
import first.core.util.ProtocalUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

public class UDPDecoder extends MessageToMessageDecoder<DatagramPacket> {

    @Override
    protected void decode(ChannelHandlerContext ctx,
                          DatagramPacket datagramPacket, List<Object> out) throws Exception {
        Protocal protocal = ProtocalUtil.byte2Protocal(datagramPacket.content());
        if(protocal == null){
            return;
        }
        long pid = protocal.getPid();
        if(protocal.getCode() == Code.CSUDP){
            UDPSenderCache.put(pid,new Player(pid,datagramPacket.sender()));
        }
        out.add(protocal);
    }
}