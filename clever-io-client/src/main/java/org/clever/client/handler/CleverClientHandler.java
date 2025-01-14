package org.clever.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.clever.client.context.ClientContext;
import org.clever.core.protocol.ClientProtocol;
import org.clever.core.protocol.Push;
import org.clever.core.protocol.Response;

/**
 * 客户端的处理逻辑
 */
public class CleverClientHandler extends SimpleChannelInboundHandler<ClientProtocol> {

    private final ClientContext clientContext;

    public CleverClientHandler(ClientContext clientContext) {
        this.clientContext = clientContext;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ClientProtocol msg) throws Exception {
        if (msg instanceof Response) {
            Response response = (Response) msg;
            if (response.isHeartbeat()) {
                return;
            }
            clientContext.getResponseManage().executorResponse(response);
        } else if (msg instanceof Push) {
            Push push = (Push) msg;
            if (Push.TOPIC_CLOSE.equalsIgnoreCase(push.getTopic())) {
                ctx.close().syncUninterruptibly();
            }
            // 订阅 push
            clientContext.getPushManage().onMessage(push);
        }



    }
}
