package org.clever.master.context;

import lombok.Getter;
import lombok.Setter;
import org.clever.core.context.Context;
import org.clever.master.adapter.MasterAdapter;
import org.clever.master.auth.Authentication;
import org.clever.master.config.MasterServerConfig;
import org.clever.master.session.MasterSessionManager;
import org.clever.master.worker.register.WorkerRegister;

@Getter
@Setter
public class MasterContext implements Context {

    private MasterServerConfig masterServerConfig; // master 配置
    private Authentication authentication; // 认证
    private MasterAdapter masterAdapter; // 请求处理
    private MasterSessionManager masterSessionManager; // session 管理
    private WorkerRegister workerRegister; // worker 注册

    /**
     * internal worker
     * 说看: master 启动时, 会创建一个 internal worker, 用于处理内部请求
     */
//    private InternalWorkerStart internalWorker;
    private WorkerScheduleContext internalWorkerContext;
    public MasterContext() {

        init();
    }

    private void init() {
        this.internalWorkerContext = new WorkerScheduleContext();
    }

    @Override
    public void destroy() {

        if (masterSessionManager != null) {
            masterSessionManager.destroy();
        }
        if (workerRegister != null) {
            workerRegister.destroy();
        }

        internalWorkerContext.destroy();;
    }
}
