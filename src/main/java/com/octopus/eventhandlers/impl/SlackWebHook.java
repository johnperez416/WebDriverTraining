package com.octopus.eventhandlers.impl;

import com.octopus.eventhandlers.EventHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.text.DecimalFormat;
import java.util.Map;

public class SlackWebHook implements EventHandler {
    private static final String HOOK_URL = "Hook-Url";
    private static final String SLACK_FAILURE_ONLY = "Slack-Failure-Only";
    private static final DecimalFormat df = new DecimalFormat("#.##");

    @Override
    public Map<String, String> finished(final String id,
                                        final boolean status,
                                        final String featureFile,
                                        final String txtOutput,
                                        final String htmlOutputDir,
                                        final Map<String, String> headers,
                                        final Map<String, String> previousResults) {
        if (!headers.containsKey(HOOK_URL)) {
            System.out.println("The " + HOOK_URL +
                    " headers must be defined to return the results via Slack");
            return previousResults;
        }

        if (proceed(status, headers, SLACK_FAILURE_ONLY)) {
            try (final CloseableHttpClient client = HttpClients.createDefault()) {
                final HttpPost httpPost = new HttpPost(headers.get(HOOK_URL));
                httpPost.setHeader("Content-Type", "application/json");
                httpPost.setEntity(new StringEntity("{" +
                        "\"text\":\"Cucumber test " +
                        (status ? "succeeded" : "failed") + ": " + id + ". " +
                        (previousResults.containsKey(UploadToS3.S3_REPORT_URL)
                                ? " " + previousResults.get(UploadToS3.S3_REPORT_URL)
                                : "") +
                        "\"}"));
                try (final CloseableHttpResponse response = client.execute(httpPost)) {
                    if (response.getStatusLine().getStatusCode() != 200) {
                        throw new Exception("Failed to post to slack");
                    }
                }
            } catch (final Exception ex) {
                System.out.println("Failed to send result to Slack.");
            }
        }

        return previousResults;
    }
}
