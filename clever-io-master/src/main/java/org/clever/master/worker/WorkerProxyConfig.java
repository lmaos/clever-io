package org.clever.master.worker;

import lombok.Getter;
import lombok.Setter;
import org.clever.master.worker.schedule.local.LocalWorkerSchedule;

@Getter
@Setter
public class WorkerProxyConfig {
    /**
     * 业务处理的前缀.
     */
    private String prefix = "";
    /**
     * worker host. 远程 Worker 时配置 ip1:port1;ip2:port2;
     */
    private String workerHost = "";
    /**
     * worker schedule class : 本地调度, 和远程调度.
     * 用于处理 与worker 的业务的部分.
     */
    private String workerScheduleClass = LocalWorkerSchedule.class.getName();
}
