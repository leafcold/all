package first.bean;/*

/**
 * 前面的2个字节是用来获取code 内容的
 * 后面的是字节长度用来进行转化的netty bytebuf 转化为对象
 */


import io.netty.buffer.ByteBuf;
import lombok.Data;

import java.net.InetSocketAddress;

import static io.netty.buffer.Unpooled.wrappedBuffer;

@Data
public class Protocal {

    public static int toArry(){
        return 1;
    }

    private short code;

    private int length;

    private long pid;

    private byte[] probuffer;


    public Protocal(short code,int length,long pid, byte[] probuffer){
        this.code=code;
        this.length=length;
        this.pid =pid;
        this.probuffer=probuffer;
    }

    /**
     * 基础类型转换
     * @return
     */
    public ByteBuf toArray(){
        byte[] bytes = new byte[2+4+8+length];
        bytes[0] = (byte)(code>>8&0xff);
        bytes[1] = (byte)(code&0xff);
        for(int i=0;i<4;i++) {
            bytes[2+i] = (byte)(length >> 8*(3-i));
        }
        for(int i=0;i<8;i++) {
            bytes[6+i] = (byte)(pid >> 8*(7-i));
        }
        if (length >= 0) System.arraycopy(probuffer, 0, bytes, 14, length);

        return wrappedBuffer(bytes);
    }

}
