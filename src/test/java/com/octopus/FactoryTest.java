package com.octopus;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

@RunWith(Parameterized.class)
public class FactoryTest {

    private static final AutomatedBrowserFactory AUTOMATED_BROWSER_FACTORY
            = new AutomatedBrowserFactory();

    private String browser;

    public FactoryTest(final String browser) {
        this.browser = browser;
    }

    @Parameterized.Parameters
    public static Iterable data() {
        return Arrays.asList(
                "FirefoxNoImplicitWait",
                "Firefox"
        );
    }

    @Test
    public void openURL() {
        final AutomatedBrowser automatedBrowser =
                AUTOMATED_BROWSER_FACTORY.getAutomatedBrowser(browser);
        automatedBrowser.init();
        automatedBrowser.goTo("https://google.com/");
        automatedBrowser.destroy();
    }
}