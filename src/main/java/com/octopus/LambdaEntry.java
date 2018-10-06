package com.octopus;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import org.apache.commons.io.FileUtils;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class LambdaEntry {
    private static final String CHROME_HEADLESS_PACKAGE =
            "https://s3.amazonaws.com/webdriver-testing-resources/stable-headless-chromium-amazonlinux-2017-03.zip";
    private static final String CHROME_DRIVER =
            "https://s3.amazonaws.com/webdriver-testing-resources/chromedriver_linux64.zip";

    public String runCucumber(String feature) throws Throwable {

        File driverDirectory = null;
        File chromeDirectory = null;
        File outputFile = null;
        File txtOutputFile = null;
        File featureFile = null;

        try {
            driverDirectory = downloadChromeDriver();
            chromeDirectory = downloadChromeHeadless();
            outputFile = Files.createTempFile("output", ".json").toFile();
            txtOutputFile = Files.createTempFile("output", ".txt").toFile();
            featureFile = writeFeatureToFile(feature);

            cucumber.api.cli.Main.run(
                    new String[]{
                            "--monochrome",
                            "--glue", "com.octopus.decoratorbase",
                            "--format", "json:" + outputFile.toString(),
                            "--format", "pretty:" + txtOutputFile.toString(),
                            featureFile.getAbsolutePath()},
                    Thread.currentThread().getContextClassLoader());

            sendEmail("admin@matthewcasperson.com", FileUtils.readFileToString(txtOutputFile, Charset.defaultCharset()));

            return FileUtils.readFileToString(outputFile, Charset.defaultCharset());

        } finally {
            FileUtils.deleteQuietly(driverDirectory);
            FileUtils.deleteQuietly(chromeDirectory);
            FileUtils.deleteQuietly(outputFile);
            FileUtils.deleteQuietly(txtOutputFile);
            FileUtils.deleteQuietly(featureFile);
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
            unzipFile(downloadedFile.getAbsolutePath(), extractedDir.getAbsolutePath());
            return extractedDir;
        } finally {
            FileUtils.deleteQuietly(downloadedFile);
        }

    }

    private void unzipFile(final String fileZip, final String outputDirectory) throws IOException {

        final byte[] buffer = new byte[1024];

        try (final ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip))) {
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                final String fileName = zipEntry.getName();
                final File newFile = new File(outputDirectory + "/" + fileName);
                try (final FileOutputStream fos = new FileOutputStream(newFile)) {
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                }
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
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
}