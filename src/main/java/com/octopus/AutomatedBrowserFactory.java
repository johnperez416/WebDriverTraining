package com.octopus;

import com.octopus.decorators.*;

/**
 * A factory to create AutomatedBrowser instances with various configurations.
 */
public class AutomatedBrowserFactory {

    /**
     * Return a preconfigured AutomatedBrowser instance.
     *
     * @param browser The name of the AutomatedBrowser to create
     * @return a preconfigured AutomatedBrowser instance
     */
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

        if ("ChromeNoImplicitWaitNoProxy".equalsIgnoreCase(browser)) {
            return getChromeBrowserNoImplicitWaitNoProxy();
        }

        if ("FirefoxNoImplicitWait".equalsIgnoreCase(browser)) {
            return getFirefoxBrowserNoImplicitWait();
        }

        if ("FirefoxNoImplicitWaitNoProxy".equalsIgnoreCase(browser)) {
            return getFirefoxBrowserNoImplicitWaitNoProxy();
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

        if ("ChromeHeadlessNoImplicitWaitLambda".equalsIgnoreCase(browser)) {
            return getChromeBrowserNoImplicitWaitLambda(true);
        }

        if ("ChromeNoImplicitWaitLambda".equalsIgnoreCase(browser)) {
            return getChromeBrowserNoImplicitWaitLambda(false);
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
                                        new HighlightDecorator(
                                                new WebDriverDecorator()
                                        )
                                )
                        )
                )
        );
    }

    private AutomatedBrowser getChromeBrowserNoImplicitWait() {
        return new ChromeDecorator(
                new BrowserMobDecorator(
                        new MouseMovementDecorator(
                                new HighlightDecorator(
                                        new WebDriverDecorator()
                                )
                        )
                )
        );
    }

    private AutomatedBrowser getChromeBrowserNoImplicitWaitNoProxy() {
        return new ChromeDecorator(
            new MouseMovementDecorator(
                    new HighlightDecorator(
                            new WebDriverDecorator()
                    )
            )
        );
    }

    private AutomatedBrowser getFirefoxBrowserNoImplicitWait() {
        return new FirefoxDecorator(
                new BrowserMobDecorator(
                        new MouseMovementDecorator(
                                new HighlightDecorator(
                                        new WebDriverDecorator()
                                )
                        )
                )
        );
    }

    private AutomatedBrowser getFirefoxBrowserNoImplicitWaitNoProxy() {
        return new FirefoxDecorator(
                new MouseMovementDecorator(
                        new HighlightDecorator(
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
                                        new HighlightDecorator(
                                                new WebDriverDecorator()
                                        )
                                )
                        )
                )
        );
    }

    private AutomatedBrowser getBrowserStackEdgeNoImplicitWait() {
        return new BrowserStackDecorator(
                new BrowserStackEdgeDecorator(
                        new MouseMovementDecorator(
                                new HighlightDecorator(
                                        new WebDriverDecorator()
                                )
                        )
                )
        );
    }

    private AutomatedBrowser getBrowserStackAndroid() {
        return new BrowserStackDecorator(
                new BrowserStackAndroidDecorator(
                        new ImplicitWaitDecorator(10,
                                new MouseMovementDecorator(
                                        new HighlightDecorator(
                                                new WebDriverDecorator()
                                        )
                                )
                        )
                )
        );
    }

    private AutomatedBrowser getBrowserStackAndroidNoImplicitWait() {
        return new BrowserStackDecorator(
                new BrowserStackAndroidDecorator(
                        new MouseMovementDecorator(
                                new HighlightDecorator(
                                        new WebDriverDecorator()
                                )
                        )
                )
        );
    }

    private AutomatedBrowser getChromeBrowserNoImplicitWaitLambda(final boolean headless) {
        return new ChromeLambdaDecorator(headless,
                new MouseMovementDecorator(
                        new HighlightDecorator(
                                new WebDriverDecorator()
                        )
                )
        );
    }
}