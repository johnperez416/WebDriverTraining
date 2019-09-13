package com.octopus.exceptions;

public class VideoException extends RuntimeException {

    public VideoException() {

    }

    public VideoException(final String message) {
        super(message);
    }

    public VideoException(final Throwable cause) {
        super(cause);
    }

    public VideoException(final String message, final Throwable cause) {
        super(message, cause);
    }
}