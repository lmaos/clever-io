package org.clever.master.worker.schedule;

import org.clever.core.protocol.Request;
import org.clever.core.protocol.Response;
import org.clever.master.session.MasterSession;

public interface WorkerSchedule {
    public abstract Response.WriteResponse handlerRequest(MasterSession session, Request request) throws Exception;
}
