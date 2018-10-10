package com.octopus.eventhandlers.impl;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.octopus.eventhandlers.EventHandler;
import com.octopus.utils.ZipUtils;
import com.octopus.utils.impl.ZipUtilsImpl;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.UUID;

public class UploadToS3 implements EventHandler {
    private static final ZipUtils ZIP_UTILS = new ZipUtilsImpl();
    private final String reportDir;
    private final String bucketName;
    private final String clientRegion;

    public UploadToS3(final String reportDir, final String bucketName, final String clientRegion) {
        this.reportDir = reportDir;
        this.bucketName = bucketName;
        this.clientRegion = clientRegion;
    }

    @Override
    public void finished(String id, boolean status) {
        final String fileObjKeyName = (status ? "SUCCEEDED" : "FAILED") + "-htmlreport-" + id + "-" + UUID.randomUUID() + ".zip";

        File report = null;

        try {
            report =  File.createTempFile("htmlreport", ".zip");
            ZIP_UTILS.zipDirectory(report.getAbsolutePath(), reportDir);

            final AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(clientRegion)
                    .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
                    .build();

            // Upload a file as a new object with ContentType and title specified.
            final PutObjectRequest request = new PutObjectRequest(bucketName, fileObjKeyName, report);
            final ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("application/zip");
            metadata.addUserMetadata("x-amz-meta-title", "Cucumber Report");
            request.setMetadata(metadata);
            s3Client.putObject(request);

            System.out.println("UPLOADED " + (status ? "SUCCEEDED" : "FAILED") + " Cucumber Test ID " + id +
                    " to s3://" + bucketName + "/" + fileObjKeyName);
        } catch(final Exception ex) {
            System.out.println("The report was not uploaded to S3. Error message: " + ex.getMessage());
        } finally {
            FileUtils.deleteQuietly(report);
        }
    }
}
