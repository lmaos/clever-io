package org.clever.client.health;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HealthConfig {
    private int poolSize = 1;
    private long loopInterval = 1000;
}
