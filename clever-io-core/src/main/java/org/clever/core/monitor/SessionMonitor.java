package org.clever.core.monitor;

import org.clever.core.destroy.Destroy;

import java.util.Collection;

/**
 * 会话的存活情况监控
 */
public interface SessionMonitor extends Destroy {
    /**
     * 监控会话存活情况
     */
    void monitor(SessionMonitorEvent event);

    /** 取消监控
     *
     */
    void unmonitor(String sessionKey);

    /**
     * 存活的会话
     */
    Session getSession(String sessionKey);

    /**
     * 存活的全部会话
     */
    Collection<Session> getSessions();

    boolean isAutoClose();
}
