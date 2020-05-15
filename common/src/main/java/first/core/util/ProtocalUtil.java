package first.core.util;

import first.bean.Protocal;
import io.netty.buffer.ByteBuf;

import static first.core.log.Logger.MLOG;

public class ProtocalUtil {

    public static Protocal byte2Protocal(ByteBuf byteBuf) throws Exception{
        // 直接copy的代码
        if (byteBuf.readableBytes() < 4) {
            MLOG.info("当前消息有问题直接丢弃");
            return null;
        }
        //获取code值 在缓冲区的前2个字节
        byteBuf.markReaderIndex();
        short codeLength = byteBuf.readShort();
        //获取长度值 在缓冲区的第2到第4个字节
        int dataLength = byteBuf.readInt();

        long pid =byteBuf.readLong();

        if (codeLength <= 0 || dataLength <= 0) {
            MLOG.info("当前消息的格式不对丢弃");
            return null;
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
                return null;
            }
        }
        //将数据存储到body内
        byte[] body = new byte[dataLength];
        byteBuf.readBytes(body);
        return new Protocal(codeLength, dataLength,pid, body);
    }


}
