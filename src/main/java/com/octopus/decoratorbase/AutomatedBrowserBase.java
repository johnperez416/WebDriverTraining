package com.octopus.decoratorbase;

import com.octopus.AutomatedBrowser;
import com.octopus.AutomatedBrowserFactory;
import com.octopus.exceptions.BrowserException;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.HashMap;
import java.util.Map;

public class AutomatedBrowserBase implements AutomatedBrowser {
    static private final String LastReturn = "LastReturn";
    static private final AutomatedBrowserFactory AUTOMATED_BROWSER_FACTORY = new AutomatedBrowserFactory();
    private Map<String, String> aliases = new HashMap<>();
    static private Map<String, String> externalAliases = new HashMap<>();
    private AutomatedBrowser automatedBrowser;
    private static AutomatedBrowser sharedAutomatedBrowser;

    public AutomatedBrowserBase() {

    }

    public AutomatedBrowserBase(final AutomatedBrowser automatedBrowser) {
        this.automatedBrowser = automatedBrowser;
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
        }
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

    @Given("^I set the following aliases:$")
    public void setAliases(Map<String, String> aliases) {
        this.aliases.putAll(aliases);
    }

    @Override
    @And("^I (?:sleep|wait) for \"([^\"]*)\" seconds$")
    public void sleep(String seconds) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().sleep(getAliases().getOrDefault(seconds, seconds));
        }
    }

    @Given("^I open the( shared)? browser \"([^\"]*)\"$")
    public void openBrowser(String shared, String browser) {
        if (sharedAutomatedBrowser != null) {
            throw new BrowserException("Can not open a browser with an existing shared browser.");
        }

        if (shared != null) {
            automatedBrowser = sharedAutomatedBrowser = AUTOMATED_BROWSER_FACTORY.getAutomatedBrowser(browser);
            automatedBrowser.init();
        } else {
            automatedBrowser = AUTOMATED_BROWSER_FACTORY.getAutomatedBrowser(browser);
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
    public void setDefaultExplicitWaitTime(int waitTime) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().setDefaultExplicitWaitTime(waitTime);
        }
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
    public void goTo(String url) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().goTo(getAliases().getOrDefault(url, url));
        }
    }

    @And("^I start recording the screen to the directory \"([^\"]*)\"$")
    @Override
    public void startScreenRecording(final String file) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().startScreenRecording(getAliases().getOrDefault(file, file));
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
        System.out.println("Start of alias dump");
        getAliases().entrySet().forEach(entrySet -> System.out.println(entrySet.getKey() + ": " + entrySet.getValue()));
    }

    @And("^I save a screenshot to \"([^\"]*)\"$")
    @Override
    public void takeScreenshot(String filename) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().takeScreenshot(getAliases().getOrDefault(filename, filename));
        }
    }

    @And("^I set the window size to \"([^\"]*)\" x \"([^\"]*)\"$")
    @Override
    public void setWindowSize(final String width, final String height) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().setWindowSize(
                    getAliases().getOrDefault(width, width),
                    getAliases().getOrDefault(height, height));
        }
    }

    @And("^I click the \\w+(?:\\s+\\w+)* with the id \"([^\"]*)\"$")
    @Override
    public void clickElementWithId(final String id) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().clickElementWithId(getAliases().getOrDefault(id, id));
        }
    }

    @And("^I click the \\w+(?:\\s+\\w+)* with the id \"([^\"]*)\" waiting up to \"(\\d+)\" seconds?$")
    @Override
    public void clickElementWithId(final String id, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().clickElementWithId(getAliases().getOrDefault(id, id), waitTime);
        }
    }

    @And("^I select the option \"([^\"]*)\" from the \\w+(?:\\s+\\w+)* with the id \"([^\"]*)\"$")
    @Override
    public void selectOptionByTextFromSelectWithId(final String optionText, final String id) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().selectOptionByTextFromSelectWithId(
                    getAliases().getOrDefault(optionText, optionText),
                    getAliases().getOrDefault(id, id));
        }
    }

    @And("^I select the option \"([^\"]*)\" from the \\w+(?:\\s+\\w+)* with the id \"([^\"]*)\" waiting up to \"(\\d+)\" seconds?$")
    @Override
    public void selectOptionByTextFromSelectWithId(final String optionText, final String id, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().selectOptionByTextFromSelectWithId(
                    getAliases().getOrDefault(optionText, optionText),
                    getAliases().getOrDefault(id, id),
                    waitTime);
        }
    }

    @And("^I populate the \\w+(?:\\s+\\w+)* with the id \"([^\"]*)\" with the text \"([^\"]*)\"$")
    @Override
    public void populateElementWithId(final String id, final String text) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().populateElementWithId(
                    getAliases().getOrDefault(id, id),
                    getAliases().getOrDefault(text, text));
        }
    }

    @And("^I populate the \\w+(?:\\s+\\w+)* with the id \"([^\"]*)\" with the text \"([^\"]*)\" waiting up to \"(\\d+)\" seconds?$")
    @Override
    public void populateElementWithId(final String id, final String text, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().populateElementWithId(
                    getAliases().getOrDefault(id, id),
                    getAliases().getOrDefault(text, text),
                    waitTime);
        }
    }

    @Override
    public String getTextFromElementWithId(final String id) {
        if (getAutomatedBrowser() != null) {
            return getAutomatedBrowser().getTextFromElementWithId(getAliases().getOrDefault(id, id));
        }

        return null;
    }

    @Override
    public String getTextFromElementWithId(final String id, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            return getAutomatedBrowser().getTextFromElementWithId(getAliases().getOrDefault(id, id), waitTime);
        }

        return null;
    }

    @And("^I click the \\w+(?:\\s+\\w+)* with the xpath \"([^\"]*)\"$")
    @Override
    public void clickElementWithXPath(final String xpath) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().clickElementWithXPath(getAliases().getOrDefault(xpath, xpath));
        }
    }

    @And("^I click the \\w+(?:\\s+\\w+)* with the xpath \"([^\"]*)\" waiting up to \"(\\d+)\" seconds?$")
    @Override
    public void clickElementWithXPath(final String xpath, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().clickElementWithXPath(getAliases().getOrDefault(xpath, xpath), waitTime);
        }
    }

    @And("^I select the option \"([^\"]*)\" from the \\w+(?:\\s+\\w+)* with the xpath \"([^\"]*)\"$")
    @Override
    public void selectOptionByTextFromSelectWithXPath(final String optionText, final String xpath) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().selectOptionByTextFromSelectWithXPath(
                    getAliases().getOrDefault(optionText, optionText),
                    getAliases().getOrDefault(xpath, xpath));
        }
    }

    @And("^I select the option \"([^\"]*)\" from the \\w+(?:\\s+\\w+)* with the xpath \"([^\"]*)\" waiting up to \"(\\d+)\" seconds?$")
    @Override
    public void selectOptionByTextFromSelectWithXPath(final String optionText, final String xpath, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().selectOptionByTextFromSelectWithXPath(
                    getAliases().getOrDefault(optionText, optionText),
                    getAliases().getOrDefault(xpath, xpath),
                    waitTime);
        }
    }

    @And("^I populate the \\w+(?:\\s+\\w+)* with the xpath \"([^\"]*)\" with the text \"([^\"]*)\"$")
    @Override
    public void populateElementWithXPath(final String xpath, final String text) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().populateElementWithXPath(
                    getAliases().getOrDefault(xpath, xpath),
                    getAliases().getOrDefault(text, text));
        }
    }

    @And("^I populate the \\w+(?:\\s+\\w+)* with the xpath \"([^\"]*)\" with the text \"([^\"]*)\" waiting up to \"(\\d+)\" seconds?$")
    @Override
    public void populateElementWithXPath(final String xpath, final String text, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().populateElementWithXPath(
                    getAliases().getOrDefault(xpath, xpath),
                    getAliases().getOrDefault(text, text),
                    waitTime);
        }
    }

    @Override
    public String getTextFromElementWithXPath(final String xpath) {
        if (getAutomatedBrowser() != null) {
            return getAutomatedBrowser().getTextFromElementWithXPath(getAliases().getOrDefault(xpath, xpath));
        }

        return null;
    }

    @Override
    public String getTextFromElementWithXPath(final String xpath, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            return getAutomatedBrowser().getTextFromElementWithXPath(getAliases().getOrDefault(xpath, xpath), waitTime);
        }

        return null;
    }

    @And("^I click the \\w+(?:\\s+\\w+)* with the css selector \"([^\"]*)\"$")
    @Override
    public void clickElementWithCSSSelector(final String cssSelector) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().clickElementWithCSSSelector(getAliases().getOrDefault(cssSelector, cssSelector));
        }
    }

    @And("^I click the \\w+(?:\\s+\\w+)* with the css selector \"([^\"]*)\" waiting up to \"(\\d+)\" seconds?$")
    @Override
    public void clickElementWithCSSSelector(final String cssSelector, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().clickElementWithCSSSelector(getAliases().getOrDefault(cssSelector, cssSelector), waitTime);
        }
    }

    @And("^I select the option \"([^\"]*)\" from the \\w+(?:\\s+\\w+)* with the css selector \"([^\"]*)\"$")
    @Override
    public void selectOptionByTextFromSelectWithCSSSelector(final String optionText, final String cssSelector) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().selectOptionByTextFromSelectWithCSSSelector(
                    getAliases().getOrDefault(optionText, optionText),
                    getAliases().getOrDefault(cssSelector, cssSelector));
        }
    }

    @And("^I select the option \"([^\"]*)\" from the \\w+(?:\\s+\\w+)* with the css selector \"([^\"]*)\" waiting up to \"(\\d+)\" seconds?$")
    @Override
    public void selectOptionByTextFromSelectWithCSSSelector(final String optionText, final String cssSelector, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().selectOptionByTextFromSelectWithCSSSelector(
                    getAliases().getOrDefault(optionText, optionText),
                    getAliases().getOrDefault(cssSelector, cssSelector),
                    waitTime);
        }
    }

    @And("^I populate the \\w+(?:\\s+\\w+)* with the css selector \"([^\"]*)\" with the text \"([^\"]*)\"$")
    @Override
    public void populateElementWithCSSSelector(final String cssSelector, final String text) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().populateElementWithCSSSelector(
                    getAliases().getOrDefault(cssSelector, cssSelector),
                    getAliases().getOrDefault(text, text));
        }
    }

    @And("^I populate the \\w+(?:\\s+\\w+)* with the css selector \"([^\"]*)\" with the text \"([^\"]*)\" waiting up to \"(\\d+)\" seconds?$")
    @Override
    public void populateElementWithCSSSelector(final String cssSelector, final String text, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().populateElementWithCSSSelector(
                    getAliases().getOrDefault(cssSelector, cssSelector),
                    getAliases().getOrDefault(text, text),
                    waitTime);
        }
    }

    @Override
    public String getTextFromElementWithCSSSelector(final String cssSelector) {
        if (getAutomatedBrowser() != null) {
            return getAutomatedBrowser().getTextFromElementWithCSSSelector(
                    getAliases().getOrDefault(cssSelector, cssSelector));
        }

        return null;
    }

    @Override
    public String getTextFromElementWithCSSSelector(final String cssSelector, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            return getAutomatedBrowser().getTextFromElementWithCSSSelector(
                    getAliases().getOrDefault(cssSelector, cssSelector),
                    waitTime);
        }

        return null;
    }

    @And("^I click the \\w+(?:\\s+\\w+)* with the name \"([^\"]*)\"$")
    @Override
    public void clickElementWithName(final String name) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().clickElementWithName(getAliases().getOrDefault(name, name));
        }
    }

    @And("^I click the \\w+(?:\\s+\\w+)* with the name \"([^\"]*)\" waiting up to \"(\\d+)\" seconds?$")
    @Override
    public void clickElementWithName(final String name, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().clickElementWithName(getAliases().getOrDefault(name, name), waitTime);
        }
    }

    @And("^I select the option \"([^\"]*)\" from the \\w+(?:\\s+\\w+)* with the name \"([^\"]*)\"$")
    @Override
    public void selectOptionByTextFromSelectWithName(final String optionText, final String name) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().selectOptionByTextFromSelectWithName(
                    getAliases().getOrDefault(optionText, optionText),
                    getAliases().getOrDefault(name, name));
        }
    }

    @And("^I select the option \"([^\"]*)\" from the \\w+(?:\\s+\\w+)* with the name \"([^\"]*)\" waiting up to \"(\\d+)\" seconds?$")
    @Override
    public void selectOptionByTextFromSelectWithName(final String optionText, final String name, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().selectOptionByTextFromSelectWithName(
                    getAliases().getOrDefault(optionText, optionText),
                    getAliases().getOrDefault(name, name),
                    waitTime);
        }
    }

    @And("^I populate the \\w+(?:\\s+\\w+)* with the name \"([^\"]*)\" with the text \"([^\"]*)\"$")
    @Override
    public void populateElementWithName(final String name, final String text) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().populateElementWithName(
                    getAliases().getOrDefault(name, name),
                    getAliases().getOrDefault(text, text));
        }
    }

    @And("^I populate the \\w+(?:\\s+\\w+)* with the name \"([^\"]*)\" with the text \"([^\"]*)\" waiting up to \"(\\d+)\" seconds?$")
    @Override
    public void populateElementWithName(final String name, final String text, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().populateElementWithName(
                    getAliases().getOrDefault(name, name),
                    getAliases().getOrDefault(text, text),
                    waitTime);
        }
    }

    @Override
    public String getTextFromElementWithName(final String name) {
        if (getAutomatedBrowser() != null) {
            return getAutomatedBrowser().getTextFromElementWithName(getAliases().getOrDefault(name, name));
        }

        return null;
    }

    @Override
    public String getTextFromElementWithName(final String name, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            return getAutomatedBrowser().getTextFromElementWithName(
                    getAliases().getOrDefault(name, name),
                    waitTime);
        }

        return null;
    }

    @And("^I click the \"([^\"]*)\" \\w+(?:\\s+\\w+)*$")
    @Override
    public void clickElement(final String locator) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().clickElement(getAliases().getOrDefault(locator, locator));
        }
    }

    @And("^I click the \"([^\"]*)\" \\w+(?:\\s+\\w+)* waiting up to \"(\\d+)\" seconds?$")
    @Override
    public void clickElement(final String locator, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().clickElement(getAliases().getOrDefault(locator, locator), waitTime);
        }
    }

    @And("^I select the option \"([^\"]*)\" from the \"([^\"]*)\" \\w+(?:\\s+\\w+)*$")
    @Override
    public void selectOptionByTextFromSelect(final String optionText, final String locator) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().selectOptionByTextFromSelect(
                    getAliases().getOrDefault(optionText, optionText),
                    getAliases().getOrDefault(locator, locator));
        }
    }

    @And("^I select the option \"([^\"]*)\" from the \"([^\"]*)\" \\w+(?:\\s+\\w+)* waiting up to \"(\\d+)\" seconds?$")
    @Override
    public void selectOptionByTextFromSelect(final String optionText, final String locator, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().selectOptionByTextFromSelect(
                    getAliases().getOrDefault(optionText, optionText),
                    getAliases().getOrDefault(locator, locator),
                    waitTime);
        }
    }

    @And("^I populate the \"([^\"]*)\" \\w+(?:\\s+\\w+)* with the text \"([^\"]*)\"$")
    @Override
    public void populateElement(final String locator, final String text) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().populateElement(
                    getAliases().getOrDefault(locator, locator),
                    getAliases().getOrDefault(text, text));
        }
    }

    @And("^I populate the \"([^\"]*)\" \\w+(?:\\s+\\w+)* with the text \"([^\"]*)\" waiting up to \"(\\d+)\" seconds?$")
    @Override
    public void populateElement(final String locator, final String text, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().populateElement(
                    getAliases().getOrDefault(locator, locator),
                    getAliases().getOrDefault(text, text),
                    waitTime);
        }
    }

    @And("^I get the text from the \"([^\"]*)\" \\w+(?:\\s+\\w+)*$")
    @Override
    public String getTextFromElement(final String locator) {
        if (getAutomatedBrowser() != null) {
            final String text = getAutomatedBrowser().getTextFromElement(getAliases().getOrDefault(locator, locator));
            aliases.put(LastReturn, text);
            return text;
        }

        aliases.put(LastReturn, null);
        return null;
    }

    @And("^I get the text from the \"([^\"]*)\" \\w+(?:\\s+\\w+)* waiting up to \"(\\d+)\" seconds?$")
    @Override
    public String getTextFromElement(final String locator, final int waitTime) {
        if (getAutomatedBrowser() != null) {
                final String text = getAutomatedBrowser().getTextFromElement(
                        getAliases().getOrDefault(locator, locator),
                        waitTime);
                aliases.put(LastReturn, text);
                return text;
        }

        aliases.put(LastReturn, null);
        return null;
    }

    @And("^I get group \"([^\"]*)\" from the regex \"([^\"]*)\" applied to text from the \"([^\"]*)\" \\w+(?:\\s+\\w+)*$")
    @Override
    public String getRegexGroupFromElement(
            final String group,
            final String regex,
            final String locator) {
        if (getAutomatedBrowser() != null) {
                final String text = getAutomatedBrowser().getRegexGroupFromElement(
                        getAliases().getOrDefault(group, group),
                        getAliases().getOrDefault(regex, regex),
                        getAliases().getOrDefault(locator, locator));
                aliases.put(LastReturn, text);
                return text;
        }

        aliases.put(LastReturn, null);
        return null;
    }

    @And("^I get group \"([^\"]*)\" from the regex \"([^\"]*)\" applied to text from the \"([^\"]*)\" \\w+(?:\\s+\\w+)* waiting up to \"(\\d+)\" seconds?$")
    @Override
    public String getRegexGroupFromElement(
            final String group,
            final String regex,
            final String locator,
            final int waitTime) {
        if (getAutomatedBrowser() != null) {
                final String text = getAutomatedBrowser().getRegexGroupFromElement(
                        getAliases().getOrDefault(group, group),
                        getAliases().getOrDefault(regex, regex),
                        getAliases().getOrDefault(locator, locator),
                        waitTime);
                aliases.put(LastReturn, text);
                return text;
        }

        aliases.put(LastReturn, null);
        return null;
    }

    @Then("^I verify the text from the \"([^\"]*)\" \\w+(?:\\s+\\w+)* matches the regex \"([^\"]*)\"$")
    @Override
    public void verifyTextFromElement(final String locator, final String regex) {
        if (getAutomatedBrowser() != null) {
                getAutomatedBrowser().verifyTextFromElement(
                        getAliases().getOrDefault(locator, locator),
                        getAliases().getOrDefault(regex, regex));
        }
    }

    @Then("^I verify the text from the \"([^\"]*)\" \\w+(?:\\s+\\w+)* matches the regex \"([^\"]*)\" waiting up to \"(\\d+)\" seconds?$")
    @Override
    public void verifyTextFromElement(final String locator, final String regex, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().verifyTextFromElement(
                    getAliases().getOrDefault(locator, locator),
                    getAliases().getOrDefault(regex, regex),
                    waitTime);
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
            getAutomatedBrowser().saveHarFile(getAliases().getOrDefault(file, file));
        }
    }

    @And("^I block the request to \"([^\"]*)\" returning the HTTP code \"\\d+\"$")
    @Override
    public void blockRequestTo(final String url, final int responseCode) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().blockRequestTo(
                    getAliases().getOrDefault(url, url),
                    responseCode);
        }
    }

    @And("^I alter the response fron \"([^\"]*)\" returning the HTTP code \"\\d+\" and the response body:$")
    @Override
    public void alterResponseFrom(final String url, final int responseCode, final String responseBody) {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().alterResponseFrom(
                    getAliases().getOrDefault(url, url),
                    responseCode,
                    getAliases().getOrDefault(responseBody, responseBody));
        }
    }

    @And("^I maximize the window$")
    @Override
    public void maximizeWindow() {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().maximizeWindow();
        }
    }
}