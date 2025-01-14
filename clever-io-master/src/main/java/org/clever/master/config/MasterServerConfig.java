package org.clever.master.config;

import lombok.Getter;
import lombok.Setter;
import org.clever.core.monitor.support.SessionMonitorConfig;

@Getter
@Setter
public class MasterServerConfig {

    private int port = 9661;

    private SessionMonitorConfig sessionMonitorConfig = new SessionMonitorConfig();

}
