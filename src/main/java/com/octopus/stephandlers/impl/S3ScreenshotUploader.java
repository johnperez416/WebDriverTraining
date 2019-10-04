package com.octopus.stephandlers.impl;

import com.octopus.decoratorbase.AutomatedBrowserBase;
import com.octopus.stephandlers.ScreenshotUploader;
import com.octopus.utils.S3Uploader;
import com.octopus.utils.SystemPropertyUtils;
import com.octopus.utils.impl.AutoDeletingTempFile;
import com.octopus.utils.impl.S3UploaderImpl;
import com.octopus.utils.impl.SystemPropertyUtilsImpl;
import org.apache.commons.io.FileUtils;

import java.io.File;
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

        try {
            final String filename = "screenshot" + UUID.randomUUID() + ".png";
            final String file = new File(System.getProperty("java.io.tmpdir") + File.separator + filename).getCanonicalFile().getAbsolutePath();
            try {
                AutomatedBrowserBase.GetInstance().takeScreenshot(file);

                if (!new File(file).exists()) {
                    return Optional.empty();
                }

                S_3_UPLOADER.uploadFileToS3(
                        SYSTEM_PROPERTY_UTILS.getProperty(SCREENSHOT_S3_BUCKET),
                        filename,
                        new File(file),
                        true
                );
                return Optional.of("https://" + SYSTEM_PROPERTY_UTILS.getProperty(SCREENSHOT_S3_BUCKET) + ".s3.amazonaws.com/" + filename);
            } finally {
                FileUtils.deleteQuietly(new File(file));
            }
        } catch (final IOException ex) {
            System.out.println("Failed to create a temporary file for the screenshot.");
            return Optional.empty();
        }
    }
}
