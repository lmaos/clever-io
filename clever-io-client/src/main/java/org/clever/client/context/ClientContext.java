package org.clever.client.context;

import io.netty.channel.EventLoopGroup;
import lombok.Getter;
import lombok.Setter;
import org.clever.client.respmg.PushManage;
import org.clever.client.respmg.ResponseManage;
import org.clever.core.context.Context;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

@Setter
@Getter
public class ClientContext implements Context {

    private final AtomicLong clientId = new AtomicLong(new Random().nextLong(10000));
    private EventLoopGroup clientEventLoopGroup;
    private ResponseManage responseManage = new ResponseManage(this);
    private PushManage pushManage = new PushManage(this);
    private HealthContext healthContext;
    /**
     * 集群客户端/ 如果true, 则客户端关闭时候不会销毁context, false客户端关闭时销毁 context
     */
    private boolean cluster = false;
    @Override
    public void destroy() {
        if (responseManage != null) {
            responseManage.destroy();
        }
        if (pushManage != null) {
            pushManage.destroy();
        }
        if (healthContext != null) {
            healthContext.destroy();
        }
        if (clientEventLoopGroup != null) {
            clientEventLoopGroup.shutdownGracefully();
        }
    }
}
