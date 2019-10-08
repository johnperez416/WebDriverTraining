package com.octopus.decorators;

import com.octopus.AutomatedBrowser;
import com.octopus.decoratorbase.AutomatedBrowserBase;
import java.util.concurrent.TimeUnit;

public class ImplicitWaitDecorator extends AutomatedBrowserBase {

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