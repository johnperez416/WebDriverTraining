package com.octopus.decorators;

import com.octopus.AutomatedBrowser;
import com.octopus.decoratorbase.AutomatedBrowserBase;
import com.octopus.utils.SystemPropertyUtils;
import com.octopus.utils.impl.SystemPropertyUtilsImpl;
import io.vavr.control.Try;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;

/**
 * A decorator to configure a Firefox session.
 */
public class FirefoxDecorator extends AutomatedBrowserBase {
    /**
     * The shared SystemPropertyUtilsImpl instance.
     */
    private static final SystemPropertyUtils SYSTEM_PROPERTY_UTILS = new SystemPropertyUtilsImpl();

    /**
     * true if the browser is headless, and false otherwise.
     */
    final boolean headless;

    /**
     * Decorator constructor.
     *
     * @param automatedBrowser The AutomatedBrowser to wrap up.
     */
    public FirefoxDecorator(final AutomatedBrowser automatedBrowser) {
        this(false, automatedBrowser);
    }

    /**
     * Decorator constructor.
     *
     * @param headless         true if the browser is to operate in headless mode, and false otherwise
     * @param automatedBrowser The AutomatedBrowser to wrap up.
     */
    public FirefoxDecorator(final boolean headless, final AutomatedBrowser automatedBrowser) {
        super(automatedBrowser);
        this.headless = headless;

        if (StringUtils.isBlank(SYSTEM_PROPERTY_UTILS.getProperty("webdriver.firefox.logfile"))) {
            Try.of(() -> File.createTempFile("firefoxlogfile", ".log"))
                    .peek(File::deleteOnExit)
                    .andThen(logfile -> System.setProperty("webdriver.firefox.logfile", logfile.getAbsolutePath()));
        }
    }

    @Override
    public void init() {
        final FirefoxProfile myprofile = new FirefoxProfile();
        myprofile.setPreference("network.automatic-ntlm-auth.trusted-uris", "localhost");
        myprofile.setPreference("network.negotiate-auth.delegation-uris", "localhost");
        myprofile.setPreference("network.negotiate-auth.trusted-uris", "localhost");
        myprofile.setPreference("network.automatic-ntlm-auth.allow-non-fqdn", "true");
        myprofile.setPreference("browser.cache.memory.enable", false);
        myprofile.setPreference("browser.cache.memory.capacity", 0);
        myprofile.setPreference("browser.fullscreen.autohide", false);

        final FirefoxOptions options = new FirefoxOptions(getFirefoxOptions());
        options.setHeadless(headless);
        options.setProfile(myprofile);
        options.merge(getDesiredCapabilities());
        final WebDriver webDriver = new FirefoxDriver(options);
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