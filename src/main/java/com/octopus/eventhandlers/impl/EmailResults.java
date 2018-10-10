package com.octopus.eventhandlers.impl;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import com.octopus.eventhandlers.EventHandler;

public class EmailResults implements EventHandler {
    private final String to;
    private final String results;

    public EmailResults(final String to, final String results) {
        this.to = to;
        this.results = results;
    }

    @Override
    public void finished(final String id, final boolean status) {
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
                                    .withCharset("UTF-8").withData((status ? "SUCCEEDED" : "FAILED") + " WebDriver Test Results ID " + id)))
                    .withSource("admin@matthewcasperson.com");
            client.sendEmail(request);
        } catch (final Exception ex) {
            System.out.println("The email was not sent. Error message: " + ex.getMessage());
        }
    }
}
