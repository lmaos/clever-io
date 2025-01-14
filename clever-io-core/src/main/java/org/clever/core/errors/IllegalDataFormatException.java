package org.clever.core.errors;

public class IllegalDataFormatException extends CleverIOException {
    public IllegalDataFormatException() {
        super();
    }

    public IllegalDataFormatException(String message) {
        super(message);
    }

    public IllegalDataFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalDataFormatException(Throwable cause) {
        super(cause);
    }
}
