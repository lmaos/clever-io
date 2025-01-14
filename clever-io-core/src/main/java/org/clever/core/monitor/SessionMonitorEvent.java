package org.clever.core.monitor;

/**
 * 会话存活监控的事件
 */
public interface SessionMonitorEvent {

    Session getSession();

    /**
     * 执行事件的操作
     * @param event
     */
    void executor(String event) throws Exception;
}
