package com.garygriffaw.itrequestservice.exceptions;

public class InvalidCombinationException extends RuntimeException {
    public InvalidCombinationException() {
    }

    public InvalidCombinationException(String message) {
        super(message);
    }

    public InvalidCombinationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidCombinationException(Throwable cause) {
        super(cause);
    }

    protected InvalidCombinationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
