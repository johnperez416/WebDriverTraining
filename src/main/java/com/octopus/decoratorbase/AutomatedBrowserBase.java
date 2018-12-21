package com.octopus.decoratorbase;

import com.octopus.AutomatedBrowser;
import com.octopus.AutomatedBrowserFactory;
import com.octopus.utils.TimedResult;
import com.octopus.utils.impl.TimedExecutionImpl;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class AutomatedBrowserBase implements AutomatedBrowser {
    static private final String LastReturn = "LastReturn";
    static private final AutomatedBrowserFactory AUTOMATED_BROWSER_FACTORY = new AutomatedBrowserFactory();
    static private Map<String, String> externalAliases = new HashMap<>();
    private Map<String, String> aliases = new HashMap<>();
    private AutomatedBrowser automatedBrowser;
    private long totalWaitTime = 0;
    private long numberWaitCount = 0;

    private <T> T addNewTimedCall(final Callable<T> timedExecution) {
        final TimedResult<T> result =
                new TimedExecutionImpl<T>().timedExecution(timedExecution);
        ++totalWaitTime;
        numberWaitCount += result.getMillis();
        return result.getResult();
    }

    public static void setExternalAliases(final Map<String, String> externalAliases) {
        if (externalAliases == null) return;
        AutomatedBrowserBase.externalAliases.putAll(externalAliases);
    }

    public AutomatedBrowserBase() {

    }

    public AutomatedBrowserBase(final AutomatedBrowser automatedBrowser) {
        this.automatedBrowser = automatedBrowser;
    }

    public AutomatedBrowser getAutomatedBrowser() {
        return automatedBrowser;
    }

    @After
    public void afterScenario(final Scenario scenario) {
        if (scenario.isFailed()) {
            closeBrowser();
        }
    }

    @Given("^I set the following aliases:$")
    public void setAliases(Map<String, String> aliases) {
        this.aliases.putAll(aliases);
    }

    private Map<String, String> getAliases() {
        final Map<String, String> combinedAliases = new HashMap<>();
        combinedAliases.putAll(aliases);
        combinedAliases.putAll(externalAliases);
        return combinedAliases;
    }

    @Given("^I open the browser \"([^\"]*)\"$")
    public void openBrowser(String browser) {
        automatedBrowser = AUTOMATED_BROWSER_FACTORY.getAutomatedBrowser(browser);
        automatedBrowser.init();
    }

    @Given("^I close the browser$")
    public void closeBrowser() {
        if (automatedBrowser != null) {
            automatedBrowser.destroy();
            automatedBrowser = null;
        }
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

    @And("^I click the \\w+(?:\\s+\\w+)* with the id \"([^\"]*)\"$")
    @Override
    public void clickElementWithId(final String id) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().clickElementWithId(getAliases().getOrDefault(id, id));
                return null;
            });
        }
    }

    @And("^I click the \\w+(?:\\s+\\w+)* with the id \"([^\"]*)\" waiting up to \"(\\d+)\" seconds?$")
    @Override
    public void clickElementWithId(final String id, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().clickElementWithId(getAliases().getOrDefault(id, id), waitTime);
                return null;
            });
        }
    }

    @And("^I select the option \"([^\"]*)\" from the \\w+(?:\\s+\\w+)* with the id \"([^\"]*)\"$")
    @Override
    public void selectOptionByTextFromSelectWithId(final String optionText, final String id) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().selectOptionByTextFromSelectWithId(
                        getAliases().getOrDefault(optionText, optionText),
                        getAliases().getOrDefault(id, id));
                return null;
            });
        }
    }

    @And("^I select the option \"([^\"]*)\" from the \\w+(?:\\s+\\w+)* with the id \"([^\"]*)\" waiting up to \"(\\d+)\" seconds?$")
    @Override
    public void selectOptionByTextFromSelectWithId(final String optionText, final String id, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().selectOptionByTextFromSelectWithId(
                        getAliases().getOrDefault(optionText, optionText),
                        getAliases().getOrDefault(id, id),
                        waitTime);
                return null;
            });
        }
    }

    @And("^I populate the \\w+(?:\\s+\\w+)* with the id \"([^\"]*)\" with the text \"([^\"]*)\"$")
    @Override
    public void populateElementWithId(final String id, final String text) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().populateElementWithId(
                        getAliases().getOrDefault(id, id),
                        getAliases().getOrDefault(text, text));
                return null;
            });
        }
    }

    @And("^I populate the \\w+(?:\\s+\\w+)* with the id \"([^\"]*)\" with the text \"([^\"]*)\" waiting up to \"(\\d+)\" seconds?$")
    @Override
    public void populateElementWithId(final String id, final String text, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().populateElementWithId(
                        getAliases().getOrDefault(id, id),
                        getAliases().getOrDefault(text, text),
                        waitTime);
                return null;
            });
        }
    }

    @Override
    public String getTextFromElementWithId(final String id) {
        if (getAutomatedBrowser() != null) {
            return addNewTimedCall(() ->
                    getAutomatedBrowser().getTextFromElementWithId(getAliases().getOrDefault(id, id))
            );
        }

        return null;
    }

    @Override
    public String getTextFromElementWithId(final String id, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            return addNewTimedCall(() -> getAutomatedBrowser().getTextFromElementWithId(getAliases().getOrDefault(id, id), waitTime));
        }

        return null;
    }

    @And("^I click the \\w+(?:\\s+\\w+)* with the xpath \"([^\"]*)\"$")
    @Override
    public void clickElementWithXPath(final String xpath) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().clickElementWithXPath(getAliases().getOrDefault(xpath, xpath));
                return null;
            });
        }
    }

    @And("^I click the \\w+(?:\\s+\\w+)* with the xpath \"([^\"]*)\" waiting up to \"(\\d+)\" seconds?$")
    @Override
    public void clickElementWithXPath(final String xpath, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().clickElementWithXPath(getAliases().getOrDefault(xpath, xpath), waitTime);
                return null;
            });
        }
    }

    @And("^I select the option \"([^\"]*)\" from the \\w+(?:\\s+\\w+)* with the xpath \"([^\"]*)\"$")
    @Override
    public void selectOptionByTextFromSelectWithXPath(final String optionText, final String xpath) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().selectOptionByTextFromSelectWithXPath(
                        getAliases().getOrDefault(optionText, optionText),
                        getAliases().getOrDefault(xpath, xpath));
                return null;
            });
        }
    }

    @And("^I select the option \"([^\"]*)\" from the \\w+(?:\\s+\\w+)* with the xpath \"([^\"]*)\" waiting up to \"(\\d+)\" seconds?$")
    @Override
    public void selectOptionByTextFromSelectWithXPath(final String optionText, final String xpath, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().selectOptionByTextFromSelectWithXPath(
                        getAliases().getOrDefault(optionText, optionText),
                        getAliases().getOrDefault(xpath, xpath),
                        waitTime);
                return null;
            });
        }
    }

    @And("^I populate the \\w+(?:\\s+\\w+)* with the xpath \"([^\"]*)\" with the text \"([^\"]*)\"$")
    @Override
    public void populateElementWithXPath(final String xpath, final String text) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().populateElementWithXPath(
                        getAliases().getOrDefault(xpath, xpath),
                        getAliases().getOrDefault(text, text));
                return null;
            });
        }
    }

    @And("^I populate the \\w+(?:\\s+\\w+)* with the xpath \"([^\"]*)\" with the text \"([^\"]*)\" waiting up to \"(\\d+)\" seconds?$")
    @Override
    public void populateElementWithXPath(final String xpath, final String text, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().populateElementWithXPath(
                        getAliases().getOrDefault(xpath, xpath),
                        getAliases().getOrDefault(text, text),
                        waitTime);
                return null;
            });
        }
    }

    @Override
    public String getTextFromElementWithXPath(final String xpath) {
        if (getAutomatedBrowser() != null) {
            return addNewTimedCall(() -> getAutomatedBrowser().getTextFromElementWithXPath(getAliases().getOrDefault(xpath, xpath)));
        }

        return null;
    }

    @Override
    public String getTextFromElementWithXPath(final String xpath, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            return addNewTimedCall(() -> getAutomatedBrowser().getTextFromElementWithXPath(getAliases().getOrDefault(xpath, xpath), waitTime));
        }

        return null;
    }

    @And("^I click the \\w+(?:\\s+\\w+)* with the css selector \"([^\"]*)\"$")
    @Override
    public void clickElementWithCSSSelector(final String cssSelector) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().clickElementWithCSSSelector(getAliases().getOrDefault(cssSelector, cssSelector));
                return null;
            });
        }
    }

    @And("^I click the \\w+(?:\\s+\\w+)* with the css selector \"([^\"]*)\" waiting up to \"(\\d+)\" seconds?$")
    @Override
    public void clickElementWithCSSSelector(final String cssSelector, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().clickElementWithCSSSelector(getAliases().getOrDefault(cssSelector, cssSelector), waitTime);
                return null;
            });
        }
    }

    @And("^I select the option \"([^\"]*)\" from the \\w+(?:\\s+\\w+)* with the css selector \"([^\"]*)\"$")
    @Override
    public void selectOptionByTextFromSelectWithCSSSelector(final String optionText, final String cssSelector) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().selectOptionByTextFromSelectWithCSSSelector(
                        getAliases().getOrDefault(optionText, optionText),
                        getAliases().getOrDefault(cssSelector, cssSelector));
                return null;
            });
        }
    }

    @And("^I select the option \"([^\"]*)\" from the \\w+(?:\\s+\\w+)* with the css selector \"([^\"]*)\" waiting up to \"(\\d+)\" seconds?$")
    @Override
    public void selectOptionByTextFromSelectWithCSSSelector(final String optionText, final String cssSelector, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().selectOptionByTextFromSelectWithCSSSelector(
                        getAliases().getOrDefault(optionText, optionText),
                        getAliases().getOrDefault(cssSelector, cssSelector),
                        waitTime);
                return null;
            });
        }
    }

    @And("^I populate the \\w+(?:\\s+\\w+)* with the css selector \"([^\"]*)\" with the text \"([^\"]*)\"$")
    @Override
    public void populateElementWithCSSSelector(final String cssSelector, final String text) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().populateElementWithCSSSelector(
                        getAliases().getOrDefault(cssSelector, cssSelector),
                        getAliases().getOrDefault(text, text));
                return null;
            });
        }
    }

    @And("^I populate the \\w+(?:\\s+\\w+)* with the css selector \"([^\"]*)\" with the text \"([^\"]*)\" waiting up to \"(\\d+)\" seconds?$")
    @Override
    public void populateElementWithCSSSelector(final String cssSelector, final String text, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().populateElementWithCSSSelector(
                        getAliases().getOrDefault(cssSelector, cssSelector),
                        getAliases().getOrDefault(text, text),
                        waitTime);
                return null;
            });
        }
    }

    @Override
    public String getTextFromElementWithCSSSelector(final String cssSelector) {
        if (getAutomatedBrowser() != null) {
            return addNewTimedCall(() -> getAutomatedBrowser().getTextFromElementWithCSSSelector(
                    getAliases().getOrDefault(cssSelector, cssSelector)));
        }

        return null;
    }

    @Override
    public String getTextFromElementWithCSSSelector(final String cssSelector, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            return addNewTimedCall(() -> getAutomatedBrowser().getTextFromElementWithCSSSelector(
                    getAliases().getOrDefault(cssSelector, cssSelector),
                    waitTime));
        }

        return null;
    }

    @And("^I click the \\w+(?:\\s+\\w+)* with the name \"([^\"]*)\"$")
    @Override
    public void clickElementWithName(final String name) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().clickElementWithName(getAliases().getOrDefault(name, name));
                return null;
            });
        }
    }

    @And("^I click the \\w+(?:\\s+\\w+)* with the name \"([^\"]*)\" waiting up to \"(\\d+)\" seconds?$")
    @Override
    public void clickElementWithName(final String name, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().clickElementWithName(getAliases().getOrDefault(name, name), waitTime);
                return null;
            });
        }
    }

    @And("^I select the option \"([^\"]*)\" from the \\w+(?:\\s+\\w+)* with the name \"([^\"]*)\"$")
    @Override
    public void selectOptionByTextFromSelectWithName(final String optionText, final String name) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().selectOptionByTextFromSelectWithName(
                        getAliases().getOrDefault(optionText, optionText),
                        getAliases().getOrDefault(name, name));
                return null;
            });
        }
    }

    @And("^I select the option \"([^\"]*)\" from the \\w+(?:\\s+\\w+)* with the name \"([^\"]*)\" waiting up to \"(\\d+)\" seconds?$")
    @Override
    public void selectOptionByTextFromSelectWithName(final String optionText, final String name, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().selectOptionByTextFromSelectWithName(
                        getAliases().getOrDefault(optionText, optionText),
                        getAliases().getOrDefault(name, name),
                        waitTime);
                return null;
            });
        }
    }

    @And("^I populate the \\w+(?:\\s+\\w+)* with the name \"([^\"]*)\" with the text \"([^\"]*)\"$")
    @Override
    public void populateElementWithName(final String name, final String text) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().populateElementWithName(
                        getAliases().getOrDefault(name, name),
                        getAliases().getOrDefault(text, text));
                return null;
            });
        }
    }

    @And("^I populate the \\w+(?:\\s+\\w+)* with the name \"([^\"]*)\" with the text \"([^\"]*)\" waiting up to \"(\\d+)\" seconds?$")
    @Override
    public void populateElementWithName(final String name, final String text, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().populateElementWithName(
                        getAliases().getOrDefault(name, name),
                        getAliases().getOrDefault(text, text),
                        waitTime);
                return null;
            });
        }
    }

    @Override
    public String getTextFromElementWithName(final String name) {
        if (getAutomatedBrowser() != null) {
            return addNewTimedCall(() -> getAutomatedBrowser().getTextFromElementWithName(getAliases().getOrDefault(name, name)));
        }

        return null;
    }

    @Override
    public String getTextFromElementWithName(final String name, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            return addNewTimedCall(() -> getAutomatedBrowser().getTextFromElementWithName(
                    getAliases().getOrDefault(name, name),
                    waitTime));
        }

        return null;
    }

    @And("^I click the \"([^\"]*)\" \\w+(?:\\s+\\w+)*$")
    @Override
    public void clickElement(final String locator) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().clickElement(getAliases().getOrDefault(locator, locator));
                return null;
            });
        }
    }

    @And("^I click the \"([^\"]*)\" \\w+(?:\\s+\\w+)* waiting up to \"(\\d+)\" seconds?$")
    @Override
    public void clickElement(final String locator, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().clickElement(getAliases().getOrDefault(locator, locator), waitTime);
                return null;
            });
        }
    }

    @And("^I select the option \"([^\"]*)\" from the \"([^\"]*)\" \\w+(?:\\s+\\w+)*$")
    @Override
    public void selectOptionByTextFromSelect(final String optionText, final String locator) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().selectOptionByTextFromSelect(
                        getAliases().getOrDefault(optionText, optionText),
                        getAliases().getOrDefault(locator, locator));
                return null;
            });
        }
    }

    @And("^I select the option \"([^\"]*)\" from the \"([^\"]*)\" \\w+(?:\\s+\\w+)* waiting up to \"(\\d+)\" seconds?$")
    @Override
    public void selectOptionByTextFromSelect(final String optionText, final String locator, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().selectOptionByTextFromSelect(
                        getAliases().getOrDefault(optionText, optionText),
                        getAliases().getOrDefault(locator, locator),
                        waitTime);
                return null;
            });
        }
    }

    @And("^I populate the \"([^\"]*)\" \\w+(?:\\s+\\w+)* with the text \"([^\"]*)\"$")
    @Override
    public void populateElement(final String locator, final String text) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().populateElement(
                        getAliases().getOrDefault(locator, locator),
                        getAliases().getOrDefault(text, text));
                return null;
            });
        }
    }

    @And("^I populate the \"([^\"]*)\" \\w+(?:\\s+\\w+)* with the text \"([^\"]*)\" waiting up to \"(\\d+)\" seconds?$")
    @Override
    public void populateElement(final String locator, final String text, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().populateElement(
                        getAliases().getOrDefault(locator, locator),
                        getAliases().getOrDefault(text, text),
                        waitTime);
                return null;
            });
        }
    }

    @And("^I get the text from the \"([^\"]*)\" \\w+(?:\\s+\\w+)*$")
    @Override
    public String getTextFromElement(final String locator) {
        if (getAutomatedBrowser() != null) {
            return addNewTimedCall(() -> {
                final String text = getAutomatedBrowser().getTextFromElement(getAliases().getOrDefault(locator, locator));
                aliases.put(LastReturn, text);
                return text;
            });
        }

        aliases.put(LastReturn, null);
        return null;
    }

    @And("^I get the text from the \"([^\"]*)\" \\w+(?:\\s+\\w+)* waiting up to \"(\\d+)\" seconds?$")
    @Override
    public String getTextFromElement(final String locator, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            return addNewTimedCall(() -> {
                final String text = getAutomatedBrowser().getTextFromElement(
                        getAliases().getOrDefault(locator, locator),
                        waitTime);
                aliases.put(LastReturn, text);
                return text;
            });
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
            return addNewTimedCall(() -> {
                final String text = getAutomatedBrowser().getRegexGroupFromElement(
                        getAliases().getOrDefault(group, group),
                        getAliases().getOrDefault(regex, regex),
                        getAliases().getOrDefault(locator, locator));
                aliases.put(LastReturn, text);
                return text;
            });
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
            return addNewTimedCall(() -> {
                final String text = getAutomatedBrowser().getRegexGroupFromElement(
                        getAliases().getOrDefault(group, group),
                        getAliases().getOrDefault(regex, regex),
                        getAliases().getOrDefault(locator, locator),
                        waitTime);
                aliases.put(LastReturn, text);
                return text;
            });
        }

        aliases.put(LastReturn, null);
        return null;
    }

    @Then("^I verify the text from the \"([^\"]*)\" \\w+(?:\\s+\\w+)* matches the regex \"([^\"]*)\"$")
    @Override
    public void verifyTextFromElement(final String locator, final String regex) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().verifyTextFromElement(
                        getAliases().getOrDefault(locator, locator),
                        getAliases().getOrDefault(regex, regex));
                return null;
            });
        }
    }

    @Then("^I verify the text from the \"([^\"]*)\" \\w+(?:\\s+\\w+)* matches the regex \"([^\"]*)\" waiting up to \"(\\d+)\" seconds?$")
    @Override
    public void verifyTextFromElement(final String locator, final String regex, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
            getAutomatedBrowser().verifyTextFromElement(
                    getAliases().getOrDefault(locator, locator),
                    getAliases().getOrDefault(regex, regex),
                    waitTime);
                return null;
            });
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