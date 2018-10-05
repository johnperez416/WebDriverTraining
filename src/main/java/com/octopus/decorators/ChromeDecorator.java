package com.octopus.decorators;

import com.octopus.AutomatedBrowser;
import com.octopus.decoratorbase.AutomatedBrowserBase;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeDecorator extends AutomatedBrowserBase {

    final boolean headless;

    public ChromeDecorator(final AutomatedBrowser automatedBrowser) {
        super(automatedBrowser);
        this.headless = false;
    }

    public ChromeDecorator(final boolean headless, final AutomatedBrowser automatedBrowser) {
        super(automatedBrowser);
        this.headless = headless;
    }

    @Override
    public void init() {
        final ChromeOptions options = new ChromeOptions();
        options.setHeadless(headless);
        options.merge(getDesiredCapabilities());
        final WebDriver webDriver = new ChromeDriver(options);
        getAutomatedBrowser().setWebDriver(webDriver);
        getAutomatedBrowser().init();

    }
}