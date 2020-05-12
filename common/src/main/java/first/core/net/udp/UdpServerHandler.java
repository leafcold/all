package first.core.net.udp;/*
 *创建者: zsqentitas框架
 *创建时间:2020/4/30 20:15
 */

import first.bean.Protocal;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

public class UdpServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
        ByteBuf buf = packet.copy().content();
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "UTF-8");
        System.out.println(body);
        //向客户端发送消息
        String json = "来自服务端: 南无阿弥陀佛";
        // 由于数据报的数据是以字符数组传的形式存储的，所以传转数据
        byte[] bytes = json.getBytes("UTF-8");
        DatagramPacket data = new DatagramPacket(Unpooled.copiedBuffer(bytes), packet.sender());
        ctx.channel().writeAndFlush(data);//向客户端发送消息
        String json1 = "第二个连接";
        // 由于数据报的数据是以字符数组传的形式存储的，所以传转数据
        byte[] bytes1 = json1.getBytes("UTF-8");
        DatagramPacket data1 = new DatagramPacket(Unpooled.copiedBuffer(bytes1), packet.sender());
        ctx.channel().writeAndFlush(data1);
    }
}
