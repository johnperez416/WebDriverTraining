package com.octopus.pages.ticketmonster;

import com.octopus.AutomatedBrowser;

import com.octopus.pages.BasePage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckoutPage extends BasePage {

    private static final String SECTION_DROP_DOWN_LIST = "sectionSelect";
    private static final String ADULT_TICKET_COUNT = "tickets-1";
    private static final String ADD_TICKETS_BUTTON = "add";
    private static final String EMAIL_ADDRESS = "email";
    private static final String CHECKOUT_BUTTON = "submit";
    private static final Pattern TICKET_PRICE_REGEX = Pattern.compile("@ \\$(\\d+\\.\\d+)");
    private static final String ADULT_TICKET_PRICE = ".input-group-addon";


    public CheckoutPage(final AutomatedBrowser automatedBrowser) {
        super(automatedBrowser);
    }

    public CheckoutPage selectSection(final String section) {
        automatedBrowser.selectOptionByTextFromSelect(section, SECTION_DROP_DOWN_LIST, WAIT_TIME);
        return this;
    }

    public float getSectionAdultPrices() {
        final String price = automatedBrowser.getTextFromElement(ADULT_TICKET_PRICE);
        return getPriceFromTicketPriceString(price);
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

    private float getPriceFromTicketPriceString(final String input) {
        final Matcher matcher = TICKET_PRICE_REGEX.matcher(input);
        if (matcher.matches()) {
            final String priceString = matcher.group(1);
            return Float.parseFloat(priceString);
        }

        throw new IllegalArgumentException("String " + input + " does not match the regex");
    }
}