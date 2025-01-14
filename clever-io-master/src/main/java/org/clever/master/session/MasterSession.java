package org.clever.master.session;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.EncoderException;
import lombok.*;
import org.clever.core.monitor.Session;
import org.clever.core.protocol.ClientProtocol;
import org.clever.core.protocol.ProtocolFactory;

import java.util.UUID;

public class MasterSession implements Session {
    /**
     * 唯一的 session key
     */
    @Getter
    private final String sessionKey = UUID.randomUUID().toString();
    /**
     * 创建时间, 毫秒
     */
    @Getter
    private final long createTime = System.currentTimeMillis();
    /**
     * 最后一次访问时间, 毫秒
     */
    @Getter
    private volatile long lastAccessTime = System.currentTimeMillis();

    @Getter
    private ChannelHandlerContext ctx;

    /**
     * 用户ID, 当资源请求时候, 存在 UserId的时候.设置.
     * userId == null, 则未登录的情况.
     */
    @Setter(AccessLevel.PROTECTED)
    @Getter
    private String userId;

    /**
     * 刷新最后访问时间
     */
    public void touch() {
        lastAccessTime = System.currentTimeMillis();
    }



    public MasterSession(@NonNull ChannelHandlerContext ctx) {
        this.ctx = ctx;
        addCloseListener(() -> {
            close = true;
        });
    }


    // send request
    public ChannelFuture send(ClientProtocol response) {
        if (ctx != null) {
            return ctx.writeAndFlush(response);
        } else {
            return null;
        }
    }
    private volatile boolean close;
    @Override
    public synchronized void closeSession() {
        if (!close) {
            try {
                send(ProtocolFactory.createClosePush()).syncUninterruptibly();
            } catch (EncoderException e) {}
            ctx.channel().close().syncUninterruptibly();
            close = true;
        }

    }

    public void addCloseListener(Runnable listener) {
        ctx.channel().closeFuture().addListener(future -> listener.run());
    }


    @Override
    public boolean equals(Object obj) {
        return obj instanceof MasterSession && ((MasterSession) obj).sessionKey.equals(sessionKey);
    }

    @Override
    public int hashCode() {
        return sessionKey.hashCode();
    }
}
