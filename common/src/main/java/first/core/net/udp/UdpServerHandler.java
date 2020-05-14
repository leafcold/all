package first.core.net.udp;/*
 *创建者: zsqentitas框架
 *创建时间:2020/4/30 20:15
 */

import co.paralleluniverse.common.util.Pair;
import first.bean.Protocal;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.ReferenceCountUtil;

import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static first.core.context.Context.ContextMap;
import static first.core.context.Context.protoMap;

public class UdpServerHandler extends SimpleChannelInboundHandler<Protocal> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Protocal msg) throws Exception {
        //copy的zsq的代码，有问题找他
        try {
            // Do something with msg
            Protocal in = (Protocal) msg;
            Pair<Object, Method> objectPair = ContextMap.get(in.getCode());
            Object invokeObject = protoMap.get(((Protocal) msg).getCode()).invoke(null, new Object[]{in.getProbuffer()});
            objectPair.getSecond().invoke(objectPair.getFirst(), invokeObject, ctx);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("当前解析失败");
        } finally {
            ReferenceCountUtil.release(msg);
        }

    }
}
