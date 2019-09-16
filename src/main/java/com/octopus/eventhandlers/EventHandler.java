package com.octopus.eventhandlers;

import java.util.Map;

public interface EventHandler {
    Map<String, String> finished(final String id,
                                 final boolean status,
                                 final String featureFile,
                                 final String txtOutput,
                                 final String htmlOutputDir,
                                 final Map<String, String> headers,
                                 final Map<String, String> previousResults);

    default boolean proceed(final boolean status,
                            final Map<String, String> headers,
                            final String failureOnlyHeader) {
        return !status ||
                !headers.containsKey(failureOnlyHeader) ||
                headers.get(failureOnlyHeader).equalsIgnoreCase(Boolean.FALSE.toString());
    }
}
