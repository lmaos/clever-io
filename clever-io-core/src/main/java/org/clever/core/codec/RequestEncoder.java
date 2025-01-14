package org.clever.core.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.clever.core.protocol.BinaryPacket;
import org.clever.core.protocol.ProtocolUtils;
import org.clever.core.protocol.Request;
import org.clever.core.protocol.Response;

import java.util.List;

public class RequestEncoder extends MessageToMessageEncoder<Request> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Request msg, List<Object> out) throws Exception {
        BinaryPacket binaryPacket = ProtocolUtils.toBinaryPacket(msg);
        out.add(binaryPacket);
    }

}
