package com.octopus;

import com.octopus.pages.ticketmonster.*;
import org.junit.Assert;
import org.junit.Test;

public class TicketMonsterTest {

    private static final AutomatedBrowserFactory AUTOMATED_BROWSER_FACTORY =
            new AutomatedBrowserFactory();

    private static final int WAIT_TIME = 30;

    @Test
    public void purchaseTickets() {

        final AutomatedBrowser automatedBrowser =
                AUTOMATED_BROWSER_FACTORY.getAutomatedBrowser("ChromeNoImplicitWait");

        try {

            automatedBrowser.init();

            automatedBrowser.captureHarFile();

            automatedBrowser.goTo("https://ticket-monster.herokuapp.com");

            automatedBrowser.clickElement("Buy tickets now", WAIT_TIME);

            automatedBrowser.clickElement("Concert", WAIT_TIME);

            automatedBrowser.clickElement("Rock concert of the decade", WAIT_TIME);

            automatedBrowser.selectOptionByTextFromSelect("Toronto : Roy Thomson Hall", "venueSelector", WAIT_TIME);

            automatedBrowser.clickElement("bookButton", WAIT_TIME);

            automatedBrowser.selectOptionByTextFromSelect("A - Premier platinum reserve", "sectionSelect", WAIT_TIME);

            automatedBrowser.populateElement("tickets-1", "2", WAIT_TIME);

            automatedBrowser.clickElement("add", WAIT_TIME);

            automatedBrowser.populateElement("email", "email@example.org", WAIT_TIME);

            automatedBrowser.clickElement("submit", WAIT_TIME);

            final String email = automatedBrowser.getTextFromElement("div.col-md-6:nth-child(1) > div:nth-child(1) > p:nth-child(2)", WAIT_TIME);
            Assert.assertTrue(email.contains("email@example.org"));

            final String event = automatedBrowser.getTextFromElement("div.col-md-6:nth-child(1) > div:nth-child(1) > p:nth-child(3)", WAIT_TIME);
            Assert.assertTrue(event.contains("Rock concert of the decade"));

            final String venue = automatedBrowser.getTextFromElement("div.col-md-6:nth-child(1) > div:nth-child(1) > p:nth-child(4)", WAIT_TIME);
            Assert.assertTrue(venue.contains("Roy Thomson Hall"));
        } finally {
            try {
                automatedBrowser.saveHarFile("ticketmonster.har");
            } finally {
                automatedBrowser.destroy();
            }
        }
    }

    @Test
    public void purchaseTicketsPageObjectModel() {

        final AutomatedBrowser automatedBrowser =
                AUTOMATED_BROWSER_FACTORY.getAutomatedBrowser("ChromeNoImplicitWait");

        try {

            automatedBrowser.init();

            final EventsPage eventsPage = new MainPage(automatedBrowser)
                    .openPage()
                    .buyTickets();

            final VenuePage venuePage = eventsPage
                    .selectEvent("Concert", "Rock concert of the decade");

            final CheckoutPage checkoutPage = venuePage
                    .selectVenue("Toronto : Roy Thomson Hall")
                    .book();

            final ConfirmationPage confirmationPage = checkoutPage
                    .buySectionTickets("A - Premier platinum reserve", 2)
                    .checkout("email@example.org");

            Assert.assertTrue(confirmationPage.getEmail().contains("email@example.org"));
            Assert.assertTrue(confirmationPage.getEvent().contains("Rock concert of the decade"));
            Assert.assertTrue(confirmationPage.getVenue().contains("Roy Thomson Hall"));

        } finally {
            automatedBrowser.destroy();
        }
    }
}