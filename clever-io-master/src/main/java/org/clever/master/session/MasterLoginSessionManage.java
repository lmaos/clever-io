package org.clever.master.session;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 管理已经登陆的会话.
 */
public class MasterLoginSessionManage {
    // 登录会话管理
    // userId 与 Session 映射.
    private final Map<String, Set<MasterSession>> userIdSessionMap = new ConcurrentHashMap<>();

    //
    public void loginSession(MasterSession session) {
        if (session.getUserId() != null && !session.getUserId().isEmpty()) {
            userIdSessionMap.computeIfAbsent(session.getUserId(), k -> ConcurrentHashMap.newKeySet())
                    .add(session);
        }
    }

    /**
     * 取消与UserId的绑定.
     */
    public void loginoutSession(MasterSession session) {
        if (session.getUserId() == null || session.getUserId().isEmpty()) {
            return;
        }
        userIdSessionMap.computeIfPresent(session.getUserId(), (k, v) -> {
            v.remove(session);
            return v;
        });
    }


    public Set<MasterSession> getUserIdSessions(String userId) {
        return userIdSessionMap.get(userId);
    }

    /**
     * 用户是否已经绑定.
     */
    public boolean isUserIdExist(String userId) {
        return userIdSessionMap.containsKey(userId);
    }
}
