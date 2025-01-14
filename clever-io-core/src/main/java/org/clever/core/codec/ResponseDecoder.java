package org.clever.core.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.clever.core.errors.IllegalTypeException;
import org.clever.core.protocol.Packet;
import org.clever.core.protocol.Push;
import org.clever.core.protocol.Response;

import java.util.List;

public class ResponseDecoder extends MessageToMessageDecoder<Packet> {


    @Override
    protected void decode(ChannelHandlerContext ctx, Packet msg, List<Object> out) throws Exception {
        if (msg.getType() == Packet.TYPE_HEARTBEAT) {
            Response response = new Response.HeartbeatResponse(msg.getId(), msg.getTimestamp());
            out.add(response);
        } else if (msg.getType() == Packet.TYPE_RESPONSE){
            Response response = new Response.ReadResponse(msg);
            out.add(response);
        } else if (msg.getType() == Packet.TYPE_PUSH) {
            Push push = new Push.ReadPush(msg);
            out.add(push);
        } else {
            throw new IllegalTypeException("不支持的协议类型");
        }
    }
}
