package org.clever.master.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.MultiThreadIoEventLoopGroup;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.clever.master.config.MasterServerConfig;
import org.clever.master.context.MasterContext;
import org.clever.master.context.MasterContextFactory;
import org.clever.master.handler.MasterServerChannelInitializer;

/**
 * 主机服务. 用来接入用户的请求.
 */
public class MasterServer {
    private MasterContext masterContext;
    private MasterServerConfig masterServerConfig;
    private Channel channel;
    private ChannelFuture channelFuture;
    private boolean start = false;
    public MasterServer(MasterServerConfig masterServerConfig) {
        this.masterServerConfig = masterServerConfig;
        // 创建上下文
        this.masterContext = MasterContextFactory.createMasterContext(masterServerConfig);
    }

    /**
     * 启动服务器
     */
    public synchronized ChannelFuture start() {
        if (start) {
            return null;
        }
        // 创建线程组
        EventLoopGroup bossGroup = new MultiThreadIoEventLoopGroup(NioIoHandler.newFactory());
        EventLoopGroup workerGroup = new MultiThreadIoEventLoopGroup(NioIoHandler.newFactory());
        // 创建服务端引导程序
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        ChannelFuture bind = serverBootstrap.group(bossGroup, workerGroup) // 设置线程组
                .channel(NioServerSocketChannel.class) // 设置Channel类型, 为服务器 NIO SocketChannel
                .childHandler(new MasterServerChannelInitializer(masterContext)) // 设置ChannelHandler
                .bind(masterServerConfig.getPort()); // 绑定端口
        this.channel = bind.channel();
        // 添加关闭监听器
        this.channel.closeFuture().addListener(future -> {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        });
        this.channelFuture = bind;
        this.start = true;
        bind.syncUninterruptibly(); // 等待绑定成功
        return bind;
    }

    /**
     * 停止服务器
     */
    public synchronized void stop() {
        if (!start) {
            return;
        }
        channel.close();
        channel = null;
        channelFuture = null;
    }

    /**
     *
     * @return
     */
    public synchronized boolean isStart() {
        return start;
    }

}
