package org.clever.core.errors;

import java.io.IOException;

public class CleverIOException extends IOException {
    public CleverIOException() {
    }

    public CleverIOException(String message) {
        super(message);
    }

    public CleverIOException(String message, Throwable cause) {
        super(message, cause);
    }

    public CleverIOException(Throwable cause) {
        super(cause);
    }
}
