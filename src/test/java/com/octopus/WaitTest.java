package com.octopus;

import java.net.URISyntaxException;
import org.junit.Test;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;

public class WaitTest {

    private static final AutomatedBrowserFactory AUTOMATED_BROWSER_FACTORY = new AutomatedBrowserFactory();

    @Test
    public void clickDynamicElement() throws URISyntaxException, InterruptedException {
        final AutomatedBrowser automatedBrowser = AUTOMATED_BROWSER_FACTORY.getAutomatedBrowser("Firefox");

        try {
            automatedBrowser.init();
            automatedBrowser.goTo(FormTest.class.getResource("/form.html").toURI().toString());
            automatedBrowser.clickElementWithId("newdiv_element");
        } finally {
            automatedBrowser.destroy();
        }
    }

    @Test
    public void clickDynamicElementWithExplicitWait() throws URISyntaxException {
        final AutomatedBrowser automatedBrowser =
                AUTOMATED_BROWSER_FACTORY.getAutomatedBrowser("Firefox");

        try {
            automatedBrowser.init();
            automatedBrowser.goTo(FormTest.class.getResource("/form.html").toURI().toString());
            automatedBrowser.clickElementWithId("newdiv_element", 10);

        } finally {
            automatedBrowser.destroy();
        }
    }

    @Test(expected = WebDriverException.class)
    public void clickHiddenElement() throws URISyntaxException {

        final AutomatedBrowser automatedBrowser =
                AUTOMATED_BROWSER_FACTORY.getAutomatedBrowser("Firefox");

        try {
            automatedBrowser.init();
            automatedBrowser.goTo(FormTest.class.getResource("/form.html").toURI().toString());
            automatedBrowser.clickElementWithId("div3_element");
        } finally {
            automatedBrowser.destroy();
        }
    }

    @Test
    public void clickHiddenElementWithExplicitWait() throws URISyntaxException {
        final AutomatedBrowser automatedBrowser =
                AUTOMATED_BROWSER_FACTORY.getAutomatedBrowser("Firefox");

        try {
            automatedBrowser.init();
            automatedBrowser.goTo(FormTest.class.getResource("/form.html").toURI().toString());
            automatedBrowser.clickElementWithId("div3_element", 10);
        } finally {
            automatedBrowser.destroy();
        }
    }

    @Test(expected = TimeoutException.class)
    public void mixedTestShortExplicitWait() throws URISyntaxException {

        final AutomatedBrowser automatedBrowser = AUTOMATED_BROWSER_FACTORY.getAutomatedBrowser("Firefox");

        final long start = System.currentTimeMillis();

        try {
            automatedBrowser.init();
            automatedBrowser.goTo(WaitTest.class.getResource("/form.html").toURI().toString());
            automatedBrowser.clickElementWithId("This ID does not exist", 1);
        } finally {
            automatedBrowser.destroy();
            final long duration = System.currentTimeMillis() - start;
            System.out.println("Duration: " + duration / 1000);
        }
    }

    @Test(expected = TimeoutException.class)
    public void mixedTestLongExplicitWait() throws URISyntaxException {

        final AutomatedBrowser automatedBrowser = AUTOMATED_BROWSER_FACTORY.getAutomatedBrowser("Firefox");

        final long start = System.currentTimeMillis();

        try {
            automatedBrowser.init();
            automatedBrowser.goTo(WaitTest.class.getResource("/form.html").toURI().toString());
            automatedBrowser.clickElementWithId("This ID does not exist", 20);
        } finally {
            automatedBrowser.destroy();
            final long duration = System.currentTimeMillis() - start;
            System.out.println("Duration: " + duration / 1000);
        }
    }

    @Test
    public void shortExplicitWaitForDynamicElement() throws URISyntaxException {

        final AutomatedBrowser automatedBrowser = AUTOMATED_BROWSER_FACTORY.getAutomatedBrowser("Firefox");

        final long start = System.currentTimeMillis();

        try {
            automatedBrowser.init();
            automatedBrowser.goTo(WaitTest.class.getResource("/form.html").toURI().toString());
            automatedBrowser.clickElementWithId("newdiv_element", 2);
        } finally {
            automatedBrowser.destroy();
            final long duration = System.currentTimeMillis() - start;
            System.out.println("Duration: " + duration / 1000);
        }
    }

    @Test(expected = TimeoutException.class)
    public void shortExplicitWaitNoImplicitWaitForDynamicElement() throws URISyntaxException {
        final AutomatedBrowser automatedBrowser = AUTOMATED_BROWSER_FACTORY.getAutomatedBrowser("FirefoxNoImplicitWait");
        final long start = System.currentTimeMillis();

        try {
            automatedBrowser.init();
            automatedBrowser.goTo(WaitTest.class.getResource("/form.html").toURI().toString());
            automatedBrowser.clickElementWithId("newdiv_element", 2);
        } finally {
            automatedBrowser.destroy();
            final long duration = System.currentTimeMillis() - start;
            System.out.println("Duration: " + duration / 1000);
        }
    }
}