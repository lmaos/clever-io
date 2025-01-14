package org.clever.core.protocol;

import lombok.Getter;

/**
 * 心跳/健康存活的状态
 */
public abstract class HeartbeatProtocol implements Protocol {
    protected HeartbeatProtocol(long id, long timestamp) {
        this.id = id;
        this.timestamp = timestamp;
    }

    protected HeartbeatProtocol(long id) {
        this.id = id;
        this.timestamp = System.currentTimeMillis();
    }

    @Getter
    private long id;
    @Getter
    private long timestamp;

    @Override
    public boolean isHeartbeat() {
        return true;
    }
}
