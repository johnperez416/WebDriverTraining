package com.octopus.utils;

import java.io.File;

public interface S3Uploader {
    void uploadFileToS3(String region, String bucket, String filename, File file, boolean publicAcl);
    void uploadFileToS3(String bucket, String filename, File file, boolean publicAcl);
}
