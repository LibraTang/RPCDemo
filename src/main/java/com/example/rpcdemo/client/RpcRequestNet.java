package com.example.rpcdemo.client;

import com.example.rpcdemo.core.RPC;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * netty客户端连接配置类
 */
@Slf4j
public class RpcRequestNet {
    /**
     * 全局map，每个请求对应一个锁，用于同步等待
     */
    public static Map<String, RpcRequest> requestLockMap = new ConcurrentHashMap<>();
    /**
     * 阻塞等待连接成功的锁
     */
    public static Lock connectLock = new ReentrantLock();
    /**
     * 用于线程通信
     */
    public static Condition connectCondition = connectLock.newCondition();

    /**
     * 单例
     */
    private static RpcRequestNet instance;

    /**
     * 私有构造方法，单例模式
     */
    private RpcRequestNet() throws InterruptedException {
        // netty线程组
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        // netty启动辅助类
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        // 以换行符分包，防止粘包/拆包问题，2048是最大长度，到达最大长度未出现换行符则抛出异常
                        socketChannel.pipeline().addLast(new LineBasedFrameDecoder(2048));
                        // 将收到的对象转换为字符串
                        socketChannel.pipeline().addLast(new StringDecoder());
                        // 回调处理
                        socketChannel.pipeline().addLast(new RpcRequestHandler());
                    }
                });
        // 启动
        ChannelFuture future = bootstrap.connect(RPC.getClientConfig().getHost(), RPC.getClientConfig().getPort()).sync();
    }

    /**
     * 单例模式避免重复连接
     * @return
     * @throws InterruptedException
     */
    public static synchronized RpcRequestNet connect() throws InterruptedException {
        if (instance == null) {
            instance = new RpcRequestNet();
        }
        return instance;
    }

    /**
     * 向服务端发送请求
     * @param request
     */
    public void send(RpcRequest request) throws InterruptedException {
        if (RpcRequestHandler.channelCtx == null) {
            // 阻塞等待连接成功
            connectLock.lock();
            try {
                log.info("正在等待连接服务端");
                connectCondition.await();
            } finally {
                connectLock.unlock();
            }
        }

        String requestJson = null;
        try {
            requestJson = RPC.requestEncode(request);
        } catch (JsonProcessingException e) {
            log.error("请求实体转化为json出错");
        }

        assert requestJson != null : "json对象为空";
        ByteBuf requestBuf = Unpooled.copiedBuffer(requestJson.getBytes());
        RpcRequestHandler.channelCtx.writeAndFlush(requestBuf);
        log.info("请求 {} 已发送", request.getRequestId());
        // 阻塞等待服务端处理完毕返回结果
        synchronized (request) {
            // 放弃对象锁，阻塞等待notify
            request.wait();
        }
        log.info("请求 {} 接收结果完毕", request.getRequestId());
    }
}
