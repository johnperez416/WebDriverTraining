package com.octopus.eventhandlers;

import java.util.Map;

/**
 * When using this application from an environment like AWS Lambda, it is useful to perform actions after a feature
 * has finished. This interface provides a generic set of functions that allow processing to be done after a
 * feature is complete.
 */
public interface EventHandler {
    /**
     * Called once a feature is completed.
     *
     * @param id              An identifier for the execution of the app.
     * @param status          true if the feature completed successfully, and false otherwise
     * @param featureFile     The path to the feature file
     * @param txtOutput       The result of the Cucumber text output plugin
     * @param htmlOutputDir   The directory containing the output of the Cucumber HTML plugin
     * @param headers         Any headers that were defined when the Lambda was executed.
     * @param previousResults A generic map containing any information saved by other event listeners.
     * @return A generic map containing any information to be passed to other event listeners.
     */
    Map<String, String> finished(String id,
                                 boolean status,
                                 String featureFile,
                                 String txtOutput,
                                 String htmlOutputDir,
                                 Map<String, String> headers,
                                 Map<String, String> previousResults);

    /**
     * Called to determine if the event handler is to proceed.
     *
     * @param status            true if the feature completed successfully, and false otherwise
     * @param headers           Any headers that were defined when the Lambda was executed.
     * @param failureOnlyHeader The name of a header used to determine if this event handler is to only run on failure.
     * @return true if the event handler is to proceed, and false otherwise.
     */
    default boolean proceed(boolean status,
                            Map<String, String> headers,
                            String failureOnlyHeader) {
        return !status
                || !headers.containsKey(failureOnlyHeader)
                || headers.get(failureOnlyHeader).equalsIgnoreCase(Boolean.FALSE.toString());
    }
}
