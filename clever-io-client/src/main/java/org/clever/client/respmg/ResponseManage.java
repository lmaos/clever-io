package org.clever.client.respmg;

import lombok.Getter;
import org.clever.client.context.ClientContext;
import org.clever.core.destroy.Destroy;
import org.clever.core.protocol.Request;
import org.clever.core.protocol.Response;

public class ResponseManage implements Destroy {
    private ClientContext clientContext;

    public ResponseManage(ClientContext clientContext) {
        this.clientContext = clientContext;
    }

    @Getter
    private final SyncResponseManage syncResponseManage = new SyncResponseManage();
    @Getter
    private final AsyncResponseManage asyncResponseManage = new AsyncResponseManage();


    public Request.BaseRequest createRequest() {
        return new Request.BaseRequest(clientContext.getClientId().incrementAndGet());
    }

    public void addAsyncResponse(long id, AsyncResponse asyncResponse) {
        asyncResponseManage.addAsyncResponse(id, asyncResponse);
    }

    /**
     *
     * @param response
     */
    public void executorResponse(Response response) {
        if (!asyncResponseManage.asyncResponse(response)) {
            syncResponseManage.syncResponse(response);
        }
    }

    // 同步应答

    public SyncResponse createSyncResponse(long id, long timeout) {
        return syncResponseManage.createSyncResponse(id, timeout);
    }

    public void removeSyncResponse(long id) {
        syncResponseManage.removeSyncResponse(id);
    }
    @Override
    public void destroy() {
        asyncResponseManage.destroy();
        syncResponseManage.destroy();
    }

}
