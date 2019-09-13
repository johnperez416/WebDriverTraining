package com.octopus.exceptions;

public class BrowserException extends RuntimeException {

    public BrowserException() {

    }

    public BrowserException(final String message) {
        super(message);
    }

    public BrowserException(final Throwable cause) {
        super(cause);
    }

    public BrowserException(final String message, final Throwable cause) {
        super(message, cause);
    }
}