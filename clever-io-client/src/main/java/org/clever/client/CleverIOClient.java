package org.clever.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.MultiThreadIoEventLoopGroup;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import org.clever.client.config.ClientConfig;
import org.clever.client.config.ConnectConfig;
import org.clever.client.context.ClientContext;
import org.clever.client.context.ClientContextFactory;
import org.clever.client.error.ClientCloseException;
import org.clever.client.handler.ClevelClientChannelInitializer;
import org.clever.client.health.HealthWatch;
import org.clever.client.respmg.ClientRequest;
import org.clever.core.protocol.ProtocolFactory;
import org.clever.core.protocol.Request;

public class CleverIOClient {

    @Getter
    private ClientContext clientContext;
    @Getter
    private boolean started = false;
    @Getter
    private ConnectConfig hostConfig;

    private HealthWatch healthWatch;
    private ChannelFuture channelFuture;
    private Channel channel;

    public CleverIOClient(ClientConfig config) {
        this.hostConfig = config.getConnectConfig();
        this.clientContext = ClientContextFactory.create(config);
    }

    /**
     * 直接使用 客户端的上下文, 创建.
     * @param clientContext
     * @param connectConfig 链接配置
     */
    public CleverIOClient(ClientContext clientContext, ConnectConfig connectConfig) {
        this.clientContext = clientContext;
        this.hostConfig = connectConfig;
    }

    /**
     * 启动客户端
     */
    public synchronized CleverIOClient start() {
        if (started) {
            return this;
        }

        EventLoopGroup eventLoopGroup;
        if (clientContext.getClientEventLoopGroup() != null) {
            eventLoopGroup = clientContext.getClientEventLoopGroup();
        } else {
            eventLoopGroup = new MultiThreadIoEventLoopGroup(NioIoHandler.newFactory());
        }
        // netty client
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ClevelClientChannelInitializer(clientContext));
        ChannelFuture bind = bootstrap.connect(hostConfig.getHost(), hostConfig.getPort());

        this.channel = bind.channel();
        this.channelFuture = bind;
        if (clientContext.getClientEventLoopGroup() == null) {
            this.channel.closeFuture().addListener(future -> {
                System.out.println("客户端已经关闭了.");
                synchronized (CleverIOClient.this) {
                    if (healthWatch != null) {
                        healthWatch.destroy();
                    }
                    this.started = false;
                    if (!this.clientContext.isCluster()) {
                        this.clientContext.destroy();
                    }
                }
                eventLoopGroup.shutdownGracefully();
            });
        }
        this.started = true;
        // 等待链接成功 继续执行
        bind.syncUninterruptibly();
        healthWatch = new HealthWatch(this);
        healthWatch.start();
        return this;
    }


    /**
     * 关闭当前客户端.
     */
    public synchronized void close() {
        if (!started) {
            return;
        }
        if (channelFuture != null) {
            channel.close();
            channelFuture = null;
            channel = null;
        }
    }

    public synchronized void reconnect() {
        if (!started) {
            return;
        }
        close();
        start();
    }
    public ChannelFuture send(Request request)  {
        if (!started) {
            return null;
        }
        return this.channel.writeAndFlush(request);
    }


    public ClientRequest createRequest() throws ClientCloseException {
        if (!started) {
            throw new ClientCloseException("客户端已经关闭了.");
        }
        ClientRequest clientRequest = new ClientRequest(this);
        clientRequest.setHeader(ProtocolFactory.createHeader());
        return clientRequest;
    }
}
