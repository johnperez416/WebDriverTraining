package com.octopus.exceptions;

public class ScriptException extends RuntimeException {

    public ScriptException() {

    }

    public ScriptException(final String message) {
        super(message);
    }

    public ScriptException(final Throwable cause) {
        super(cause);
    }

    public ScriptException(final String message, final Throwable cause) {
        super(message, cause);
    }
}