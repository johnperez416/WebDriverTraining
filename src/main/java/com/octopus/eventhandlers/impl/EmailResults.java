package com.octopus.eventhandlers.impl;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import com.octopus.eventhandlers.EventHandler;

import java.util.Map;

public class EmailResults implements EventHandler {
    private static final String EMAIL_CLIENT_REGION = "Email-Client-Region";
    private static final String EMAIL_TO = "Email-To";
    private static final String EMAIL_FROM = "Email-From";

    @Override
    public void finished(final String id, final boolean status, final String content, final Map<String, String> headers) {
        if (!(headers.containsKey(EMAIL_TO) &&
                headers.containsKey(EMAIL_FROM) &&
                headers.containsKey(EMAIL_CLIENT_REGION))) {
            System.out.println("The " + EMAIL_TO + ", " + EMAIL_FROM + " and " + EMAIL_CLIENT_REGION +
                    " headers must be defined to send an email report.");
            return;
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
                                            .withCharset("UTF-8").withData(content)))
                            .withSubject(new Content()
                                    .withCharset("UTF-8").withData((status ? "SUCCEEDED" : "FAILED") + " WebDriver Test Results ID " + id)))
                    .withSource(headers.get(EMAIL_FROM));
            client.sendEmail(request);
        } catch (final Exception ex) {
            System.out.println("The email was not sent. Error message: " + ex.getMessage());
        }
    }
}
