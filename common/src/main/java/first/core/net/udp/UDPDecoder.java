package first.core.net.udp;

import first.bean.Protocal;
import first.com.model.Player;
import first.com.protocol.move.PersonMove;
import first.core.invoke.Code;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import javafx.util.Pair;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.List;

import static first.core.context.Context.ContextMap;
import static first.core.context.Context.protoMap;
import static first.core.log.Logger.MLOG;

public class UDPDecoder extends MessageToMessageDecoder<DatagramPacket> {

    @Override
    protected void decode(ChannelHandlerContext ctx,
                          DatagramPacket datagramPacket, List<Object> out) throws Exception {
        ByteBuf byteBuf = datagramPacket.content();
        // 直接copy的代码
        if (byteBuf.readableBytes() < 4) {
            MLOG.info("当前消息有问题直接丢弃");
            return;
        }
        //获取code值 在缓冲区的前2个字节
        byteBuf.markReaderIndex();
        short codeLength = byteBuf.readShort();
        //获取长度值 在缓冲区的第2到第4个字节
        int dataLength = byteBuf.readInt();

        long pid =byteBuf.readLong();

        if (codeLength <= 0 || dataLength <= 0) {
            MLOG.info("当前消息的格式不对丢弃");
            return;
        }
        //获取当前字节在缓存位置 进行标记
        int first = byteBuf.readerIndex();
        while (true) {
            int second = byteBuf.writerIndex();
            if (second - first == dataLength) {
                break;
            }
            if (second % 1024 == 0) {
                byteBuf.resetReaderIndex();
                return;
            }
        }
        //将数据存储到body内
        byte[] body = new byte[dataLength];
        byteBuf.readBytes(body);
        Protocal protocal = new Protocal(codeLength, dataLength,pid, body);
        if(protocal.getCode() == Code.CSUDP){
            UDPSenderCache.put(pid,new Player(pid,datagramPacket.sender()));
        }
        out.add(protocal);
    }
}