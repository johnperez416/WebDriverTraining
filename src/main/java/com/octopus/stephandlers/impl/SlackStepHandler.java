package com.octopus.stephandlers.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.octopus.Constants;
import com.octopus.exceptions.NetworkException;
import com.octopus.stephandlers.ScreenshotUploader;
import com.octopus.utils.RetryService;
import com.octopus.utils.SystemPropertyUtils;
import com.octopus.utils.impl.RetryServiceImpl;
import com.octopus.utils.impl.SystemPropertyUtilsImpl;
import cucumber.api.PickleStepTestStep;
import cucumber.api.Result;
import cucumber.api.event.EventListener;
import cucumber.api.event.EventPublisher;
import cucumber.api.event.TestStepFinished;
import lombok.Builder;
import lombok.Getter;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.retry.RetryCallback;

import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Logger;

public class SlackStepHandler implements EventListener {
    private static final Logger LOGGER = Logger.getLogger(SlackStepHandler.class.toString());
    private static final RetryService RETRY_SERVICE = new RetryServiceImpl();
    public static final String SLACK_HOOK_URL = "slackHookUrl";
    public static final String SLACK_HANDLER_ENABLED = "slackStepHandlerEnabled";
    public static final String SLACK_HANDLER_ERROR_ONLY = "slackStepHandlerErrorOnly";
    private static final SystemPropertyUtils SYSTEM_PROPERTY_UTILS = new SystemPropertyUtilsImpl();
    private static final ScreenshotUploader[] SCREENSHOT_UPLOADER = new ScreenshotUploader[]{
            new S3ScreenshotUploader()
    };

    private void handleTestStepFinished(final TestStepFinished event) {
        if (!SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(SLACK_HANDLER_ENABLED, false)) {
            return;
        }

        if (event.result.isOk(false) && SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(SLACK_HANDLER_ERROR_ONLY, false)) {
            return;
        }

        if (event.result.getStatus() == Result.Type.SKIPPED) {
            return;
        }

        if (!SYSTEM_PROPERTY_UTILS.hasProperty(SLACK_HOOK_URL)) {
            LOGGER.info("The " + SLACK_HOOK_URL +
                    " system property must be defined to return the results via Slack");
            return;
        }

        final Optional<String> imageUrl = Arrays.stream(SCREENSHOT_UPLOADER)
                .map(ScreenshotUploader::takeAndUploadScreenshot)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();

        try (final CloseableHttpClient client = HttpClients.createDefault()) {
            final SlackMessage message = SlackMessage
                    .builder()
                    .text(SYSTEM_PROPERTY_UTILS.getPropertyNullAsEmpty(Constants.STEP_HANDLER_MESSAGE, " ") +
                            event.result.getStatus() + " " + getStepName(event))
                    .build();

            imageUrl.ifPresent(s ->
                    message.attachments = new Attachments[]{
                        Attachments
                                .builder()
                                .color(event.result.getStatus() == Result.Type.PASSED ? "good" : "danger")
                                .imageUrl(s)
                                .build()
                    }
            );

            RETRY_SERVICE.getTemplate(
                    SYSTEM_PROPERTY_UTILS.getPropertyAsInt(Constants.SLACK_RETRIES, Constants.DEFAULT_SLACK_RETRIES),
                    SYSTEM_PROPERTY_UTILS.getPropertyAsInt(Constants.SLACK_BACKOFF, Constants.DEFAULT_SLACK_BACKOFF))
                    .execute((RetryCallback<Void, Exception>) context -> {
                final HttpPost httpPost = new HttpPost(SYSTEM_PROPERTY_UTILS.getProperty(SLACK_HOOK_URL));
                httpPost.setHeader("Content-Type", "application/json");
                httpPost.setEntity(new StringEntity(new ObjectMapper().writeValueAsString(message)));
                try (final CloseableHttpResponse response = client.execute(httpPost)) {
                    if (response.getStatusLine().getStatusCode() != 200) {
                        throw new NetworkException("Failed to post to slack - response code was " +
                                response.getStatusLine().getStatusCode());
                    }
                }
                return null;
            });
        } catch (final Exception ex) {
            LOGGER.warning("Failed to send result to Slack. " + ex.toString());
        }
    }

    @Override
    public void setEventPublisher(final EventPublisher publisher) {
        publisher.registerHandlerFor(TestStepFinished.class, this::handleTestStepFinished);
    }

    private String getStepName(final TestStepFinished event) {
        if (event.testStep instanceof PickleStepTestStep) {
            return ((PickleStepTestStep) event.testStep).getStepText();
        }

        return event.testStep.getCodeLocation();
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

    public String color;

    @JsonProperty("image_url")
    public String imageUrl;
}


