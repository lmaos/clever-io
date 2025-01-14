package org.clever.core.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.clever.core.protocol.BinaryPacket;
import org.clever.core.protocol.JsonPacket;
import org.clever.core.protocol.ProtocolUtils;
import org.clever.core.protocol.Request;

import java.util.List;

/**
 * Request to JsonPacket
 */
public class RequestJsonEncoder extends MessageToMessageEncoder<Request> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Request msg, List<Object> out) throws Exception {
        JsonPacket packet = ProtocolUtils.toJsonPacket(msg);
        out.add(packet);
    }

}
