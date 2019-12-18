package com.octopus;

import org.apache.commons.lang3.tuple.Pair;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.List;
import java.util.concurrent.CompletableFuture;

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

    void refreshIfExists(String locator, String doesNotExist);

    void refreshIfExists(String locator, String doesNotExist, int waitTime);

    void maximizeWindow();

    void fullscreen();

    default void setWindowSize(int width, int height) {setWindowSize(width + "", height + "");}

    void setWindowSize(String width, String height);

    void scrollDown(String distance);

    void scrollUp(String distance);

    void verifyUrl(String regex);

    void browserZoomIn();

    void browserZoomOut();
    //</editor-fold>

    //<editor-fold desc="Aliases">
    void dumpAliases();

    void writeAliasValueToFile(String alias, String filename);

    void copyLastReturnAliasTo(String newAlias);
    //</editor-fold>

    //<editor-fold desc="Keyboard Interaction">
    default void pressEscape(String locator) {pressEscape(null, locator);}

    void pressEscape(String force, String locator);

    void pressEscape(String force, String locator, int waitTime);

    default void pressEnter(String locator) {pressEnter(null, locator);}

    void pressEnter(String force, String locator);

    void pressEnter(String force, String locator, int waitTime);

    void pressFunctionKey(String key);
    //</editor-fold>

    //<editor-fold desc="Screenshots and Recording">
    void startScreenRecording(final String file, final String capturedArtifact);

    void stopScreenRecording();

    CompletableFuture<Void> takeScreenshot(String filename, boolean force, String captureArtifact);

    CompletableFuture<Void> takeScreenshot(String filename, String captureArtifact);

    CompletableFuture<Void> takeScreenshot(String directory, String filename, String captureArtifact);
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

    void clickElementIfOtherExists(String force, String locator, String ifOtherExists);

    void clickElementIfOtherExists(String force, String locator, int waitTime, String ifOtherExists);

    default void clickElementIfOtherExists(String locator, String ifOtherExists) {clickElementIfExists(null, locator, ifOtherExists);}

    default void clickElementIfOtherExists(String locator, int waitTime, String ifOtherExists) {clickElementIfExists(null, locator, waitTime, ifOtherExists);}

    void clickElementIfOtherNotExists(String force, String locator, String ifOtherExists);

    void clickElementIfOtherNotExists(String force, String locator, int waitTime, String ifOtherExists);

    default void clickElementIfOtherNotExists(String locator, String ifOtherExists) {clickElementIfExists(null, locator, ifOtherExists);}

    default void clickElementIfOtherNotExists(String locator, int waitTime, String ifOtherExists) {clickElementIfExists(null, locator, waitTime, ifOtherExists);}

    void clickElementIfExists(String force, String locator, String ifExistsOption);

    void clickElementIfExists(String force, String locator, int waitTime, String ifExistsOption);

    default void clickElement(String locator) {
        clickElementIfExists(locator, null);}

    default void clickElement(String locator, int waitTime) {
        clickElementIfExists(locator, waitTime,null);}

    default void clickElementIfExists(String locator, String ifExistsOption) {clickElementIfExists(null, locator, ifExistsOption);}

    default void clickElementIfExists(String locator, int waitTime, String ifExistsOption) {clickElementIfExists(null, locator, waitTime, ifExistsOption);}

    default void selectOptionByTextFromSelect(String optionText, String locator) {
        selectOptionByTextFromSelectIfExists(optionText, locator, null);}

    default void selectOptionByTextFromSelect(String optionText, String locator, int waitTime) {
        selectOptionByTextFromSelectIfExists(optionText, locator, waitTime, null);}

    void selectOptionByTextFromSelectIfExists(String optionText, String locator, String ifExistsOption);

    void selectOptionByTextFromSelectIfExists(String optionText, String locator, int waitTime, String ifExistsOption);

    default void selectOptionByValueFromSelect(String optionValue, String locator) {
        selectOptionByValueFromSelectIfExists(optionValue, locator, null);}

    default void selectOptionByValueFromSelect(String optionValue, String locator, int waitTime) {
        selectOptionByValueFromSelectIfExists(optionValue, locator, waitTime, null);}

    void selectOptionByValueFromSelectIfExists(String optionValue, String locator, String ifExistsOption);

    void selectOptionByValueFromSelectIfExists(String optionValue, String locator, int waitTime, String ifExistsOption);

    default void populateElement(String locator, String text) {populateElement(null, locator, "0", text, null);}

    default void populateElement(String locator, String text, int waitTime) {populateElement(null, locator, "0", text, waitTime, null);}

    default void populateElement(String force, String locator, String keystrokeDelay, String text) {populateElement(force, locator, keystrokeDelay, text, null);}

    default void populateElement(String locator, String keystrokeDelay, String text, int waitTime) {populateElement(null, locator, keystrokeDelay, text, waitTime, null);}

    void populateElement(String force, String locator, String keystrokeDelay, String text, String ifExistsOption);

    void populateElement(String force, String locator, String keystrokeDelay, String text, int waitTime, String ifExistsOption);

    void populateHiddenElement(String force, String locator, String text, int waitTime, String ifExistsOption);

    void populateHiddenElement(String force, String locator, String text, String ifExistsOption);

    default void clear(String locator) {
        clearIfExists(null, locator, null);}

    default void clear(String locator, int waitTime) {
        clearIfExists(null, locator, waitTime, null);}

    void clearIfExists(String force, String locator, String ifExistsOption);

    void clearIfExists(String force, String locator, int waitTime, String ifExistsOption);

    default void mouseOver(String locator) {
        mouseOverIfExists(null, locator, null);}

    default void mouseOver(String locator, int waitTime) {
        mouseOverIfExists(null, locator, waitTime, null);}

    void mouseOverIfExists(String force, String locator, String ifExistsOption);

    void mouseOverIfExists(String force, String locator, int waitTime, String ifExistsOption);

    default void focus(String locator) {
        focusIfExists(null, locator, null);}

    default void focus(String locator, int waitTime) {
        focusIfExists(null, locator, waitTime, null);}

    void focusIfExists(String force, String locator, String ifExistsOption);

    void focusIfExists(String force, String locator, int waitTime, String ifExistsOption);

    default String getTextFromElement(String locator) {return getTextFromElementIfExists(locator, null);}

    default String getTextFromElement(String locator, int waitTime) {return getTextFromElementIfExists(locator, waitTime, null);}

    String getTextFromElementIfExists(String locator, String ifExistsOption);

    String getTextFromElementIfExists(String locator, int waitTime, String ifExistsOption);

    default String getRegexGroupFromElement(String group, String regex, String locator) {return getRegexGroupFromElementIfExists(group, regex, locator, null);}

    default String getRegexGroupFromElement(String group, String regex, String locator, int waitTime) {return getRegexGroupFromElementIfExists(group, regex, locator, waitTime, null);}

    String getRegexGroupFromElementIfExists(String group, String regex, String locator, String ifExistsOption);

    String getRegexGroupFromElementIfExists(String group, String regex, String locator, int waitTime, String ifExistsOption);

    default void verifyTextFromElement(String locator, String regex) {verifyTextFromElementIfExists(locator, regex, null);}

    default void verifyTextFromElement(String locator, String regex, int waitTime) {verifyTextFromElementIfExists(locator, regex, waitTime, null);}

    void verifyTextFromElementIfExists(String locator, String regex, String ifExistsOption);

    void verifyTextFromElementIfExists(String locator, String regex, int waitTime, String ifExistsOption);

    default void scrollElementIntoView(String locator, String offset) {
        scrollElementIntoViewIfExists(locator, offset, null);}

    default void scrollElementIntoView(String locator, String offset, int waitTime) {
        scrollElementIntoViewIfExists(locator, offset, waitTime, null);}

    void scrollElementIntoViewIfExists(String locator, String offset, String ifExistsOption);

    void scrollElementIntoViewIfExists(String locator, String offset, int waitTime, String ifExistsOption);

    default void elementHighlight(String location, String lift, String locator, final String offset) {
        elementHighlightIfExists(location, lift, locator, offset, null);}

    default void elementHighlight(String location, String lift, String locator, final String offset, int waitTime) {
        elementHighlightIfExists(location, lift, locator, offset, waitTime, null);}

    void elementHighlightIfExists(String location, String lift, String locator, final String offset, String ifExistsOption);

    void elementHighlightIfExists(String location, String lift, String locator, String offset, int waitTime, String ifExistsOption);

    void removeElementHighlight(String locator, final String ifExistsOption);

    void removeElementHighlight(String locator, int waitTime, final String ifExistsOption);

    default void verifyElementExists(String locator) {verifyElementExists(locator, null);}

    void verifyElementExists(String locator, String ifExistsOption);

    void verifyElementExists(String locator, int waitTime, String ifExistsOption);

    void verifyElementDoesNotExist(String locator);

    void verifyElementDoesNotExist(String locator, int waitTime);

    String getTitle();
    //</editor-fold>

    //<editor-fold desc="Network Alter and Capture">
    void captureHarFile();

    void captureCompleteHarFile();

    void saveHarFile(String file);

    void blockRequestTo(String url, int responseCode);

    void alterResponseFrom(String url, int responseCode, String responseBody);

    List<Pair<String, Integer>> getErrors();
    //</editor-fold>

    //<editor-fold desc="Transitions and Annotations">
    void clearTransition();

    void fadeScreen(String red, String green, String blue, String duration);

    void displayNote(String text, String duration);
    //</editor-fold>

    //<editor-fold desc="Raw JavaScript">
    void runJavascript(String code);
    //</editor-fold>

    //<editor-fold desc="Statistics">
    int getInteractionCount();
    //</editor-fold>

    //<editor-fold desc="Octopus">
    void setOctopusPercent(String percent, String message);

    void writeAliasValueToOctopusVariable(String alias, String variable);

    void writeAliasValueToOctopusSensitiveVariable(String alias, String variable);

    void defineArtifact(String name, String path);
    //</editor-fold>

    //<editor-fold desc="Github Actions">
    void setGithubEnvironmentVariable(String name, String value);
    void setGithubOutputParameter(String name, String value);
    void addGithubSystemPath(String path);
    void setGithubDebugMessage(String message);
    void setGithubWarningMessage(String message);
    void setGithubErrorMessage(String message);
    void maskGithubValue(String value);
    void stopGithubLogging(String token);
    void startGithubLogging(String token);
    //</editor-fold>
}