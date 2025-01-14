package org.clever.core.monitor.support;

import lombok.NonNull;
import org.clever.core.monitor.Session;
import org.clever.core.monitor.SessionMonitor;
import org.clever.core.monitor.SessionMonitorEvent;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author clever.cat
 *
 * 默认实现会话监控
 */
public class DefaultSessionMonitor implements SessionMonitor {

    private final SessionMonitorConfig config;
    private ScheduledExecutorService executor;
    public DefaultSessionMonitor(@NonNull SessionMonitorConfig config) {
        this.config = config;

        // 初始化
        init();
    }

    private final Map<String, SessionMonitorEvent> sessionEventMap = new ConcurrentHashMap<>();

    // 启用一个线程池来进行监控
    @Override
    public void monitor(SessionMonitorEvent event) {
        if (event != null) {
            sessionEventMap.put(event.getSession().getSessionKey(), event);
            System.out.println("加入监控:" + event.getSession().getSessionKey());
            try {
                event.executor("monitor");
            } catch (Exception e) {
                logerror(e, "会话监控出现异常, monitor");
            }
        }
    }

    @Override
    public void unmonitor(String sessionKey) {
        SessionMonitorEvent event = sessionEventMap.remove(sessionKey);
        if (event != null) {
           try {
               event.executor("unmonitor, unmonitor");
           } catch (Exception e) {
               logerror(e, "会话监控出现异常");
           }
        }
    }


    private void init() {
        if (this.executor == null) {
            // 创建线程池
            this.executor = Executors.newScheduledThreadPool(config.getThreadPoolSize(), r -> {
                Thread thread = new Thread(r);
                thread.setName("session-monitor-" + thread.getId());
                thread.setDaemon(true);
                return thread;
            });
        }
        this.executor.scheduleWithFixedDelay(this::run, config.getLoopInterval(), config.getLoopInterval(), TimeUnit.MILLISECONDS);
    }

    private void run() {
        // for sessionEventMap
        for (Map.Entry<String, SessionMonitorEvent> entry : sessionEventMap.entrySet()) {
            try {
                SessionMonitorEvent sessionEvent = entry.getValue();
                if (System.currentTimeMillis() - sessionEvent.getSession().getLastAccessTime() < config.getMaxSessionTime()) {
                    sessionEvent.executor("session_monitor");
                } else {
                    // 执行自动关闭会话
                    if (config.isAutoClose()) {
                        sessionEvent.executor("session_close_before");
                        sessionEvent.getSession().closeSession();
                    }
                    sessionEvent.executor("session_close");
                }
            } catch (Exception e) {
                logerror(e, "会话监控出现异常");
            }
        }
    }

    private void logerror(Exception e, String msg) {
        e.printStackTrace();
    }
    @Override
    public void  destroy() {
        if (this.executor != null) {
            this.executor.shutdown();
            this.executor = null;
        }
    }

    @Override
    public boolean isAutoClose() {
        return config.isAutoClose();
    }


    @Override
    public Session getSession(String sessionKey) {
        SessionMonitorEvent sessionEvent = sessionEventMap.get(sessionKey);
        if (sessionEvent != null) {
            return sessionEvent.getSession();
        }
        return null;
    }

    @Override
    public Collection<Session> getSessions() {
        Collection<SessionMonitorEvent> values = sessionEventMap.values();
        return values.stream().map(SessionMonitorEvent::getSession).toList();
    }
}
