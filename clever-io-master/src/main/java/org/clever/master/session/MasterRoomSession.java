package org.clever.master.session;

import lombok.Getter;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author clever.cat
 * @date 2020/08/01 16:04
 *
 * 组/房间会话
 */
public class MasterRoomSession {
    @Getter
    private final String roomId;

    public MasterRoomSession(String roomId) {
        this.roomId = roomId;
    }

    /**
     * 房间内的会话, KEY 是 Session KEY
     */
    private Map<String, MasterSession> masterSessionMap = new ConcurrentHashMap<>(128);

    public MasterSession getMasterSession(String sessionKey) {
        return masterSessionMap.get(sessionKey);
    }

    public void addMasterSession(MasterSession masterSession) {
        masterSessionMap.put(masterSession.getSessionKey(), masterSession);
    }

    public void removeMasterSession(String sessionKey) {
        masterSessionMap.remove(sessionKey);
    }

    // 全部会话
    public Set<MasterSession> getMasterSessions() {
        return  new HashSet<>(masterSessionMap.values());
    }
}
