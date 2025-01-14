package org.clever.client.context;

import lombok.Getter;
import lombok.Setter;
import org.clever.client.health.HealthConfig;
import org.clever.core.context.Context;

import java.util.concurrent.ScheduledExecutorService;
@Getter
@Setter
public class HealthContext implements Context {
    private HealthConfig healthConfig;
    private ScheduledExecutorService healthWatchExecutor;

    @Override
    public void destroy() {
        if (healthWatchExecutor != null) {
            healthWatchExecutor.shutdown();
        }
    }
}
