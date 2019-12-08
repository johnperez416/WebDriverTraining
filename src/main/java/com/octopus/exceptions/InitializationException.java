package com.octopus.exceptions;

public class InitializationException extends RuntimeException {

    public InitializationException() {

    }

    public InitializationException(final String message) {
        super(message);
    }

    public InitializationException(final Throwable cause) {
        super(cause);
    }

    public InitializationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}