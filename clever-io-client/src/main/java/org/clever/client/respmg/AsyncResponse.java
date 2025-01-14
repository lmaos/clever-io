package org.clever.client.respmg;

import lombok.Getter;
import org.clever.core.protocol.Request;

public class AsyncResponse {
    @Getter
    private Request request;
    @Getter
    private AsyncResponseCallback callback;
    @Getter
    private long requestTime;
    @Getter
    private long timeout; // 异步应答超时时间

    public AsyncResponse(Request request, AsyncResponseCallback callback, long timeout) {
        this.request = request;
        this.callback = callback;
        this.requestTime = System.currentTimeMillis();
        this.timeout = timeout;
    }

}
