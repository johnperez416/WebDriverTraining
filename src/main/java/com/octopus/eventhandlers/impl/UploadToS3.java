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
import java.util.Map;
import java.util.UUID;

public class UploadToS3 implements EventHandler {
    private static final ZipUtils ZIP_UTILS = new ZipUtilsImpl();
    private static final String BUCKET_NAME = "S3-Bucket-Name";
    private static final String CLIENT_REGION = "S3-Client-Region";

    @Override
    public void finished(final String id,
                         final boolean status,
                         final String featureFile,
                         final String content,
                         final Map<String, String> headers) {
        if (!(headers.containsKey(BUCKET_NAME) && headers.containsKey(CLIENT_REGION))) {
            System.out.println("The " + BUCKET_NAME + " and " + CLIENT_REGION +
                    " headers must be defined to upload the results to S3.");
            return;
        }

        final String fileObjKeyName = (status ? "SUCCEEDED" : "FAILED") + "-htmlreport-" + id + "-" + UUID.randomUUID() + ".zip";

        File report = null;

        try {
            FileUtils.copyFileToDirectory(new File(featureFile), report);
            report =  File.createTempFile("htmlreport", ".zip");
            ZIP_UTILS.zipDirectory(report.getAbsolutePath(), content);

            final AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(headers.get(CLIENT_REGION))
                    .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
                    .build();

            // Upload a file as a new object with ContentType and title specified.
            final PutObjectRequest request = new PutObjectRequest(headers.get(BUCKET_NAME), fileObjKeyName, report);
            final ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("application/zip");
            metadata.addUserMetadata("x-amz-meta-title", "Cucumber Report");
            request.setMetadata(metadata);
            s3Client.putObject(request);

            System.out.println("UPLOADED " + (status ? "SUCCEEDED" : "FAILED") + " Cucumber Test ID " + id +
                    " to s3://" + headers.get(BUCKET_NAME) + "/" + fileObjKeyName);
        } catch(final Exception ex) {
            System.out.println("The report was not uploaded to S3. Error message: " + ex.getMessage());
        } finally {
            FileUtils.deleteQuietly(report);
        }
    }
}
