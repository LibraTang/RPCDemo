package com.example.rpcdemo.server;

import com.example.rpcdemo.core.RPC;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.channels.ServerSocketChannel;

/**
 * netty服务端连接配置类
 */
@Slf4j
public class RpcResponseNet {

    private static RpcResponseNet instance;

    /**
     * 私有构造方法
     */
    private RpcResponseNet() {
        // netty主从线程模型
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // 启动辅助类
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    // 连接请求的最大队列长度为 backlog 参数，如果队列满时收到连接请求，则拒绝该连接
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // 以换行符分包，防止粘包/拆包问题，2048是最大长度，到达最大长度未出现换行符则抛出异常
                            socketChannel.pipeline().addLast(new LineBasedFrameDecoder(2048));
                            // 将接收到的对象转化为字符串
                            socketChannel.pipeline().addLast(new StringDecoder());
                            socketChannel.pipeline().addLast(new RpcResponseHandler());
                        }
                    });
            // 绑定端口，同步等待成功
            ChannelFuture future = bootstrap.bind(RPC.getServerConfig().getPort()).sync();
            log.info("服务端已启动于端口: {}", RPC.getServerConfig().getPort());
            // 同步等待服务端监听端口关闭
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 释放资源退出
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * 单例模式避免重复开启
     * @return
     */
    public static synchronized RpcResponseNet connect() {
        if (instance == null) {
            instance = new RpcResponseNet();
        }
        return instance;
    }

}
