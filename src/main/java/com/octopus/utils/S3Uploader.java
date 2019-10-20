package com.octopus.utils;

import java.io.File;
import java.util.concurrent.CompletableFuture;

public interface S3Uploader {
    CompletableFuture<Void> uploadFileToS3(String region, String bucket, String filename, File file, boolean publicAcl);
    CompletableFuture<Void> uploadFileToS3(String bucket, String filename, File file, boolean publicAcl);
}
