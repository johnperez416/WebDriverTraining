package com.octopus.eventhandlers.impl;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.octopus.eventhandlers.EventHandler;
import com.octopus.utils.ZipUtils;
import com.octopus.utils.impl.AutoDeletingTempFile;
import com.octopus.utils.impl.ZipUtilsImpl;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public class UploadToS3 implements EventHandler {
    /**
     * The header that saves the URL of the uploaded report.
     */
    public static final String S3_REPORT_URL = "S3-Report-Url";
    /**
     * The shared Logger instance.
     */
    private static final Logger LOGGER = Logger.getLogger(UploadToS3.class.toString());
    /**
     * The maximum file name length.
     */
    private static final int MAX_ID_LENGTH = 50;
    /**
     * The shared ZipUtilsImpl instance.
     */
    private static final ZipUtils ZIP_UTILS = new ZipUtilsImpl();
    /**
     * The header that defines the S3 bucket name.
     */
    private static final String BUCKET_NAME = "S3-Bucket-Name";
    /**
     * The header that defines the S3 region.
     */
    private static final String CLIENT_REGION = "S3-Client-Region";
    /**
     * The header that defines if the report should only be uploaded on error.
     */
    private static final String S3_FAILURE_ONLY = "S3-Failure-Only";

    @Override
    public Map<String, String> finished(final String id,
                                        final boolean status,
                                        final String featureFile,
                                        final String txtOutput,
                                        final String htmlOutputDir,
                                        final Map<String, String> headers,
                                        final Map<String, String> previousResults) {
        if (!(headers.containsKey(BUCKET_NAME) && headers.containsKey(CLIENT_REGION))) {
            LOGGER.info("The " + BUCKET_NAME + " and " + CLIENT_REGION
                    + " headers must be defined to upload the results to S3.");
            return previousResults;
        }

        if (proceed(status, headers, S3_FAILURE_ONLY)) {
            final String fileObjKeyName = (status ? "SUCCEEDED" : "FAILED") + "-"
                    // Sanitise the filename
                    + StringUtils.left(id.replaceAll("[^A-Za-z0-9_]", "_"), MAX_ID_LENGTH) + "-"
                    + UUID.randomUUID() + ".zip";

            try (final AutoDeletingTempFile report = new AutoDeletingTempFile("htmlreport", ".zip")) {
                FileUtils.copyFileToDirectory(new File(featureFile), new File(htmlOutputDir));
                ZIP_UTILS.zipDirectory(report.getFile().getAbsolutePath(), new File(htmlOutputDir).getAbsolutePath());

                final AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                        .withRegion(headers.get(CLIENT_REGION))
                        .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
                        .build();

                // Upload a file as a new object with ContentType and title specified.
                final PutObjectRequest request = new PutObjectRequest(headers.get(BUCKET_NAME), fileObjKeyName, report.getFile());
                final ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType("application/zip");
                metadata.addUserMetadata("x-amz-meta-title", "Cucumber Report");
                request.setMetadata(metadata);
                s3Client.putObject(request);

                LOGGER.info("UPLOADED " + (status ? "SUCCEEDED" : "FAILED") + " Cucumber Test ID " + id
                        + " to s3://" + headers.get(BUCKET_NAME) + "/" + fileObjKeyName);

                return new HashMap<>() {{
                    this.putAll(previousResults);
                    this.put(S3_REPORT_URL, "s3://" + headers.get(BUCKET_NAME) + "/" + fileObjKeyName);
                }};
            } catch (final Exception ex) {
                LOGGER.info("The report file " + fileObjKeyName + " was not uploaded to S3. Error message: " + ex.toString());
            }
        }

        return previousResults;
    }
}
