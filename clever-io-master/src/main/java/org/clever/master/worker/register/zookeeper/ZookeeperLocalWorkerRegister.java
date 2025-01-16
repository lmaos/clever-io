package org.clever.master.worker.register.zookeeper;

import org.clever.master.worker.register.WorkerRegister;
import org.clever.master.worker.schedule.AbstractWorkerSchedule;

public class ZookeeperLocalWorkerRegister implements WorkerRegister {
    @Override
    public AbstractWorkerSchedule findSchedule(String uri) {
        return null;
    }


    @Override
    public void destroy() {

    }
}
