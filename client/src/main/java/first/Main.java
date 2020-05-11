package first;/*
 *创建者: zsq
 *创建时间:2020/3/18 14:38
 */

import first.bean.Protocal;
import first.com.protocol.move.PersonMove;
import first.core.CLientHandler;
import first.core.TimeClientHandler;
import first.core.net.tcp.DecoderHandler;
import first.core.net.tcp.EncoderHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Main {
    /// tcp
//    public static void main(String[] args) throws Exception {
//        String host = "172.16.2.24";
//        int port = 12306;
//        EventLoopGroup workerGroup = new NioEventLoopGroup();
//
//        try {
//            Bootstrap b = new Bootstrap(); // (1)
//            b.group(workerGroup); // (2)
//            b.channel(NioSocketChannel.class); // (3)
//            b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
//            b.handler(new ChannelInitializer<SocketChannel>() {
//                @Override
//                public void initChannel(SocketChannel ch) throws Exception {
//                    ch.pipeline().addLast(new DecoderHandler());
//                    ch.pipeline().addLast(new TimeClientHandler());
//                    ch.pipeline().addLast(new EncoderHandler());
//                }
//            });
//
//            // Start the client.
//            ChannelFuture f = b.connect(host, port).sync(); // (5)
//            PersonMove.Person.Builder person =PersonMove.Person.newBuilder();
//            person.setId(100);
//            person.setEmail(String.valueOf(System.currentTimeMillis()));
//            person.setName("my");
//            PersonMove.Person nowPerSon=person.build();
//            Protocal protocal=new Protocal((short) 1,nowPerSon.toByteArray().length,nowPerSon.toByteArray());
//            f.channel().writeAndFlush(protocal.toArray());
//
//            // Wait until the connection is closed.
//            f.channel().closeFuture().sync();
//        } finally {
//            workerGroup.shutdownGracefully();
//        }
//    }


   // udp
    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioDatagramChannel.class)
                    .handler(new CLientHandler());

            Channel ch = b.bind(0).sync().channel();

            ch.writeAndFlush(new DatagramPacket(
                    Unpooled.copiedBuffer("来自客户端:南无本师释迦牟尼佛", CharsetUtil.UTF_8),
                    new InetSocketAddress("127.0.0.1", 12310))).sync();

            ch.closeFuture().await();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }

//
//
//
//
//
//
//
    }
}
