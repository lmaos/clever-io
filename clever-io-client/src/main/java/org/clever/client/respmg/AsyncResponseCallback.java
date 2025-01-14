package org.clever.client.respmg;

import org.clever.core.protocol.Request;
import org.clever.core.protocol.Response;

public interface AsyncResponseCallback {

    void onSuccess(Request request, Response response) throws Exception;

    void onFailure(Request request, Throwable throwable) throws Exception;
}
