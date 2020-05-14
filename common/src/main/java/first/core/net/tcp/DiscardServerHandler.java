package first.core.net.tcp;/*
 *创建者: zsq
 *创建时间:2020/3/17 10:14
 */

import co.paralleluniverse.common.util.Pair;
import first.bean.Protocal;
import first.com.protocol.move.PersonMove;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.lang.reflect.Method;

import static first.core.context.Context.ContextMap;
import static first.core.context.Context.protoMap;

public class DiscardServerHandler  extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(final ChannelHandlerContext ctx) { // (1)
        System.out.println("已经连接上");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            // Do something with msg
            Protocal in = (Protocal) msg;
            Pair<Object, Method> objectPair=ContextMap.get(in.getCode());
            Object invokeObject=protoMap.get(((Protocal) msg).getCode()).invoke(null,new Object[]{in.getProbuffer()});
            objectPair.getSecond().invoke(objectPair.getFirst(),invokeObject,ctx);
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("当前解析失败");
        }
        finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
