package org.clever.client.config;

import lombok.Getter;
import lombok.Setter;
import org.clever.client.health.HealthConfig;

@Getter
@Setter
public class ClientConfig {
    private ConnectConfig connectConfig = new ConnectConfig();
    private HealthConfig healthConfig = new HealthConfig();
}
