package org.clever.master.session;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 房间管理器
 */
public class MasterRoomSessionManager {
    private final Map<String, MasterRoomSession> roomSessionMap = new ConcurrentHashMap<>();
    // 映射 SessionKey 与 roomId;
    private final Map<String, Set<String>> sessionRoomMap = new ConcurrentHashMap<>();
    /**
     * 房间会话. 如果房间会话不存在，则创建一个
     */
    private final MasterRoomSession getRoomSession(String roomId) {
        MasterRoomSession roomSession = roomSessionMap.get(roomId);
        if (roomSession == null) {
            roomSession = new MasterRoomSession(roomId);
            roomSessionMap.put(roomId, roomSession);
        }
        return roomSession;
    }


    /**
     * 加入这个房间的会话
     *
     * @param roomId 房间ID
     * @param session 会话
     */
    public void joinRoom(String roomId, MasterSession session) {
        MasterRoomSession roomSession = getRoomSession(roomId);
        roomSession.addMasterSession(session);
        sessionRoomMap.computeIfAbsent(session.getSessionKey(), k -> ConcurrentHashMap.newKeySet()).add(roomId);
    }

    /** 离开房间
     *
     * @param roomId 房间ID
     * @param sessionKey 会话Key
     */
    public void leaveRoom(String roomId, String sessionKey) {
        MasterRoomSession roomSession = getRoomSession(roomId);
        roomSession.removeMasterSession(sessionKey);
        sessionRoomMap.computeIfPresent(sessionKey, (k, v) -> {
            v.remove(roomId);
            return v;
        });
    }

    /**
     * 离开我加入的全部房间
     * @param sessionKey
     */
    public void leaveRoom(String sessionKey) {
        Set<String> roomIds = sessionRoomMap.get(sessionKey);
        if (roomIds != null) {
            for (String roomId : roomIds) {
                leaveRoom(roomId, sessionKey);
            }
            sessionRoomMap.remove(sessionKey);
        }
    }

    /**
     * 获得当前服务的房间下的全部会话
     *
     * @param roomId 房间ID
     * @return 房间下的全部会话
     */
    public Set<MasterSession> getRoomSessions(String roomId) {
        MasterRoomSession roomSession = getRoomSession(roomId);
        return roomSession.getMasterSessions();
    }

    /**
     * 当前服务器是否存在这个房间. 如果存在返回 true
     *
     * @param roomId 房间ID
     * @return true 存在, false 不存在
     *
     */
    public boolean isRoomExist(String roomId) {
        return roomSessionMap.containsKey(roomId);
    }

    /**
     * 当前服务器中的房间人数./ 集群环境中也只是当前服务器的房间人数.
     */
    public int getRoomSessionCount(String roomId) {
        MasterRoomSession masterRoomSession = roomSessionMap.get(roomId);
        return masterRoomSession == null ? 0 : masterRoomSession.getMasterSessions().size();
    }
}
