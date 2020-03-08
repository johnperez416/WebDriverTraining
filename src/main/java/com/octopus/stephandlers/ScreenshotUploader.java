package com.octopus.stephandlers;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * An interface to a step handler that takes a screenshot and uploads it after each step.
 */
public interface ScreenshotUploader {
    /**
     * Take a screenshot and upload it somewhere.
     *
     * @return A future that is completed when the screenshot is uploaded.
     */
    Optional<CompletableFuture<String>> takeAndUploadScreenshot();
}
