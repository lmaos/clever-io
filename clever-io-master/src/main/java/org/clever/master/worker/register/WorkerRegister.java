package org.clever.master.worker.register;

import org.clever.core.destroy.Destroy;
import org.clever.master.worker.adapter.WorkerAdapter;

/**
 * Worker 注册接口, 配置worker转发的方式的实现.
 */
public interface WorkerRegister extends Destroy {
    /**
     * 通过URI查找工作适配器.
     * @param uri 资源请求
     * @return worker适配器
     */
    WorkerAdapter getWorkerAdapter(String uri);

}
