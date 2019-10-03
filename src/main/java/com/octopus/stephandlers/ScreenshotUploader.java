package com.octopus.stephandlers;

import java.util.Optional;

public interface ScreenshotUploader {
    Optional<String> takeAndUploadScreenshot();
}
