package com.octopus.pages.ticketmonster;

import com.octopus.AutomatedBrowser;

import com.octopus.pages.BasePage;

public class CheckoutPage extends BasePage {

    private static final String SECTION_DROP_DOWN_LIST = "sectionSelect";
    private static final String ADULT_TICKET_COUNT = "tickets-1";
    private static final String ADD_TICKETS_BUTTON = "add";
    private static final String EMAIL_ADDRESS = "email";
    private static final String CHECKOUT_BUTTON = "submit";

    public CheckoutPage(final AutomatedBrowser automatedBrowser) {

        super(automatedBrowser);

    }

    public CheckoutPage buySectionTickets(final String section, final
    Integer adultCount) {
        automatedBrowser.selectOptionByTextFromSelect(section, SECTION_DROP_DOWN_LIST, WAIT_TIME);
        automatedBrowser.populateElement(ADULT_TICKET_COUNT, adultCount.toString(), WAIT_TIME);
        automatedBrowser.clickElement(ADD_TICKETS_BUTTON, WAIT_TIME);
        return this;
    }

    public ConfirmationPage checkout(final String email) {
        automatedBrowser.populateElement(EMAIL_ADDRESS, email, WAIT_TIME);
        automatedBrowser.clickElement(CHECKOUT_BUTTON, WAIT_TIME);
        return new ConfirmationPage(automatedBrowser);
    }
}