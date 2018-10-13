package com.octopus.decorators;

import com.octopus.AutomatedBrowser;
import com.octopus.decoratorbase.AutomatedBrowserBase;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ChromeDecorator extends AutomatedBrowserBase {

    final boolean headless;
    final boolean noSandbox;
    File userData;

    public ChromeDecorator(final AutomatedBrowser automatedBrowser) {
        super(automatedBrowser);
        this.headless = false;
        this.noSandbox = false;
        try {
            this.userData = Files.createTempDirectory("user-data").toFile();
        } catch (IOException e) {
            this.userData = null;
        }
    }

    public ChromeDecorator(final boolean headless, final AutomatedBrowser automatedBrowser) {
        super(automatedBrowser);
        this.headless = headless;
        this.noSandbox = false;
    }

    public ChromeDecorator(final boolean headless, final boolean noSandbox, final AutomatedBrowser automatedBrowser) {
        super(automatedBrowser);
        this.headless = headless;
        this.noSandbox = noSandbox;
    }

    @Override
    public void init() {
        final ChromeOptions options = new ChromeOptions();
        // --no-sandbox can fix issues when running Chrome in Docker
        if (noSandbox) {
            options.addArguments("--no-sandbox");
        }
        options.setHeadless(headless);
        // https://bugs.chromium.org/p/chromedriver/issues/detail?id=795
        // A random user-data dir can fix issues with multiple tests opening and closing Chrome
        if (userData != null) {
            options.addArguments("--user-data-dir=" + userData.getAbsolutePath());
        }
        options.merge(getDesiredCapabilities());
        final WebDriver webDriver = new ChromeDriver(options);
        getAutomatedBrowser().setWebDriver(webDriver);
        getAutomatedBrowser().init();

    }
}