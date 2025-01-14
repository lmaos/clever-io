package org.clever.client.error;

import org.clever.core.errors.CleverIOException;

public class ClientCloseException extends CleverIOException {
    public ClientCloseException() {
    }

    public ClientCloseException(String message) {
        super(message);
    }

    public ClientCloseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientCloseException(Throwable cause) {
        super(cause);
    }
}
