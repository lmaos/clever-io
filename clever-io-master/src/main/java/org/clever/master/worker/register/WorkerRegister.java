package org.clever.master.worker.register;

import org.clever.core.destroy.Destroy;
import org.clever.master.worker.schedule.AbstractWorkerSchedule;

/**
 * Worker 注册接口, 配置worker转发的方式的实现.
 */
public interface WorkerRegister extends Destroy {

    AbstractWorkerSchedule findSchedule(String uri);


}
