package com.octopus.pages.ticketmonster;

import com.octopus.AutomatedBrowser;

import com.octopus.pages.BasePage;

public class MainPage extends BasePage {

    private static final String URL =
            "https://ticket-monster.herokuapp.com";

    private static final String BUY_TICKETS_NOW = "Buy tickets now";

    public MainPage(final AutomatedBrowser automatedBrowser) {

        super(automatedBrowser);

    }

    public MainPage openPage() {
        automatedBrowser.goTo(URL);
        return this;
    }

    public EventsPage buyTickets() {
        automatedBrowser.clickElement(BUY_TICKETS_NOW, WAIT_TIME);
        return new EventsPage(automatedBrowser);
    }
}