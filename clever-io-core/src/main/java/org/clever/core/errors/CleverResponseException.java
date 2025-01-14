package org.clever.core.errors;

import org.clever.core.protocol.Response;

public class CleverResponseException extends Exception {

    private final Response response;

    public CleverResponseException(Response response) {
        super(response.getStatus());
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return null;
    }
}
