package org.clever.master.session;

import lombok.Getter;
import org.clever.core.destroy.Destroy;
import org.clever.core.monitor.SessionMonitor;
import org.clever.master.session.monitor.MasterSessionMonitorEvent;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * master 会话管理
 */
public class MasterSessionManager implements Destroy {

    // 会话监控
    private final SessionMonitor sessionMonitor;
    // 链接的会话集合
    private final Map<String, MasterSession> sessionMap = new ConcurrentHashMap<>();
    // room session
    @Getter
    private final MasterRoomSessionManager roomSessionManager = new MasterRoomSessionManager();
    @Getter
    private final  MasterLoginSessionManage loginSessionManage = new MasterLoginSessionManage();

    /**
     *
     * @param sessionMonitor 会话监控
     */
    public MasterSessionManager(SessionMonitor sessionMonitor) {
        this.sessionMonitor = sessionMonitor;
    }

    /**
     * 钩子, 在连接建立时调用, 添加session, 添加监控.
     */
    public void addSession(MasterSession session) {
        sessionMap.put(session.getSessionKey(), session);
        sessionMonitor.monitor(new MasterSessionMonitorEvent(session));
        loginSessionManage.loginSession(session);

    }

    /**
     * 钩子, 在断开连接时调用, 移除session, 移除监控, 离开房间.
     * @param session
     */
    public void removeSession(MasterSession session) {
        sessionMap.remove(session.getSessionKey());
        sessionMonitor.unmonitor(session.getSessionKey());
        roomSessionManager.leaveRoom(session.getSessionKey());
        loginSessionManage.loginoutSession(session);
    }


    public MasterSession getSession(String sessionKey) {
        return sessionMap.get(sessionKey);
    }


    public Set<MasterSession> getUserIdSessions(String userId) {
        return loginSessionManage.getUserIdSessions(userId);
    }

    // 总连接数
    public int getSessionCount() {
        return sessionMap.size();
    }


    public void destroy() {
        if (sessionMonitor != null) {
            sessionMonitor.destroy();
        }
    }
}
