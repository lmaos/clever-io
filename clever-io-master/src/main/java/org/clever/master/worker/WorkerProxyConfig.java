package org.clever.master.worker;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkerProxyConfig {
    private String prefix; // worker proxy前缀
    private String workerHost; // worker host
    /**
     * worker adapter class : 本地适配器, 和远程适配器.
     * 用于处理 与worker 的业务的部分.
     */
    private String workerScheduleClass;
}
