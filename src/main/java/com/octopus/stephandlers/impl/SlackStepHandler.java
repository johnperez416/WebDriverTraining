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
import io.cucumber.plugin.EventListener;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.plugin.event.Status;
import io.cucumber.plugin.event.TestStepFinished;
import io.vavr.control.Try;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import lombok.Builder;
import lombok.Getter;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.retry.RetryCallback;

/**
 * A step handler to generate a slack message after each step.
 */
public class SlackStepHandler implements EventListener {
    /**
     * The system property that defines the Slack hook URL.
     */
    public static final String SLACK_HOOK_URL = "slackHookUrl";
    /**
     * The system property that defines if this step handler is enabled.
     */
    public static final String SLACK_HANDLER_ENABLED = "slackStepHandlerEnabled";
    /**
     * The system property that defines if this step handler is enabled only for errors.
     */
    public static final String SLACK_HANDLER_ERROR_ONLY = "slackStepHandlerErrorOnly";
    /**
     * The shared Logger instance.
     */
    private static final Logger LOGGER = Logger.getLogger(SlackStepHandler.class.toString());
    /**
     * An executor to send messages serially, but in background threads.
     */
    private static final Executor EXECUTOR = Executors.newSingleThreadExecutor();
    /**
     * The shared RetryServiceImpl instance.
     */
    private static final RetryService RETRY_SERVICE = new RetryServiceImpl();
    /**
     * The shared SystemPropertyUtilsImpl instance.
     */
    private static final SystemPropertyUtils SYSTEM_PROPERTY_UTILS = new SystemPropertyUtilsImpl();
    /**
     * A list of known screenshot uploaders.
     */
    private static final ScreenshotUploader[] SCREENSHOT_UPLOADER = new ScreenshotUploader[]{
            new S3ScreenshotUploader()
    };

    private void handleTestStepFinished(final TestStepFinished event) {
        if (!SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(SLACK_HANDLER_ENABLED, false)) {
            return;
        }

        if (event.getResult().getStatus().isOk(false) && SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(SLACK_HANDLER_ERROR_ONLY, false)) {
            return;
        }

        if (event.getResult().getStatus() == Status.SKIPPED) {
            return;
        }

        if (!SYSTEM_PROPERTY_UTILS.hasProperty(SLACK_HOOK_URL)) {
            LOGGER.info("The " + SLACK_HOOK_URL
                    + " system property must be defined to return the results via Slack");
            return;
        }

        // Run through all the enabled options for uploading screenshots, and return the first successful image upload
        final Optional<CompletableFuture<String>> imageUrlFuture = Arrays.stream(SCREENSHOT_UPLOADER)
                // take the screenshot and get the future
                .map(ScreenshotUploader::takeAndUploadScreenshot)
                // only accept results that resulted in a future
                .filter(Optional::isPresent)
                // get the future
                .map(Optional::get)
                // get the first success
                .findFirst();

        // Post to slack from a thread to prevent any video recording from being held up
        CompletableFuture.supplyAsync(() -> {

            // get the value from the future, wrapping any exceptions
            final Optional<String> imageUrl = imageUrlFuture.map(s -> Try.of(s::get))
                    // ignore any failed attempts to get the value
                    .filter(Try::isSuccess)
                    // get the value
                    .map(Try::get);

            final Optional<Throwable> imageFailure = imageUrlFuture.map(s -> Try.of(s::get))
                    // ignore any failed attempts to get the value
                    .filter(Try::isFailure)
                    // get the value
                    .map(Try::getCause);

            try (final CloseableHttpClient client = HttpClients.createDefault()) {
                final SlackMessage message = SlackMessage
                        .builder()
                        .text(SYSTEM_PROPERTY_UTILS.getPropertyNullAsEmpty(Constants.STEP_HANDLER_MESSAGE, " ")
                                + event.getResult().getStatus() +
                                " " +
                                getStepName(event) +
                                Optional.ofNullable(event.getResult().getError())
                                        .map(e -> " " + e.toString())
                                        .orElse(""))
                        .build();

                imageUrl.ifPresentOrElse(s ->
                        // attach the image if it exists
                                message.attachments = new Attachments[]{
                                        Attachments
                                                .builder()
                                                .color(event.getResult().getStatus() == Status.PASSED ? "good" : "danger")
                                                .imageUrl(s)
                                                .build()
                                }
                        ,
                        // attach the reason why we have no image if such a failure exists
                        () -> imageFailure.ifPresent(s ->
                                message.attachments = new Attachments[]{
                                        Attachments
                                                .builder()
                                                .color(event.getResult().getStatus() == Status.PASSED ? "warning" : "danger")
                                                .text(s.toString())
                                                .build()

                                }
                        )
                );


                return RETRY_SERVICE.getTemplate(
                        SYSTEM_PROPERTY_UTILS.getPropertyAsInt(Constants.SLACK_RETRIES, Constants.DEFAULT_SLACK_RETRIES),
                        SYSTEM_PROPERTY_UTILS.getPropertyAsInt(Constants.SLACK_BACKOFF, Constants.DEFAULT_SLACK_BACKOFF))
                        .execute((RetryCallback<Void, Exception>) context -> {
                            final HttpPost httpPost = new HttpPost(SYSTEM_PROPERTY_UTILS.getProperty(SLACK_HOOK_URL));
                            httpPost.setHeader("Content-Type", "application/json");
                            httpPost.setEntity(new StringEntity(new ObjectMapper().writeValueAsString(message)));
                            try (final CloseableHttpResponse response = client.execute(httpPost)) {
                                if (response.getStatusLine().getStatusCode() != 200) {
                                    throw new NetworkException("Slack response code was "
                                            + response.getStatusLine().getStatusCode());
                                }
                            }
                            return null;
                        });
            } catch (final Exception ex) {
                LOGGER.warning("Failed to send post to Slack. " + ex.toString());
                return null;
            }
        }, EXECUTOR);
    }

    @Override
    public void setEventPublisher(final EventPublisher publisher) {
        publisher.registerHandlerFor(TestStepFinished.class, this::handleTestStepFinished);
    }

    private String getStepName(final TestStepFinished event) {
        if (event.getTestStep() instanceof PickleStepTestStep) {
            return ((PickleStepTestStep) event.getTestStep()).getStep().getText();
        }

        return event.getTestStep().getCodeLocation();
    }
}

@Getter
@Builder
class SlackMessage {
    /**
     * The message text.
     */
    public String text;
    /**
     * The message attachments (like images).
     */
    public Attachments[] attachments;
}

@Getter
@Builder
class Attachments {
    /**
     * The attachment text.
     */
    public String text;

    /**
     * The attachment color.
     */
    public String color;

    /**
     * The attachment image url.
     */
    @JsonProperty("image_url")
    public String imageUrl;
}


