package com.octopus.exceptions;

public class InteractionException extends RuntimeException {

    public InteractionException() {

    }

    public InteractionException(final String message) {
        super(message);
    }

    public InteractionException(final Throwable cause) {
        super(cause);
    }

    public InteractionException(final String message, final Throwable cause) {
        super(message, cause);
    }
}