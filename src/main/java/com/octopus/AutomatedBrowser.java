package com.octopus;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public interface AutomatedBrowser {

    void setDefaultExplicitWaitTime(int waitTime);

    int getDefaultExplicitWaitTime();

    WebDriver getWebDriver();

    void setWebDriver(WebDriver webDriver);

    DesiredCapabilities getDesiredCapabilities();

    void init();

    void destroy();

    void goTo(String url);

    void startScreenRecording(final String file);

    void stopScreenRecording();

    void dumpAliases();

    void takeScreenshot(String filename);

    void setWindowSize(String width, String height);

    void sleep(String seconds);

    void clickElementWithId(String id);

    void clickElementWithId(String id, int waitTime);

    void selectOptionByTextFromSelectWithId(String optionText, String id);

    void selectOptionByTextFromSelectWithId(String optionText, String id, int waitTime);

    void populateElementWithId(String id, String text);

    void populateElementWithId(String id, String text, int waitTime);

    String getTextFromElementWithId(String id);

    String getTextFromElementWithId(String id, int waitTime);

    void clickElementWithXPath(String xpath);

    void clickElementWithXPath(String xpath, int waitTime);

    void selectOptionByTextFromSelectWithXPath(String optionText, String xpath);

    void selectOptionByTextFromSelectWithXPath(String optionText, String xpath, int waitTime);

    void populateElementWithXPath(String xpath, String text);

    void populateElementWithXPath(String xpath, String text, int waitTime);

    String getTextFromElementWithXPath(String xpath);

    String getTextFromElementWithXPath(String xpath, int waitTime);

    void clickElementWithCSSSelector(String cssSelector);

    void clickElementWithCSSSelector(String cssSelector, int waitTime);

    void selectOptionByTextFromSelectWithCSSSelector(String optionText, String cssSelector);

    void selectOptionByTextFromSelectWithCSSSelector(String optionText, String cssSelector, int waitTime);

    void populateElementWithCSSSelector(String cssSelector, String text);

    void populateElementWithCSSSelector(String cssSelector, String text, int waitTime);

    String getTextFromElementWithCSSSelector(String cssSelector);

    String getTextFromElementWithCSSSelector(String cssSelector, int waitTime);

    void clickElementWithName(String name);

    void clickElementWithName(String name, int waitTime);

    void selectOptionByTextFromSelectWithName(String optionText, String name);

    void selectOptionByTextFromSelectWithName(String optionText, String name, int waitTime);

    void populateElementWithName(String name, String text);

    void populateElementWithName(String name, String text, int waitTime);

    String getTextFromElementWithName(String name);

    String getTextFromElementWithName(String name, int waitTime);

    void clickElement(String locator);

    void clickElement(String locator, int waitTime);

    void selectOptionByTextFromSelect(String optionText, String locator);

    void selectOptionByTextFromSelect(String optionText, String locator, int waitTime);

    void populateElement(String locator, String text);

    void populateElement(String locator, String text, int waitTime);

    void mouseOver(String locator);

    void mouseOver(String locator, int waitTime);

    String getTextFromElement(String locator);

    String getTextFromElement(String locator, int waitTime);

    String getRegexGroupFromElement(String group, String regex, String locator);

    String getRegexGroupFromElement(String group, String regex, String locator, int waitTime);

    void verifyTextFromElement(String locator, String regex);

    void verifyTextFromElement(String locator, String regex, int waitTime);

    void scrollElementIntoView(String locator, String offset);

    void scrollElementIntoView(String locator, String offset, int waitTime);

    void captureHarFile();

    void captureCompleteHarFile();

    void saveHarFile(String file);

    void blockRequestTo(String url, int responseCode);

    void alterResponseFrom(String url, int responseCode, String responseBody);

    void maximizeWindow();

    void elementHighlight(String location, String locator, final String offset);

    void elementHighlight(String location, String locator, final String offset, int waitTime);

    void removeElementHighlight(String locator);

    void removeElementHighlight(String locator, int waitTime);
}