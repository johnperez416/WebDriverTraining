package com.octopus.decorators;

import com.octopus.AutomatedBrowser;
import com.octopus.decoratorbase.AutomatedBrowserBase;
import com.octopus.exceptions.ConfigurationException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.net.MalformedURLException;

import java.net.URL;

public class BrowserStackDecorator extends AutomatedBrowserBase {

    public static final String USERNAME_ENV = "BROWSERSTACK_USERNAME";
    public static final String AUTOMATE_KEY_ENV = "BROWSERSTACK_KEY";

    public BrowserStackDecorator(final AutomatedBrowser automatedBrowser) {
        super(automatedBrowser);
    }

    @Override
    public void init() {
        try {
            final String url = "https://" +
                    System.getenv(USERNAME_ENV) + ":" +
                    System.getenv(AUTOMATE_KEY_ENV) +
                    "@hub-cloud.browserstack.com/wd/hub";
            final WebDriver webDriver = new RemoteWebDriver(new URL(url), getDesiredCapabilities());
            getAutomatedBrowser().setWebDriver(webDriver);
            getAutomatedBrowser().init();
        } catch (MalformedURLException ex) {
            throw new ConfigurationException(ex);
        }
    }
}