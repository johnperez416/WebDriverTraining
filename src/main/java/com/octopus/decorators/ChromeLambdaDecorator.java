package com.octopus.decorators;

import com.octopus.AutomatedBrowser;
import com.octopus.decoratorbase.AutomatedBrowserBase;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeLambdaDecorator extends AutomatedBrowserBase
{
    private final boolean headless;

    public ChromeLambdaDecorator(final boolean headless, final AutomatedBrowser automatedBrowser) {
        super(automatedBrowser);
        this.headless = headless;
        System.setProperty("webdriver.chrome.silentOutput", "true");
    }

    @Override
    public void init() {
        final ChromeOptions options = new ChromeOptions();
        if (headless) {
            options.addArguments("--headless");
        }
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1366,768");
        options.addArguments("--single-process");
        options.addArguments("--no-sandbox");
        options.addArguments("--user-data-dir=/tmp/user-data");
        options.addArguments("--data-path=/tmp/data-path");
        options.addArguments("--homedir=/tmp");
        options.addArguments("--disk-cache-dir=/tmp/cache-dir");

        if (System.getProperty("chrome.binary") != null) {
            options.setBinary(System.getProperty("chrome.binary"));
        }

        options.merge(getDesiredCapabilities());
        final WebDriver webDriver = new ChromeDriver(options);
        getAutomatedBrowser().setWebDriver(webDriver);
        getAutomatedBrowser().init();
    }

    @Override
    public void fadeScreen(final String red, final String green, final String blue, final String duration) {
        // Assume a headless environment means we either can't or don't want to do fades
        if (!this.headless) {
            if (getAutomatedBrowser() != null) {
                getAutomatedBrowser().fadeScreen(red, green, blue, duration);
            }
        }
    }
}