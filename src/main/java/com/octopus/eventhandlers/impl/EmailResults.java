package com.octopus.eventhandlers.impl;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.octopus.eventhandlers.EventHandler;
import java.util.Map;

public class EmailResults implements EventHandler {
    /**
     * The header defining the region from which to send the SMS message.
     */
    private static final String EMAIL_CLIENT_REGION = "Email-Client-Region";
    /**
     * The header defining the email to address.
     */
    private static final String EMAIL_TO = "Email-To";
    /**
     * The header defining the email from address.
     */
    private static final String EMAIL_FROM = "Email-From";

    @Override
    public Map<String, String> finished(final String id,
                                        final boolean status,
                                        final String featureFile,
                                        final String txtOutput,
                                        final String htmlOutputDir,
                                        final Map<String, String> headers,
                                        final Map<String, String> previousResults) {
        if (!(headers.containsKey(EMAIL_TO)
                && headers.containsKey(EMAIL_FROM)
                && headers.containsKey(EMAIL_CLIENT_REGION))) {
            System.out.println("The " + EMAIL_TO + ", " + EMAIL_FROM + " and " + EMAIL_CLIENT_REGION
                    + " headers must be defined to send an email report.");
            return previousResults;
        }

        try {
            final AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard()
                    .withRegion(headers.get(EMAIL_CLIENT_REGION)).build();

            final SendEmailRequest request = new SendEmailRequest()
                    .withDestination(new Destination()
                            .withToAddresses(headers.get(EMAIL_TO)))
                    .withMessage(new Message()
                            .withBody(new Body()
                                    .withText(new Content()
                                            .withCharset("UTF-8").withData(txtOutput)))
                            .withSubject(new Content()
                                    .withCharset("UTF-8").withData((status ? "SUCCEEDED" : "FAILED") + " WebDriver Test Results ID " + id)))
                    .withSource(headers.get(EMAIL_FROM));
            client.sendEmail(request);
        } catch (final Exception ex) {
            System.out.println("The email was not sent. Error message: " + ex.getMessage());
        }

        return previousResults;
    }
}
