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
import org.openqa.selenium.firefox.ProfilesIni;

import java.io.File;

public class FirefoxDecorator extends AutomatedBrowserBase {
    private static final SystemPropertyUtils SYSTEM_PROPERTY_UTILS = new SystemPropertyUtilsImpl();

    final boolean headless;
    File logFile;

    public FirefoxDecorator(final AutomatedBrowser automatedBrowser) {
        super(automatedBrowser);
        this.headless = false;
    }

    public FirefoxDecorator(final boolean headless, final AutomatedBrowser automatedBrowser) {
        super(automatedBrowser);
        this.headless = headless;

        if (StringUtils.isBlank(SYSTEM_PROPERTY_UTILS.getProperty("webdriver.firefox.logfile"))) {
            logFile = Try.of(() -> File.createTempFile("firefoxlogfile", ".log")).getOrNull();
            if (logFile != null) {
                System.setProperty("webdriver.firefox.logfile", logFile.getAbsolutePath());
            }
        }
    }

    @Override
    public void init() {
        final FirefoxProfile myprofile = new FirefoxProfile();
        myprofile.setPreference("network.automatic-ntlm-auth.trusted-uris", "localhost");
        myprofile.setPreference("network.negotiate-auth.delegation-uris", "localhost");
        myprofile.setPreference("network.negotiate-auth.trusted-uris", "localhost");
        myprofile.setPreference("network.automatic-ntlm-auth.allow-non-fqdn", "true");

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
        if (logFile != null) {
            FileUtils.deleteQuietly(logFile);
        }
    }
}