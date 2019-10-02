package com.octopus.eventhandlers.impl;

import com.octopus.eventhandlers.EventHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SeqLogging implements EventHandler {
    public static final String SEQ_API_KEY = "Seq-Api-Key";
    public static final String SEQ_MESSAGE = "Seq-Message";
    public static final String SEQ_URL = "Seq-Url";
    public static final String SEQ_LEVEL = "Seq-Level";
    public static final String SEQ_FAILURE_ONLY = "Seq-Failure-Only";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final List<String> LEVELS = Arrays.asList("Verbose", "Debug", "Information", "Warning", "Error", "Fatal");

    @Override
    public Map<String, String> finished(final String id,
                                        final boolean status,
                                        final String featureFile,
                                        final String txtOutput,
                                        final String htmlOutputDir,
                                        final Map<String, String> headers,
                                        final Map<String, String> previousResults) {
        if (!headers.containsKey(SEQ_API_KEY) ||
                !headers.containsKey(SEQ_MESSAGE) ||
                !headers.containsKey(SEQ_URL)) {
            System.out.println("The " + SEQ_API_KEY + ", " + SEQ_MESSAGE + " and " + SEQ_URL +
                    " headers must be defined to return the results via Seq");
            return previousResults;
        }

        if (proceed(status, headers, SEQ_FAILURE_ONLY)) {
            try (final CloseableHttpClient client = HttpClients.createDefault()) {
                final HttpPost httpPost = new HttpPost(headers.get(SEQ_URL) + "/api/events/raw");
                httpPost.setHeader("Content-Type", "application/json");
                httpPost.setHeader("X-Seq-ApiKey", headers.get(SEQ_API_KEY));
                final String body = "{\"Events\": [{" +
                        "\"Timestamp\": \"" + DATE_FORMAT.format(LocalDateTime.now()) + "\", " +
                        "\"Level\": \"" + getLevel(headers) + "\", " +
                        "\"Properties\": {" +
                        "\"success\":\"" + status + "\", " +
                        "\"MessageTemplate\":\"" + headers.get(SEQ_MESSAGE) + " " +
                        (status ? "succeeded" : "failed") + ": " + id + ". " +
                        (previousResults.containsKey(UploadToS3.S3_REPORT_URL)
                                ? " " + previousResults.get(UploadToS3.S3_REPORT_URL)
                                : "") +
                        "\"}]}";
                httpPost.setEntity(new StringEntity(body));
                try (final CloseableHttpResponse response = client.execute(httpPost)) {
                    if (!(response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 201)) {
                        throw new Exception("Failed to post to Seq");
                    }
                }
            } catch (final Exception ex) {
                System.out.println("Failed to send result to Seq.");
            }
        }

        return previousResults;
    }

    private String getLevel(final Map<String, String> headers) {
        return LEVELS.contains(headers.get(SEQ_LEVEL)) ?
                headers.get(SEQ_LEVEL) :
                "Information";
    }
}