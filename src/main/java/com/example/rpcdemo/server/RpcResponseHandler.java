package com.example.rpcdemo.server;

import com.example.rpcdemo.client.RpcRequest;
import com.example.rpcdemo.core.RPC;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * netty回调处理
 */
@Slf4j
public class RpcResponseHandler extends ChannelHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String requestJson = (String)msg;
        log.info("收到rpc请求: {}", requestJson);

        // 把请求json解析为请求实体
        RpcRequest request = RPC.requestDecode(requestJson);
        // 反射调用请求执行的方法
        Object result = InvokeUtil.invoke(request);

        RpcResponse response = new RpcResponse();
        response.setRequestId(request.getRequestId());
        response.setResult(result);

        // 编码为json字符串，包括分隔符
        String responseJson = RPC.responseEncode(response);
        ByteBuf responseBuf = Unpooled.copiedBuffer(responseJson.getBytes());
        ctx.writeAndFlush(responseBuf);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 将消息队列中的数据写入到socketChannel发给客户端
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
