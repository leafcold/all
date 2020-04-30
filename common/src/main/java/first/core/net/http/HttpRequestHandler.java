package first.core.net.http;/*
 *创建者: zsq
 *创建时间:2020/4/16 17:13
 */

import first.bean.Protocal;
import first.com.protocol.move.PersonMove;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import javafx.util.Pair;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static first.core.context.Context.*;
import static io.netty.handler.codec.http.HttpUtil.is100ContinueExpected;

public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
        //100 Continue
        if (is100ContinueExpected(req)) {
            ctx.write(new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.CONTINUE));
        }
        // 获取请求的uri
        String url = req.uri();
        if("/favicon.ico".equals(url)){
            //System.out.println("请求favicon.ico");
            return;
        }
        Map<String,String> resMap = new HashMap<String,String>();
        resMap.put("method",req.method().name());
        resMap.put("url",url);


        //这可能需要再处理一下uri
        ParseUrlUtil parseUrlUtil=new ParseUrlUtil();
        parseUrlUtil.parser(url);
        Pair<Object, Method> objectPair=HttpContextMap.get(parseUrlUtil.getStrUrl());
        Object invokeObject=HttpMap.get(parseUrlUtil.getStrUrl()).invoke(null,parseUrlUtil);
        String msg=(String) objectPair.getValue().invoke(objectPair.getKey(),invokeObject);



        //String msg = "<html><head><title>test</title></head><body>你请求uri为：" + url+"</body></html>";
        // 创建http响应
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8));
        // 设置头信息
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
        //response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
        // 将html write到客户端
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }


}