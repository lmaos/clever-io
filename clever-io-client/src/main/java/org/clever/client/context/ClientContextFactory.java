package org.clever.client.context;

import org.clever.client.config.ClientConfig;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ClientContextFactory {
    public static ClientContext create(ClientConfig clientConfig) {
        ClientContext clientContext = new ClientContext();
        clientContext.setCluster(false);
        ScheduledExecutorService healthWatchExecutor = Executors.newScheduledThreadPool(1, r -> {
            Thread thread = new Thread(r);
            thread.setName("health-watch");
            thread.setDaemon(true);
            return thread;
        });
        // 健康检查上下文
        HealthContext healthContext = new HealthContext();
        healthContext.setHealthConfig(clientConfig.getHealthConfig());
        healthContext.setHealthWatchExecutor(healthWatchExecutor);
        clientContext.setHealthContext(healthContext);
        return clientContext;
    }
}
