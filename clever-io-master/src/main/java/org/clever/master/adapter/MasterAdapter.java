package org.clever.master.adapter;

import org.clever.core.destroy.Destroy;
import org.clever.core.errors.CleverResponseException;
import org.clever.core.protocol.Request;
import org.clever.master.session.MasterSession;

/**
 * @author clevel-cat
 *
 * Master 的 URI 的适配接口
 */
public interface MasterAdapter extends Destroy {

    void handlerRequest(MasterSession session, Request request) throws CleverResponseException;
}
