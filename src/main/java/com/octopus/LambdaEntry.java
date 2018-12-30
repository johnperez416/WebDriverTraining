package com.octopus;

import com.amazonaws.services.lambda.runtime.Context;
import com.octopus.eventhandlers.EventHandler;
import com.octopus.eventhandlers.impl.SaveKubernetesConfigMap;
import com.octopus.eventhandlers.impl.SlackWebHook;
import com.octopus.eventhandlers.impl.UploadToS3;
import com.octopus.utils.EnvironmentAliasesProcessor;
import com.octopus.utils.ZipUtils;
import com.octopus.utils.impl.EnvironmentAliasesProcessorImpl;
import com.octopus.utils.impl.ZipUtilsImpl;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
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
    private static final EnvironmentAliasesProcessor ENVIRONMENT_ALIASES_PROCESSOR =
            new EnvironmentAliasesProcessorImpl();
    private static final String RETRY_HEADER = "Test-Retry";
    private static final ZipUtils ZIP_UTILS = new ZipUtilsImpl();
    private static final EventHandler[] EVENT_HANDLERS = new EventHandler[]{
            new UploadToS3(),
            new SlackWebHook(),
            new SaveKubernetesConfigMap()
    };
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
        File junitOutput = null;

        try {
            cleanTmpFolder();

            ENVIRONMENT_ALIASES_PROCESSOR.addHeaderVarsAsAliases(input.getHeaders());

            driverDirectory = downloadChromeDriver();
            chromeDirectory = downloadChromeHeadless();
            featureFile = writeFeatureToFile(input.getFeature());

            final int retryCount = NumberUtils.toInt(
                    input.getHeaders().getOrDefault(RETRY_HEADER, "1"),
                    1);

            int retValue = 0;

            for (int x = 0; x < retryCount; ++x) {
                outputFile = createCleanFile(outputFile, "output", ".json");
                txtOutputFile = createCleanFile(txtOutputFile, "output", ".txt");
                junitOutput = createCleanFile(junitOutput, "junit", ".xml");
                htmlOutput = createCleanDirectory(htmlOutput, "htmloutput");

                retValue = cucumber.api.cli.Main.run(
                    new String[]{
                            "--monochrome",
                            "--glue", "com.octopus.decoratorbase",
                            "--plugin", "json:" + outputFile.toString(),
                            "--plugin", "pretty:" + txtOutputFile.toString(),
                            "--plugin", "html:" + htmlOutput.toString(),
                            "--plugin", "junit:" + junitOutput.toString(),
                            featureFile.getAbsolutePath()},
                    Thread.currentThread().getContextClassLoader());
                if (retValue == 0) {
                    break;
                }
            }

            System.out.println((retValue == 0 ? "SUCCEEDED" : "FAILED") + " Cucumber Test ID " + input.getId());

            final String featureFilePath = featureFile.getAbsolutePath();
            final boolean status = retValue == 0;
            final String outputTextFile = FileUtils.readFileToString(txtOutputFile, Charset.defaultCharset());
            Arrays.stream(EVENT_HANDLERS).reduce(
                    new HashMap<String, String>(),
                    (results, handler) -> new HashMap<>(handler.finished(
                            input.getId(),
                            status,
                            featureFilePath,
                            outputTextFile,
                            input.getHeaders(),
                            results)),
                    (a, b) -> a
            );

            return FileUtils.readFileToString(outputFile, Charset.defaultCharset());
        } finally {
            FileUtils.deleteQuietly(driverDirectory);
            FileUtils.deleteQuietly(chromeDirectory);
            FileUtils.deleteQuietly(outputFile);
            FileUtils.deleteQuietly(txtOutputFile);
            FileUtils.deleteQuietly(featureFile);
            FileUtils.deleteQuietly(htmlOutput);
            FileUtils.deleteQuietly(junitOutput);

            System.out.println("FINISHED Cucumber Test ID " + input.getId());
        }
    }

    private File createCleanFile(final File last, final String prefix, final String suffix) throws IOException {
        FileUtils.deleteQuietly(last);
        return Files.createTempFile(prefix, suffix).toFile();
    }

    private File createCleanDirectory(final File last, final String name) throws IOException {
        FileUtils.deleteQuietly(last);
        return Files.createTempDirectory(name).toFile();
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

    /**
     * Before we start, try cleaning the tmp directory to remove
     * any left over files.
     */
    private void cleanTmpFolder() {
        try {
            FileUtils.cleanDirectory(new File("/tmp"));
        } catch (IOException e) {
            // silent failure
        }
    }
}