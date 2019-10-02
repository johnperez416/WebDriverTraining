package com.octopus.decorators;

import com.octopus.AutomatedBrowser;
import com.octopus.decoratorbase.AutomatedBrowserBase;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.ProfilesIni;

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
}