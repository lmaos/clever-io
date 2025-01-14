package org.clever.core.monitor.support;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.TimeUnit;

@Getter
@Setter
public class SessionMonitorConfig {
    /**
     * 循环间隔时间, 毫秒
     */
    private long loopInterval = 1000;
    /**
     * 最大会话时间，单位毫秒
     */
    private long maxSessionTime = 10000;

    // 创建一个监控线程池.
    private int threadPoolSize = 1;

    /**
     * 是否自动关闭超时会话
     */
    private boolean autoClose = true;
}
