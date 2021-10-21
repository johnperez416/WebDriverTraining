package com.octopus.decorators;

import com.octopus.AutomatedBrowser;
import com.octopus.decoratorbase.AutomatedBrowserBase;
import java.util.concurrent.TimeUnit;

/**
 * A decorator that adds implicit waits to WebDriver commands. This must not be used with the simple element
 * locators, as implicit waits conflict with the explicit waits used by the simple locators.
 */
public class ImplicitWaitDecorator extends AutomatedBrowserBase {

    /**
     * How long to wait for elements to be available.
     */
    private final int waitTime;

    public ImplicitWaitDecorator(final int waitTime, final AutomatedBrowser automatedBrowser) {
        super(automatedBrowser);
        this.waitTime = waitTime;
    }

    @Override
    public void init() {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser()
                    .getWebDriver()
                    .manage()
                    .timeouts()
                    .implicitlyWait(waitTime, TimeUnit.SECONDS);

            getAutomatedBrowser().init();
        }
    }
}