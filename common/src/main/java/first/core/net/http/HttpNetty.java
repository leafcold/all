package first.core.net.http;/*
 *创建者: zsq
 *创建时间:2020/4/16 17:21
 */

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;


public class HttpNetty {
    public static void initHttpNetty(int httpPort) {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup work = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap(); // (2)
            b.group(boss, work)
                    .channel(NioServerSocketChannel.class) // (3)
                    .childHandler(new HttpServerInitializer());
            ChannelFuture f = b.bind(new InetSocketAddress(httpPort)).sync(); // (7)
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }
    }

}
