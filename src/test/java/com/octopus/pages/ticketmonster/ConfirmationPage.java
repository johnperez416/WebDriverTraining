package com.octopus.pages.ticketmonster;

import com.octopus.AutomatedBrowser;
import com.octopus.pages.BasePage;

public class ConfirmationPage extends BasePage {

    private static final String EMAIL_ADDRESS = "div.col-md-6:nth-child(1) > div:nth-child(1) > p:nth-child(2)";

    private static final String EVENT_NAME = "div.col-md-6:nth-child(1) > div:nth-child(1) > p:nth-child(3)";

    private static final String VENUE_NAME = "div.col-md-6:nth-child(1) > div:nth-child(1) > p:nth-child(4)";

    public ConfirmationPage(final AutomatedBrowser automatedBrowser) {
        super(automatedBrowser);
    }

    public String getEmail() {
        return automatedBrowser.getTextFromElement(EMAIL_ADDRESS, WAIT_TIME);
    }

    public String getEvent() {
        return automatedBrowser.getTextFromElement(EVENT_NAME, WAIT_TIME);
    }

    public String getVenue() {
        return automatedBrowser.getTextFromElement(VENUE_NAME, WAIT_TIME);
    }
}