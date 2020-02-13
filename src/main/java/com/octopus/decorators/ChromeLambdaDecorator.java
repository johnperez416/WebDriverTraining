package com.octopus.decorators;

import com.google.common.io.Files;
import com.octopus.AutomatedBrowser;
import com.octopus.decoratorbase.AutomatedBrowserBase;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;

public class ChromeLambdaDecorator extends AutomatedBrowserBase
{
    private final boolean headless;
    private final File userData;
    private final File dataPath;
    private final File cacheDir;
    private final File homeDir;

    public ChromeLambdaDecorator(final boolean headless, final AutomatedBrowser automatedBrowser) {
        super(automatedBrowser);
        this.headless = headless;
        System.setProperty("webdriver.chrome.silentOutput", "true");

        userData = Files.createTempDir();
        dataPath = Files.createTempDir();
        cacheDir = Files.createTempDir();
        homeDir = Files.createTempDir();
    }

    @Override
    public void init() {
        final ChromeOptions options = new ChromeOptions();
        if (headless) {
            options.addArguments("--headless");
        }
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--single-process");
        options.addArguments("--no-sandbox");
        options.addArguments("--user-data-dir=" + userData.getAbsolutePath());
        options.addArguments("--data-path=" + dataPath.getAbsolutePath());
        options.addArguments("--homedir=" + homeDir.getAbsolutePath());
        options.addArguments("--disk-cache-dir=" + cacheDir.getAbsolutePath());

        if (System.getProperty("chrome.binary") != null) {
            options.setBinary(System.getProperty("chrome.binary"));
        }

        options.merge(getDesiredCapabilities());
        final WebDriver webDriver = new ChromeDriver(options);
        getAutomatedBrowser().setWebDriver(webDriver);
        getAutomatedBrowser().init();
    }

    @Override
    public void destroy() {
        super.destroy();
        FileUtils.deleteQuietly(userData);
        FileUtils.deleteQuietly(dataPath);
        FileUtils.deleteQuietly(cacheDir);
        FileUtils.deleteQuietly(homeDir);
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