package com.example.rpcdemo.client;

import com.example.rpcdemo.core.RPC;
import com.example.rpcdemo.server.RpcResponse;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * netty回调处理
 */
public class RpcRequestHandler extends ChannelHandlerAdapter {

    public static ChannelHandlerContext channelCtx;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        channelCtx = ctx;
        RpcRequestNet.connectLock.lock();
        RpcRequestNet.connectCondition.signalAll();
        RpcRequestNet.connectLock.unlock();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String responseJson = (String)msg;
        RpcResponse response = RPC.responseDecode(responseJson);
        synchronized (RpcRequestNet.requestLockMap.get(response.getRequestId())) {
            // 唤醒在该对象上wait的线程
            RpcRequest request = RpcRequestNet.requestLockMap.get(response.getRequestId());
            request.setResult(response.getResult());
            request.notifyAll();
        }
    }
}
