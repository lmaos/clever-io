package org.clever.core.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.clever.core.protocol.*;

import java.util.List;

public class ResponseEncoder extends MessageToMessageEncoder<ClientProtocol> {
    @Override
    protected void encode(ChannelHandlerContext ctx, ClientProtocol msg, List<Object> out) throws Exception {
            BinaryPacket binaryPacket = ProtocolUtils.toBinaryPacket(msg);
            out.add(binaryPacket);
    }
}
