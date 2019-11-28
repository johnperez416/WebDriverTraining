package com.octopus.decoratorbase;

import com.octopus.AutomatedBrowser;
import com.octopus.AutomatedBrowserFactory;
import com.octopus.Constants;
import com.octopus.exceptions.BrowserException;
import com.octopus.exceptions.NetworkException;
import com.octopus.exceptions.SaveException;
import com.octopus.utils.JavaLauncherUtils;
import com.octopus.utils.OSUtils;
import com.octopus.utils.SystemPropertyUtils;
import com.octopus.utils.impl.JavaLauncherUtilsImpl;
import com.octopus.utils.impl.OSUtilsImpl;
import com.octopus.utils.impl.SystemPropertyUtilsImpl;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.text.StringSubstitutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

public class AutomatedBrowserBase implements AutomatedBrowser {
    private static final Logger LOGGER = Logger.getLogger(AutomatedBrowserBase.class.toString());
    static private final String LastReturn = "LastReturn";
    static private final AutomatedBrowserFactory AUTOMATED_BROWSER_FACTORY = new AutomatedBrowserFactory();
    private static final SystemPropertyUtils SYSTEM_PROPERTY_UTILS = new SystemPropertyUtilsImpl();
    private static final JavaLauncherUtils JAVA_LAUNCHER_UTILS = new JavaLauncherUtilsImpl();
    private static final OSUtils OS_UTILS = new OSUtilsImpl();
    private Map<String, String> aliases = new HashMap<>();
    static private Map<String, String> externalAliases = new HashMap<>();
    private AutomatedBrowser automatedBrowser;
    private static AutomatedBrowser sharedAutomatedBrowser;
    private static AutomatedBrowserBase instanceAutomatedBrowser;

    public static AutomatedBrowserBase getInstance() {
        return instanceAutomatedBrowser;
    }

    public AutomatedBrowserBase() {
        instanceAutomatedBrowser = this;
    }

    public AutomatedBrowserBase(final AutomatedBrowser automatedBrowser) {
        this.automatedBrowser = automatedBrowser;
    }

    public static void setExternalAliases(final Map<String, String> externalAliases) {
        if (externalAliases == null) return;
        AutomatedBrowserBase.externalAliases.putAll(externalAliases);
    }

    @Before
    public void reuseSharedBrowser() {
        automatedBrowser = sharedAutomatedBrowser;
    }

    @After
    public void afterScenario(final Scenario scenario) {
        if (scenario.isFailed()) {
            closeBrowser();
            stopScreenRecording();
            if (SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.DUMP_ALIASES_ON_FAILURE, false)) {
                dumpAliases();
            }
        }

        LOGGER.info("\nRecorded " + getInteractionCount() + " interactions for the browser session");
    }

    private Map<String, String> getAliases() {
        final Map<String, String> combinedAliases = new HashMap<>();
        combinedAliases.putAll(aliases);
        combinedAliases.putAll(externalAliases);
        return combinedAliases;
    }

    public AutomatedBrowser getAutomatedBrowser() {
        return automatedBrowser;
    }

    @And("^I run the feature \"([^\"]*)\"$")
    public void executeFeature(final String featureFile) {
        if (new File(featureFile).exists()) {
            JAVA_LAUNCHER_UTILS.launchAppInternally(new String[] {featureFile});
        } else {
            final String[] mainCommand = System.getProperty("sun.java.command").split(" ");
            final String featurePath = new File(mainCommand[mainCommand.length - 1]).getParentFile().getAbsolutePath();
            JAVA_LAUNCHER_UTILS.launchAppInternally(new String[] {(new File(featurePath, featureFile).getAbsolutePath())});
        }
    }

    @Given("^I set the following aliases:$")
    public void setAliases(final Map<String, String> aliases) {
        this.aliases.putAll(aliases);
    }

    @Override
    @And("^I (?:sleep|wait) for \"([^\"]*)\" seconds?$")
    public void sleep(final String seconds) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().sleep(getSubstitutedString(seconds));
        }
    }

    @Given("^I open the( shared)? browser \"([^\"]*)\"$")
    public void openBrowser(final String shared, final String browser) {
        if (sharedAutomatedBrowser != null) {
            throw new BrowserException("Can not open a browser with an existing shared browser.");
        }

        if (shared != null) {
            automatedBrowser = sharedAutomatedBrowser = AUTOMATED_BROWSER_FACTORY.getAutomatedBrowser(getSubstitutedString(browser));
            automatedBrowser.init();
        } else {
            automatedBrowser = AUTOMATED_BROWSER_FACTORY.getAutomatedBrowser(getSubstitutedString(browser));
            automatedBrowser.init();
        }
    }

    @Given("^I close the browser$")
    public void closeBrowser() {
        if (automatedBrowser != null) {
            automatedBrowser.destroy();
        }

        automatedBrowser = null;
        sharedAutomatedBrowser = null;
    }

    @And("^I set the default explicit wait time to \"(\\d+)\" seconds?$")
    @Override
    public void setDefaultExplicitWaitTime(final int waitTime) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().setDefaultExplicitWaitTime(waitTime);
        }
    }

    @Override
    public int getDefaultExplicitWaitTime() {
        if (getAutomatedBrowser() != null) {
            return getAutomatedBrowser().getDefaultExplicitWaitTime();
        }

        return 0;
    }

    @Override
    public WebDriver getWebDriver() {
        if (getAutomatedBrowser() != null) {
            return getAutomatedBrowser().getWebDriver();
        }

        return null;
    }

    @Override
    public void setWebDriver(final WebDriver webDriver) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().setWebDriver(webDriver);
        }
    }

    @Override
    public DesiredCapabilities getDesiredCapabilities() {
        if (getAutomatedBrowser() != null) {
            return getAutomatedBrowser().getDesiredCapabilities();
        }

        return new DesiredCapabilities();
    }

    @Override
    public void init() {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().init();
        }
    }

    @Override
    public void destroy() {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().destroy();
        }
    }

    @And("^I open the URL \"([^\"]*)\"$")
    @Override
    public void goTo(final String url) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().goTo(getSubstitutedString(url));
        }
    }

    @And("^I refresh the page$")
    @Override
    public void refresh() {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().refresh();
        }
    }

    @And("^I refresh the page if the \"([^\"]*)\" \\w+(?:\\s+\\w+)* (does not )?exists?$")
    @Override
    public void refreshIfExists(final String locator, final String doesNotExist) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().refreshIfExists(
                    getSubstitutedString(locator),
                    doesNotExist);
        }
    }

    @And("^I refresh the page if the \"([^\"]*)\" \\w+(?:\\s+\\w+)* (does not )?exists? waiting up to \"(\\d+)\" seconds?$")
    @Override
    public void refreshIfExists(final String locator, final String doesNotExist, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().refreshIfExists(
                    getSubstitutedString(locator),
                    doesNotExist,
                    waitTime);
        }
    }

    @And("^I start recording the screen(?: to the directory \"([^\"]*)\")(?: and capture as an Octopus artifact called \"([^\"]*)\")?$")
    @Override
    public void startScreenRecording(final String file, final String capturedArtifact) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().startScreenRecording(
                    getSubstitutedString(file),
                    getSubstitutedString(capturedArtifact));
        }
    }

    @And("^I stop recording the screen$")
    @Override
    public void stopScreenRecording() {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().stopScreenRecording();
        }
    }

    @And("^I dump the aliases$")
    @Override
    public void dumpAliases() {
        LOGGER.info("\nStart of alias dump");
        getAliases().entrySet().forEach(entrySet -> LOGGER.info(entrySet.getKey() + ": " + entrySet.getValue()));
    }

    @And("^I write the value of the alias \"([^\"]*)\" to the file \"([^\"]*)\"$")
    @Override
    public void writeAliasValueToFile(final String alias, final String filename) {
        try {
            FileUtils.write(
                    new File(OS_UTILS.fixFileName(getSubstitutedString(filename))),
                    getSubstitutedString(alias),
                    StandardCharsets.UTF_8);
        } catch (final IOException ex) {
            throw new SaveException("Failed to write alias value to " + filename, ex);
        }
    }

    @Override
    public CompletableFuture<Void> takeScreenshot(final String filename, boolean force, final String captureArtifact) {
        if (getAutomatedBrowser() != null) {
            return getAutomatedBrowser().takeScreenshot(
                    getSubstitutedString(filename),
                    force,
                    captureArtifact);
        }
        return CompletableFuture.completedFuture(null);
    }

    @And("^I save a screenshot to \"([^\"]*)\"(?: and capture as an Octopus artifact called \"([^\"]*)\")?$")
    @Override
    public CompletableFuture<Void> takeScreenshot(final String filename, final String captureArtifact) {
        if (getAutomatedBrowser() != null) {
            return getAutomatedBrowser().takeScreenshot(
                    getSubstitutedString(filename),
                    getSubstitutedString(captureArtifact));
        }
        return CompletableFuture.completedFuture(null);
    }

    @And("^I save a screenshot to \"([^\"]*)\" called \"([^\"]*)\"(?: and capture as an Octopus artifact called \"([^\"]*)\")?$")
    @Override
    public CompletableFuture<Void> takeScreenshot(final String directory, final String filename, final String captureArtifact) {
        if (getAutomatedBrowser() != null) {
            return getAutomatedBrowser().takeScreenshot(
                    getSubstitutedString(directory),
                    getSubstitutedString(filename),
                    getSubstitutedString(captureArtifact));
        }
        return CompletableFuture.completedFuture(null);
    }

    @And("^I set the window size to \"([^\"]*)\" x \"([^\"]*)\"$")
    @Override
    public void setWindowSize(final String width, final String height) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().setWindowSize(
                    getSubstitutedString(width),
                    getSubstitutedString(height));
        }
    }

    @And("^I click the \\w+(?:\\s+\\w+)* with the id \"([^\"]*)\"$")
    @Override
    public void clickElementWithId(final String id) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().clickElementWithId(getSubstitutedString(id));
        }
    }

    @And("^I click the \\w+(?:\\s+\\w+)* with the id \"([^\"]*)\" waiting up to \"(\\d+)\" seconds?$")
    @Override
    public void clickElementWithId(final String id, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().clickElementWithId(getSubstitutedString(id), waitTime);
        }
    }

    @And("^I select the option \"([^\"]*)\" from the \\w+(?:\\s+\\w+)* with the id \"([^\"]*)\"$")
    @Override
    public void selectOptionByTextFromSelectWithId(final String optionText, final String id) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().selectOptionByTextFromSelectWithId(
                    getSubstitutedString(optionText),
                    getSubstitutedString(id));
        }
    }

    @And("^I select the option \"([^\"]*)\" from the \\w+(?:\\s+\\w+)* with the id \"([^\"]*)\" waiting up to \"(\\d+)\" seconds?$")
    @Override
    public void selectOptionByTextFromSelectWithId(final String optionText, final String id, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().selectOptionByTextFromSelectWithId(
                    getSubstitutedString(optionText),
                    getSubstitutedString(id),
                    waitTime);
        }
    }

    @And("^I populate the \\w+(?:\\s+\\w+)* with the id \"([^\"]*)\" with(?: the text)? \"([^\"]*)\"$")
    @Override
    public void populateElementWithId(final String id, final String text) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().populateElementWithId(
                    getSubstitutedString(id),
                    getSubstitutedString(text));
        }
    }

    @And("^I populate the \\w+(?:\\s+\\w+)* with the id \"([^\"]*)\" with(?: the text)? \"([^\"]*)\" waiting up to \"(\\d+)\" seconds?$")
    @Override
    public void populateElementWithId(final String id, final String text, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().populateElementWithId(
                    getSubstitutedString(id),
                    getSubstitutedString(text),
                    waitTime);
        }
    }

    @Override
    public String getTextFromElementWithId(final String id) {
        if (getAutomatedBrowser() != null) {
            return getAutomatedBrowser().getTextFromElementWithId(getSubstitutedString(id));
        }

        return null;
    }

    @Override
    public String getTextFromElementWithId(final String id, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            return getAutomatedBrowser().getTextFromElementWithId(getSubstitutedString(id), waitTime);
        }

        return null;
    }

    @And("^I click the \\w+(?:\\s+\\w+)* with the xpath \"([^\"]*)\"$")
    @Override
    public void clickElementWithXPath(final String xpath) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().clickElementWithXPath(getSubstitutedString(xpath));
        }
    }

    @And("^I click the \\w+(?:\\s+\\w+)* with the xpath \"([^\"]*)\" waiting up to \"(\\d+)\" seconds?$")
    @Override
    public void clickElementWithXPath(final String xpath, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().clickElementWithXPath(getSubstitutedString(xpath), waitTime);
        }
    }

    @And("^I select the option \"([^\"]*)\" from the \\w+(?:\\s+\\w+)* with the xpath \"([^\"]*)\"$")
    @Override
    public void selectOptionByTextFromSelectWithXPath(final String optionText, final String xpath) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().selectOptionByTextFromSelectWithXPath(
                    getSubstitutedString(optionText),
                    getSubstitutedString(xpath));
        }
    }

    @And("^I select the option \"([^\"]*)\" from the \\w+(?:\\s+\\w+)* with the xpath \"([^\"]*)\" waiting up to \"(\\d+)\" seconds?$")
    @Override
    public void selectOptionByTextFromSelectWithXPath(final String optionText, final String xpath, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().selectOptionByTextFromSelectWithXPath(
                    getSubstitutedString(optionText),
                    getSubstitutedString(xpath),
                    waitTime);
        }
    }

    @And("^I populate the \\w+(?:\\s+\\w+)* with the xpath \"([^\"]*)\" with(?: the text)? \"([^\"]*)\"$")
    @Override
    public void populateElementWithXPath(final String xpath, final String text) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().populateElementWithXPath(
                    getSubstitutedString(xpath),
                    getSubstitutedString(text));
        }
    }

    @And("^I populate the \\w+(?:\\s+\\w+)* with the xpath \"([^\"]*)\" with(?: the text)? \"([^\"]*)\" waiting up to \"(\\d+)\" seconds?$")
    @Override
    public void populateElementWithXPath(final String xpath, final String text, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().populateElementWithXPath(
                    getSubstitutedString(xpath),
                    getSubstitutedString(text),
                    waitTime);
        }
    }

    @Override
    public String getTextFromElementWithXPath(final String xpath) {
        if (getAutomatedBrowser() != null) {
            return getAutomatedBrowser().getTextFromElementWithXPath(getSubstitutedString(xpath));
        }

        return null;
    }

    @Override
    public String getTextFromElementWithXPath(final String xpath, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            return getAutomatedBrowser().getTextFromElementWithXPath(getSubstitutedString(xpath), waitTime);
        }

        return null;
    }

    @And("^I click the \\w+(?:\\s+\\w+)* with the css selector \"([^\"]*)\"$")
    @Override
    public void clickElementWithCSSSelector(final String cssSelector) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().clickElementWithCSSSelector(getSubstitutedString(cssSelector));
        }
    }

    @And("^I click the \\w+(?:\\s+\\w+)* with the css selector \"([^\"]*)\" waiting up to \"(\\d+)\" seconds?$")
    @Override
    public void clickElementWithCSSSelector(final String cssSelector, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().clickElementWithCSSSelector(getSubstitutedString(cssSelector), waitTime);
        }
    }

    @And("^I select the option \"([^\"]*)\" from the \\w+(?:\\s+\\w+)* with the css selector \"([^\"]*)\"$")
    @Override
    public void selectOptionByTextFromSelectWithCSSSelector(final String optionText, final String cssSelector) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().selectOptionByTextFromSelectWithCSSSelector(
                    getSubstitutedString(optionText),
                    getSubstitutedString(cssSelector));
        }
    }

    @And("^I select the option \"([^\"]*)\" from the \\w+(?:\\s+\\w+)* with the css selector \"([^\"]*)\" waiting up to \"(\\d+)\" seconds?$")
    @Override
    public void selectOptionByTextFromSelectWithCSSSelector(final String optionText, final String cssSelector, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().selectOptionByTextFromSelectWithCSSSelector(
                    getSubstitutedString(optionText),
                    getSubstitutedString(cssSelector),
                    waitTime);
        }
    }

    @And("^I populate the \\w+(?:\\s+\\w+)* with the css selector \"([^\"]*)\" with(?: the text)? \"([^\"]*)\"$")
    @Override
    public void populateElementWithCSSSelector(final String cssSelector, final String text) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().populateElementWithCSSSelector(
                    getSubstitutedString(cssSelector),
                    getSubstitutedString(text));
        }
    }

    @And("^I populate the \\w+(?:\\s+\\w+)* with the css selector \"([^\"]*)\" with(?: the text)? \"([^\"]*)\" waiting up to \"(\\d+)\" seconds?$")
    @Override
    public void populateElementWithCSSSelector(final String cssSelector, final String text, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().populateElementWithCSSSelector(
                    getSubstitutedString(cssSelector),
                    getSubstitutedString(text),
                    waitTime);
        }
    }

    @Override
    public String getTextFromElementWithCSSSelector(final String cssSelector) {
        if (getAutomatedBrowser() != null) {
            return getAutomatedBrowser().getTextFromElementWithCSSSelector(
                    getSubstitutedString(cssSelector));
        }

        return null;
    }

    @Override
    public String getTextFromElementWithCSSSelector(final String cssSelector, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            return getAutomatedBrowser().getTextFromElementWithCSSSelector(
                    getSubstitutedString(cssSelector),
                    waitTime);
        }

        return null;
    }

    @And("^I click the \\w+(?:\\s+\\w+)* with the name \"([^\"]*)\"$")
    @Override
    public void clickElementWithName(final String name) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().clickElementWithName(getSubstitutedString(name));
        }
    }

    @And("^I click the \\w+(?:\\s+\\w+)* with the name \"([^\"]*)\" waiting up to \"(\\d+)\" seconds?$")
    @Override
    public void clickElementWithName(final String name, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().clickElementWithName(getSubstitutedString(name), waitTime);
        }
    }

    @And("^I select the option \"([^\"]*)\" from the \\w+(?:\\s+\\w+)* with the name \"([^\"]*)\"$")
    @Override
    public void selectOptionByTextFromSelectWithName(final String optionText, final String name) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().selectOptionByTextFromSelectWithName(
                    getSubstitutedString(optionText),
                    getSubstitutedString(name));
        }
    }

    @And("^I select the option \"([^\"]*)\" from the \\w+(?:\\s+\\w+)* with the name \"([^\"]*)\" waiting up to \"(\\d+)\" seconds?$")
    @Override
    public void selectOptionByTextFromSelectWithName(final String optionText, final String name, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().selectOptionByTextFromSelectWithName(
                    getSubstitutedString(optionText),
                    getSubstitutedString(name),
                    waitTime);
        }
    }

    @And("^I populate the \\w+(?:\\s+\\w+)* with the name \"([^\"]*)\" with(?: the text)? \"([^\"]*)\"$")
    @Override
    public void populateElementWithName(final String name, final String text) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().populateElementWithName(
                    getSubstitutedString(name),
                    getSubstitutedString(text));
        }
    }

    @And("^I populate the \\w+(?:\\s+\\w+)* with the name \"([^\"]*)\" with(?: the text)? \"([^\"]*)\" waiting up to \"(\\d+)\" seconds?$")
    @Override
    public void populateElementWithName(final String name, final String text, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().populateElementWithName(
                    getSubstitutedString(name),
                    getSubstitutedString(text),
                    waitTime);
        }
    }

    @Override
    public String getTextFromElementWithName(final String name) {
        if (getAutomatedBrowser() != null) {
            return getAutomatedBrowser().getTextFromElementWithName(getSubstitutedString(name));
        }

        return null;
    }

    @Override
    public String getTextFromElementWithName(final String name, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            return getAutomatedBrowser().getTextFromElementWithName(
                    getSubstitutedString(name),
                    waitTime);
        }

        return null;
    }

    @And("^I( force)? click the \"([^\"]*)\" \\w+(?:\\s+\\w+)*? if the \"([^\"]*)\" \\w+(?:\\s+\\w+)*? exists$")
    @Override
    public void clickElementIfOtherExists(final String force, final String locator, final String ifOtherExists) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().clickElementIfOtherExists(
                    force,
                    getSubstitutedString(locator),
                    getSubstitutedString(ifOtherExists));
        }
    }

    @And("^I( force)? click the \"([^\"]*)\" \\w+(?:\\s+\\w+)*? waiting up to \"(\\d+)\" seconds? if the \"([^\"]*)\" \\w+(?:\\s+\\w+)*? exists$")
    @Override
    public void clickElementIfOtherExists(final String force, final String locator, final int waitTime, final String ifOtherExists) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().clickElementIfOtherExists(
                    force,
                    getSubstitutedString(locator),
                    waitTime,
                    getSubstitutedString(ifOtherExists));
        }
    }

    @And("^I( force)? click the \"([^\"]*)\" \\w+(?:\\s+\\w+)*? if the \"([^\"]*)\" \\w+(?:\\s+\\w+)*? does not exist$")
    @Override
    public void clickElementIfOtherNotExists(final String force, final String locator, final String ifOtherExists) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().clickElementIfOtherNotExists(
                    force,
                    getSubstitutedString(locator),
                    getSubstitutedString(ifOtherExists));
        }
    }

    @And("^I( force)? click the \"([^\"]*)\" \\w+(?:\\s+\\w+)*? waiting up to \"(\\d+)\" seconds? if the \"([^\"]*)\" \\w+(?:\\s+\\w+)*? does not exist$")
    @Override
    public void clickElementIfOtherNotExists(final String force, final String locator, final int waitTime, final String ifOtherExists) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().clickElementIfOtherNotExists(
                    force,
                    getSubstitutedString(locator),
                    waitTime,
                    getSubstitutedString(ifOtherExists));
        }
    }

    @And("^I( force)? click the \"([^\"]*)\" \\w+(?:\\s+\\w+)*?( if it exists)?$")
    @Override
    public void clickElementIfExists(final String force, final String locator, final String ifExistsOption) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().clickElementIfExists(
                    force,
                    getSubstitutedString(locator),
                    ifExistsOption);
        }
    }

    @And("^I( force)? click the \"([^\"]*)\" \\w+(?:\\s+\\w+)* waiting up to \"(\\d+)\" seconds?( if it exists)?$")
    @Override
    public void clickElementIfExists(final String force, final String locator, final int waitTime, final String ifExistsOption) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().clickElementIfExists(
                    force,
                    getSubstitutedString(locator),
                    waitTime,
                    ifExistsOption);
        }
    }

    @And("^I select the option \"([^\"]*)\" from the \"([^\"]*)\" \\w+(?:\\s+\\w+)*?( if it exists)?$")
    @Override
    public void selectOptionByTextFromSelectIfExists(final String optionText, final String locator, final String ifExistsOption) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().selectOptionByTextFromSelectIfExists(
                    getSubstitutedString(optionText),
                    getSubstitutedString(locator),
                    ifExistsOption);
        }
    }

    @And("^I select the option \"([^\"]*)\" from the \"([^\"]*)\" \\w+(?:\\s+\\w+)* waiting up to \"(\\d+)\" seconds?( if it exists)?$")
    @Override
    public void selectOptionByTextFromSelectIfExists(final String optionText, final String locator, final int waitTime, final String ifExistsOption) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().selectOptionByTextFromSelectIfExists(
                    getSubstitutedString(optionText),
                    getSubstitutedString(locator),
                    waitTime,
                    ifExistsOption);
        }
    }

    @And("^I select the option value \"([^\"]*)\" from the \"([^\"]*)\" \\w+(?:\\s+\\w+)*?( if it exists)?$")
    @Override
    public void selectOptionByValueFromSelectIfExists(final String optionValue, final String locator, final String ifExistsOption) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().selectOptionByValueFromSelectIfExists(
                    getSubstitutedString(optionValue),
                    getSubstitutedString(locator),
                    ifExistsOption);
        }
    }

    @And("^I select the option value \"([^\"]*)\" from the \"([^\"]*)\" \\w+(?:\\s+\\w+)* waiting up to \"(\\d+)\" seconds?( if it exists)?$")
    @Override
    public void selectOptionByValueFromSelectIfExists(final String optionValue, final String locator, final int waitTime, final String ifExistsOption) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().selectOptionByValueFromSelectIfExists(
                    getSubstitutedString(optionValue),
                    getSubstitutedString(locator),
                    waitTime,
                    ifExistsOption);
        }
    }

    @And("^I populate the \"([^\"]*)\" \\w+(?:\\s+\\w+)* (?:with a keystroke delay of (\"([^\"]*)\") )?with(?: the text)? \"([^\"]*)\"( if it exists)?$")
    @Override
    public void populateElement(final String locator, final String keystrokeDelay, final String text, final String ifExistsOption) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().populateElement(
                    getSubstitutedString(locator),
                    getSubstitutedString(keystrokeDelay),
                    getSubstitutedString(text),
                    ifExistsOption);
        }
    }

    @And("^I populate the \"([^\"]*)\" \\w+(?:\\s+\\w+)* (?:with a keystroke delay of \"([^\"]*)\" )?with(?: the text)?:$")
    @Override
    public void populateElement(final String locator, final String keystrokeDelay, final String text) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().populateElement(
                    getSubstitutedString(locator),
                    getSubstitutedString(keystrokeDelay),
                    getSubstitutedString(text),
                    null);
        }
    }

    @And("^I populate the \"([^\"]*)\" \\w+(?:\\s+\\w+)* (?:with a keystroke delay of \"([^\"]*)\" )?with(?: the text)? \"([^\"]*)\" waiting up to \"(\\d+)\" seconds?( if it exists)?$")
    @Override
    public void populateElement(final String locator, final String keystrokeDelay, final String text, final int waitTime, final String ifExistsOption) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().populateElement(
                    getSubstitutedString(locator),
                    getSubstitutedString(keystrokeDelay),
                    getSubstitutedString(text),
                    waitTime,
                    ifExistsOption);
        }
    }

    @And("^I populate the hidden \"([^\"]*)\" \\w+(?:\\s+\\w+)* with(?: the text)? \"([^\"]*)\"( if it exists)?$")
    @Override
    public void populateHiddenElement(final String locator, final String text, final String ifExistsOption) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().populateHiddenElement(
                    getSubstitutedString(locator),
                    getSubstitutedString(text),
                    ifExistsOption);
        }
    }

    @And("^I populate the hidden \"([^\"]*)\" \\w+(?:\\s+\\w+)* with(?: the text)? \"([^\"]*)\" waiting up to \"(\\d+)\" seconds?( if it exists)?$")
    @Override
    public void populateHiddenElement(final String locator, final String text, final int waitTime, final String ifExistsOption) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().populateHiddenElement(
                    getSubstitutedString(locator),
                    getSubstitutedString(text),
                    waitTime,
                    ifExistsOption);
        }
    }

    @And("^I clear the \"([^\"]*)\" \\w+(?:\\s+\\w+)*?( if it exists)?$")
    @Override
    public void clearIfExists(final String locator, final String ifExistsOption) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().clearIfExists(
                    getSubstitutedString(locator),
                    ifExistsOption);
        }
    }

    @And("^I clear the \"([^\"]*)\" \\w+(?:\\s+\\w+)* waiting up to \"(\\d+)\" seconds?( if it exists)?$")
    @Override
    public void clearIfExists(final String locator, final int waitTime, final String ifExistsOption) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().clearIfExists(
                    getSubstitutedString(locator),
                    waitTime,
                    ifExistsOption);
        }
    }

    @And("^I scroll down \"([^\"]*)\" px$")
    @Override
    public void scrollDown(final String distance) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().scrollDown(
                    getSubstitutedString(distance));
        }
    }

    @And("^I scroll up \"([^\"]*)\" px$")
    @Override
    public void scrollUp(final String distance) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().scrollUp(
                    getSubstitutedString(distance));
        }
    }

    @Then("^I verify the (current )?URL matches the regex \"([^\"]*)\"$")
    @Override
    public void verifyUrl(final String regex) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().verifyUrl(getSubstitutedString(regex));
        }
    }

    @And("^I zoom the browser in$")
    @Override
    public void browserZoomIn() {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().browserZoomIn();
        }
    }

    @And("^I zoom the browser out$")
    @Override
    public void browserZoomOut() {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().browserZoomOut();
        }
    }

    @And("^I( force)? mouse over the \"([^\"]*)\" \\w+(?:\\s+\\w+)*?( if it exists)?")
    @Override
    public void mouseOverIfExists(final String force, final String locator, final String ifExistsOption) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().mouseOverIfExists(
                    force,
                    getSubstitutedString(locator),
                    ifExistsOption);
        }
    }

    @And("^I( force)? mouse over the \"([^\"]*)\" \\w+(?:\\s+\\w+)* waiting up to \"(\\d+)\" seconds?( if it exists)?")
    @Override
    public void mouseOverIfExists(final String force, final String locator, final int waitTime, final String ifExistsOption) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().mouseOverIfExists(
                    force,
                    getSubstitutedString(locator),
                    waitTime,
                    ifExistsOption);
        }
    }

    @And("^I( force)? focus(?: on) the \"([^\"]*)\" \\w+(?:\\s+\\w+)*?( if it exists)?")
    @Override
    public void focusIfExists(final String force, final String locator, final String ifExistsOption) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().focusIfExists(
                    force,
                    getSubstitutedString(locator),
                    ifExistsOption);
        }
    }

    @And("^I( force)? focus(?: on) the \"([^\"]*)\" \\w+(?:\\s+\\w+)* waiting up to \"(\\d+)\" seconds?( if it exists)?")
    @Override
    public void focusIfExists(final String force, final String locator, final int waitTime, final String ifExistsOption) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().focusIfExists(
                    force,
                    getSubstitutedString(locator),
                    waitTime,
                    ifExistsOption);
        }
    }

    @And("^I get the text from the \"([^\"]*)\" \\w+(?:\\s+\\w+)*?( if it exists)?$")
    @Override
    public String getTextFromElementIfExists(final String locator, final String ifExistsOption) {
        if (getAutomatedBrowser() != null) {
            final String text = getAutomatedBrowser().getTextFromElementIfExists(
                    getSubstitutedString(locator),
                    ifExistsOption);
            aliases.put(LastReturn, text);
            return text;
        }

        aliases.put(LastReturn, null);
        return null;
    }

    @And("^I get the text from the \"([^\"]*)\" \\w+(?:\\s+\\w+)* waiting up to \"(\\d+)\" seconds?( if it exists)?$")
    @Override
    public String getTextFromElementIfExists(final String locator, final int waitTime, final String ifExistsOption) {
        if (getAutomatedBrowser() != null) {
            final String text = getAutomatedBrowser().getTextFromElementIfExists(
                    getSubstitutedString(locator),
                    waitTime,
                    ifExistsOption);
            aliases.put(LastReturn, text);
            return text;
        }

        aliases.put(LastReturn, null);
        return null;
    }

    @And("^I get group \"([^\"]*)\" from the regex \"([^\"]*)\" applied to text from the \"([^\"]*)\" \\w+(?:\\s+\\w+)*?( if it exists)?$")
    @Override
    public String getRegexGroupFromElementIfExists(
            final String group,
            final String regex,
            final String locator,
            final String ifExistsOption) {
        if (getAutomatedBrowser() != null) {
            final String text = getAutomatedBrowser().getRegexGroupFromElementIfExists(
                    getSubstitutedString(group),
                    getSubstitutedString(regex),
                    getSubstitutedString(locator),
                    ifExistsOption);
            aliases.put(LastReturn, text);
            return text;
        }

        aliases.put(LastReturn, null);
        return null;
    }

    @And("^I get group \"([^\"]*)\" from the regex \"([^\"]*)\" applied to text from the \"([^\"]*)\" \\w+(?:\\s+\\w+)* waiting up to \"(\\d+)\" seconds?( if it exists)?$")
    @Override
    public String getRegexGroupFromElementIfExists(
            final String group,
            final String regex,
            final String locator,
            final int waitTime,
            final String ifExistsOption) {
        if (getAutomatedBrowser() != null) {
            final String text = getAutomatedBrowser().getRegexGroupFromElementIfExists(
                    getSubstitutedString(group),
                    getSubstitutedString(regex),
                    getSubstitutedString(locator),
                    waitTime,
                    ifExistsOption);
            aliases.put(LastReturn, text);
            return text;
        }

        aliases.put(LastReturn, null);
        return null;
    }

    @Then("^I verify the text from the \"([^\"]*)\" \\w+(?:\\s+\\w+)* matches the regex \"([^\"]*)\"( if it exists)?$")
    @Override
    public void verifyTextFromElementIfExists(final String locator, final String regex, final String ifExistsOption) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().verifyTextFromElementIfExists(
                    getSubstitutedString(locator),
                    getSubstitutedString(regex),
                    ifExistsOption);
        }
    }

    @Then("^I verify the text from the \"([^\"]*)\" \\w+(?:\\s+\\w+)* matches the regex \"([^\"]*)\" waiting up to \"(\\d+)\" seconds?( if it exists)?$")
    @Override
    public void verifyTextFromElementIfExists(final String locator, final String regex, final int waitTime, final String ifExistsOption) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().verifyTextFromElementIfExists(
                    getSubstitutedString(locator),
                    getSubstitutedString(regex),
                    waitTime,
                    ifExistsOption);
        }
    }

    @Override
    @And("^I scroll the \"([^\"]*)\" \\w+(?:\\s+\\w+)* into view(?: offset by \"([^\"]*)\")?( if it exists)?$")
    public void scrollElementIntoViewIfExists(final String locator, final String offset, final String ifExistsOption) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().scrollElementIntoViewIfExists(
                    getSubstitutedString(locator),
                    getSubstitutedString(offset),
                    ifExistsOption);
        }
    }

    @Override
    @And("^I scroll the \"([^\"]*)\" \\w+(?:\\s+\\w+)* into view(?: offset by \"([^\"]*)\")? waiting up to \"(\\d+)\" seconds?( if it exists)?$")
    public void scrollElementIntoViewIfExists(final String locator, final String offset, final int waitTime, final String ifExistsOption) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().scrollElementIntoViewIfExists(
                    getSubstitutedString(locator),
                    getSubstitutedString(offset),
                    waitTime,
                    ifExistsOption);
        }
    }

    @And("^I capture the HAR file$")
    @Override
    public void captureHarFile() {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().captureHarFile();
        }
    }

    @And("^I capture the complete HAR file$")
    @Override
    public void captureCompleteHarFile() {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().captureCompleteHarFile();
        }
    }

    @And("^I save the HAR file to \"([^\"]*)\"$")
    @Override
    public void saveHarFile(final String file) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().saveHarFile(getSubstitutedString(file));
        }
    }

    @And("^I block the request to \"([^\"]*)\" returning the HTTP code \"(\\d+)\"$")
    @Override
    public void blockRequestTo(final String url, final int responseCode) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().blockRequestTo(
                    getSubstitutedString(url),
                    responseCode);
        }
    }

    @And("^I alter the response from \"([^\"]*)\" returning the HTTP code \"(\\d+)\" and the response body:$")
    @Override
    public void alterResponseFrom(final String url, final int responseCode, final String responseBody) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().alterResponseFrom(
                    getSubstitutedString(url),
                    responseCode,
                    getSubstitutedString(responseBody));
        }
    }

    @Override
    public List<Pair<String, Integer>> getErrors() {
        if (getAutomatedBrowser() != null) {
            return getAutomatedBrowser().getErrors();
        }

        return List.of();
    }

    @And("^I verify there were no network errors$")
    public void verifyNoErrorsInHar() {
        final var errors = getErrors();

        if (!errors.isEmpty()) {
            errors.forEach(e -> LOGGER.warning("Request to " + e.getKey() + " returned " + e.getRight()));
            throw new NetworkException("Errors found in the HAR file");
        }
    }

    @And("^I maximize the window$")
    @Override
    public void maximizeWindow() {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().maximizeWindow();
        }
    }

    @And("^I highlight( outside| inside)?( and lift)? the \"([^\"]*)\" \\w+(?:\\s+\\w+)*?(?: with an offset of \"([^\"]*)\")?( if it exists)?$")
    @Override
    public void elementHighlightIfExists(final String position, final String lift, final String locator, final String offset, final String ifExistsOption) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().elementHighlightIfExists(
                    position,
                    lift,
                    getSubstitutedString(locator),
                    getSubstitutedString(offset),
                    ifExistsOption);
        }
    }

    @And("^I highlight( outside| inside)?( and lift)? the \"([^\"]*)\" \\w+(?:\\s+\\w+)*?(?: with an offset of \"([^\"]*)\")? waiting up to \"(\\d+)\" seconds?( if it exists)?$")
    @Override
    public void elementHighlightIfExists(final String position, final String lift, final String locator, final String offset, final int waitTime, final String ifExistsOption) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().elementHighlightIfExists(
                    position,
                    lift,
                    getSubstitutedString(locator),
                    getSubstitutedString(offset),
                    waitTime,
                    ifExistsOption);
        }
    }

    @And("^I remove the highlight from the \"([^\"]*)\" \\w+(?:\\s+\\w+)*?( if it exists)?$")
    @Override
    public void removeElementHighlight(final String locator, final String ifExistsOption) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().removeElementHighlight(getSubstitutedString(locator), ifExistsOption);
        }
    }

    @And("^I remove the highlight from the \"([^\"]*)\" \\w+(?:\\s+\\w+)* waiting up to \"(\\d+)\" seconds( if it exists)?$")
    @Override
    public void removeElementHighlight(final String locator, final int waitTime, final String ifExistsOption) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().removeElementHighlight(getSubstitutedString(locator), waitTime, ifExistsOption);
        }
    }

    @Then("^I verify the \"([^\"]*)\" \\w+(?:\\s+\\w+)* is present( if it exists)?$")
    @Override
    public void verifyElementExists(final String locator, final String ifExistsOption) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().verifyElementExists(
                    getSubstitutedString(locator),
                    ifExistsOption);
        }
    }

    @Then("^I verify the \"([^\"]*)\" \\w+(?:\\s+\\w+)* is present waiting up to \"(\\d+)\" seconds( if it exists)?$")
    @Override
    public void verifyElementExists(final String locator, final int waitTime, final String ifExistsOption) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().verifyElementExists(
                    getSubstitutedString(locator),
                    waitTime,
                    ifExistsOption);
        }
    }

    @Then("^I verify the \"([^\"]*)\" \\w+(?:\\s+\\w+)* is not present$")
    @Override
    public void verifyElementDoesNotExist(final String locator) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().verifyElementDoesNotExist(
                    getSubstitutedString(locator));
        }
    }

    @Then("^I verify the \"([^\"]*)\" \\w+(?:\\s+\\w+)* is not present waiting up to \"(\\d+)\" seconds$")
    @Override
    public void verifyElementDoesNotExist(final String locator, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().verifyElementDoesNotExist(
                    getSubstitutedString(locator),
                    waitTime);
        }
    }

    @And("^I get the text from the page title$")
    @Override
    public String getTitle() {
        if (getAutomatedBrowser() != null) {
            final String text = getAutomatedBrowser().getTitle();
            aliases.put(LastReturn, text);
            return text;
        }
        return null;
    }

    @And("^I press the escape key (?:on|in|from) the \"([^\"]*)\" \\w+(?:\\s+\\w+)*$")
    @Override
    public void pressEscape(final String locator) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().pressEscape(getSubstitutedString(locator));
        }
    }

    @And("^I press the escape key (?:on|in|from) the \"([^\"]*)\" \\w+(?:\\s+\\w+)* waiting up to \"(\\d+)\" seconds$")
    @Override
    public void pressEscape(final String locator, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().pressEscape(getSubstitutedString(locator), waitTime);
        }
    }

    @And("^I press the enter key (?:on|in|from) the \"([^\"]*)\" \\w+(?:\\s+\\w+)*$")
    @Override
    public void pressEnter(final String locator) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().pressEnter(getSubstitutedString(locator));
        }
    }

    @And("^I press the enter key (?:on|in|from) the \"([^\"]*)\" \\w+(?:\\s+\\w+)* waiting up to \"(\\d+)\" seconds$")
    @Override
    public void pressEnter(final String locator, int waitTime) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().pressEnter(getSubstitutedString(locator), waitTime);
        }
    }

    @Then("^I clear the transition$")
    @Override
    public void clearTransition() {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().clearTransition();
        }
    }

    @Then("^I fade the screen to \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" over \"([^\"]*)\" milliseconds$")
    @Override
    public void fadeScreen(String red, String green, String blue, String duration) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().fadeScreen(
                    getSubstitutedString(red),
                    getSubstitutedString(green),
                    getSubstitutedString(blue),
                    getSubstitutedString(duration));
        }
    }

    @Then("^I display a note with the text \"([^\"]*)\" for \"([^\"]*)\" seconds?")
    @Override
    public void displayNote(final String text, final String duration) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().displayNote(
                    getSubstitutedString(text),
                    getSubstitutedString(duration));
        }
    }

    @And("^I run the following JavaScript:$")
    @Override
    public void runJavascript(final String code) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().runJavascript(
                    getSubstitutedString(code));
        }
    }

    public int getInteractionCount() {
        if (getAutomatedBrowser() != null) {
            return getAutomatedBrowser().getInteractionCount();
        }
        return 0;
    }

    @And("^I set the Octopus step percentage to \"([^\"]*)\" with the message \"([^\"]*)\"$")
    @Override
    public void setOctopusPercent(final String percent, final String message) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().setOctopusPercent(
                    getSubstitutedString(percent),
                    getSubstitutedString(message));
        }
    }

    @And("^I write the value of the alias \"([^\"]*)\" to the Octopus variable \"([^\"]*)\"$")
    @Override
    public void writeAliasValueToOctopusVariable(final String alias, final String variable) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().writeAliasValueToOctopusVariable(
                    getSubstitutedString(alias),
                    getSubstitutedString(variable));
        }
    }

    @And("^I write the value of the alias \"([^\"]*)\" to the Octopus sensitive variable \"([^\"]*)\"$")
    @Override
    public void writeAliasValueToOctopusSensitiveVariable(final String alias, final String variable) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().writeAliasValueToOctopusSensitiveVariable(
                    getSubstitutedString(alias),
                    getSubstitutedString(variable));
        }
    }

    private String getSubstitutedString(final String string) {
        if (StringUtils.isEmpty(string)) {
            return string;
        }

        return new StringSubstitutor(getAliases(), "#{", "}")
                .setEnableSubstitutionInVariables(true)
                .replace(getAliases().getOrDefault(string, string));
    }
}