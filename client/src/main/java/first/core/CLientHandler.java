package first.core;/*
 *创建者: zsq
 *创建时间:2020/4/30 21:07
 */

import first.ClientMain;
import first.bean.Protocal;
import first.com.protocol.Login.PersonLogin;
import first.com.protocol.move.PersonMove;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

import java.net.InetSocketAddress;

import static first.core.invoke.Code.CSPlayerMove;
import static first.core.invoke.Code.CSUDP;

public  class CLientHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    public static int count = 0;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) throws Exception {
        System.out.println(datagramPacket); //收到消息
        count++;
        if(count == 1){
            Channel channel = channelHandlerContext.channel();
            new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
                            while(true){
                                try {
                                    Thread.sleep(10000L);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                PersonMove.CSPlayerMove.Builder sc = PersonMove.CSPlayerMove.newBuilder();
                                sc.setPlayerId(ClientMain.playerId);
                                sc.setMove(PersonMove.MoveInfo.newBuilder());
                                sc.getMove().toBuilder().setCtime(System.currentTimeMillis());
                                byte[] bytes = sc.build().toByteArray();
                                Protocal protocal = new Protocal(CSPlayerMove, bytes.length,sc.getPlayerId(),bytes);
                                ByteBuf msg = protocal.toArray();
                                channel.writeAndFlush(new DatagramPacket(msg, new InetSocketAddress("127.0.0.1", 12310)));
                                if(count ==1 ){
                                    break;
                                }
                            }

                        }
                    }
            ).start();
        }


    }
}