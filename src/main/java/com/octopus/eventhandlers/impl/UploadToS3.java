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
import io.vavr.control.Try;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UploadToS3 implements EventHandler {
    private static final int MAX_ID_LENGTH = 50;
    private static final ZipUtils ZIP_UTILS = new ZipUtilsImpl();
    public static final String S3_REPORT_URL = "S3-Report-Url";
    private static final String BUCKET_NAME = "S3-Bucket-Name";
    private static final String CLIENT_REGION = "S3-Client-Region";
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
            System.out.println("The " + BUCKET_NAME + " and " + CLIENT_REGION +
                    " headers must be defined to upload the results to S3.");
            return previousResults;
        }

        if (proceed(status, headers, S3_FAILURE_ONLY)) {
            final String fileObjKeyName = (status ? "SUCCEEDED" : "FAILED") + "-" +
                    // Sanitise the filename
                    StringUtils.left(id.replaceAll("[^A-Za-z0-9_]", "_"), MAX_ID_LENGTH) + "-" +
                    UUID.randomUUID() + ".zip";

            try (final AutoDeletingTempFile report = new AutoDeletingTempFile("htmlreport", ".zip")) {
                FileUtils.copyFileToDirectory(new File(featureFile), new File(htmlOutputDir));
                FileUtils.listFiles(new File("."), new String[]{"png"}, false)
                        .forEach(file -> Try.run(() -> FileUtils.copyFileToDirectory(file, new File(htmlOutputDir))));
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

                System.out.println("UPLOADED " + (status ? "SUCCEEDED" : "FAILED") + " Cucumber Test ID " + id +
                        " to s3://" + headers.get(BUCKET_NAME) + "/" + fileObjKeyName);

                return new HashMap<String, String>() {{
                    this.putAll(previousResults);
                    this.put(S3_REPORT_URL, "s3://" + headers.get(BUCKET_NAME) + "/" + fileObjKeyName);
                }};
            } catch (final Exception ex) {
                System.out.println("The report file " + fileObjKeyName + " was not uploaded to S3. Error message: " + ex.toString());
            }
        }

        return previousResults;
    }
}
