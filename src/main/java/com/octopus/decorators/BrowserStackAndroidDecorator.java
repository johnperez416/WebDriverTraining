package com.octopus.decorators;

import com.octopus.AutomatedBrowser;
import com.octopus.decoratorbase.AutomatedBrowserBase;
import org.openqa.selenium.remote.DesiredCapabilities;

public class BrowserStackAndroidDecorator extends AutomatedBrowserBase {
    public BrowserStackAndroidDecorator(final AutomatedBrowser automatedBrowser) {
        super(automatedBrowser);
    }

    @Override
    public DesiredCapabilities getDesiredCapabilities() {

        final DesiredCapabilities caps = getAutomatedBrowser().getDesiredCapabilities();

        caps.setCapability("browserName", "android");
        caps.setCapability("os_version", "9.0");
        caps.setCapability("device", "Samsung Galaxy S8 Plus");
        caps.setCapability("real_mobile", "true");

        return caps;
    }

    @Override
    public void maximizeWindow() {
        // do nothing
    }
}