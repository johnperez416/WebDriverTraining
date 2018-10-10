package com.octopus.eventhandlers;

import java.util.Map;

public interface EventHandler {
    void finished(final String id, final boolean status, final String content, final Map<String, String> headers);
}
