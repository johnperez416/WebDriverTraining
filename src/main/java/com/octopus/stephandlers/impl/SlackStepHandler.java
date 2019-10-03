package com.octopus.stephandlers.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.octopus.stephandlers.ScreenshotUploader;
import com.octopus.stephandlers.StepHandler;
import com.octopus.utils.SystemPropertyUtils;
import com.octopus.utils.impl.SystemPropertyUtilsImpl;
import io.cucumber.core.api.Scenario;
import lombok.Builder;
import lombok.Getter;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.util.Arrays;
import java.util.Optional;

public class SlackStepHandler implements StepHandler {
    public static final String SLACK_HOOK_URL = "slackHookUrl";
    public static final String SLACK_HANDER_ENABLED = "slackStepHandlerEnabled";
    private static final SystemPropertyUtils SYSTEM_PROPERTY_UTILS = new SystemPropertyUtilsImpl();
    private static final ScreenshotUploader[] SCREENSHOT_UPLOADER = new ScreenshotUploader[]{
            new S3ScreenshotUploader()
    };

    @Override
    public void handleStep(final Scenario scenario) {
        if (!SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(SLACK_HANDER_ENABLED, false)) {
            return;
        }

        if (!SYSTEM_PROPERTY_UTILS.hasProperty(SLACK_HOOK_URL)) {
            System.out.println("The " + SLACK_HOOK_URL +
                    " system property must be defined to return the results via Slack");
            return;
        }

        final Optional<String> imageUrl = Arrays.stream(SCREENSHOT_UPLOADER)
                .map(x -> x.takeAndUploadScreenshot())
                .filter(x -> x.isPresent())
                .map(x -> x.get())
                .findFirst();

        if (!imageUrl.isPresent())
            return;

        try (final CloseableHttpClient client = HttpClients.createDefault()) {
            final SlackMessage message = SlackMessage.builder()
                    .text(SYSTEM_PROPERTY_UTILS.getPropertyNullAsEmpty(STEP_HANDLER_MESSAGE) +
                            " Scenario " + scenario.getName() + " status " + scenario.getStatus().name())
                    .attachments(new Attachments[]{
                            Attachments.builder()
                                    .text(scenario.getName())
                                    .imageUrl(imageUrl.get())
                                    .build()

                    })
                    .build();

            final HttpPost httpPost = new HttpPost(SYSTEM_PROPERTY_UTILS.getProperty(SLACK_HOOK_URL));
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setEntity(new StringEntity(new ObjectMapper().writeValueAsString(message)));
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

@Getter
@Builder
class SlackMessage {
    public String text;
    public Attachments[] attachments;
}

@Getter
@Builder
class Attachments {
    public String text;

    @JsonProperty("image_url")
    public String imageUrl;
}


