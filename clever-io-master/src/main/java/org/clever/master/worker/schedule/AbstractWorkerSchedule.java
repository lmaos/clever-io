package org.clever.master.worker.schedule;

import lombok.Getter;
import org.clever.core.destroy.Destroy;
import org.clever.core.protocol.Request;
import org.clever.core.protocol.Response;
import org.clever.master.session.MasterSession;
import org.clever.master.worker.WorkerProxyConfig;
import org.clever.master.worker.register.WorkerRegister;

/**
 * Worker 调度接口, 如何去调度执行这个worker
 */
public abstract class AbstractWorkerSchedule implements WorkerSchedule, Destroy {
    @Getter
    private WorkerProxyConfig workerProxyConfig;
    @Getter
    private WorkerRegister workerRegister;
    public AbstractWorkerSchedule(WorkerRegister workerRegister, WorkerProxyConfig workerProxyConfig) {
        this.workerRegister = workerRegister;
        this.workerProxyConfig = workerProxyConfig;
    }

    public abstract Response.WriteResponse handlerRequest(MasterSession session, Request request) throws Exception;

}
