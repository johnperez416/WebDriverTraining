package com.octopus.decorators;

import com.octopus.AutomatedBrowser;
import com.octopus.decoratorbase.AutomatedBrowserBase;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class FirefoxDecorator extends AutomatedBrowserBase {

    final boolean headless;

    public FirefoxDecorator(final AutomatedBrowser automatedBrowser) {
        super(automatedBrowser);
        this.headless = false;
    }

    public FirefoxDecorator(final boolean headless, final AutomatedBrowser automatedBrowser) {
        super(automatedBrowser);
        this.headless = headless;
    }

    @Override
    public void init() {
        final FirefoxOptions options = new FirefoxOptions();
        options.setHeadless(headless);
        options.merge(getDesiredCapabilities());
        final WebDriver webDriver = new FirefoxDriver(options);
        getAutomatedBrowser().setWebDriver(webDriver);
        getAutomatedBrowser().init();
    }
}