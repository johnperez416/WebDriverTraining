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
            return getChromeBrowserNoImplicitWait();
        }

        if ("FirefoxNoImplicitWait".equalsIgnoreCase(browser)) {
            return getFirefoxBrowserNoImplicitWait();
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

        if ("ChromeNoImplicitWaitLambda".equalsIgnoreCase(browser)) {
            return getChromeBrowserNoImplicitWaitLambda();
        }

        throw new IllegalArgumentException("Unknown browser " + browser);

    }

    private AutomatedBrowser getChromeBrowser(final boolean headless) {
        return new ChromeDecorator(headless,
                new ImplicitWaitDecorator(10,
                        new BrowserMobDecorator(
                                new MouseMovementDecorator(
                                        new HighlightDecorator(
                                            new WebDriverDecorator()
                                        )
                                )
                        )
                )
        );
    }

    private AutomatedBrowser getFirefoxBrowser(final boolean headless) {
        return new FirefoxDecorator(headless,
                new ImplicitWaitDecorator(10,
                        new BrowserMobDecorator(
                                new MouseMovementDecorator(
                                        new WebDriverDecorator()
                                )
                        )
                )
        );
    }

    private AutomatedBrowser getChromeBrowserNoImplicitWait() {
        return new ChromeDecorator(
                new BrowserMobDecorator(
                        new MouseMovementDecorator(
                                new WebDriverDecorator()
                        )
                )
        );
    }

    private AutomatedBrowser getFirefoxBrowserNoImplicitWait() {
        return new FirefoxDecorator(
                new BrowserMobDecorator(
                        new MouseMovementDecorator(
                                new WebDriverDecorator()
                        )
                )
        );
    }

    private AutomatedBrowser getBrowserStackEdge() {
        return new BrowserStackDecorator(
                new BrowserStackEdgeDecorator(
                        new ImplicitWaitDecorator(10,
                                new MouseMovementDecorator(
                                        new WebDriverDecorator()
                                )
                        )
                )
        );
    }

    private AutomatedBrowser getBrowserStackEdgeNoImplicitWait() {
        return new BrowserStackDecorator(
                new BrowserStackEdgeDecorator(
                        new MouseMovementDecorator(
                                new WebDriverDecorator()
                        )
                )
        );
    }

    private AutomatedBrowser getBrowserStackAndroid() {
        return new BrowserStackDecorator(
                new BrowserStackAndroidDecorator(
                        new ImplicitWaitDecorator(10,
                                new MouseMovementDecorator(
                                        new WebDriverDecorator()
                                )
                        )
                )
        );
    }

    private AutomatedBrowser getBrowserStackAndroidNoImplicitWait() {
        return new BrowserStackDecorator(
                new BrowserStackAndroidDecorator(
                        new MouseMovementDecorator(
                                new WebDriverDecorator()
                        )
                )
        );
    }

    private AutomatedBrowser getChromeBrowserNoImplicitWaitLambda() {
        return new ChromeHeadlessLambdaDecorator(
                new MouseMovementDecorator(
                        new WebDriverDecorator()
                )
        );
    }
}