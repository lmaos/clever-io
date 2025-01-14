package org.clever.core.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.clever.core.protocol.BinaryPacket;
import org.clever.core.protocol.JsonPacket;
import org.clever.core.protocol.ProtocolUtils;
import org.clever.core.protocol.Response;

import java.util.List;

/**
 * Response to JsonPacket
 */
public class ResponseJsonEncoder extends MessageToMessageEncoder<Response> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Response msg, List<Object> out) throws Exception {
        JsonPacket packet = ProtocolUtils.toJsonPacket(msg);
        out.add(packet);
    }

}
