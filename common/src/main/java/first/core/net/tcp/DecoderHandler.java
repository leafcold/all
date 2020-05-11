package first.core.net.tcp;/*
 *创建者: zsq
 *创建时间:2020/3/18 16:14
 */

import first.bean.Protocal;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import static first.core.log.Logger.MLOG;

public class  DecoderHandler extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        MLOG.info("当前消息时间{}",System.currentTimeMillis());
        if (byteBuf.readableBytes() < 4) {
            MLOG.info("当前消息有问题直接丢弃");
            return;
        }
        //获取code值 在缓冲区的前2个字节
        byteBuf.markReaderIndex();
        short codeLength = byteBuf.readShort();
        //获取长度值 在缓冲区的第2到第4个字节
        int dataLength = byteBuf.readInt();
        if (codeLength <= 0 || dataLength <= 0) {
            MLOG.info("当前消息的格式不对丢弃");
            return;
        }
        //获取当前字节在缓存位置 进行标记
        int first=byteBuf.readerIndex();
        while(true){
            int second=byteBuf.writerIndex();
            if (second-first==dataLength) {
                break;
            }
            if(second%1024==0) {
                byteBuf.resetReaderIndex();
                return;
            }
        }
        //将数据存储到body内
        byte[] body=new byte[dataLength];
        byteBuf.readBytes(body);
        Protocal protocal = new Protocal(codeLength,dataLength,body);
        list.add(protocal);
    }
}
