package com.octopus.exceptions;

public class NetworkException extends RuntimeException {

    public NetworkException() {

    }

    public NetworkException(final String message) {
        super(message);
    }

    public NetworkException(final String message, final Throwable ex) {
        super(message, ex);
    }

    public NetworkException(final Exception ex) {
        super(ex);
    }
}