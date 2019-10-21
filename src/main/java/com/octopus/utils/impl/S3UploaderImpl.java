package com.octopus.utils.impl;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.octopus.utils.S3Uploader;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class S3UploaderImpl implements S3Uploader {
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(10);

    @Override
    public CompletableFuture<Void> uploadFileToS3(final String region, final String bucket, final String filename, final File file, final boolean publicAcl) {
        return CompletableFuture.supplyAsync(() -> {
            final AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(region)
                    .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
                    .build();

            final PutObjectRequest request = new PutObjectRequest(bucket, filename, file)
                    .withCannedAcl(publicAcl ? CannedAccessControlList.PublicRead : CannedAccessControlList.Private);
            s3Client.putObject(request);
            return null;
        }, EXECUTOR_SERVICE);
    }

    @Override
    public CompletableFuture<Void> uploadFileToS3(final String bucket, final String filename, final File file, final boolean publicAcl) {
        return uploadFileToS3(
                StringUtils.defaultIfBlank(System.getenv("AWS_DEFAULT_REGION"), Regions.DEFAULT_REGION.getName()),
                bucket,
                filename,
                file,
                publicAcl);
    }
}
