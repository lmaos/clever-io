package org.clever.core.errors;

public class MagicException extends CleverIOException {


    public MagicException() {
    }

    public MagicException(String message) {
        super(message);
    }

    public MagicException(String message, Throwable cause) {
        super(message, cause);
    }

    public MagicException(Throwable cause) {
        super(cause);
    }
}
