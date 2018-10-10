package com.octopus;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import com.octopus.eventhandlers.EventHandler;
import com.octopus.eventhandlers.impl.EmailResults;
import com.octopus.eventhandlers.impl.UploadToS3;
import com.octopus.utils.ZipUtils;
import com.octopus.utils.impl.ZipUtilsImpl;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Map;

class LambdaInput {
    private String id;
    private String feature;
    private Map<String, String> headers;

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

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(final Map<String, String> headers) {
        this.headers = headers;
    }
}

public class LambdaEntry {
    private static final ZipUtils ZIP_UTILS = new ZipUtilsImpl();
    private static final EventHandler EMAIL_RESULTS = new EmailResults();
    private static final EventHandler UPLOAD_TO_S3 = new UploadToS3();
    private static final String CHROME_HEADLESS_PACKAGE =
            "https://s3.amazonaws.com/webdriver-testing-resources/stable-headless-chromium-amazonlinux-2017-03.zip";
    private static final String CHROME_DRIVER =
            "https://s3.amazonaws.com/webdriver-testing-resources/chromedriver_linux64.zip";

    public String runCucumber(final LambdaInput input, final Context context) throws Throwable {
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

            EMAIL_RESULTS.finished(
                    input.getId(),
                    retValue == 0,
                    FileUtils.readFileToString(txtOutputFile, Charset.defaultCharset()),
                    input.getHeaders());
            UPLOAD_TO_S3.finished(
                    input.getId(),
                    retValue == 0,
                    htmlOutput.getAbsolutePath(),
                    input.getHeaders());

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
}