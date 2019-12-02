package com.octopus.decorators;

import com.octopus.Constants;
import com.octopus.decoratorbase.AutomatedBrowserBase;
import com.octopus.exceptions.InteractionException;
import com.octopus.exceptions.SaveException;
import com.octopus.exceptions.ValidationException;
import com.octopus.exceptions.WebElementException;
import com.octopus.utils.*;
import com.octopus.utils.impl.*;
import io.cucumber.java.en.And;
import io.vavr.control.Try;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class WebDriverDecorator extends AutomatedBrowserBase {
    private static final Logger LOGGER = Logger.getLogger(WebDriverDecorator.class.toString());
    private static final ServiceMessageGenerator SERVICE_MESSAGE_GENERATOR = new ServiceMessageGeneratorImpl();
    private static final SimpleBy SIMPLE_BY = new SimpleByImpl();
    private static final ScreenTransitions SCREEN_TRANSITIONS = new ScreenTransitionsImpl();
    private static final SystemPropertyUtils SYSTEM_PROPERTY_UTILS = new SystemPropertyUtilsImpl();
    private static final S3Uploader S_3_UPLOADER = new S3UploaderImpl();
    private static final ScreenRecorderService SCREEN_RECORDER_SERVICE = new ScreenRecorderServiceImpl();
    private static final OSUtils OS_UTILS = new OSUtilsImpl();
    private int defaultExplicitWaitTime;
    private WebDriver webDriver;

    @Override
    public void setDefaultExplicitWaitTime(final int waitTime) {
        defaultExplicitWaitTime = waitTime;
    }

    @Override
    public int getDefaultExplicitWaitTime() {
        return defaultExplicitWaitTime;
    }

    @Override
    public WebDriver getWebDriver() {
        return webDriver;
    }

    @Override
    public void setWebDriver(final WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    @Override
    public void destroy() {
        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().destroy();
        }

        if (webDriver != null) {
            webDriver.quit();
        }
    }

    @Override
    public void startScreenRecording(final String file, final String capturedArtifact) {
        final File movie = SCREEN_RECORDER_SERVICE.start(file == null ? new File(".") : new File(OS_UTILS.fixFileName(file)));
        if (StringUtils.isNotBlank(capturedArtifact)) {
            SERVICE_MESSAGE_GENERATOR.newArtifact(movie, capturedArtifact);
        }
    }

    public static void staticStopScreenRecording() {
        SCREEN_RECORDER_SERVICE.stop();
    }

    @Override
    public void stopScreenRecording() {
        staticStopScreenRecording();
    }

    @Override
    public void goTo(final String url) {
        webDriver.get(url);
    }

    @Override
    public CompletableFuture<Void> takeScreenshot(final String directory, final String filename, final String captureArtifact) {
        return takeScreenshot(directory + File.separator + filename, captureArtifact);
    }

    @Override
    public CompletableFuture<Void> takeScreenshot(final String file, final String captureArtifact) {
        return takeScreenshot(file, false, captureArtifact);
    }

    @Override
    public CompletableFuture<Void> takeScreenshot(final String file, boolean force, final String captureArtifact) {
        if (!force && SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.DISABLE_SCREENSHOTS, false) || webDriver == null) {
            return CompletableFuture.completedFuture(null);
        }

        // Don't attempt to take a screenshot after the window has been closed
        if (((RemoteWebDriver)webDriver).getSessionId() == null) {
            return CompletableFuture.completedFuture(null);
        }

        final File screenshot = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);

        try {
            if (file.startsWith("s3://")) {
                final String[] urlParts = file.split("/");
                if (urlParts.length >= 4) {

                    if (StringUtils.isNotBlank(captureArtifact)) {
                        final File destination = Files.createTempFile("screenshot", ".png").toFile();
                        SERVICE_MESSAGE_GENERATOR.newArtifact(destination, captureArtifact);
                    }

                    // The file uploading is done in a thread in the background
                    return S_3_UPLOADER.uploadFileToS3(
                            urlParts[2],
                            file.substring(5 + urlParts[2].length() + 1),
                            screenshot,
                            true);
                } else {
                    throw new SaveException("S3 paths must be in the format S3://bucket/filename or S3://bucket/dir/filename. The path \"" + file + "\" is missing some elements.");
                }
            } else {
                final File destination = new File(OS_UTILS.fixFileName(file));
                FileUtils.copyFile(screenshot, destination);
                if (StringUtils.isNotBlank(captureArtifact)) {
                    SERVICE_MESSAGE_GENERATOR.newArtifact(destination, captureArtifact);
                }
                return CompletableFuture.completedFuture(null);
            }
        } catch (final IOException ex) {
            throw new SaveException("Failed to copy the screenshot to " + file, ex);
        }
    }

    @Override
    public void sleep(String seconds) {
        try {
            Thread.sleep(Integer.parseInt(seconds) * 1000);
        } catch (final InterruptedException e) {
            // ignore
        }
    }

    @Override
    public void setWindowSize(final String width, final String height) {
        webDriver.manage().window().setSize(new Dimension(Integer.parseInt(width), Integer.parseInt(height)));
    }

    @Override
    public void clickElementWithId(final String id) {
        if (getDefaultExplicitWaitTime() <= 0) {
            webDriver.findElement(By.id(id)).click();
        } else {
            clickElementWithId(id, getDefaultExplicitWaitTime());
        }
    }

    @Override
    public void clickElementWithId(final String id, final int waitTime) {
        if (waitTime <= 0) {
            clickElementWithId(id);
        } else {
            final WebDriverWait wait = new WebDriverWait(webDriver, waitTime);
            wait.until(ExpectedConditions.elementToBeClickable((By.id(id)))).click();
        }
    }

    @Override
    public void selectOptionByTextFromSelectWithId(final String optionText, final String selectId) {
        if (getDefaultExplicitWaitTime() <= 0) {
            new Select(webDriver.findElement(By.id(selectId))).selectByVisibleText(optionText);
        } else {
            selectOptionByTextFromSelectWithId(optionText, selectId, getDefaultExplicitWaitTime());
        }
    }

    @Override
    public void selectOptionByTextFromSelectWithId(final String optionText, final String id, final int waitTime) {
        if (waitTime <= 0) {
            selectOptionByTextFromSelectWithId(optionText, id);
        } else {
            final WebDriverWait wait = new WebDriverWait(webDriver, waitTime);
            new Select(wait.until(ExpectedConditions.elementToBeClickable((By.id(id))))).selectByVisibleText(optionText);
        }
    }

    @Override
    public void populateElementWithId(final String id, final String text) {
        if (getDefaultExplicitWaitTime() <= 0) {
            webDriver.findElement(By.id(id)).sendKeys(text);
        } else {
            populateElementWithId(id, text, getDefaultExplicitWaitTime());
        }
    }

    @Override
    public void populateElementWithId(final String id, final String text, final int waitTime) {
        if (waitTime <= 0) {
            populateElementWithId(id, text);
        } else {
            final WebDriverWait wait = new WebDriverWait(webDriver, waitTime);
            wait.until(ExpectedConditions.presenceOfElementLocated((By.id(id)))).sendKeys(text);
        }
    }

    @Override
    public String getTextFromElementWithId(final String id) {
        if (getDefaultExplicitWaitTime() <= 0) {
            final WebElement element = webDriver.findElement(By.id(id));

            if (StringUtils.isNotBlank(element.getAttribute("value"))) {
                return element.getAttribute("value");
            }

            return element.getText();
        } else {
            return getTextFromElementWithId(id, getDefaultExplicitWaitTime());
        }
    }

    @Override
    public String getTextFromElementWithId(final String id, final int waitTime) {
        if (waitTime <= 0) {
            return getTextFromElementWithId(id);
        } else {
            final WebDriverWait wait = new WebDriverWait(webDriver, waitTime);
            return wait.until(ExpectedConditions.presenceOfElementLocated((By.id(id)))).getText();
        }
    }

    @Override
    public void clickElementWithXPath(final String xpath) {
        if (getDefaultExplicitWaitTime() <= 0) {
            webDriver.findElement(By.xpath(xpath)).click();
        } else {
            clickElementWithXPath(xpath, getDefaultExplicitWaitTime());
        }
    }

    @Override
    public void clickElementWithXPath(final String xpath, final int waitTime) {
        if (waitTime <= 0) {
            clickElementWithXPath(xpath);
        } else {
            final WebDriverWait wait = new WebDriverWait(webDriver, waitTime);
            wait.until(ExpectedConditions.elementToBeClickable((By.xpath(xpath)))).click();
        }
    }

    @Override
    public void selectOptionByTextFromSelectWithXPath(final String optionText, final String xpath) {
        if (getDefaultExplicitWaitTime() <= 0) {
            new Select(webDriver.findElement(By.xpath(xpath))).selectByVisibleText(optionText);
        } else {
            selectOptionByTextFromSelectWithXPath(optionText, xpath, getDefaultExplicitWaitTime());
        }
    }

    @Override
    public void selectOptionByTextFromSelectWithXPath(final String optionText, final String xpath, final int waitTime) {
        if (waitTime <= 0) {
            selectOptionByTextFromSelectWithXPath(optionText, xpath);
        } else {
            final WebDriverWait wait = new WebDriverWait(webDriver, waitTime);
            new Select(wait.until(ExpectedConditions.elementToBeClickable((By.xpath(xpath))))).selectByVisibleText(optionText);
        }
    }

    @Override
    public void populateElementWithXPath(final String xpath, final String text) {
        if (getDefaultExplicitWaitTime() <= 0) {
            webDriver.findElement(By.xpath(xpath)).sendKeys(text);
        } else {
            populateElementWithXPath(xpath, text, getDefaultExplicitWaitTime());
        }
    }

    @Override
    public void populateElementWithXPath(final String xpath, final String text, final int waitTime) {
        if (waitTime <= 0) {
            populateElementWithXPath(xpath, text);
        } else {
            final WebDriverWait wait = new WebDriverWait(webDriver, waitTime);
            wait.until(ExpectedConditions.presenceOfElementLocated((By.xpath(xpath)))).sendKeys(text);
        }
    }

    @Override
    public String getTextFromElementWithXPath(final String xpath) {
        if (getDefaultExplicitWaitTime() <= 0) {
            final WebElement element = webDriver.findElement(By.xpath(xpath));

            if (StringUtils.isNotBlank(element.getAttribute("value"))) {
                return element.getAttribute("value");
            }

            return element.getText();
        } else {
            return getTextFromElementWithXPath(xpath, getDefaultExplicitWaitTime());
        }
    }

    @Override
    public String getTextFromElementWithXPath(final String xpath, final int waitTime) {
        if (waitTime <= 0) {
            return getTextFromElementWithXPath(xpath);
        } else {
            final WebDriverWait wait = new WebDriverWait(webDriver, waitTime);
            return wait.until(ExpectedConditions.presenceOfElementLocated((By.xpath(xpath)))).getText();
        }
    }

    @Override
    public void clickElementWithCSSSelector(final String cssSelector) {
        if (getDefaultExplicitWaitTime() <= 0) {
            webDriver.findElement(By.cssSelector(cssSelector)).click();
        } else {
            clickElementWithCSSSelector(cssSelector, getDefaultExplicitWaitTime());
        }
    }

    @Override
    public void clickElementWithCSSSelector(String css, final int waitTime) {
        if (waitTime <= 0) {
            clickElementWithCSSSelector(css);
        } else {
            final WebDriverWait wait = new WebDriverWait(webDriver, waitTime);
            wait.until(ExpectedConditions.elementToBeClickable((By.cssSelector(css)))).click();
        }
    }

    @Override
    public void selectOptionByTextFromSelectWithCSSSelector(final String optionText, final String cssSelector) {
        if (getDefaultExplicitWaitTime() <= 0) {
            new Select(webDriver.findElement(By.cssSelector(cssSelector))).selectByVisibleText(optionText);
        } else {
            selectOptionByTextFromSelectWithCSSSelector(optionText, cssSelector, getDefaultExplicitWaitTime());
        }
    }


    @Override
    public void selectOptionByTextFromSelectWithCSSSelector(final String optionText, final String css, final int waitTime) {
        if (waitTime <= 0) {
            selectOptionByTextFromSelectWithCSSSelector(optionText, css);
        } else {
            final WebDriverWait wait = new WebDriverWait(webDriver, waitTime);
            new Select(wait.until(ExpectedConditions.elementToBeClickable((By.cssSelector(css))))).selectByVisibleText(optionText);
        }
    }

    @Override
    public void populateElementWithCSSSelector(final String cssSelector, final String text) {
        if (getDefaultExplicitWaitTime() <= 0) {
            webDriver.findElement(By.cssSelector(cssSelector)).sendKeys(text);
        } else {
            populateElementWithCSSSelector(cssSelector, text, getDefaultExplicitWaitTime());
        }
    }

    @Override
    public void populateElementWithCSSSelector(String css, final String text, final int waitTime) {
        if (waitTime <= 0) {
            populateElementWithCSSSelector(css, text);
        } else {
            final WebDriverWait wait = new WebDriverWait(webDriver, waitTime);
            wait.until(ExpectedConditions.presenceOfElementLocated((By.cssSelector(css)))).sendKeys(text);
        }
    }

    @Override
    public String getTextFromElementWithCSSSelector(final String cssSelector) {
        if (getDefaultExplicitWaitTime() <= 0) {
            final WebElement element = webDriver.findElement(By.cssSelector(cssSelector));

            if (StringUtils.isNotBlank(element.getAttribute("value"))) {
                return element.getAttribute("value");
            }

            return element.getText();
        } else {
            return getTextFromElementWithCSSSelector(cssSelector, getDefaultExplicitWaitTime());
        }
    }

    @Override
    public String getTextFromElementWithCSSSelector(String css, final int waitTime) {
        if (waitTime <= 0) {
            return getTextFromElementWithCSSSelector(css);
        } else {
            final WebDriverWait wait = new WebDriverWait(webDriver, waitTime);
            return wait.until(ExpectedConditions.presenceOfElementLocated((By.cssSelector(css)))).getText();
        }
    }

    @Override
    public void clickElementWithName(final String name) {
        if (getDefaultExplicitWaitTime() <= 0) {
            webDriver.findElement(By.name(name)).click();
        } else {
            clickElementWithName(name, getDefaultExplicitWaitTime());
        }
    }

    @Override
    public void clickElementWithName(final String name, final int waitTime) {
        if (waitTime <= 0) {
            clickElementWithName(name);
        } else {
            final WebDriverWait wait = new WebDriverWait(webDriver, waitTime);
            wait.until(ExpectedConditions.elementToBeClickable((By.name(name)))).click();
        }
    }

    @Override
    public void selectOptionByTextFromSelectWithName(final String optionText, final String name) {
        if (getDefaultExplicitWaitTime() <= 0) {
            new Select(webDriver.findElement(By.name(name))).selectByVisibleText(optionText);
        } else {
            selectOptionByTextFromSelectWithName(optionText, name, getDefaultExplicitWaitTime());
        }
    }

    @Override
    public void selectOptionByTextFromSelectWithName(final String optionText, final String name, final int waitTime) {
        if (waitTime <= 0) {
            selectOptionByTextFromSelectWithName(name, optionText);
        } else {
            final WebDriverWait wait = new WebDriverWait(webDriver, waitTime);
            new Select(wait.until(ExpectedConditions.elementToBeClickable((By.name(name))))).selectByVisibleText(optionText);
        }
    }

    @Override
    public void populateElementWithName(final String name, final String text) {
        if (getDefaultExplicitWaitTime() <= 0) {
            webDriver.findElement(By.name(name)).sendKeys(text);
        } else {
            populateElementWithName(name, text, getDefaultExplicitWaitTime());
        }
    }

    @Override
    public void populateElementWithName(final String name, final String text, final int waitTime) {
        if (waitTime <= 0) {
            populateElementWithName(name, text);
        } else {
            final WebDriverWait wait = new WebDriverWait(webDriver, waitTime);
            wait.until(ExpectedConditions.presenceOfElementLocated((By.name(name)))).sendKeys(text);
        }
    }

    @Override
    public String getTextFromElementWithName(final String name) {
        if (getDefaultExplicitWaitTime() <= 0) {
            final WebElement element = webDriver.findElement(By.name(name));

            if (StringUtils.isNotBlank(element.getAttribute("value"))) {
                return element.getAttribute("value");
            }

            return element.getText();
        } else {
            return getTextFromElementWithName(name, getDefaultExplicitWaitTime());
        }
    }

    @Override
    public String getTextFromElementWithName(final String name, final int waitTime) {
        if (waitTime <= 0) {
            return getTextFromElementWithName(name);
        } else {
            final WebDriverWait wait = new WebDriverWait(webDriver, waitTime);
            return wait.until(ExpectedConditions.presenceOfElementLocated((By.name(name)))).getText();
        }
    }

    @Override
    public void clickElementIfOtherExists(final String force, final String locator, final String ifOtherExists) {
        clickElementIfOtherExists(force, locator, getDefaultExplicitWaitTime(), ifOtherExists);
    }

    @Override
    public void clickElementIfOtherExists(final String force, final String locator, final int waitTime, final String ifOtherExists) {
        Try.of(() -> SIMPLE_BY.getElement(
                getWebDriver(),
                ifOtherExists,
                waitTime,
                ExpectedConditions::presenceOfElementLocated))
                .onSuccess(e -> clickElementIfExists(force, locator, waitTime, null));
    }

    @Override
    public void clickElementIfOtherNotExists(final String force, final String locator, final String ifOtherExists) {
        clickElementIfOtherNotExists(force, locator, getDefaultExplicitWaitTime(), ifOtherExists);
    }

    @Override
    public void clickElementIfOtherNotExists(final String force, final String locator, final int waitTime, final String ifOtherExists) {
        Try.of(() -> SIMPLE_BY.getElement(
                getWebDriver(),
                ifOtherExists,
                waitTime,
                ExpectedConditions::presenceOfElementLocated))
                .onFailure(ex -> clickElementIfExists(force, locator, waitTime, null));
    }

    @Override
    public void clickElementIfExists(final String force, final String locator, final String ifExistsOption) {
        clickElementIfExists(force, locator, getDefaultExplicitWaitTime(), ifExistsOption);
    }

    @Override
    public void clickElementIfExists(final String force, final String locator, final int waitTime, final String ifExistsOption) {
        try {
            if (force != null) {
                final WebElement element = SIMPLE_BY.getElement(
                        getWebDriver(),
                        locator,
                        waitTime,
                        ExpectedConditions::presenceOfElementLocated);
                ((JavascriptExecutor) getWebDriver()).executeScript("arguments[0].click();", element);
            } else {
                SIMPLE_BY.getElement(
                        getWebDriver(),
                        locator,
                        waitTime,
                        ExpectedConditions::elementToBeClickable).click();
            }
        } catch (final WebElementException ex) {
            if (StringUtils.isEmpty(ifExistsOption)) {
                throw ex;
            }
        }
    }

    @Override
    public void selectOptionByTextFromSelectIfExists(final String optionText, final String locator, final String ifExistsOption) {
        selectOptionByTextFromSelectIfExists(optionText, locator, getDefaultExplicitWaitTime(), ifExistsOption);
    }

    @Override
    public void selectOptionByTextFromSelectIfExists(final String optionText, final String locator, final int waitTime, final String ifExistsOption) {
        try {
            new Select(SIMPLE_BY.getElement(
                    getWebDriver(),
                    locator,
                    waitTime,
                    ExpectedConditions::elementToBeClickable)).selectByVisibleText(optionText);
        } catch (final WebElementException ex) {
            if (StringUtils.isEmpty(ifExistsOption)) {
                throw ex;
            }
        }
    }

    @Override
    public void selectOptionByValueFromSelectIfExists(final String optionValue, final String locator, final String ifExistsOption) {
        selectOptionByValueFromSelectIfExists(optionValue, locator, getDefaultExplicitWaitTime(), ifExistsOption);
    }

    @Override
    public void selectOptionByValueFromSelectIfExists(final String optionValue, final String locator, final int waitTime, final String ifExistsOption) {
        try {
            new Select(SIMPLE_BY.getElement(
                    getWebDriver(),
                    locator,
                    waitTime,
                    ExpectedConditions::elementToBeClickable)).selectByValue(optionValue);
        } catch (final WebElementException ex) {
            if (StringUtils.isEmpty(ifExistsOption)) {
                throw ex;
            }
        }
    }

    @Override
    public void populateElement(final String locator, final String keystrokeDelay, final String text, final String ifExistsOption) {
        populateElement(locator, keystrokeDelay, text, getDefaultExplicitWaitTime(), ifExistsOption);
    }

    @Override
    public void populateElement(final String locator, final String keystrokeDelay, final String text, final int waitTime, final String ifExistsOption) {
        try {
            populateElementWithText(
                    text,
                    SIMPLE_BY.getElement(
                            getWebDriver(),
                            locator,
                            waitTime,
                            ExpectedConditions::elementToBeClickable),
                    NumberUtils.toInt(keystrokeDelay, Constants.DEFAULT_INPUT_DELAY));
        } catch (final WebElementException ex) {
            if (StringUtils.isEmpty(ifExistsOption)) {
                throw ex;
            }
        }
    }

    @Override
    public void populateHiddenElement(final String locator, final String text, final String ifExistsOption) {
        populateHiddenElement(locator, text, getDefaultExplicitWaitTime(), ifExistsOption);
    }

    @Override
    public void populateHiddenElement(final String locator, final String text, final int waitTime, final String ifExistsOption) {
        try {
            populateElementWithText(
                    text,
                    SIMPLE_BY.getElement(
                            getWebDriver(),
                            locator,
                            waitTime,
                            ExpectedConditions::presenceOfElementLocated),
                    0);
        } catch (final WebElementException ex) {
            if (StringUtils.isEmpty(ifExistsOption)) {
                throw ex;
            }
        }
    }

    @Override
    public void clearIfExists(final String locator, final String ifExistsOption) {
        clearIfExists(locator, getDefaultExplicitWaitTime(), ifExistsOption);
    }

    @Override
    public void clearIfExists(final String locator, final int waitTime, final String ifExistsOption) {
        try {
            SIMPLE_BY.getElement(
                    getWebDriver(),
                    locator,
                    waitTime,
                    ExpectedConditions::elementToBeClickable).clear();
        } catch (final WebElementException ex) {
            if (StringUtils.isEmpty(ifExistsOption)) {
                throw ex;
            }
        }
    }

    @Override
    public void scrollDown(final String distance) {
        ((JavascriptExecutor) getWebDriver()).executeScript(
                "window.scrollBy(0,arguments[0])", NumberUtils.toInt(distance, 0));
    }

    @Override
    public void scrollUp(final String distance) {
        ((JavascriptExecutor) getWebDriver()).executeScript("window.scrollBy(0,arguments[0])", NumberUtils.toInt(distance, 0) * -1);
    }

    @Override
    public void verifyUrl(final String regex) {
        if (!Pattern.compile(regex).matcher(getWebDriver().getCurrentUrl()).matches()) {
            throw new ValidationException("The URL " + getWebDriver().getCurrentUrl() + " does not match the regex " + regex);
        }
    }

    @Override
    public String getTextFromElementIfExists(final String locator, final String ifExistsOption) {
        return getTextFromElementIfExists(locator, getDefaultExplicitWaitTime(), ifExistsOption);
    }

    @Override
    public String getTextFromElementIfExists(final String locator, final int waitTime, final String ifExistsOption) {
        try {
            final WebElement element = SIMPLE_BY.getElement(
                    getWebDriver(),
                    locator,
                    waitTime,
                    ExpectedConditions::presenceOfElementLocated);

            if (StringUtils.isNotBlank(element.getAttribute("value"))) {
                return element.getAttribute("value");
            }

            return element.getText();
        } catch (final WebElementException ex) {
            if (StringUtils.isEmpty(ifExistsOption)) {
                throw ex;
            }
        }

        return null;
    }

    @Override
    public void maximizeWindow() {
        webDriver.manage().window().maximize();
    }

    @Override
    public void verifyElementExists(final String locator, final String ifExistsOption) {
        verifyElementExists(locator, getDefaultExplicitWaitTime(), ifExistsOption);
    }

    @Override
    public void verifyElementExists(final String locator, final int waitTime, final String ifExistsOption) {
        try {
            SIMPLE_BY.getElement(
                    getWebDriver(),
                    locator,
                    waitTime,
                    ExpectedConditions::presenceOfElementLocated);
        } catch (final WebElementException ex) {
            if (StringUtils.isEmpty(ifExistsOption)) {
                throw ex;
            }
        }
    }

    @Override
    public void verifyElementDoesNotExist(final String locator) {
        verifyElementDoesNotExist(locator, getDefaultExplicitWaitTime());
    }

    @Override
    public void verifyElementDoesNotExist(String locator, int waitTime) {
        final long startTime = System.currentTimeMillis();

        while (System.currentTimeMillis() < startTime + waitTime * 1000) {
            if (Try.of(() -> SIMPLE_BY.getElement(
                    getWebDriver(),
                    locator,
                    0,
                    ExpectedConditions::presenceOfElementLocated)).isFailure()) {
                return;
            }
        }

        throw new WebElementException("Element located by " + locator + " was still present after " + waitTime + " seconds");
    }

    @Override
    public String getTitle() {
        return getWebDriver().getTitle();
    }


    @Override
    public void browserZoomIn() {
        if (SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.DISABLE_ZOOM, true)) {
            return;
        }

        try {
            final Robot robot = new Robot();
            if (OSValidator.isMac()) {
                robot.keyPress(KeyEvent.VK_META);
                robot.keyPress(KeyEvent.VK_EQUALS);
                robot.keyRelease(KeyEvent.VK_META);
                robot.keyRelease(KeyEvent.VK_EQUALS);
            } else {
                robot.keyPress(KeyEvent.VK_CONTROL);
                robot.keyPress(KeyEvent.VK_EQUALS);
                robot.keyRelease(KeyEvent.VK_CONTROL);
                robot.keyRelease(KeyEvent.VK_EQUALS);
            }
        } catch (final AWTException ex) {
            throw new InteractionException(ex);
        }
    }

    @Override
    public void browserZoomOut() {
        if (SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.DISABLE_ZOOM, true)) {
            return;
        }

        try {
            final Robot robot = new Robot();
            if (OSValidator.isMac()) {
                robot.keyPress(KeyEvent.VK_META);
                robot.keyPress(KeyEvent.VK_MINUS);
                robot.keyRelease(KeyEvent.VK_META);
                robot.keyRelease(KeyEvent.VK_MINUS);
            } else {
                robot.keyPress(KeyEvent.VK_CONTROL);
                robot.keyPress(KeyEvent.VK_MINUS);
                robot.keyRelease(KeyEvent.VK_CONTROL);
                robot.keyRelease(KeyEvent.VK_MINUS);
            }
        } catch (final AWTException ex) {
            throw new InteractionException(ex);
        }
    }

    @Override
    public void scrollElementIntoViewIfExists(final String locator, final String offset, final String ifExistsOption) {
        scrollElementIntoViewIfExists(locator, offset, getDefaultExplicitWaitTime(), ifExistsOption);
    }

    @Override
    public void scrollElementIntoViewIfExists(final String locator, final String offset, final int waitTime, final String ifExistsOption) {
        try {

            final int scrollTime = 1000;
            final WebElement element = SIMPLE_BY.getElement(
                    getWebDriver(),
                    locator,
                    waitTime,
                    ExpectedConditions::presenceOfElementLocated);
            ((JavascriptExecutor) getWebDriver()).executeScript("""
                            var getScrollParent = function () {
                                var regex = /(auto|scroll)/;

                                var parents = function (node, ps) {
                                    if (node.parentNode === null) { return ps; }
                                    return parents(node.parentNode, ps.concat([node]));
                                };

                                var style = function (node, prop) {
                                    return getComputedStyle(node, null).getPropertyValue(prop);
                                };

                                var overflow = function (node) {
                                    return style(node, "overflow") + style(node, "overflow-y") + style(node, "overflow-x");
                                };

                                var scroll = function (node) {
                                    return regex.test(overflow(node));
                                };

                                var windowInsteadOfHtml = function (node) {
                                    return node === document.documentElement || node === document.body
                                            ? window
                                            : node;
                                }

                                var scrollParent = function (node) {
                                    if (!(node instanceof HTMLElement || node instanceof SVGElement)) {
                                        return ;
                                    }

                                    var ps = parents(node.parentNode, []);

                                    for (var i = 0; i < ps.length; i += 1) {
                                        if (scroll(ps[i])) {
                                            return windowInsteadOfHtml(ps[i]);
                                        }
                                    }

                                    return windowInsteadOfHtml(document.scrollingElement || document.documentElement);
                                };

                                return scrollParent;
                            }();

                            function getElementY(element) {
                                var parent = getScrollParent(element)
                                return getScrollTop(parent) + element.getBoundingClientRect().top
                            }

                            function getScrollTop(parent) {
                                // scrollTop is undefined on the window object
                                return parent.scrollTop !== undefined ? parent.scrollTop : parent.scrollY;
                            }

                            function getScrollingHeight(parent) {
                                return parent.scrollHeight !== undefined ? parent.scrollHeight : document.body.scrollHeight;
                            }

                            function scrollElement(parent, distance) {
                                if (parent.scrollTop !== undefined) {
                                    parent.scrollTop = distance
                                } else {
                                    parent.scrollTo(0, distance)
                                }
                            }

                            function doScrolling(element, offset, duration) {
                                var parent = getScrollParent(element)
                                var startingY = getScrollTop(parent)
                                var elementY = getElementY(element) + offset
                                // If element is close to page's bottom then parent will scroll only to some position above the element.
                                var targetY = getScrollingHeight(parent) - elementY < parent.innerHeight ? getScrollingHeight(parent) - parent.innerHeight : elementY
                                var diff = targetY - startingY
                                // Easing function: easeInOutCubic
                                // From: https://gist.github.com/gre/1650294
                                var easing = function (t) { return t<.5 ? 4*t*t*t : (t-1)*(2*t-2)*(2*t-2)+1 }
                                var start

                                if (!diff) return
                                // Bootstrap our animation - it will get called right before next frame shall be rendered.
                                window.requestAnimationFrame(function step(timestamp) {
                                    if (!start) start = timestamp
                                    // Elapsed milliseconds since start of scrolling.
                                    var time = timestamp - start
                                    // Get percent of completion in range [0, 1].
                                    var percent = Math.min(time / duration, 1)
                                    // Apply the easing.
                                    // It can cause bad-looking slow frames in browser performance tool, so be careful.
                                    percent = easing(percent)

                                    scrollElement(parent, startingY + diff * percent)

                                    // Proceed with animation as long as we wanted it to.
                                    if (time < duration) {
                                        window.requestAnimationFrame(step)
                                    }
                                })
                            }
                            doScrolling(arguments[0], arguments[1], arguments[2]);
                            """,
                    element,
                    Integer.parseInt(offset == null ? "0" : offset),
                    scrollTime);
            Try.run(() -> Thread.sleep(scrollTime));
        } catch (final WebElementException ex) {
            if (StringUtils.isEmpty(ifExistsOption)) {
                throw ex;
            }
        }
    }

    @Override
    public void pressEscape(final String locator) {
        pressEscape(locator, getDefaultExplicitWaitTime());
    }

    @Override
    public void pressEscape(final String locator, final int waitTime) {
        SIMPLE_BY.getElement(
                getWebDriver(),
                locator,
                waitTime,
                ExpectedConditions::presenceOfElementLocated).sendKeys(Keys.ESCAPE);
    }

    @Override
    public void pressEnter(final String locator) {
        pressEnter(locator, getDefaultExplicitWaitTime());
    }

    @Override
    public void pressEnter(final String locator, final int waitTime) {
        SIMPLE_BY.getElement(
                getWebDriver(),
                locator,
                waitTime,
                ExpectedConditions::presenceOfElementLocated).sendKeys(Keys.ENTER);
    }

    @Override
    public void pressFunctionKey(final String key) {
        switch (key) {
            case "F1": ((RemoteWebDriver)getWebDriver()).getKeyboard().pressKey(Keys.F1); break;
            case "F2": ((RemoteWebDriver)getWebDriver()).getKeyboard().pressKey(Keys.F2); break;
            case "F3": ((RemoteWebDriver)getWebDriver()).getKeyboard().pressKey(Keys.F3); break;
            case "F4": ((RemoteWebDriver)getWebDriver()).getKeyboard().pressKey(Keys.F4); break;
            case "F5": ((RemoteWebDriver)getWebDriver()).getKeyboard().pressKey(Keys.F5); break;
            case "F6": ((RemoteWebDriver)getWebDriver()).getKeyboard().pressKey(Keys.F6); break;
            case "F7": ((RemoteWebDriver)getWebDriver()).getKeyboard().pressKey(Keys.F7); break;
            case "F8": ((RemoteWebDriver)getWebDriver()).getKeyboard().pressKey(Keys.F8); break;
            case "F9": ((RemoteWebDriver)getWebDriver()).getKeyboard().pressKey(Keys.F9); break;
            case "F10": ((RemoteWebDriver)getWebDriver()).getKeyboard().pressKey(Keys.F10); break;
            case "F11": ((RemoteWebDriver)getWebDriver()).getKeyboard().pressKey(Keys.F11); break;
            case "F12": ((RemoteWebDriver)getWebDriver()).getKeyboard().pressKey(Keys.F12); break;
        }

    }

    @Override
    public void clearTransition() {
        SCREEN_TRANSITIONS.clear();
    }

    @Override
    public void fadeScreen(final String red, final String green, final String blue, final String duration) {
        SCREEN_TRANSITIONS.fadeScreen(
                NumberUtils.toFloat(red, 1),
                NumberUtils.toFloat(green, 1),
                NumberUtils.toFloat(blue, 1),
                NumberUtils.toLong(duration, 2000));
    }

    @Override
    public void displayNote(final String text, final String duration) {
        final int fadeOut = 1;
        final String style = "z-index: 999999; position: fixed; bottom: 50px; height: 100px; line-height: 100px; left: 50px; right: 50px; text-align: center; background: rgba(0, 0, 0, 1); color: white; font-family: Arial, Helvetica, sans-serif; font-size: 2em; opacity: 0.7; transition: opacity " + fadeOut + "s linear;";
        ((JavascriptExecutor) getWebDriver()).executeScript(
                "const div = document.createElement('div'); " +
                        "div.setAttribute('id', 'webdriver-note'); " +
                        "div.setAttribute('style', '" + style + "'); " +
                        "const span = document.createElement('span');" +
                        "span.setAttribute('style', 'display: inline-block; vertical-align: middle; line-height: normal;'); " +
                        "span.innerHTML = '" +
                        text.replaceAll("\\\\", "\\\\\\\\\\\\\\\\").replaceAll("'", "\\\\'") +
                        "'; " +
                        "div.appendChild(span); " +
                        "document.body.appendChild(div); ");

        Try.run(() -> Thread.sleep(NumberUtils.toInt(duration, 1) * 1000));

        ((JavascriptExecutor) getWebDriver()).executeScript(
                "const div = document.getElementById('webdriver-note'); " +
                        "div.style.opacity = 0; " +
                        "window.setTimeout(function(){document.body.removeChild(div)}, " + fadeOut * 1000 + ");");

        Try.run(() -> Thread.sleep(fadeOut * 1000));
    }

    public void refresh() {
        getWebDriver().navigate().refresh();
    }

    @Override
    public void refreshIfExists(final String locator, final String doesNotExist) {
        refreshIfExists(locator, doesNotExist, getDefaultExplicitWaitTime());
    }

    @Override
    public void refreshIfExists(final String locator, final String doesNotExist, final int waitTime) {
        final Try element = Try.run(() -> SIMPLE_BY.getElement(
                getWebDriver(),
                locator,
                waitTime,
                ExpectedConditions::presenceOfElementLocated));

        if ((StringUtils.isNotBlank(doesNotExist) && element.isFailure()) ||
                StringUtils.isBlank(doesNotExist) && element.isSuccess()) {
            refresh();
        }
    }

    @Override
    public void verifyTextFromElementIfExists(final String locator, final String regex, final String ifExistsOption) {
        verifyTextFromElementIfExists(locator, regex, getDefaultExplicitWaitTime(), ifExistsOption);
    }

    @Override
    public void verifyTextFromElementIfExists(final String locator, final String regex, final int waitTime, final String ifExistsOption) {
        try {
            final String content = getTextFromElementIfExists(locator, waitTime, ifExistsOption);

            if (!Pattern.compile(regex).matcher(content).matches()) {
                throw new ValidationException("The text content \"" + content + "\" does not match the regex \"" + regex + "\"");
            }
        } catch (final WebElementException ex) {
            if (StringUtils.isEmpty(ifExistsOption)) {
                throw ex;
            }
        }
    }

    @Override
    public void setOctopusPercent(final String percent, final String message) {
        SERVICE_MESSAGE_GENERATOR.setProgress(NumberUtils.toInt(percent, 0), message);
    }

    @Override
    public void writeAliasValueToOctopusVariable(final String alias, final String variable) {
        SERVICE_MESSAGE_GENERATOR.newVariable(alias, variable);
    }

    @Override
    public void writeAliasValueToOctopusSensitiveVariable(final String alias, final String variable) {
        SERVICE_MESSAGE_GENERATOR.newVariable(alias, variable, true);
    }

    @Override
    public void defineArtifact(String name, String path) {
        SERVICE_MESSAGE_GENERATOR.newArtifact(path, name);
    }

    @Override
    public void runJavascript(final String code) {
        ((JavascriptExecutor) getWebDriver()).executeScript(code);
    }

    private void populateElementWithText(final String text, final WebElement element, final int keystrokeDelay) {
        if (keystrokeDelay <= 0) {
            element.sendKeys(text);
        } else {
            text.chars().forEach(c -> {
                element.sendKeys(String.valueOf((char) c));
                Try.run(() -> Thread.sleep(Constants.DEFAULT_INPUT_DELAY));
            });
        }
    }
}