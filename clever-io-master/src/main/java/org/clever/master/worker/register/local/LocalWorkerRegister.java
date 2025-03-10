package org.clever.master.worker.register.local;

import lombok.Getter;
import org.clever.core.lang.PrefixMatchMap;
import org.clever.master.context.MasterContext;
import org.clever.master.worker.WorkerProxyConfig;
import org.clever.master.worker.register.WorkerRegister;
import org.clever.master.worker.schedule.AbstractWorkerSchedule;
import org.clever.worker.start.WorkerStart;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * 本地配置Worker的注册使用
 */
public class LocalWorkerRegister implements WorkerRegister {

    private PrefixMatchMap<AbstractWorkerSchedule> workerScheduleMap = new PrefixMatchMap<>();
    @Getter
    private MasterContext masterContext;
    /**
     * 本地的 Worker 启动
     */
    @Getter
    private WorkerStart workerStart;

    public LocalWorkerRegister(MasterContext masterContext) {
        this.masterContext = masterContext;
    }
    @Override
    public AbstractWorkerSchedule findSchedule(String uri) {
        AbstractWorkerSchedule workerSchedule = workerScheduleMap.get(uri);
        if (workerSchedule == null) {

        }
        return workerSchedule;
    }

    public void addWorkerSchedule(String prefix, AbstractWorkerSchedule workerSchedule) {
        workerScheduleMap.put(prefix, workerSchedule);
    }

    /**
     * 配置  WorkerSchedule . 通过ProxyConfig 本地配置Worker的注册使用
     *
     * @param proxyConfigs
     * @return
     */
    public LocalWorkerRegister config(List<WorkerProxyConfig> proxyConfigs) {
        for (WorkerProxyConfig proxyConfig : proxyConfigs) {
            try {
                Class<?> workerScheduleClass = Class.forName(proxyConfig.getWorkerScheduleClass());
                if (!AbstractWorkerSchedule.class.isAssignableFrom(workerScheduleClass)) {
                    Constructor<AbstractWorkerSchedule> declaredConstructor = (Constructor<AbstractWorkerSchedule>) workerScheduleClass
                            .getDeclaredConstructor(MasterContext.class, WorkerProxyConfig.class);
                    AbstractWorkerSchedule workerSchedule = declaredConstructor.newInstance(masterContext, proxyConfig);
                    addWorkerSchedule(proxyConfig.getPrefix(), workerSchedule);
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                     InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        return this;
    }
    @Override
    public void destroy() {

    }
}
