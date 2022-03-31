package com.octopus.decorators;

import com.octopus.AutomatedBrowser;
import com.octopus.decoratorbase.AutomatedBrowserBase;
import com.octopus.exceptions.ConfigurationException;
import java.net.MalformedURLException;
import java.net.URL;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * A decorator to configure a Browser Stack session.
 */
public class BrowserStackDecorator extends AutomatedBrowserBase {

    /**
     * The environment variable that folds the BrowserStack username.
     */
    public static final String USERNAME_ENV = "BROWSERSTACK_USERNAME";
    /**
     * The environment variable that folds the BrowserStack key.
     */
    public static final String AUTOMATE_KEY_ENV = "BROWSERSTACK_KEY";

    /**
     * Decorator constructor.
     *
     * @param automatedBrowser The AutomatedBrowser to wrap up.
     */
    public BrowserStackDecorator(final AutomatedBrowser automatedBrowser) {
        super(automatedBrowser);
    }

    @Override
    public void init() {
        try {
            final String url = "https://"
                    + System.getenv(USERNAME_ENV) + ":"
                    + System.getenv(AUTOMATE_KEY_ENV)
                    + "@hub-cloud.browserstack.com/wd/hub";
            final WebDriver webDriver = new RemoteWebDriver(new URL(url), getDesiredCapabilities());
            getAutomatedBrowser().setWebDriver(webDriver);
            getAutomatedBrowser().init();
        } catch (MalformedURLException ex) {
            throw new ConfigurationException(ex);
        }
    }
}