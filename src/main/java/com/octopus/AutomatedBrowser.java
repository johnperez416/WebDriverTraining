package com.octopus;

import org.apache.commons.lang3.tuple.Pair;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface AutomatedBrowser {

    /**
     * @return The default explicit wait time
     */
    int getDefaultExplicitWaitTime();

    /**
     * Defines how long to wait for by default for any elements located with an explicit wait time. This includes
     * all steps that use the simple locator syntax.
     *
     * @param waitTime The default wait time for elements located with an explicit wait.
     */
    void setDefaultExplicitWaitTime(int waitTime);

    /**
     * @return The WebDriver instance from the browser
     */
    WebDriver getWebDriver();

    /**
     * @param webDriver The WebDriver instance from the browser
     */
    void setWebDriver(WebDriver webDriver);

    /**
     * @return The desired capabilities used when building the WebDriver instance.
     */
    DesiredCapabilities getDesiredCapabilities();

    /**
     * Build the WebDriver instance.
     */
    void init();

    /**
     * Quit the WebDriver instance and perform any additional cleanup.
     */
    void destroy();

    /**
     * Sleep for a period of seconds.
     *
     * @param seconds The time to sleep for.
     */
    void sleep(String seconds);

    //<editor-fold desc="Browser Navigation and Window Interaction">

    /**
     * Open the supplied URL.
     *
     * @param url The URL to open.
     */
    void goTo(String url);

    /**
     * Refresh the page
     */
    void refresh();

    /**
     * Refresh the page if an element exists on the page or does not exit.
     *
     * @param locator      The element to locate.
     * @param doesNotExist This string is not blank if the lack of the element triggers a page refresh.
     */
    void refreshIfExists(String locator, String doesNotExist);

    /**
     * Refresh the page if an element exists on the page or does not exit.
     *
     * @param locator      The element to locate.
     * @param doesNotExist This string is not blank if the lack of the element triggers a page refresh.
     * @param waitTime     How long to wait for the element to appear.
     */
    void refreshIfExists(String locator, String doesNotExist, int waitTime);

    /**
     * Maximize the browser window.
     */
    void maximizeWindow();

    /**
     * Full screen the browser window.
     */
    void fullscreen();

    /**
     * Set the window size.
     *
     * @param width  The width of the window
     * @param height The height of the window
     */
    default void setWindowSize(int width, int height) {
        setWindowSize(width + "", height + "");
    }

    /**
     * Set the window size.
     *
     * @param width  The width of the window
     * @param height The height of the window
     */
    void setWindowSize(String width, String height);

    /**
     * Scroll down.
     *
     * @param distance The distance in pixels to scroll down.
     */
    void scrollDown(String distance);

    /**
     * Scroll up.
     *
     * @param distance The distance in pixels to scroll up.
     */
    void scrollUp(String distance);

    /**
     * Test the current URL against a regex, and throw an exception if it does not match.
     *
     * @param regex The regex to test the URL against.
     */
    void verifyUrl(String regex);

    /**
     * Zoom the browser in (make everything bigger).
     */
    void browserZoomIn();

    /**
     * Zoom the browser in (make everything smaller).
     */
    void browserZoomOut();
    //</editor-fold>

    //<editor-fold desc="Aliases">

    /**
     * Print all the aliases to the console.
     */
    void dumpAliases();

    /**
     * Write the value of an alias to a file.
     *
     * @param alias    The alias whose value is saved to the file.
     * @param filename The name of the file.
     */
    void writeAliasValueToFile(String alias, String filename);

    /**
     * Copy the value of the alias LastReturn to a new alias.
     *
     * @param shared   This string is not blank if the new alias is to be shared across scenarios.
     * @param newAlias The name of the new alias.
     */
    void copyLastReturnAliasTo(String shared, String newAlias);
    //</editor-fold>

    //<editor-fold desc="Keyboard Interaction">

    /**
     * Press the escape key on the element.
     *
     * @param locator The element to press the key on.
     */
    default void pressEscape(String locator) {
        pressEscape(null, locator);
    }

    /**
     * Press the escape key on the element.
     *
     * @param force   Use JavaScript to send the key event.
     * @param locator The element to press the key on.
     */
    void pressEscape(String force, String locator);

    /**
     * Press the escape key on the element.
     *
     * @param force    Use JavaScript to send the key event.
     * @param locator  The element to press the key on.
     * @param waitTime The amount of time to wait for the element.
     */
    void pressEscape(String force, String locator, int waitTime);

    /**
     * Press the enter key on the element.
     *
     * @param locator The element to press the key on.
     */
    default void pressEnter(String locator) {
        pressEnter(null, locator);
    }

    /**
     * Press the enter key on the element.
     *
     * @param force   Use JavaScript to send the key event.
     * @param locator The element to press the key on.
     */
    void pressEnter(String force, String locator);

    /**
     * Press the enter key on the element.
     *
     * @param force    Use JavaScript to send the key event.
     * @param locator  The element to press the key on.
     * @param waitTime The amount of time to wait for the element.
     */
    void pressEnter(String force, String locator, int waitTime);

    /**
     * Press an arrow key on the element.
     *
     * @param force   Use JavaScript to send the key event.
     * @param locator The element to press the key on.
     * @param key     The name of the arrow key: up, down, left or right.
     */
    void pressArrow(final String force, final String key, final String locator);

    /**
     * Press an arrow key on the element.
     *
     * @param force    Use JavaScript to send the key event.
     * @param locator  The element to press the key on.
     * @param key      The name of the arrow key: up, down, left or right.
     * @param waitTime The amount of time to wait for the element.
     */
    void pressArrow(final String force, final String key, final String locator, int waitTime);

    /**
     * Press a function key.
     *
     * @param key The name of the function key: F1 - F12.
     */
    void pressFunctionKey(String key);
    //</editor-fold>

    //<editor-fold desc="Screenshots and Recording">

    /**
     * Start recording the screen. Only useful when not running in headless mode.
     *
     * @param file             The name of the file to save the recording to.
     * @param capturedArtifact If this is not blank it is used as the name of an Octopus artifact capturing the recording file.
     */
    void startScreenRecording(final String file, final String capturedArtifact);

    /**
     * Stop screen recording.
     */
    void stopScreenRecording();

    /**
     * Take a screenshot.
     *
     * @param filename        The file to save the screenshot to. This can be a S3 url to save directly to AWS S3.
     * @param force           If this string is not blank, it means the screenshot will be taken regardless of the global setting disabling screenshots.
     * @param captureArtifact If this is not blank it is used as the name of an Octopus artifact capturing the screenshot file.
     * @return A future that is completed when the screenshot is captured and optionally uploaded.
     */
    CompletableFuture<Void> takeScreenshot(String filename, boolean force, String captureArtifact);

    /**
     * Take a screenshot.
     *
     * @param filename        The file to save the screenshot to. This can be a S3 url to save directly to AWS S3.
     * @param captureArtifact If this is not blank it is used as the name of an Octopus artifact capturing the screenshot file.
     * @return A future that is completed when the screenshot is captured and optionally uploaded.
     */
    CompletableFuture<Void> takeScreenshot(String filename, String captureArtifact);

    /**
     * Take a screenshot.
     *
     * @param directory       The directory holding the screenshot file.
     * @param filename        The file to save the screenshot to. This can be a S3 base url to save directly to AWS S3.
     * @param captureArtifact If this is not blank it is used as the name of an Octopus artifact capturing the screenshot file.
     * @return A future that is completed when the screenshot is captured and optionally uploaded.
     */
    CompletableFuture<Void> takeScreenshot(String directory, String filename, String captureArtifact);
    //</editor-fold>

    //<editor-fold desc="ID Selection">

    /**
     * Click the element found with the supplied id.
     *
     * @param id The id of the element to click.
     */
    void clickElementWithId(String id);

    /**
     * Click the element found with the supplied id.
     *
     * @param id       The id of the element to click.
     * @param waitTime The amount of time to wait for the element.
     */
    void clickElementWithId(String id, int waitTime);

    /**
     * Select an option by its visible text from a drop down list.
     *
     * @param optionText The text of the option to select.
     * @param id         The ID of the drop down list.
     */
    void selectOptionByTextFromSelectWithId(String optionText, String id);

    /**
     * Select an option by its visible text from a drop down list.
     *
     * @param optionText The text of the option to select.
     * @param id         The ID of the drop down list.
     * @param waitTime   The amount of time to wait for the element.
     */
    void selectOptionByTextFromSelectWithId(String optionText, String id, int waitTime);

    /**
     * Populate a text box or text area with the supplied text.
     *
     * @param id   The ID of the element to populate.
     * @param text The text to populate the element with.
     */
    void populateElementWithId(String id, String text);

    /**
     * Populate a text box or text area with the supplied text.
     *
     * @param id       The ID of the element to populate.
     * @param text     The text to populate the element with.
     * @param waitTime The amount of time to wait for the element.
     */
    void populateElementWithId(String id, String text, int waitTime);

    /**
     * Get the text from the element with the supplied ID.
     *
     * @param id The ID of the element to get the text from.
     * @return The text of the element.
     */
    String getTextFromElementWithId(String id);

    /**
     * Get the text from the element with the supplied ID.
     *
     * @param id       The ID of the element to get the text from.
     * @param waitTime The amount of time to wait for the element.
     * @return The text of the element.
     */
    String getTextFromElementWithId(String id, int waitTime);
    //</editor-fold>

    //<editor-fold desc="XPath Selection">

    /**
     * Click the element found with the supplied xpath.
     *
     * @param xpath The xpath of the element to click.
     */
    void clickElementWithXPath(String xpath);

    /**
     * Click the element found with the supplied xpath.
     *
     * @param xpath    The xpath of the element to click.
     * @param waitTime The amount of time to wait for the element.
     */
    void clickElementWithXPath(String xpath, int waitTime);

    /**
     * Select an option by its visible text from a drop down list.
     *
     * @param optionText The text of the option to select.
     * @param xpath      The xpath of the drop down list.
     */
    void selectOptionByTextFromSelectWithXPath(String optionText, String xpath);

    /**
     * Select an option by its visible text from a drop down list.
     *
     * @param optionText The text of the option to select.
     * @param xpath      The xpath of the drop down list.
     * @param waitTime   The amount of time to wait for the element.
     */
    void selectOptionByTextFromSelectWithXPath(String optionText, String xpath, int waitTime);

    /**
     * Populate a text box or text area with the supplied text.
     *
     * @param xpath The xpath of the element to populate.
     * @param text  The text to populate the element with.
     */
    void populateElementWithXPath(String xpath, String text);

    /**
     * Populate a text box or text area with the supplied text.
     *
     * @param xpath    The xpath of the element to populate.
     * @param text     The text to populate the element with.
     * @param waitTime The amount of time to wait for the element.
     */
    void populateElementWithXPath(String xpath, String text, int waitTime);

    /**
     * Get the text from the element with the supplied xpath.
     *
     * @param xpath The xpath of the element to get the text from.
     * @return The text of the element.
     */
    String getTextFromElementWithXPath(String xpath);

    /**
     * Get the text from the element with the supplied xpath.
     *
     * @param xpath    The xpath of the element to get the text from.
     * @param waitTime The amount of time to wait for the element.
     * @return The text of the element.
     */
    String getTextFromElementWithXPath(String xpath, int waitTime);
    //</editor-fold>

    //<editor-fold desc="CSS Selection">

    /**
     * Click the element found with the supplied CSS selector.
     *
     * @param cssSelector The CSS selector of the element to click.
     */
    void clickElementWithCSSSelector(String cssSelector);

    /**
     * Click the element found with the supplied CSS selector.
     *
     * @param cssSelector The CSS selector of the element to click.
     * @param waitTime    The amount of time to wait for the element.
     */
    void clickElementWithCSSSelector(String cssSelector, int waitTime);

    /**
     * Select an option by its visible text from a drop down list.
     *
     * @param optionText  The text of the option to select.
     * @param cssSelector The CSS selector of the drop down list.
     */
    void selectOptionByTextFromSelectWithCSSSelector(String optionText, String cssSelector);

    /**
     * Select an option by its visible text from a drop down list.
     *
     * @param optionText  The text of the option to select.
     * @param cssSelector The CSS selector of the drop down list.
     * @param waitTime    The amount of time to wait for the element.
     */
    void selectOptionByTextFromSelectWithCSSSelector(String optionText, String cssSelector, int waitTime);

    /**
     * Populate a text box or text area with the supplied text.
     *
     * @param cssSelector The CSS selector of the element to populate.
     * @param text        The text to populate the element with.
     */
    void populateElementWithCSSSelector(String cssSelector, String text);

    /**
     * Populate a text box or text area with the supplied text.
     *
     * @param cssSelector The CSS selector of the element to populate.
     * @param text        The text to populate the element with.
     * @param waitTime    The amount of time to wait for the element.
     */
    void populateElementWithCSSSelector(String cssSelector, String text, int waitTime);

    /**
     * Get the text from the element with the supplied ID.
     *
     * @param cssSelector The CSS selector of the element to get the text from.
     * @return The text of the element.
     */
    String getTextFromElementWithCSSSelector(String cssSelector);

    /**
     * Get the text from the element with the supplied ID.
     *
     * @param cssSelector The CSS selector of the element to get the text from.
     * @param waitTime    The amount of time to wait for the element.
     * @return The text of the element.
     */
    String getTextFromElementWithCSSSelector(String cssSelector, int waitTime);
    //</editor-fold>

    //<editor-fold desc="Name Selection">

    /**
     * Click the element found with the supplied name.
     *
     * @param name The name of the element to click.
     */
    void clickElementWithName(String name);

    /**
     * Click the element found with the supplied name.
     *
     * @param name     The name of the element to click.
     * @param waitTime The amount of time to wait for the element.
     */
    void clickElementWithName(String name, int waitTime);

    /**
     * Select an option by its visible text from a drop down list.
     *
     * @param optionText The text of the option to select.
     * @param name       The name of the drop down list.
     */
    void selectOptionByTextFromSelectWithName(String optionText, String name);

    /**
     * Select an option by its visible text from a drop down list.
     *
     * @param optionText The text of the option to select.
     * @param name       The name of the drop down list.
     * @param waitTime   The amount of time to wait for the element.
     */
    void selectOptionByTextFromSelectWithName(String optionText, String name, int waitTime);

    /**
     * Populate a text box or text area with the supplied text.
     *
     * @param name The name of the element to populate.
     * @param text The text to populate the element with.
     */
    void populateElementWithName(String name, String text);

    /**
     * Populate a text box or text area with the supplied text.
     *
     * @param name     The name of the element to populate.
     * @param text     The text to populate the element with.
     * @param waitTime The amount of time to wait for the element.
     */
    void populateElementWithName(String name, String text, int waitTime);

    /**
     * Get the text from the element with the supplied name.
     *
     * @param name The name of the element to get the text from.
     * @return The text of the element.
     */
    String getTextFromElementWithName(String name);

    /**
     * Get the text from the element with the supplied name.
     *
     * @param name     The name of the element to get the text from.
     * @param waitTime The amount of time to wait for the element.
     * @return The text of the element.
     */
    String getTextFromElementWithName(String name, int waitTime);
    //</editor-fold>

    //<editor-fold desc="Simple Selection">

    /**
     * Click an element if a second element exists
     *
     * @param force         If not blank use JavaScript to click the element.
     * @param locator       The locator of the element to click.
     * @param ifOtherExists The locator of the second element that must exist for the first element to be clicked.
     */
    void clickElementIfOtherExists(String force, String locator, String ifOtherExists);

    /**
     * Click an element if a second element exists
     *
     * @param force         If not blank use JavaScript to click the element.
     * @param locator       The locator of the element to click.
     * @param waitTime      The amount of time to wait for the element.
     * @param ifOtherExists The locator of the second element that must exist for the first element to be clicked.
     */
    void clickElementIfOtherExists(String force, String locator, Integer waitTime, String ifOtherExists);

    /**
     * Click an element if a second element exists
     *
     * @param locator       The locator of the element to click.
     * @param ifOtherExists The locator of the second element that must exist for the first element to be clicked.
     */
    default void clickElementIfOtherExists(String locator, String ifOtherExists) {
        clickElementIfExists(null, locator, ifOtherExists);
    }

    /**
     * Click an element if a second element exists
     *
     * @param locator       The locator of the element to click.
     * @param waitTime      The amount of time to wait for the element.
     * @param ifOtherExists The locator of the second element that must exist for the first element to be clicked.
     */
    default void clickElementIfOtherExists(String locator, Integer waitTime, String ifOtherExists) {
        clickElementIfExists(null, locator, waitTime, ifOtherExists);
    }

    /**
     * Click an element if a second element does not exist.
     *
     * @param force         If not blank use JavaScript to click the element.
     * @param locator       The locator of the element to click.
     * @param ifOtherExists The locator of the second element that must not exist for the first element to be clicked.
     */
    void clickElementIfOtherNotExists(String force, String locator, String ifOtherExists);

    /**
     * Click an element if a second element does not exist.
     *
     * @param force         If not blank use JavaScript to click the element.
     * @param locator       The locator of the element to click.
     * @param waitTime      The amount of time to wait for the element.
     * @param ifOtherExists The locator of the second element that must not exist for the first element to be clicked.
     */
    void clickElementIfOtherNotExists(String force, String locator, Integer waitTime, String ifOtherExists);

    /**
     * Click an element if a second element does not exist.
     *
     * @param locator       The locator of the element to click.
     * @param ifOtherExists The locator of the second element that must not exist for the first element to be clicked.
     */
    default void clickElementIfOtherNotExists(String locator, String ifOtherExists) {
        clickElementIfExists(null, locator, ifOtherExists);
    }

    /**
     * Click an element if a second element does not exist.
     *
     * @param locator       The locator of the element to click.
     * @param waitTime      The amount of time to wait for the element.
     * @param ifOtherExists The locator of the second element that must not exist for the first element to be clicked.
     */
    default void clickElementIfOtherNotExists(String locator, Integer waitTime, String ifOtherExists) {
        clickElementIfExists(null, locator, waitTime, ifOtherExists);
    }

    void clickElementIfExists(String force, String locator, String ifExistsOption);

    void clickElementIfExists(String force, String locator, Integer waitTime, String ifExistsOption);

    default void clickElement(String locator) {
        clickElementIfExists(locator, null);
    }

    default void clickElement(String locator, Integer waitTime) {
        clickElementIfExists(locator, waitTime,null);}

    default void clickElementIfExists(String locator, String ifExistsOption) {clickElementIfExists(null, locator, ifExistsOption);}

    default void clickElementIfExists(String locator, Integer waitTime, String ifExistsOption) {clickElementIfExists(null, locator, waitTime, ifExistsOption);}

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

    void setDefaultHighlightOffset(String offset);

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

    default void verifyElementIsClickable(String locator) {verifyElementIsClickable(locator, null);}

    void verifyElementIsClickable(String locator, String ifExistsOption);

    void verifyElementIsClickable(String locator, int waitTime, String ifExistsOption);

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
    Object runJavascript(String code);
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

    void switchToIFrame(String locator);
    void switchToMainFrame();
}