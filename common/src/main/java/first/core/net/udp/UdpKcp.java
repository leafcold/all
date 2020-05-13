package first.core.net.udp;/*
 *创建者: zsq
 *创建时间:2020/3/16 20:19
 */

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class UdpKcp {

    public static void initUdpNetty() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        try {
            //通过NioDatagramChannel创建Channel，并设置Socket参数支持广播
            Bootstrap b = new Bootstrap();
            b.group(bossGroup)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel)
                                throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            pipeline.addLast(new UDPDecoder());
                            pipeline.addLast(new UdpServerHandler());
                            pipeline.addLast(new UDPEncoder());
                        }
                    });
            b.bind(12310).sync().channel().closeFuture().await();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
        }

    }

}
