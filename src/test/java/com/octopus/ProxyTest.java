package com.octopus;

import org.junit.Assert;
import org.junit.Test;

import java.net.URISyntaxException;

public class ProxyTest {
    private static final AutomatedBrowserFactory AUTOMATED_BROWSER_FACTORY = new AutomatedBrowserFactory();

    @Test
    public void modifyRequests() {
        final AutomatedBrowser automatedBrowser =
                AUTOMATED_BROWSER_FACTORY.getAutomatedBrowser("Firefox");

        automatedBrowser.init();
        automatedBrowser.blockRequestTo(".*?\\.png", 201);
        automatedBrowser.blockRequestTo("https://.*?twitter\\.com/", 500);
        automatedBrowser.goTo("https://octopus.com/");
    }

    @Test
    public void mockRequests() throws URISyntaxException {
        final AutomatedBrowser automatedBrowser = AUTOMATED_BROWSER_FACTORY.getAutomatedBrowser("Firefox");

        try {
            automatedBrowser.init();

            automatedBrowser.alterResponseFrom(
                    ".*?raw\\.githubusercontent\\.com.*",
                    200,
                    "{\"query\":{\"count\":1,\"created\":\"2018-04-28T05:20:30Z\",\"lang\":\"en-US\",\"results\":{\"channel\":{\"astronomy\":{\"sunset\":\"7:00 pm\"}}}}}");

            automatedBrowser.goTo(FormTest.class.getResource("/apitest.html").toURI().toString());

            final String sunset = automatedBrowser.getTextFromElementWithId("sunset", 60);
            Assert.assertTrue(sunset, sunset.contains("7:00 pm"));
        } finally {
            automatedBrowser.destroy();
        }
    }

    @Test
    public void mockRequests2() throws URISyntaxException {
        final AutomatedBrowser automatedBrowser = AUTOMATED_BROWSER_FACTORY.getAutomatedBrowser("Firefox");

        try {
            automatedBrowser.init();

            automatedBrowser.alterResponseFrom(
                    ".*?raw\\.githubusercontent\\.com.*",
                    200,
                    "{\"query\":{\"count\":1,\"created\":\"2018-04-28T05:20:30Z\",\"lang\":\"en-US\",\"results\":{\"channel\":{\"astronomy\":{\"sunset\":\"7:4 pm\"}}}}}");

            automatedBrowser.goTo(FormTest.class.getResource("/apitest.html").toURI().toString());

            final String sunset = automatedBrowser.getTextFromElementWithId("sunset", 60);
            Assert.assertTrue(sunset, sunset.contains("7:4 pm"));
        } finally {
            automatedBrowser.destroy();
        }
    }
}
