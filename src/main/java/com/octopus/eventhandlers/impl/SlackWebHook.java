package com.octopus.eventhandlers.impl;

import com.octopus.decoratorbase.AutomatedBrowserBase;
import com.octopus.eventhandlers.EventHandler;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.util.Map;

public class SlackWebHook implements EventHandler {
    private static final String HOOK_URL = "Hook-Url";

    @Override
    public void finished(final String id,
                         final boolean status,
                         final String featureFile,
                         final String content,
                         final Map<String, String> headers) {
        if (!headers.containsKey(HOOK_URL)) {
            System.out.println("The " + HOOK_URL +
                    " headers must be defined to return the results via Slack");
            return;
        }

        try (final CloseableHttpClient client = HttpClients.createDefault()) {
            final HttpPost httpPost = new HttpPost(headers.get(HOOK_URL));
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setEntity(new StringEntity("{\"text\":\"Cucumber test " +
                    (status ? "succeeded": "failed") + ": " + id + ". " +
                    "Average wait time " +
                    (AutomatedBrowserBase.getAverageWaitTime() / 1000) + " seconds\"}"));
            try (final CloseableHttpResponse response = client.execute(httpPost)) {
                if (response.getStatusLine().getStatusCode() != 200) {
                    throw new Exception("Failed to post to slack");
                }
            }
        } catch (final Exception ex) {
            System.out.println("Failed to send result to Slack.");
        }
    }
}
