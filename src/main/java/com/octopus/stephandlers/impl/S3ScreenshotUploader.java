package com.octopus.stephandlers.impl;

import com.octopus.decoratorbase.AutomatedBrowserBase;
import com.octopus.stephandlers.ScreenshotUploader;
import com.octopus.utils.S3Uploader;
import com.octopus.utils.SystemPropertyUtils;
import com.octopus.utils.impl.AutoDeletingTempFile;
import com.octopus.utils.impl.S3UploaderImpl;
import com.octopus.utils.impl.SystemPropertyUtilsImpl;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public class S3ScreenshotUploader implements ScreenshotUploader {
    private static final String S3_UPLOADING_ENABLED = "screenshotS3Enabled";
    private static final String SCREENSHOT_S3_BUCKET = "screenshotS3Bucket";
    private static final S3Uploader S_3_UPLOADER = new S3UploaderImpl();
    private static final SystemPropertyUtils SYSTEM_PROPERTY_UTILS = new SystemPropertyUtilsImpl();

    @Override
    public Optional<String> takeAndUploadScreenshot() {
        if (!SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(S3_UPLOADING_ENABLED, false) || AutomatedBrowserBase.GetInstance() == null)
            return Optional.empty();

        if (!SYSTEM_PROPERTY_UTILS.hasProperty(SCREENSHOT_S3_BUCKET)) {
            System.out.println("The " + SCREENSHOT_S3_BUCKET + " system property must be set");
            return Optional.empty();
        }

        try (final AutoDeletingTempFile screenshot = new AutoDeletingTempFile("screenshot" + UUID.randomUUID(), ".png")) {
            AutomatedBrowserBase.GetInstance().takeScreenshot(screenshot.getFile().getAbsolutePath());
            S_3_UPLOADER.uploadFileToS3(
                    SYSTEM_PROPERTY_UTILS.getProperty(SCREENSHOT_S3_BUCKET),
                    screenshot.getFile().getName(),
                    screenshot.getFile(),
                    true
            );
            return Optional.of("https://" + SYSTEM_PROPERTY_UTILS.getProperty(SCREENSHOT_S3_BUCKET) + ".s3.amazonaws.com/" + screenshot.getFile().getName());
        } catch (final IOException ex) {
            System.out.println("Failed to upload screenshot");
        }
        return Optional.empty();
    }
}
