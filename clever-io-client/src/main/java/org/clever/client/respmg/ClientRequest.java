package org.clever.client.respmg;

import io.netty.channel.ChannelFuture;
import lombok.Setter;
import org.clever.client.CleverIOClient;
import org.clever.client.context.ClientContext;
import org.clever.client.error.ResponseTimeoutException;
import org.clever.core.protocol.Request;
import org.clever.core.protocol.Response;

public class ClientRequest extends Request.BaseRequest {

    private CleverIOClient client;
    private ClientContext clientContext;
    @Setter
    private long requestTimeout = 60_000;
    private boolean sender = false;
    public ClientRequest(CleverIOClient client) {
        this.client = client;
        this.clientContext = this.client.getClientContext();
        setId(clientContext.getClientId().incrementAndGet());
    }

    public Response send() throws ResponseTimeoutException {
        if (sender) {
            throw new IllegalStateException("Request already sent");
        }
        sender = true;
        SyncResponse syncResponse = this.clientContext.getResponseManage().createSyncResponse(getId(), requestTimeout);
        client.send(this);
        try {
            return syncResponse.getResponse();
        } catch (ResponseTimeoutException e) {
            this.clientContext.getResponseManage().removeSyncResponse(getId());
            throw e;
        }
    }

    public ChannelFuture asyncSend(AsyncResponseCallback asyncResponseCallback) {
        if (sender) {
            throw new IllegalStateException("Request already sent");
        }
        sender = true;
        AsyncResponse asyncResponse = new AsyncResponse(this, asyncResponseCallback, requestTimeout);
        this.clientContext.getResponseManage().addAsyncResponse(getId(), asyncResponse);
        return client.send(this);
    }
}
