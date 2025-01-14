package org.clever.client.health;

import org.clever.client.CleverIOClient;
import org.clever.client.config.ClientConfig;
import org.clever.client.context.ClientContext;
import org.clever.client.context.HealthContext;
import org.clever.core.destroy.Destroy;
import org.clever.core.protocol.Request;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 健康监控
 */
public class HealthWatch implements Destroy {

    private final CleverIOClient client;
    private ScheduledExecutorService executor;
    private ClientContext clientContext;
    private HealthContext healthContext;
    private HealthConfig healthConfig;
    private ScheduledFuture<?> scheduledFuture;
    public HealthWatch(CleverIOClient client) {
        this.client = client;
        this.clientContext = client.getClientContext();
        this.healthContext = clientContext.getHealthContext();
        this.healthConfig = healthContext.getHealthConfig();
        this.executor = healthContext.getHealthWatchExecutor();
    }

    private void watch() {
        long id = this.client.getClientContext().getClientId().incrementAndGet();
        this.client.send(new Request.HeartbeatRequest(id));
    }

    public void start() {
        watch();
        // 循环监控
        this.scheduledFuture = this.executor.scheduleWithFixedDelay(this::watch, healthConfig.getLoopInterval(), healthConfig.getLoopInterval(), TimeUnit.MILLISECONDS);
    }

    public void destroy() {
        if (this.scheduledFuture != null) {
            this.scheduledFuture.cancel(true);
            this.scheduledFuture = null;
        }
    }
}
