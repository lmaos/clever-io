package org.clever.master.session.monitor;

import org.clever.core.monitor.Session;
import org.clever.core.monitor.SessionMonitorEvent;
import org.clever.master.session.MasterSession;

public class MasterSessionMonitorEvent implements SessionMonitorEvent {
    
    private final MasterSession session;
    public MasterSessionMonitorEvent (MasterSession session) {
        this.session = session;
    }
    @Override
    public Session getSession() {
        return session;
    }

    @Override
    public void executor(String event) throws Exception {
        if ("session_close".equalsIgnoreCase(event)) {
            session.closeSession();
        }
    }
}
