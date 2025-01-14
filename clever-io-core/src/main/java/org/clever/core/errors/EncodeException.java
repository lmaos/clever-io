package org.clever.core.errors;

public class EncodeException extends CleverIOException {
    public EncodeException() {
        super();
    }

    public EncodeException(String message) {
        super(message);
    }

    public EncodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public EncodeException(Throwable cause) {
        super(cause);
    }
}
