package com.octopus.decorators;

import com.octopus.AutomatedBrowser;
import com.octopus.decoratorbase.AutomatedBrowserBase;
import com.octopus.utils.SystemPropertyUtils;
import com.octopus.utils.impl.SystemPropertyUtilsImpl;
import io.vavr.control.Try;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.io.File;

public class FirefoxDecorator extends AutomatedBrowserBase {
    private static final SystemPropertyUtils SYSTEM_PROPERTY_UTILS = new SystemPropertyUtilsImpl();

    final boolean headless;

    public FirefoxDecorator(final AutomatedBrowser automatedBrowser) {
        this(false, automatedBrowser);
    }

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

        final FirefoxOptions options = new FirefoxOptions();
        options.setHeadless(headless);
        options.setProfile(myprofile);
        options.merge(getDesiredCapabilities());
        final WebDriver webDriver = new FirefoxDriver(options);
        getAutomatedBrowser().setWebDriver(webDriver);
        getAutomatedBrowser().init();
    }

    @Override
    public void destroy() {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().destroy();
        }
    }
}