package first.core.net.udp;/*
 *创建者: zsq
 *创建时间:2020/3/16 20:19
 */

import first.core.net.tcp.DecoderHandler;
import first.core.net.tcp.EncoderHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class UdpKcp {

    public static  void  initUdpNetty() {
        EventLoopGroup bossGroup=new NioEventLoopGroup();
        try
        {
            //通过NioDatagramChannel创建Channel，并设置Socket参数支持广播
            //UDP相对于TCP不需要在客户端和服务端建立实际的连接，因此不需要为连接（ChannelPipeline）设置handler
            Bootstrap b=new Bootstrap();
            b.group(bossGroup)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new UdpServerHandler());
            b.bind(12310).sync().channel().closeFuture().await();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally{
            bossGroup.shutdownGracefully();
        }

    }

}
