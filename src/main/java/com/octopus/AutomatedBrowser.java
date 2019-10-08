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

    void sleep(String seconds);

    //<editor-fold desc="Browser Navigation and Window Interaction">
    void goTo(String url);

    void refresh();

    void maximizeWindow();

    void setWindowSize(String width, String height);

    void scrollDown(String distance);

    void scrollUp(String distance);
    //</editor-fold>

    //<editor-fold desc="Aliases">
    void dumpAliases();

    void writeAliasValueToFile(String alias, String filename);
    //</editor-fold>

    //<editor-fold desc="Keyboard Interaction">
    void pressEscape(String locator);

    void pressEscape(String locator, int waitTime);
    //</editor-fold>

    //<editor-fold desc="Screenshots and Recording">
    void startScreenRecording(final String file);

    void stopScreenRecording();

    void takeScreenshot(String filename, boolean force);

    void takeScreenshot(String filename);

    void takeScreenshot(String directory, String filename);
    //</editor-fold>

    //<editor-fold desc="ID Selection">
    void clickElementWithId(String id);

    void clickElementWithId(String id, int waitTime);

    void selectOptionByTextFromSelectWithId(String optionText, String id);

    void selectOptionByTextFromSelectWithId(String optionText, String id, int waitTime);

    void populateElementWithId(String id, String text);

    void populateElementWithId(String id, String text, int waitTime);

    String getTextFromElementWithId(String id);

    String getTextFromElementWithId(String id, int waitTime);
    //</editor-fold>

    //<editor-fold desc="XPath Selection">
    void clickElementWithXPath(String xpath);

    void clickElementWithXPath(String xpath, int waitTime);

    void selectOptionByTextFromSelectWithXPath(String optionText, String xpath);

    void selectOptionByTextFromSelectWithXPath(String optionText, String xpath, int waitTime);

    void populateElementWithXPath(String xpath, String text);

    void populateElementWithXPath(String xpath, String text, int waitTime);

    String getTextFromElementWithXPath(String xpath);

    String getTextFromElementWithXPath(String xpath, int waitTime);
    //</editor-fold>

    //<editor-fold desc="CSS Selection">
    void clickElementWithCSSSelector(String cssSelector);

    void clickElementWithCSSSelector(String cssSelector, int waitTime);

    void selectOptionByTextFromSelectWithCSSSelector(String optionText, String cssSelector);

    void selectOptionByTextFromSelectWithCSSSelector(String optionText, String cssSelector, int waitTime);

    void populateElementWithCSSSelector(String cssSelector, String text);

    void populateElementWithCSSSelector(String cssSelector, String text, int waitTime);

    String getTextFromElementWithCSSSelector(String cssSelector);

    String getTextFromElementWithCSSSelector(String cssSelector, int waitTime);
    //</editor-fold>

    //<editor-fold desc="Name Selection">
    void clickElementWithName(String name);

    void clickElementWithName(String name, int waitTime);

    void selectOptionByTextFromSelectWithName(String optionText, String name);

    void selectOptionByTextFromSelectWithName(String optionText, String name, int waitTime);

    void populateElementWithName(String name, String text);

    void populateElementWithName(String name, String text, int waitTime);

    String getTextFromElementWithName(String name);

    String getTextFromElementWithName(String name, int waitTime);
    //</editor-fold>

    //<editor-fold desc="Simple Selection">
    void clickElementIfExists(String force, String locator, String ifExists);

    void clickElementIfExists(String force, String locator, int waitTime, String ifExists);

    default void clickElement(String locator) {
        clickElementIfExists(locator, null);}

    default void clickElement(String locator, int waitTime) {
        clickElementIfExists(locator, waitTime,null);}

    void clickElementIfExists(String locator, String ifExists);

    void clickElementIfExists(String locator, int waitTime, String ifExists);

    default void selectOptionByTextFromSelect(String optionText, String locator) {
        selectOptionByTextFromSelectIfExists(optionText, locator, null);}

    default void selectOptionByTextFromSelect(String optionText, String locator, int waitTime) {
        selectOptionByTextFromSelectIfExists(optionText, locator, waitTime, null);}

    void selectOptionByTextFromSelectIfExists(String optionText, String locator, String ifExists);

    void selectOptionByTextFromSelectIfExists(String optionText, String locator, int waitTime, String ifExists);

    default void selectOptionByValueFromSelect(String optionValue, String locator) {
        selectOptionByValueFromSelectIfExists(optionValue, locator, null);}

    default void selectOptionByValueFromSelect(String optionValue, String locator, int waitTime) {
        selectOptionByValueFromSelectIfExists(optionValue, locator, waitTime, null);}

    void selectOptionByValueFromSelectIfExists(String optionValue, String locator, String ifExists);

    void selectOptionByValueFromSelectIfExists(String optionValue, String locator, int waitTime, String ifExists);

    default void populateElement(String locator, String text) {populateElement(locator, text, null);}

    default void populateElement(String locator, String text, int waitTime) {populateElement(locator, text, waitTime, null);}

    void populateElement(String locator, String text, String ifExists);

    void populateElement(String locator, String text, int waitTime, String ifExists);

    default void clear(String locator) {
        clearIfExists(locator, null);}

    default void clear(String locator, int waitTime) {
        clearIfExists(locator, waitTime, null);}

    void clearIfExists(String locator, String ifExists);

    void clearIfExists(String locator, int waitTime, String ifExists);

    default void mouseOver(String locator) {
        mouseOverIfExists(locator, null);}

    default void mouseOver(String locator, int waitTime) {
        mouseOverIfExists(locator, waitTime, null);}

    void mouseOverIfExists(String locator, String ifExists);

    void mouseOverIfExists(String locator, int waitTime, String ifExists);

    default String getTextFromElement(String locator) {return getTextFromElementIfExists(locator, null);}

    default String getTextFromElement(String locator, int waitTime) {return getTextFromElementIfExists(locator, waitTime, null);}

    String getTextFromElementIfExists(String locator, String ifExists);

    String getTextFromElementIfExists(String locator, int waitTime, String ifExists);

    default String getRegexGroupFromElement(String group, String regex, String locator) {return getRegexGroupFromElementIfExists(group, regex, locator, null);}

    default String getRegexGroupFromElement(String group, String regex, String locator, int waitTime) {return getRegexGroupFromElementIfExists(group, regex, locator, waitTime, null);}

    String getRegexGroupFromElementIfExists(String group, String regex, String locator, String ifExists);

    String getRegexGroupFromElementIfExists(String group, String regex, String locator, int waitTime, String ifExists);

    default void verifyTextFromElement(String locator, String regex) {verifyTextFromElementIfExists(locator, regex, null);}

    default void verifyTextFromElement(String locator, String regex, int waitTime) {verifyTextFromElementIfExists(locator, regex, waitTime, null);}

    void verifyTextFromElementIfExists(String locator, String regex, String ifExists);

    void verifyTextFromElementIfExists(String locator, String regex, int waitTime, String ifExists);

    default void scrollElementIntoView(String locator, String offset) {
        scrollElementIntoViewIfExists(locator, offset, null);}

    default void scrollElementIntoView(String locator, String offset, int waitTime) {
        scrollElementIntoViewIfExists(locator, offset, waitTime, null);}

    void scrollElementIntoViewIfExists(String locator, String offset, String ifExists);

    void scrollElementIntoViewIfExists(String locator, String offset, int waitTime, String ifExists);

    default void elementHighlight(String location, String locator, final String offset) {
        elementHighlightIfExists(location, locator, offset, null);}

    default void elementHighlight(String location, String locator, final String offset, int waitTime) {
        elementHighlightIfExists(location, locator, offset, waitTime, null);}

    void elementHighlightIfExists(String location, String locator, final String offset, String ifExists);

    void elementHighlightIfExists(String location, String locator, final String offset, int waitTime, String ifExists);

    void removeElementHighlight(String locator);

    void removeElementHighlight(String locator, int waitTime);
    //</editor-fold>

    //<editor-fold desc="Network Alter and Capture">
    void captureHarFile();

    void captureCompleteHarFile();

    void saveHarFile(String file);

    void blockRequestTo(String url, int responseCode);

    void alterResponseFrom(String url, int responseCode, String responseBody);
    //</editor-fold>

    //<editor-fold desc="Transitions and Annotations">
    void clearTransition();

    void fadeScreen(String red, String green, String blue, String duration);

    void displayNote(String text, String duration);
    //</editor-fold>
}