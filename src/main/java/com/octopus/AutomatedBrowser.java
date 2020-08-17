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
     * Refresh the page.
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
     * Refresh the page if an element exists on the page or does not exit.
     *
     * @param duration     How long to keep refreshing the page for
     * @param locator      The element to locate.
     * @param doesNotExist This string is not blank if the lack of the element triggers a page refresh.
     */
    void refreshIfExists(int duration, String locator, String doesNotExist);

    /**
     * Refresh the page if an element exists on the page or does not exit.
     *
     * @param duration     How long to keep refreshing the page for
     * @param locator      The element to locate.
     * @param doesNotExist This string is not blank if the lack of the element triggers a page refresh.
     * @param waitTime     How long to wait for the element to appear.
     */
    void refreshIfExists(int duration, String locator, String doesNotExist, int waitTime);

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
    void pressArrow(String force, String key, String locator);

    /**
     * Press an arrow key on the element.
     *
     * @param force    Use JavaScript to send the key event.
     * @param locator  The element to press the key on.
     * @param key      The name of the arrow key: up, down, left or right.
     * @param waitTime The amount of time to wait for the element.
     */
    void pressArrow(String force, String key, String locator, int waitTime);

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
    void startScreenRecording(String file, String capturedArtifact);

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
     * Click an element if a second element exists.
     *
     * @param force         If not blank use JavaScript to click the element.
     * @param locator       The locator of the element to click.
     * @param ifOtherExists The locator of the second element that must exist for the first element to be clicked.
     */
    void clickElementIfOtherExists(String force, String locator, String ifOtherExists);

    /**
     * Click an element if a second element exists.
     *
     * @param force         If not blank use JavaScript to click the element.
     * @param locator       The locator of the element to click.
     * @param waitTime      The amount of time to wait for the element.
     * @param ifOtherExists The locator of the second element that must exist for the first element to be clicked.
     */
    void clickElementIfOtherExists(String force, String locator, Integer waitTime, String ifOtherExists);

    /**
     * Click an element if a second element exists.
     *
     * @param locator       The locator of the element to click.
     * @param ifOtherExists The locator of the second element that must exist for the first element to be clicked.
     */
    default void clickElementIfOtherExists(String locator, String ifOtherExists) {
        clickElementIfExists(null, locator, ifOtherExists);
    }

    /**
     * Click an element if a second element exists.
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

    /**
     * Click an element, optionally silently failing if it does not exist.
     *
     * @param force          If this is not blank then use JavaScript to click the element.
     * @param locator        The element locator.
     * @param ifExistsOption If this is not blank then silently fail if the element does not exist.
     */
    void clickElementIfExists(String force, String locator, String ifExistsOption);

    /**
     * Click an element, optionally silently failing if it does not exist.
     *
     * @param force          If this is not blank then use JavaScript to click the element.
     * @param locator        The element locator.
     * @param waitTime       The amount of time to wait for the element.
     * @param ifExistsOption If this is not blank then silently fail if the element does not exist.
     */
    void clickElementIfExists(String force, String locator, Integer waitTime, String ifExistsOption);

    /**
     * Click the element.
     *
     * @param locator The element locator.
     */
    default void clickElement(String locator) {
        clickElementIfExists(locator, null);
    }

    /**
     * Click the element.
     *
     * @param locator  The element locator.
     * @param waitTime The amount of time to wait for the element.
     */
    default void clickElement(String locator, Integer waitTime) {
        clickElementIfExists(locator, waitTime, null);
    }

    /**
     * Click the element.
     *
     * @param locator        The element locator.
     * @param ifExistsOption If this is not blank then silently fail if the element does not exist.
     */
    default void clickElementIfExists(String locator, String ifExistsOption) {
        clickElementIfExists(null, locator, ifExistsOption);
    }

    /**
     * Click the element.
     *
     * @param locator        The element locator.
     * @param waitTime       The amount of time to wait for the element.
     * @param ifExistsOption If this is not blank then silently fail if the element does not exist.
     */
    default void clickElementIfExists(String locator, Integer waitTime, String ifExistsOption) {
        clickElementIfExists(null, locator, waitTime, ifExistsOption);
    }

    /**
     * Select an option by its visible text from a drop down list.
     *
     * @param optionText The visible text of the option to select.
     * @param locator    The element locator.
     */
    default void selectOptionByTextFromSelect(String force, String optionText, String locator) {
        selectOptionByTextFromSelectIfExists(force, optionText, locator, null);
    }

    /**
     * Select an option by its visible text from a drop down list.
     *
     * @param optionText The visible text of the option to select.
     * @param locator    The element locator.
     * @param waitTime   The amount of time to wait for the element.
     */
    default void selectOptionByTextFromSelect(String force, String optionText, String locator, int waitTime) {
        selectOptionByTextFromSelectIfExists(force, optionText, locator, waitTime, null);
    }

    /**
     * Select an option by its visible text from a drop down list.
     *
     * @param force          If not blank, populate the element using JavaScript.
     * @param optionText     The visible text of the option to select.
     * @param locator        The element locator.
     * @param ifExistsOption If this is not blank then silently fail if the element does not exist.
     */
    void selectOptionByTextFromSelectIfExists(String force, String optionText, String locator, String ifExistsOption);

    /**
     * Select an option by its visible text from a drop down list.
     *
     * @param force          If not blank, populate the element using JavaScript.
     * @param optionText     The visible text of the option to select.
     * @param locator        The element locator.
     * @param waitTime       The amount of time to wait for the element.
     * @param ifExistsOption If this is not blank then silently fail if the element does not exist.
     */
    void selectOptionByTextFromSelectIfExists(String force, String optionText, String locator, int waitTime, String ifExistsOption);

    /**
     * Select an option by its value from a drop down list.
     *
     * @param force       If not blank, populate the element using JavaScript.
     * @param optionValue The value of the option to select.
     * @param locator     The element locator.
     */
    default void selectOptionByValueFromSelect(String force, String optionValue, String locator) {
        selectOptionByValueFromSelectIfExists(force, optionValue, locator, null);
    }

    /**
     * Select an option by its value from a drop down list.
     *
     * @param force       If not blank, populate the element using JavaScript.
     * @param optionValue The value of the option to select.
     * @param locator     The element locator.
     * @param waitTime    The amount of time to wait for the element.
     */
    default void selectOptionByValueFromSelect(String force, String optionValue, String locator, int waitTime) {
        selectOptionByValueFromSelectIfExists(force, optionValue, locator, waitTime, null);
    }

    /**
     * Select an option by its value from a drop down list.
     *
     * @param force          If not blank, populate the element using JavaScript.
     * @param optionValue    The value of the option to select.
     * @param locator        The element locator.
     * @param ifExistsOption If this is not blank then silently fail if the element does not exist.
     */
    void selectOptionByValueFromSelectIfExists(String force, String optionValue, String locator, String ifExistsOption);

    /**
     * Select an option by its value from a drop down list.
     *
     * @param force          If not blank, populate the element using JavaScript.
     * @param optionValue    The value of the option to select.
     * @param locator        The element locator.
     * @param waitTime       The amount of time to wait for the element.
     * @param ifExistsOption If this is not blank then silently fail if the element does not exist.
     */
    void selectOptionByValueFromSelectIfExists(String force, String optionValue, String locator, int waitTime, String ifExistsOption);

    /**
     * Populate an element with the supplied text.
     *
     * @param locator The element locator.
     * @param text    The text to populate the element with.
     */
    default void populateElement(String locator, String text) {
        populateElement(null, locator, "0", text, null);
    }

    /**
     * Populate an element with the supplied text.
     *
     * @param locator  The element locator.
     * @param text     The text to populate the element with.
     * @param waitTime The amount of time to wait for the element.
     */
    default void populateElement(String locator, String text, int waitTime) {
        populateElement(null, locator, "0", text, waitTime, null);
    }

    /**
     * Populate an element with the supplied text.
     *
     * @param force          If not blank, populate the element using JavaScript.
     * @param locator        The element locator.
     * @param text           The text to populate the element with.
     * @param keystrokeDelay The amount of time to deploy before entering each character. If set to zero, the text is
     *                       entered in a single operation. This value must be zero for file selector inputs.
     */
    default void populateElement(String force, String locator, String keystrokeDelay, String text) {
        populateElement(force, locator, keystrokeDelay, text, null);
    }

    /**
     * Populate an element with the supplied text.
     *
     * @param locator        The element locator.
     * @param text           The text to populate the element with.
     * @param keystrokeDelay The amount of time to deploy before entering each character. If set to zero, the text is
     *                       entered in a single operation. This value must be zero for file selector inputs.
     * @param waitTime       The amount of time to wait for the element.
     */
    default void populateElement(String locator, String keystrokeDelay, String text, int waitTime) {
        populateElement(null, locator, keystrokeDelay, text, waitTime, null);
    }

    /**
     * Populate an element with the supplied text.
     *
     * @param force          If not blank, populate the element using JavaScript.
     * @param locator        The element locator.
     * @param text           The text to populate the element with.
     * @param keystrokeDelay The amount of time to deploy before entering each character. If set to zero, the text is
     *                       entered in a single operation. This value must be zero for file selector inputs.
     * @param ifExistsOption If this is not blank then silently fail if the element does not exist.
     */
    void populateElement(String force, String locator, String keystrokeDelay, String text, String ifExistsOption);

    /**
     * Populate an element with the supplied text.
     *
     * @param force          If not blank, populate the element using JavaScript.
     * @param locator        The element locator.
     * @param text           The text to populate the element with.
     * @param keystrokeDelay The amount of time to deploy before entering each character. If set to zero, the text is
     *                       entered in a single operation. This value must be zero for file selector inputs.
     * @param waitTime       The amount of time to wait for the element.
     * @param ifExistsOption If this is not blank then silently fail if the element does not exist.
     */
    void populateElement(String force, String locator, String keystrokeDelay, String text, int waitTime, String ifExistsOption);

    /**
     * Populate an element with the supplied text. The element has to be present on the page, but not necessarily clickable.
     *
     * @param force          If not blank, populate the element using JavaScript.
     * @param locator        The element locator.
     * @param text           The text to populate the element with.
     * @param waitTime       The amount of time to wait for the element.
     * @param ifExistsOption If this is not blank then silently fail if the element does not exist.
     */
    void populateHiddenElement(String force, String locator, String text, int waitTime, String ifExistsOption);

    /**
     * Populate an element with the supplied text. The element has to be present on the page, but not necessarily clickable.
     *
     * @param force          If not blank, populate the element using JavaScript.
     * @param locator        The element locator.
     * @param text           The text to populate the element with.
     * @param ifExistsOption If this is not blank then silently fail if the element does not exist.
     */
    void populateHiddenElement(String force, String locator, String text, String ifExistsOption);

    /**
     * Clear the element of any text.
     *
     * @param locator The element locator.
     */
    default void clear(String locator) {
        clearIfExists(null, locator, null);
    }

    /**
     * Clear the element of any text.
     *
     * @param locator  The element locator.
     * @param waitTime The amount of time to wait for the element.
     */
    default void clear(String locator, int waitTime) {
        clearIfExists(null, locator, waitTime, null);
    }

    /**
     * Clear the element of any text.
     *
     * @param force          If not blank, clear the text using JavaScript.
     * @param locator        The element locator.
     * @param ifExistsOption If this is not blank then silently fail if the element does not exist.
     */
    void clearIfExists(String force, String locator, String ifExistsOption);

    /**
     * Clear the element of any text.
     *
     * @param force          If not blank, clear the text using JavaScript.
     * @param locator        The element locator.
     * @param waitTime       The amount of time to wait for the element.
     * @param ifExistsOption If this is not blank then silently fail if the element does not exist.
     */
    void clearIfExists(String force, String locator, int waitTime, String ifExistsOption);

    /**
     * Mouse over the element.
     *
     * @param locator The element locator.
     */
    default void mouseOver(String locator) {
        mouseOverIfExists(null, locator, null);
    }

    /**
     * Mouse over the element.
     *
     * @param locator  The element locator.
     * @param waitTime The amount of time to wait for the element.
     */
    default void mouseOver(String locator, int waitTime) {
        mouseOverIfExists(null, locator, waitTime, null);
    }

    /**
     * Mouse over the element.
     *
     * @param force          If not blank, trigger the mouse over event using JavaScript.
     * @param locator        The element locator.
     * @param ifExistsOption If this is not blank then silently fail if the element does not exist.
     */
    void mouseOverIfExists(String force, String locator, String ifExistsOption);

    /**
     * Mouse over the element.
     *
     * @param force          If not blank, trigger the mouse over event using JavaScript.
     * @param locator        The element locator.
     * @param waitTime       The amount of time to wait for the element.
     * @param ifExistsOption If this is not blank then silently fail if the element does not exist.
     */
    void mouseOverIfExists(String force, String locator, int waitTime, String ifExistsOption);

    /**
     * Focus on the element.
     *
     * @param locator The element locator.
     */
    default void focus(String locator) {
        focusIfExists(null, locator, null);
    }

    /**
     * Focus on the element.
     *
     * @param locator  The element locator.
     * @param waitTime The amount of time to wait for the element.
     */
    default void focus(String locator, int waitTime) {
        focusIfExists(null, locator, waitTime, null);
    }

    /**
     * Focus on the element.
     *
     * @param locator        The element locator.
     * @param ifExistsOption If this is not blank then silently fail if the element does not exist.
     * @param force          If not blank, focus on the element using JavaScript.
     */
    void focusIfExists(String force, String locator, String ifExistsOption);

    /**
     * Focus on the element.
     *
     * @param locator        The element locator.
     * @param waitTime       The amount of time to wait for the element.
     * @param ifExistsOption If this is not blank then silently fail if the element does not exist.
     * @param force          If not blank, focus on the element using JavaScript.
     */
    void focusIfExists(String force, String locator, int waitTime, String ifExistsOption);

    /**
     * Get the text from the element.
     *
     * @param locator The element locator.
     * @return The text set in the element.
     */
    default String getTextFromElement(String locator) {
        return getTextFromElementIfExists(locator, null);
    }

    /**
     * Get the text from the element.
     *
     * @param locator  The element locator.
     * @param waitTime The amount of time to wait for the element.
     * @return The text set in the element.
     */
    default String getTextFromElement(String locator, int waitTime) {
        return getTextFromElementIfExists(locator, waitTime, null);
    }

    /**
     * Get the text from the element.
     *
     * @param locator        The element locator.
     * @param ifExistsOption If this is not blank then silently fail if the element does not exist.
     * @return The text set in the element.
     */
    String getTextFromElementIfExists(String locator, String ifExistsOption);

    /**
     * Get the text from the element.
     *
     * @param locator        The element locator.
     * @param waitTime       The amount of time to wait for the element.
     * @param ifExistsOption If this is not blank then silently fail if the element does not exist.
     * @return The text set in the element.
     */
    String getTextFromElementIfExists(String locator, int waitTime, String ifExistsOption);

    /**
     * Return the regex group value extracted from the text of an element.
     *
     * @param group   The name of the regex group whose value is returned.
     * @param regex   The regex applied to the element's text.
     * @param locator The element locator.
     * @return The value of the named regex group from the supplied regex applied to the element's text.
     */
    default String getRegexGroupFromElement(String group, String regex, String locator) {
        return getRegexGroupFromElementIfExists(group, regex, locator, null);
    }

    /**
     * Return the regex group value extracted from the text of an element.
     *
     * @param group    The name of the regex group whose value is returned.
     * @param regex    The regex applied to the element's text.
     * @param locator  The element locator.
     * @param waitTime The amount of time to wait for the element.
     * @return The value of the named regex group from the supplied regex applied to the element's text.
     */
    default String getRegexGroupFromElement(String group, String regex, String locator, int waitTime) {
        return getRegexGroupFromElementIfExists(group, regex, locator, waitTime, null);
    }

    /**
     * Return the regex group value extracted from the text of an element.
     *
     * @param group          The name of the regex group whose value is returned.
     * @param regex          The regex applied to the element's text.
     * @param locator        The element locator.
     * @param ifExistsOption If this is not blank then silently fail if the element does not exist.
     * @return The value of the named regex group from the supplied regex applied to the element's text.
     */
    String getRegexGroupFromElementIfExists(String group, String regex, String locator, String ifExistsOption);

    /**
     * Return the regex group value extracted from the text of an element.
     *
     * @param group          The name of the regex group whose value is returned.
     * @param regex          The regex applied to the element's text.
     * @param locator        The element locator.
     * @param waitTime       The amount of time to wait for the element.
     * @param ifExistsOption If this is not blank then silently fail if the element does not exist.
     * @return The value of the named regex group from the supplied regex applied to the element's text.
     */
    String getRegexGroupFromElementIfExists(String group, String regex, String locator, int waitTime, String ifExistsOption);

    /**
     * Verify that the element's text matches the supplied regex.
     *
     * @param regex   The regex applied to the element's text.
     * @param locator The element locator.
     */
    default void verifyTextFromElement(String locator, String regex) {
        verifyTextFromElementIfExists(locator, regex, null);
    }

    /**
     * Verify that the element's text matches the supplied regex.
     *
     * @param regex    The regex applied to the element's text.
     * @param locator  The element locator.
     * @param waitTime The amount of time to wait for the element.
     */
    default void verifyTextFromElement(String locator, String regex, int waitTime) {
        verifyTextFromElementIfExists(locator, regex, waitTime, null);
    }

    /**
     * Verify that the element's text matches the supplied regex.
     *
     * @param regex          The regex applied to the element's text.
     * @param locator        The element locator.
     * @param ifExistsOption If this is not blank then silently fail if the element does not exist.
     */
    void verifyTextFromElementIfExists(String locator, String regex, String ifExistsOption);

    /**
     * Verify that the element's text matches the supplied regex.
     *
     * @param regex          The regex applied to the element's text.
     * @param locator        The element locator.
     * @param waitTime       The amount of time to wait for the element.
     * @param ifExistsOption If this is not blank then silently fail if the element does not exist.
     */
    void verifyTextFromElementIfExists(String locator, String regex, int waitTime, String ifExistsOption);

    /**
     * Scroll the element into view vertically, with an optional offset.
     *
     * @param locator The element locator.
     * @param offset  If not blank, offset the vertical scroll by the supplied value in pixels.
     */
    default void scrollElementIntoView(String locator, String offset) {
        scrollElementIntoViewIfExists(locator, offset, null);
    }

    /**
     * Scroll the element into view vertically, with an optional offset.
     *
     * @param locator  The element locator.
     * @param offset   If not blank, offset the vertical scroll by the supplied value in pixels.
     * @param waitTime The amount of time to wait for the element.
     */
    default void scrollElementIntoView(String locator, String offset, int waitTime) {
        scrollElementIntoViewIfExists(locator, offset, waitTime, null);
    }

    /**
     * Scroll the element into view vertically, with an optional offset.
     *
     * @param locator        The element locator.
     * @param offset         If not blank, offset the vertical scroll by the supplied value in pixels.
     * @param ifExistsOption If this is not blank then silently fail if the element does not exist.
     */
    void scrollElementIntoViewIfExists(String locator, String offset, String ifExistsOption);

    /**
     * Scroll the element into view vertically, with an optional offset.
     *
     * @param locator        The element locator.
     * @param offset         If not blank, offset the vertical scroll by the supplied value in pixels.
     * @param waitTime       The amount of time to wait for the element.
     * @param ifExistsOption If this is not blank then silently fail if the element does not exist.
     */
    void scrollElementIntoViewIfExists(String locator, String offset, int waitTime, String ifExistsOption);

    /**
     * Set the default offset applied when any external highlight is applied to an element.
     *
     * @param offset the default offset applied when any external highlight is applied to an element.
     */
    void setDefaultHighlightOffset(String offset);

    /**
     * Apply a highlight to an element.
     *
     * @param location Set to either "inside" or "outside".
     * @param lift     If not blank, change the elements z-index to bring it above other elements (this option is experimental).
     * @param locator  The element locator.
     * @param offset   If not blank, use this offset if the location is set to "outside".
     */
    default void elementHighlight(String location, String lift, String locator, final String offset) {
        elementHighlightIfExists(location, lift, locator, offset, null);
    }

    /**
     * Apply a highlight to an element.
     *
     * @param location Set to either "inside" or "outside".
     * @param lift     If not blank, change the elements z-index to bring it above other elements (this option is experimental).
     * @param locator  The element locator.
     * @param offset   If not blank, use this offset if the location is set to "outside".
     * @param waitTime The amount of time to wait for the element.
     */
    default void elementHighlight(String location, String lift, String locator, final String offset, int waitTime) {
        elementHighlightIfExists(location, lift, locator, offset, waitTime, null);
    }

    /**
     * Apply a highlight to an element.
     *
     * @param location       Set to either "inside" or "outside".
     * @param lift           If not blank, change the elements z-index to bring it above other elements (this option is experimental).
     * @param locator        The element locator.
     * @param offset         If not blank, use this offset if the location is set to "outside".
     * @param ifExistsOption If this is not blank then silently fail if the element does not exist.
     */
    void elementHighlightIfExists(String location, String lift, String locator, String offset, String ifExistsOption);

    /**
     * Apply a highlight to an element.
     *
     * @param location       Set to either "inside" or "outside".
     * @param lift           If not blank, change the elements z-index to bring it above other elements (this option is experimental).
     * @param locator        The element locator.
     * @param offset         If not blank, use this offset if the location is set to "outside".
     * @param waitTime       The amount of time to wait for the element.
     * @param ifExistsOption If this is not blank then silently fail if the element does not exist.
     */
    void elementHighlightIfExists(String location, String lift, String locator, String offset, int waitTime, String ifExistsOption);

    /**
     * Remove a highlight from an element by reapplying the style on the element before the highlight was applied.
     * Note that this method will remove any styling changes between the highlight being applied and then removed, so it
     * is best to apply the highlight, take a screenshot and remove the highlight without any other interactions inbetween.
     *
     * @param locator        The element locator.
     * @param ifExistsOption If this is not blank then silently fail if the element does not exist.
     */
    void removeElementHighlight(String locator, String ifExistsOption);

    /**
     * Remove a highlight from an element by reapplying the style on the element before the highlight was applied.
     * Note that this method will remove any styling changes between the highlight being applied and then removed, so it
     * is best to apply the highlight, take a screenshot and remove the highlight without any other interactions inbetween.
     *
     * @param locator        The element locator.
     * @param waitTime       The amount of time to wait for the element.
     * @param ifExistsOption If this is not blank then silently fail if the element does not exist.
     */
    void removeElementHighlight(String locator, int waitTime, String ifExistsOption);

    /**
     * Verify an element exists in the page.
     *
     * @param locator The element locator.
     */
    default void verifyElementExists(String locator) {
        verifyElementExists(locator, null);
    }

    /**
     * Verify an element exists in the page, optionally silently failing. This is useful if you need to wait for an
     * message or some other kind of feedback, but want to proceed regardless after a certain period of time.
     *
     * @param locator        The element locator.
     * @param ifExistsOption If this is not blank then silently fail if the element does not exist.
     */
    void verifyElementExists(String locator, String ifExistsOption);

    /**
     * Verify an element exists in the page, optionally silently failing. This is useful if you need to wait for an
     * message or some other kind of feedback, but want to proceed regardless after a certain period of time.
     *
     * @param locator        The element locator.
     * @param waitTime       The amount of time to wait for the element.
     * @param ifExistsOption If this is not blank then silently fail if the element does not exist.
     */
    void verifyElementExists(String locator, int waitTime, String ifExistsOption);

    /**
     * Verify an element is clickable.
     *
     * @param locator The element locator.
     */
    default void verifyElementIsClickable(String locator) {
        verifyElementIsClickable(locator, null);
    }

    /**
     * Verify an element is clickable. This is useful if you need to wait for an
     * message or some other kind of feedback, but want to proceed regardless after a certain period of time.
     *
     * @param locator        The element locator.
     * @param ifExistsOption If this is not blank then silently fail if the element does not exist.
     */
    void verifyElementIsClickable(String locator, String ifExistsOption);

    /**
     * Verify an element is clickable. This is useful if you need to wait for an
     * message or some other kind of feedback, but want to proceed regardless after a certain period of time.
     *
     * @param locator        The element locator.
     * @param waitTime       The amount of time to wait for the element.
     * @param ifExistsOption If this is not blank then silently fail if the element does not exist.
     */
    void verifyElementIsClickable(String locator, int waitTime, String ifExistsOption);

    /**
     * Verify an element does not exist in the page.
     *
     * @param locator The element locator.
     */
    void verifyElementDoesNotExist(String locator);

    /**
     * Verify an element does not exist in the page.
     *
     * @param locator  The element locator.
     * @param waitTime The amount of time to wait for the element.
     */
    void verifyElementDoesNotExist(String locator, int waitTime);

    /**
     * Get the page title.
     *
     * @return the page title.
     */
    String getTitle();
    //</editor-fold>

    //<editor-fold desc="Network Alter and Capture">

    /**
     * If BrowserMob is enabled, start capturing the default data in a HAR file.
     */
    void captureHarFile();

    /**
     * If BrowserMob is enabled, start capturing the all data in a HAR file.
     */
    void captureCompleteHarFile();

    /**
     * Save the network events stored since captureHarFile() or captureCompleteHarFile() were called to a file.
     *
     * @param file The name of the file to save.
     */
    void saveHarFile(String file);

    /**
     * If BrowserMob is enabled, block request to the supplied URL and immediately return the supplied response code.
     *
     * @param url          The URL to block.
     * @param responseCode The response code for any requests to the supplied URL.
     */
    void blockRequestTo(String url, int responseCode);

    /**
     * If BrowserMob is enabled, return the supply body and response code to any request to the supplied URL.
     *
     * @param url          The URL to block.
     * @param responseCode The response code for any requests to the supplied URL.
     * @param responseBody The body of the response to any requests to the supplied URL.
     */
    void alterResponseFrom(String url, int responseCode, String responseBody);

    /**
     * @return A list of HTTP errors in the network traffic recorded by captureHarFile() or captureCompleteHarFile().
     */
    List<Pair<String, Integer>> getErrors();
    //</editor-fold>

    //<editor-fold desc="Transitions and Annotations">

    /**
     * Clear any screen transitions.
     */
    void clearTransition();

    /**
     * Fade the screen from transparent to the supplied color.
     *
     * @param red      The red component of the final color.
     * @param green    The green component of the final color.
     * @param blue     The blue component of the final color.
     * @param duration The duration of the screen fade.
     */
    void fadeScreen(String red, String green, String blue, String duration);

    /**
     * Display a note in the HTML.
     *
     * @param text     The note text.
     * @param duration The duration to display the note.
     */
    void displayNote(String text, String duration);
    //</editor-fold>

    //<editor-fold desc="Raw JavaScript">

    /**
     * Run raw JavaScript.
     *
     * @param code The JavaScript code.
     * @return The value returned by the JavaScript code.
     */
    Object runJavascript(String code);
    //</editor-fold>

    //<editor-fold desc="Statistics">

    /**
     * @return the number of interactions (clicks, selects, populations etc) recorded by the browser.
     */
    int getInteractionCount();
    //</editor-fold>

    //<editor-fold desc="Octopus">

    /**
     * Print an Octopus service message that sets the percent complete for a step.
     *
     * @param percent The percent value
     * @param message The message accompanying the progress
     */
    void setOctopusPercent(String percent, String message);

    /**
     * Print an Octopus service message that sets an Octopus variable from the value of an alias.
     *
     * @param alias    The name of the source alias.
     * @param variable The name of the destination Octopus variable.
     */
    void writeAliasValueToOctopusVariable(String alias, String variable);

    /**
     * Print an Octopus service message that sets an Octopus sensitive variable from the value of an alias.
     *
     * @param alias    The name of the source alias.
     * @param variable The name of the destination Octopus variable.
     */
    void writeAliasValueToOctopusSensitiveVariable(String alias, String variable);

    /**
     * Print an Octopus service message that creates an artifact from the supplied path.
     *
     * @param name The name of the Octopus artifact.
     * @param path The path to the artifact.
     */
    void defineArtifact(String name, String path);
    //</editor-fold>

    //<editor-fold desc="Github Actions">

    /**
     * Print a GitHub action service message to create an environment variable.
     *
     * @param name  The name of the environment variable.
     * @param value The value of the environment variable.
     */
    void setGithubEnvironmentVariable(String name, String value);

    /**
     * Print a GitHub action service message to create an output parameter.
     *
     * @param name  The name of the output parameter.
     * @param value The value of the output parameter.
     */
    void setGithubOutputParameter(String name, String value);

    /**
     * Print a GitHub action service message to append a directory to the PATH.
     *
     * @param path The directory to append to the PATH.
     */
    void addGithubSystemPath(String path);

    /**
     * Print a GitHub action service message to print a debug message.
     *
     * @param message The message.
     */
    void setGithubDebugMessage(String message);

    /**
     * Print a GitHub action service message to print a warning message.
     *
     * @param message The message.
     */
    void setGithubWarningMessage(String message);

    /**
     * Print a GitHub action service message to print a error message.
     *
     * @param message The message.
     */
    void setGithubErrorMessage(String message);

    /**
     * Print a GitHub action service message to identify a value to mask from the logs.
     *
     * @param value The value to mask from the logs.
     */
    void maskGithubValue(String value);

    /**
     * Print a GitHub action service message to stop log processing.
     *
     * @param token A token used to identify the command.
     */
    void stopGithubLogging(String token);

    /**
     * Print a GitHub action service message to restart log processing.
     *
     * @param token The token passed to stopGithubLogging()
     */
    void startGithubLogging(String token);
    //</editor-fold>

    /**
     * Switch to an iframe.
     *
     * @param locator The iframe locator.
     */
    void switchToIFrame(String locator);

    /**
     * Switch back to the main page frame.
     */
    void switchToMainFrame();
}