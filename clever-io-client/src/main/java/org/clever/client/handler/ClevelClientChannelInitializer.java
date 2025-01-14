package org.clever.client.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import org.clever.client.context.ClientContext;
import org.clever.core.codec.*;

public class ClevelClientChannelInitializer extends ChannelInitializer<SocketChannel> {
    private final ClientContext clientContext;
    public ClevelClientChannelInitializer(ClientContext clientContext) {
        this.clientContext = clientContext;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("binaryPacketEncoder", new PacketEncoder());
        pipeline.addLast("requestEncoder", new RequestEncoder());

        pipeline.addLast("binaryPacketDecoder", new BinaryPacketDecoder());
        pipeline.addLast("responseDecoder", new ResponseDecoder());
        pipeline.addLast("cleverClientHandler", new CleverClientHandler(clientContext));
    }

}
