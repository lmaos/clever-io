package org.clever.master.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import org.clever.master.codec.DynamicProtocolCodec;
import org.clever.master.context.MasterContext;

/**
 * @author clever.cat
 *
 * 说明: 创建服务端Channel
 */
public class MasterServerChannelInitializer extends ChannelInitializer<SocketChannel>  {
    private MasterContext masterContext;
    public MasterServerChannelInitializer(MasterContext masterContext) {
        this.masterContext = masterContext;
    }
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 动态协议编解码器
        pipeline.addLast("DyanmicProtocolCodec", new DynamicProtocolCodec());
        // 业务逻辑处理器
        pipeline.addLast("MasterServerHandler", new MasterServerHandler(masterContext));
    }
}
