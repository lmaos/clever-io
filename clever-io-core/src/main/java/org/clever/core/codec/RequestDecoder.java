package org.clever.core.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.clever.core.protocol.*;

import java.util.List;

/**
 * 请求解码器
 */
public class RequestDecoder extends MessageToMessageDecoder<Packet> {
    @Override
    protected void decode(ChannelHandlerContext ctx, Packet msg, List<Object> out) throws Exception {

        if (msg.getType() == Packet.TYPE_HEARTBEAT) {
            Request request = new Request.HeartbeatRequest(msg.getId(), msg.getTimestamp());
            out.add(request);
        } else {
            Request request = new Request.ReadRequest(msg);
            out.add(request);
        }

    }
}
