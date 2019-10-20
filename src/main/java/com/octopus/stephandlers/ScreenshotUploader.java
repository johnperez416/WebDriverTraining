package com.octopus.stephandlers;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface ScreenshotUploader {
    Optional<CompletableFuture<String>> takeAndUploadScreenshot();
}
