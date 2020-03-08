package com.octopus.decorators;

import com.octopus.AutomatedBrowser;
import com.octopus.decoratorbase.AutomatedBrowserBase;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * A decorator to configure a Chrome session.
 */
public class ChromeDecorator extends AutomatedBrowserBase {

    /**
     * true if the browser is headless, and false otherwise.
     */
    final boolean headless;
    /**
     * The user data directory for this instance.
     */
    File userData;

    /**
     * Decorator constructor.
     *
     * @param automatedBrowser The AutomatedBrowser to wrap up.
     */
    public ChromeDecorator(final AutomatedBrowser automatedBrowser) {
        this(false, automatedBrowser);
    }

    /**
     * Decorator constructor to create an optionally headless browser.
     *
     * @param headless         true if the browser is to operate in headless mode, and false otherwise
     * @param automatedBrowser The AutomatedBrowser to wrap up.
     */
    public ChromeDecorator(final boolean headless, final AutomatedBrowser automatedBrowser) {
        super(automatedBrowser);
        this.headless = headless;
        try {
            this.userData = Files.createTempDirectory("user-data").toFile();
        } catch (IOException e) {
            this.userData = null;
        }
        // Disable the logging to System.err
        System.setProperty("webdriver.chrome.silentOutput", "true");
    }

    @Override
    public void init() {
        final ChromeOptions options = new ChromeOptions();
        if (headless) {
            options.addArguments("--window-size=1920,1080");
        }
        options.setHeadless(headless);
        // https://bugs.chromium.org/p/chromedriver/issues/detail?id=795
        // A random user-data dir can fix issues with multiple tests opening and closing Chrome
        if (userData != null) {
            options.addArguments("--user-data-dir=" + userData.getAbsolutePath());
        }
        if (System.getProperty("chrome.binary") != null) {
            options.setBinary(System.getProperty("chrome.binary"));
        }

        options.merge(getDesiredCapabilities());
        final WebDriver webDriver = new ChromeDriver(options);
        getAutomatedBrowser().setWebDriver(webDriver);
        getAutomatedBrowser().init();
    }

    @Override
    public DesiredCapabilities getDesiredCapabilities() {
        final DesiredCapabilities desiredCapabilities = super.getDesiredCapabilities();
        desiredCapabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        desiredCapabilities.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
        return desiredCapabilities;
    }

    @Override
    public void destroy() {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().destroy();
        }

        FileUtils.deleteQuietly(userData);
        userData = null;
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

    @Override
    public void startScreenRecording(final String file, final String capturedArtifact) {
        // Assume a headless environment means we either can't or don't want record the screen
        if (!this.headless) {
            if (getAutomatedBrowser() != null) {
                getAutomatedBrowser().startScreenRecording(file, capturedArtifact);
            }
        }
    }
}