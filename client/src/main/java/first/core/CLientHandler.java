package first.core;/*
 *创建者: zsq
 *创建时间:2020/4/30 21:07
 */

import co.paralleluniverse.common.util.Pair;
import first.ClientMain;
import first.bean.Protocal;
import first.com.protocol.Login.PersonLogin;
import first.com.protocol.move.PersonMove;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.ReferenceCountUtil;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;

import static first.core.context.Context.ContextMap;
import static first.core.context.Context.protoMap;
import static first.core.invoke.Code.CSPlayerMove;
import static first.core.invoke.Code.CSUDP;

public  class CLientHandler extends SimpleChannelInboundHandler<Protocal> {

    public static int count = 100;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Protocal msg) throws Exception {
        System.out.println(msg); //收到消息

        //copy的zsq的代码，有问题找他
        try {
            // Do something with msg

            Pair<Object, Method> objectPair = ContextMap.get(msg.getCode());
            Object invokeObject = protoMap.get(((Protocal) msg).getCode()).invoke(null, new Object[]{msg.getProbuffer()});
            objectPair.getSecond().invoke(objectPair.getFirst(), invokeObject, ctx);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("当前解析失败");
        } finally {
            ReferenceCountUtil.release(msg);
        }
//        count++;
//        if(count == 1){
//            Channel channel = channelHandlerContext.channel();
//            new Thread(
//                    new Runnable() {
//                        @Override
//                        public void run() {
//                            while(true){
//                                try {
//                                    Thread.sleep(10000L);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                                PersonMove.CSPlayerMove.Builder sc = PersonMove.CSPlayerMove.newBuilder();
//                                sc.setPlayerId(ClientMain.playerId);
//                                sc.setMove(PersonMove.MoveInfo.newBuilder());
//                                sc.getMove().toBuilder().setCtime(System.currentTimeMillis());
//                                byte[] bytes = sc.build().toByteArray();
//                                Protocal protocal = new Protocal(CSPlayerMove, bytes.length,sc.getPlayerId(),bytes);
//                                ByteBuf msg = protocal.toArray();
//                                channel.writeAndFlush(new DatagramPacket(msg, new InetSocketAddress("127.0.0.1", 12310)));
//                                if(count ==1 ){
//                                    break;
//                                }
//                            }
//
//                        }
//                    }
//            ).start();
//        }


    }
}