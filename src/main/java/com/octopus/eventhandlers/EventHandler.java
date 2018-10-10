package com.octopus.eventhandlers;

public interface EventHandler {
    void finished(final String id, final boolean status);
}
