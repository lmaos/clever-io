package org.clever.client.respmg;

import org.clever.core.destroy.Destroy;
import org.clever.core.protocol.Response;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 同步应答管理.
 */
public class SyncResponseManage implements Destroy {
    // id, response
    private Map<Long, SyncResponse> syncResponseMap = new ConcurrentHashMap<>(128);

    public SyncResponse createSyncResponse(long id, long timeout) {
        if (id >= 0) {
            SyncResponse syncResponse = new SyncResponse(timeout);
            syncResponseMap.put(id, syncResponse);
            return syncResponse;
        } else {
            throw new IllegalArgumentException("id < 0");
        }
    }

    // 回收同步应答
    public void removeSyncResponse(long id) {
        syncResponseMap.remove(id);
    }

    /**
     * 调用时进行应答
     * @return true: 找到对应的应答, false: 未找到对应的应答
     */
    public boolean syncResponse(Response response) {
        if (response.getId() >= 0) {
            SyncResponse syncResponse = syncResponseMap.remove(response.getId());
            if (syncResponse != null) {
                syncResponse.setResponse(response);
                return true;
            }
        }
        return false;
    }

    @Override
    public void destroy() {
        syncResponseMap.forEach((id, syncResponse) -> {
            syncResponse.setResponse(null);
        });
        syncResponseMap.clear();
    }
}
