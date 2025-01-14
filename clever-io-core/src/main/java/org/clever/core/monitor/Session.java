package org.clever.core.monitor;

import io.netty.channel.ChannelFuture;

public interface Session {

    String getSessionKey();
    long getCreateTime();
    long getLastAccessTime();
    void closeSession();
}
