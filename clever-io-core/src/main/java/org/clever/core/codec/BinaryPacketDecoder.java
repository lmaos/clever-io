package org.clever.core.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.clever.core.protocol.BinaryPacket;
import org.clever.core.protocol.ProtocolUtils;

import java.util.List;
@ChannelHandler.Sharable
public class BinaryPacketDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {

        BinaryPacket binaryPacket = ProtocolUtils.toBinaryPacket(byteBuf);
        if (binaryPacket != null) {
            list.add(binaryPacket);
        }
    }
}
