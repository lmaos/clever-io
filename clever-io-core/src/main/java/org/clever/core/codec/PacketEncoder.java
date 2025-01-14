package org.clever.core.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.clever.core.protocol.BinaryPacket;
import org.clever.core.protocol.JsonPacket;
import org.clever.core.protocol.Packet;
import org.clever.core.protocol.ProtocolUtils;
@ChannelHandler.Sharable
public class PacketEncoder extends MessageToByteEncoder<Packet> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Packet packet, ByteBuf byteBuf) throws Exception {
        if (packet instanceof BinaryPacket) {
            ProtocolUtils.encodeToByteBuf((BinaryPacket) packet, byteBuf);
        } else if (packet instanceof JsonPacket) {
            BinaryPacket binaryPacket = ProtocolUtils.toBinaryPacket((JsonPacket) packet);
            ProtocolUtils.encodeToByteBuf(binaryPacket, byteBuf);
        } else {
            throw new UnsupportedOperationException("不支持的协议类型");
        }
    }
}
