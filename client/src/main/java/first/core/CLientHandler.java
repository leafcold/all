package first.core;/*
 *创建者: zsq
 *创建时间:2020/4/30 21:07
 */

import first.bean.Protocal;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public  class CLientHandler extends SimpleChannelInboundHandler<Protocal> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Protocal protocal) throws Exception {
        System.out.println(protocal);
    }
}