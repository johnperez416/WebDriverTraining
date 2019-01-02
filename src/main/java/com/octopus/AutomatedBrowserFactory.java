package com.octopus;

import com.octopus.decorators.*;

public class AutomatedBrowserFactory {

    public AutomatedBrowser getAutomatedBrowser(final String browser) {

        if ("Chrome".equalsIgnoreCase(browser)) {
            return getChromeBrowser(false);
        }

        if ("ChromeHeadless".equalsIgnoreCase(browser)) {
            return getChromeBrowser(true);
        }


        if ("Firefox".equalsIgnoreCase(browser)) {
            return getFirefoxBrowser(false);
        }

        if ("FirefoxHeadless".equalsIgnoreCase(browser)) {
            return getFirefoxBrowser(true);
        }

        if ("ChromeNoImplicitWait".equalsIgnoreCase(browser)) {
            return getChromeBrowserNoImplicitWait(false);
        }

        if ("ChromeHeadlessNoImplicitWait".equalsIgnoreCase(browser)) {
            return getChromeBrowserNoImplicitWait(true);
        }

        if ("FirefoxNoImplicitWait".equalsIgnoreCase(browser)) {
            return getFirefoxBrowserNoImplicitWait(false);
        }

        if ("FirefoxHeadlessNoImplicitWait".equalsIgnoreCase(browser)) {
            return getFirefoxBrowserNoImplicitWait(true);
        }

        if ("BrowserStackEdge".equalsIgnoreCase(browser)) {
            return getBrowserStackEdge();
        }

        if ("BrowserStackEdgeNoImplicitWait".equalsIgnoreCase(browser)) {
            return getBrowserStackEdgeNoImplicitWait();
        }

        if ("BrowserStackAndroid".equalsIgnoreCase(browser)) {
            return getBrowserStackAndroid();
        }

        if ("BrowserStackAndroidNoImplicitWait".equalsIgnoreCase(browser)) {
            return getBrowserStackAndroidNoImplicitWait();
        }

        if ("ChromeNoImplicitWaitLambda".equalsIgnoreCase(browser) ||
                "ChromeNoImplicitWaitDocker".equalsIgnoreCase(browser)) {
            return getChromeBrowserNoImplicitWaitLambda();
        }

        throw new IllegalArgumentException("Unknown browser " + browser);

    }

    private AutomatedBrowser getChromeBrowser(final boolean headless) {
        return new ChromeDecorator(headless,
                new ImplicitWaitDecorator(10,
                        new BrowserMobDecorator(
                                new WebDriverDecorator()
                        )
                )
        );
    }

    private AutomatedBrowser getFirefoxBrowser(final boolean headless) {
        return new FirefoxDecorator(headless,
                new ImplicitWaitDecorator(10,
                        new BrowserMobDecorator(
                                new TimedInteractionDecorator(
                                    new WebDriverDecorator()
                                )
                        )
                )
        );
    }

    private AutomatedBrowser getChromeBrowserNoImplicitWait(final boolean headless) {
        return new ChromeDecorator(headless,
                new BrowserMobDecorator(
                        new TimedInteractionDecorator(
                            new WebDriverDecorator()
                        )
                )
        );
    }

    private AutomatedBrowser getFirefoxBrowserNoImplicitWait(final boolean headless) {
        return new FirefoxDecorator(headless,
                new BrowserMobDecorator(
                        new TimedInteractionDecorator(
                            new WebDriverDecorator()
                        )
                )
        );
    }

    private AutomatedBrowser getBrowserStackEdge() {
        return new BrowserStackDecorator(
                new BrowserStackEdgeDecorator(
                        new ImplicitWaitDecorator(10,
                                new TimedInteractionDecorator(
                                    new WebDriverDecorator()
                                )
                        )
                )
        );
    }

    private AutomatedBrowser getBrowserStackEdgeNoImplicitWait() {
        return new BrowserStackDecorator(
                new BrowserStackEdgeDecorator(
                        new TimedInteractionDecorator(
                            new WebDriverDecorator()
                        )
                )
        );
    }

    private AutomatedBrowser getBrowserStackAndroid() {
        return new BrowserStackDecorator(
                new BrowserStackAndroidDecorator(
                        new ImplicitWaitDecorator(10,
                                new TimedInteractionDecorator(
                                    new WebDriverDecorator()
                                )
                        )
                )
        );
    }

    private AutomatedBrowser getBrowserStackAndroidNoImplicitWait() {
        return new BrowserStackDecorator(
                new BrowserStackAndroidDecorator(
                        new TimedInteractionDecorator(
                            new WebDriverDecorator()
                        )
                )
        );
    }

    private AutomatedBrowser getChromeBrowserNoImplicitWaitLambda() {
        return new ChromeHeadlessLambdaDecorator(
                new TimedInteractionDecorator(
                    new WebDriverDecorator()
                )
        );
    }
}