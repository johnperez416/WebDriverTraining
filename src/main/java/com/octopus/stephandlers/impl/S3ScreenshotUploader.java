package com.octopus.stephandlers.impl;

import com.octopus.decoratorbase.AutomatedBrowserBase;
import com.octopus.stephandlers.ScreenshotUploader;
import com.octopus.utils.SystemPropertyUtils;
import com.octopus.utils.impl.SystemPropertyUtilsImpl;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

public class S3ScreenshotUploader implements ScreenshotUploader {
    static final Logger LOGGER = Logger.getLogger(S3ScreenshotUploader.class.toString());
    private static final String S3_UPLOADING_ENABLED = "screenshotS3Enabled";
    private static final String SCREENSHOT_S3_BUCKET = "screenshotS3Bucket";
    private static final SystemPropertyUtils SYSTEM_PROPERTY_UTILS = new SystemPropertyUtilsImpl();

    @Override
    public Optional<CompletableFuture<String>> takeAndUploadScreenshot() {
        if (!SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(S3_UPLOADING_ENABLED, false) || AutomatedBrowserBase.getInstance() == null)
            return Optional.empty();

        if (!SYSTEM_PROPERTY_UTILS.hasProperty(SCREENSHOT_S3_BUCKET)) {
            LOGGER.info("The " + SCREENSHOT_S3_BUCKET + " system property must be set");
            return Optional.empty();
        }

        final String filename = "screenshot" + UUID.randomUUID() + ".png";
        try {
            final CompletableFuture<Void> screenshot = AutomatedBrowserBase.getInstance().takeScreenshot(
                    "s3://" + SYSTEM_PROPERTY_UTILS.getProperty(SCREENSHOT_S3_BUCKET) + "/" + filename,
                    true);
            return Optional.of(screenshot.thenCompose(s ->
                    CompletableFuture.supplyAsync(() ->
                            "https://" + SYSTEM_PROPERTY_UTILS.getProperty(SCREENSHOT_S3_BUCKET) + ".s3.amazonaws.com/" + filename)));
        } catch (final Exception ex) {
            LOGGER.warning("Failed to upload screenshot. " + ex.toString());
            return Optional.empty();
        }
    }
}
