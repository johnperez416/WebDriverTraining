package com.octopus.exceptions;

public class VerificationException extends RuntimeException {
    public VerificationException() {

    }

    public VerificationException(final String message) {
        super(message);
    }

    public VerificationException(final String message, final Throwable ex)
    {
        super(message, ex);
    }

    public VerificationException(final Exception ex) {
        super(ex);
    }
}