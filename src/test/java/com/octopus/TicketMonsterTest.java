package com.octopus;

import com.kevinmost.junit_retry_rule.Retry;
import com.octopus.pages.ticketmonster.CheckoutPage;
import com.octopus.pages.ticketmonster.ConfirmationPage;
import com.octopus.pages.ticketmonster.EventsPage;
import com.octopus.pages.ticketmonster.MainPage;
import com.octopus.pages.ticketmonster.VenuePage;
import io.vavr.control.Try;
import org.junit.Assert;
import org.junit.Test;

public class TicketMonsterTest {

    private static final String BROWSER = "FirefoxNoImplicitWaitNoProxy";
    private static final AutomatedBrowserFactory AUTOMATED_BROWSER_FACTORY =
            new AutomatedBrowserFactory();

    private static final int WAIT_TIME = 30;

    @Test
    @Retry
    public void purchaseTickets() {

        final AutomatedBrowser automatedBrowser =
                AUTOMATED_BROWSER_FACTORY.getAutomatedBrowser(BROWSER);

        try {

            automatedBrowser.init();

            automatedBrowser.captureHarFile();

            automatedBrowser.goTo("https://ticket-monster.herokuapp.com");

            Try.run(() -> Thread.sleep(10000));

            automatedBrowser.clickElement("Buy tickets now", WAIT_TIME);

            automatedBrowser.clickElement("Concert", WAIT_TIME);

            automatedBrowser.clickElement("Rock concert of the decade", WAIT_TIME);

            automatedBrowser.selectOptionByTextFromSelect("", "Toronto : Roy Thomson Hall", "venueSelector", WAIT_TIME);

            automatedBrowser.clickElement("bookButton", WAIT_TIME);

            automatedBrowser.selectOptionByTextFromSelect("","A - Premier platinum reserve", "sectionSelect", WAIT_TIME);

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
    @Retry
    public void purchaseTicketsPageObjectModel() {

        final AutomatedBrowser automatedBrowser =
                AUTOMATED_BROWSER_FACTORY.getAutomatedBrowser(BROWSER);

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

    @Test
    @Retry
    public void verifyPricesPageObjectModel() {

        final AutomatedBrowser automatedBrowser =
                AUTOMATED_BROWSER_FACTORY.getAutomatedBrowser(BROWSER);

        try {
            automatedBrowser.init();

            final EventsPage eventsPage = new MainPage(automatedBrowser)
                    .openPage()
                    .buyTickets();

            final VenuePage venuePage = eventsPage
                    .selectEvent("Theatre", "Shane's Sock Puppets");

            final CheckoutPage checkoutPage = venuePage
                    .selectVenue("Toronto : Roy Thomson Hall")
                    .book();

            checkoutPage.selectSection("A - Premier platinum reserve");

            final float platinumAdultPrices = checkoutPage.getSectionAdultPrices();
            Assert.assertTrue(platinumAdultPrices > 10);
            Assert.assertTrue(platinumAdultPrices < 1000);

            checkoutPage.selectSection("B - Premier gold reserve");

            final float goldAdultPrices = checkoutPage.getSectionAdultPrices();
            Assert.assertTrue(goldAdultPrices > 10);
            Assert.assertTrue(goldAdultPrices < 1000);

            checkoutPage.selectSection("C - Premier silver reserve");

            final float silverAdultPrices = checkoutPage.getSectionAdultPrices();
            Assert.assertTrue(silverAdultPrices > 10);
            Assert.assertTrue(silverAdultPrices < 1000);

            checkoutPage.selectSection("D - General");

            final float generalAdultPrices = checkoutPage.getSectionAdultPrices();
            Assert.assertTrue(generalAdultPrices > 10);
            Assert.assertTrue(generalAdultPrices < 1000);
        } finally {
            automatedBrowser.destroy();
        }
    }
}