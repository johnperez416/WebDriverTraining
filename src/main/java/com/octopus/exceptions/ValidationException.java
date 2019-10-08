package com.octopus.exceptions;

public class ValidationException extends RuntimeException {

    public ValidationException() {

    }

    public ValidationException(final String message) {
        super(message);
    }

    public ValidationException(final Throwable cause) {
        super(cause);
    }

    public ValidationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}