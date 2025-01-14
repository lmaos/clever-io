package org.clever.core.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.clever.core.protocol.BinaryPacket;
import org.clever.core.protocol.JsonPacket;
import org.clever.core.protocol.ProtocolUtils;

import java.util.List;

@ChannelHandler.Sharable
public class JsonPacketDecoder extends MessageToMessageDecoder<TextWebSocketFrame> {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame msg, List<Object> list) throws Exception {
        JsonPacket packet = ProtocolUtils.toJsonPacket(msg);
        if (packet != null) {
            list.add(packet);
        }
    }
}
