package org.clever.master.auth;

import org.clever.core.errors.CleverResponseException;
import org.clever.core.protocol.Request;
import org.clever.master.session.MasterSession;

public interface Authentication {

    // 验证 请求
    boolean verifyRequest(MasterSession session, Request request) throws CleverResponseException;
}
