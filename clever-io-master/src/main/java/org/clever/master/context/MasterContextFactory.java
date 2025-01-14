package org.clever.master.context;

import org.clever.core.monitor.SessionMonitor;
import org.clever.core.monitor.support.DefaultSessionMonitor;
import org.clever.core.monitor.support.SessionMonitorConfig;
import org.clever.master.config.MasterServerConfig;
import org.clever.master.session.MasterSessionManager;

public class MasterContextFactory {


    // 通过 MasterServerConfig 解析称 MasterContext
    public static MasterContext createMasterContext(MasterServerConfig masterServerConfig) {
        MasterContext masterContext = new MasterContext();
        masterContext.setMasterServerConfig(masterServerConfig);
        // 创建 Master会话的管理.
        SessionMonitorConfig sessionMonitorConfig = masterServerConfig.getSessionMonitorConfig();
        SessionMonitor sessionMonitor = new DefaultSessionMonitor(sessionMonitorConfig);
        MasterSessionManager masterSessionManager = new MasterSessionManager(sessionMonitor);

        masterContext.setMasterSessionManager(masterSessionManager); // 会话管理器
        masterContext.setMasterAdapter(null); // 请求的适配器
        masterContext.setAuthentication(null); // 认证器

        return masterContext;
    }
}
