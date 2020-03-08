package com.octopus.decorators;

import com.octopus.AutomatedBrowser;
import com.octopus.decoratorbase.AutomatedBrowserBase;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * A decorator to configure a Browser Stack Windows Edge session.
 */
public class BrowserStackEdgeDecorator extends AutomatedBrowserBase {

    /**
     * Decorator constructor.
     *
     * @param automatedBrowser The AutomatedBrowser to wrap up.
     */
    public BrowserStackEdgeDecorator(final AutomatedBrowser automatedBrowser) {
        super(automatedBrowser);
    }

    @Override
    public DesiredCapabilities getDesiredCapabilities() {
        final DesiredCapabilities caps = getAutomatedBrowser().getDesiredCapabilities();

        caps.setCapability("os", "Windows");
        caps.setCapability("os_version", "10");
        caps.setCapability("browser", "Edge");
        caps.setCapability("browser_version", "insider preview");
        caps.setCapability("browserstack.local", "false");
        caps.setCapability("browserstack.selenium_version", "3.7.0");
        return caps;
    }
}