package com.octopus;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import com.octopus.utils.ZipUtils;
import com.octopus.utils.impl.ZipUtilsImpl;
import org.apache.commons.io.FileUtils;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.UUID;

class LambdaInput {
    private String id;
    private String feature;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(final String feature) {
        this.feature = feature;
    }
}

public class LambdaEntry {
    private static final ZipUtils ZIP_UTILS = new ZipUtilsImpl();
    private static final String CHROME_HEADLESS_PACKAGE =
            "https://s3.amazonaws.com/webdriver-testing-resources/stable-headless-chromium-amazonlinux-2017-03.zip";
    private static final String CHROME_DRIVER =
            "https://s3.amazonaws.com/webdriver-testing-resources/chromedriver_linux64.zip";

    public String runCucumber(final LambdaInput input) throws Throwable {
        System.out.println("STARTED Cucumber Test ID " + input.getId());

        File driverDirectory = null;
        File chromeDirectory = null;
        File outputFile = null;
        File txtOutputFile = null;
        File featureFile = null;
        File htmlOutput = null;

        try {
            driverDirectory = downloadChromeDriver();
            chromeDirectory = downloadChromeHeadless();
            outputFile = Files.createTempFile("output", ".json").toFile();
            txtOutputFile = Files.createTempFile("output", ".txt").toFile();
            featureFile = writeFeatureToFile(input.getFeature());
            htmlOutput = Files.createTempDirectory("htmloutput").toFile();

            final int retValue = cucumber.api.cli.Main.run(
                    new String[]{
                            "--monochrome",
                            "--glue", "com.octopus.decoratorbase",
                            "--plugin", "json:" + outputFile.toString(),
                            "--plugin", "pretty:" + txtOutputFile.toString(),
                            "--plugin", "html:" + htmlOutput.toString(),
                            featureFile.getAbsolutePath()},
                    Thread.currentThread().getContextClassLoader());

            System.out.println((retValue == 0 ? "SUCCEEDED" : "FAILED") + " Cucumber Test ID " + input.getId());

            uploadS3Report(
                    input.getId(),
                    retValue == 0,
                    htmlOutput.getAbsolutePath(),
                    "us-east-1",
                    "cucumber-html-report-files");
            sendEmail("admin@matthewcasperson.com", FileUtils.readFileToString(txtOutputFile, Charset.defaultCharset()));

            return FileUtils.readFileToString(outputFile, Charset.defaultCharset());
        } finally {
            FileUtils.deleteQuietly(driverDirectory);
            FileUtils.deleteQuietly(chromeDirectory);
            FileUtils.deleteQuietly(outputFile);
            FileUtils.deleteQuietly(txtOutputFile);
            FileUtils.deleteQuietly(featureFile);
            FileUtils.deleteQuietly(htmlOutput);

            System.out.println("FINISHED Cucumber Test ID " + input.getId());
        }
    }

    private File downloadChromeDriver() throws IOException {
        final File extractedDir = downloadAndExtractFile(CHROME_DRIVER, "chrome_driver");
        final String driver = extractedDir.getAbsolutePath() + "/chromedriver";
        System.setProperty("webdriver.chrome.driver", driver);
        new File(driver).setExecutable(true);
        return extractedDir;
    }

    private File downloadChromeHeadless() throws IOException {
        final File extractedDir = downloadAndExtractFile(CHROME_HEADLESS_PACKAGE, "chrome_headless");
        final String chrome = extractedDir.getAbsolutePath() + "/headless-chromium";
        System.setProperty("chrome.binary", chrome);
        new File(chrome).setExecutable(true);
        return extractedDir;
    }

    private File downloadAndExtractFile(final String download, final String tempDirPrefix) throws IOException {
        File downloadedFile = null;
        try {
            downloadedFile = File.createTempFile("download", ".zip");
            FileUtils.copyURLToFile(new URL(download), downloadedFile);
            final File extractedDir = Files.createTempDirectory(tempDirPrefix).toFile();
            ZIP_UTILS.unzipFile(downloadedFile.getAbsolutePath(), extractedDir.getAbsolutePath());
            return extractedDir;
        } finally {
            FileUtils.deleteQuietly(downloadedFile);
        }

    }

    private File writeFeatureToFile(final String feature) throws IOException {
        final File featureFile = File.createTempFile("cucumber", ".feature");
        try {
            final URL url = new URL(feature);
            FileUtils.copyURLToFile(url, featureFile);
        } catch (final MalformedURLException ex) {
            try (PrintWriter out = new PrintWriter(featureFile)) {
                out.println(feature);
            }
        }
        return featureFile;
    }

    private void sendEmail(final String to, final String results) {
        try {
            final AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard()
                    .withRegion(Regions.US_EAST_1).build();

            final SendEmailRequest request = new SendEmailRequest()
                    .withDestination(new Destination()
                            .withToAddresses(to))
                    .withMessage(new Message()
                            .withBody(new Body()
                                    .withText(new Content()
                                            .withCharset("UTF-8").withData(results)))
                            .withSubject(new Content()
                                    .withCharset("UTF-8").withData("WebDriver Test Results")))
                    .withSource("admin@matthewcasperson.com");
            client.sendEmail(request);
        } catch (final Exception ex) {
            System.out.println("The email was not sent. Error message: " + ex.getMessage());
        }
    }

    private static void uploadS3Report(
            final String id,
            final boolean status,
            final String reportDir,
            final String clientRegion,
            final String bucketName) {
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