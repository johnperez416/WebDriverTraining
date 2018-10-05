package com.octopus.exceptions;

public class ConfigurationException extends RuntimeException {
    public ConfigurationException() {

    }

    public ConfigurationException(final String message) {
        super(message);
    }

    public ConfigurationException(final String message, final Throwable ex)
    {
        super(message, ex);
    }

    public ConfigurationException(final Exception ex) {
        super(ex);
    }
}