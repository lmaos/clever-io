package org.clever.master.worker.schedule;

import lombok.Getter;
import org.clever.core.destroy.Destroy;
import org.clever.core.protocol.Push;
import org.clever.core.protocol.Request;
import org.clever.core.protocol.Response;
import org.clever.master.context.MasterContext;
import org.clever.master.session.MasterSession;
import org.clever.master.worker.WorkerProxyConfig;

/**
 * Worker 调度接口, 如何去调度执行这个worker
 */
public abstract class AbstractWorkerSchedule implements WorkerSchedule, Destroy {
    @Getter
    private WorkerProxyConfig workerProxyConfig;
    @Getter
    private MasterContext masterContext;

    public AbstractWorkerSchedule(MasterContext masterContext, WorkerProxyConfig workerProxyConfig) {
        this.workerProxyConfig = workerProxyConfig;
        this.masterContext = masterContext;
    }

    public abstract Response.WriteResponse handlerRequest(MasterSession session, Request request) throws Exception;


    protected void handlerPush(Push push) {
        // 沟通推送消息- 沟通本地.
        // 沟通远程消息 - 沟通远程.
    }
}
