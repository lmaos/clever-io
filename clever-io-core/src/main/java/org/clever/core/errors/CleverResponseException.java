package org.clever.core.errors;

import org.clever.core.protocol.ProtocolFactory;
import org.clever.core.protocol.Request;
import org.clever.core.protocol.Response;

public class CleverResponseException extends Exception {

    private final Response.WriteResponse response;

    public CleverResponseException(Response.WriteResponse response) {
        super(response.getStatus());
        this.response = response;
    }

    public CleverResponseException(Request request, String status) {
        this(ProtocolFactory.createResponse(request, status));
    }

    public Response.WriteResponse getResponse() {
        return response;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return null;
    }
}
