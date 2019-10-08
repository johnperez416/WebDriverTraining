package com.octopus.decorators;

import com.octopus.Constants;
import com.octopus.decoratorbase.AutomatedBrowserBase;
import com.octopus.exceptions.SaveException;
import com.octopus.exceptions.ValidationException;
import com.octopus.exceptions.VideoException;
import com.octopus.utils.S3Uploader;
import com.octopus.utils.ScreenTransitions;
import com.octopus.utils.SimpleBy;
import com.octopus.utils.SystemPropertyUtils;
import com.octopus.utils.impl.S3UploaderImpl;
import com.octopus.utils.impl.ScreenTransitionsImpl;
import com.octopus.utils.impl.SimpleByImpl;
import com.octopus.utils.impl.SystemPropertyUtilsImpl;
import io.cucumber.java.en.Then;
import io.vavr.control.Try;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.monte.media.Format;
import org.monte.media.FormatKeys;
import org.monte.media.VideoFormatKeys;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

public class WebDriverDecorator extends AutomatedBrowserBase {
    private static final SimpleBy SIMPLE_BY = new SimpleByImpl();
    private static final ScreenTransitions SCREEN_TRANSITIONS = new ScreenTransitionsImpl();
    private static final SystemPropertyUtils SYSTEM_PROPERTY_UTILS = new SystemPropertyUtilsImpl();
    private static final S3Uploader S_3_UPLOADER = new S3UploaderImpl();
    private static ScreenRecorder screenRecorder;
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
        if (webDriver != null) {
            webDriver.quit();
        }
    }

    @Override
    public void startScreenRecording(final String file) {
        if (SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.DISABLE_VIDEO_RECORDING, false)) {
            return;
        }

        if (screenRecorder != null) {
            throw new VideoException("The screen is already recording!");
        }

        try {
            System.out.println("Starting video recording");

            // set the graphics configuration
            final GraphicsConfiguration gc = GraphicsEnvironment
                    .getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice()
                    .getDefaultConfiguration();

            // initialize the screen recorder:
            // - default graphics configuration
            // - full screen recording
            // - record in AVI format
            // - 15 frames per second
            // - black mouse pointer
            // - no audio
            // - save capture to predefined location

            screenRecorder = new ScreenRecorder(gc,
                    gc.getBounds(),
                    new Format(FormatKeys.MediaTypeKey, FormatKeys.MediaType.FILE, FormatKeys.MimeTypeKey, FormatKeys.MIME_AVI),
                    new Format(FormatKeys.MediaTypeKey, FormatKeys.MediaType.VIDEO, FormatKeys.EncodingKey, VideoFormatKeys.ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                            VideoFormatKeys.CompressorNameKey, VideoFormatKeys.ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                            VideoFormatKeys.DepthKey, 24, VideoFormatKeys.FrameRateKey, Rational.valueOf(30),
                            VideoFormatKeys.QualityKey, 1.0f,
                            VideoFormatKeys.KeyFrameIntervalKey, 30 * 60),
                    new Format(FormatKeys.MediaTypeKey, FormatKeys.MediaType.VIDEO, FormatKeys.EncodingKey, "black", FormatKeys.FrameRateKey, Rational.valueOf(60)),
                    null,
                    new File(file));
            screenRecorder.start();
        } catch (final Exception ex) {
            throw new VideoException("Failed to set up screen recording", ex);
        }
    }

    public static void staticStopScreenRecording() {
        try {
            if (screenRecorder != null) {
                System.out.println("Stopping video recording");
                screenRecorder.stop();
            }
            screenRecorder = null;
        } catch (final IOException ex) {
            throw new VideoException("Failed to stop screen recording", ex);
        }
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
    public void takeScreenshot(final String directory, final String filename) {
        takeScreenshot(directory + File.separator + filename);
    }

    @Override
    public void takeScreenshot(final String file) {
        takeScreenshot(file, false);
    }

    @Override
    public void takeScreenshot(final String file, boolean force) {
        if (!force && SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.DISABLE_SCREENSHOTS, false)) {
            return;
        }

        try {
            if (file.startsWith("s3://")) {
                final String[] urlParts = file.split("/");
                if (urlParts.length >= 4) {
                    final File screenshot = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
                    S_3_UPLOADER.uploadFileToS3(
                            urlParts[2],
                            file.substring(5 + urlParts[2].length() + 1),
                            screenshot,
                            true);
                } else {
                    throw new SaveException("S3 paths must be in the format S3://bucket/filename or S3://bucket/dir/filename. The path \"" + file + "\" is missing some paths.");
                }
            } else {
                final File screenshot = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(screenshot, new File(file));
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
    public void clickElementIfExists(final String force, final String locator, final String ifExists) {
        clickElementIfExists(force, locator, getDefaultExplicitWaitTime(), ifExists);
    }

    @Override
    public void clickElementIfExists(final String force, final String locator, final int waitTime, final String ifExists) {
        try {
            if (force != null) {
                final WebElement element = SIMPLE_BY.getElement(
                        getWebDriver(),
                        locator,
                        waitTime,
                        by -> ExpectedConditions.presenceOfElementLocated(by));
                ((JavascriptExecutor) getWebDriver()).executeScript("arguments[0].click();", element);
            } else {
                SIMPLE_BY.getElement(
                        getWebDriver(),
                        locator,
                        waitTime,
                        by -> ExpectedConditions.elementToBeClickable(by)).click();
            }
        } catch (final TimeoutException ex) {
            if (StringUtils.isEmpty(ifExists)) {
                throw ex;
            }
        }
    }

    @Override
    public void clickElementIfExists(final String locator, final String ifExists) {
        clickElementIfExists(null, locator, ifExists);
    }

    @Override
    public void clickElementIfExists(final String locator, final int waitTime, final String ifExists) {
        clickElementIfExists(null, locator, waitTime, ifExists);
    }

    @Override
    public void selectOptionByTextFromSelectIfExists(final String optionText, final String locator, final String ifExists) {
        selectOptionByTextFromSelectIfExists(optionText, locator, getDefaultExplicitWaitTime(), ifExists);
    }

    @Override
    public void selectOptionByTextFromSelectIfExists(final String optionText, final String locator, final int waitTime, final String ifExists) {
        try {
            new Select(SIMPLE_BY.getElement(
                    getWebDriver(),
                    locator,
                    waitTime,
                    by -> ExpectedConditions.elementToBeClickable(by))).selectByVisibleText(optionText);
        } catch (final TimeoutException ex) {
            if (StringUtils.isEmpty(ifExists)) {
                throw ex;
            }
        }
    }

    @Override
    public void selectOptionByValueFromSelectIfExists(final String optionValue, final String locator, final String ifExists) {
        selectOptionByValueFromSelectIfExists(optionValue, locator, getDefaultExplicitWaitTime(), ifExists);
    }

    @Override
    public void selectOptionByValueFromSelectIfExists(final String optionValue, final String locator, final int waitTime, final String ifExists) {
        try {
            new Select(SIMPLE_BY.getElement(
                    getWebDriver(),
                    locator,
                    waitTime,
                    by -> ExpectedConditions.elementToBeClickable(by))).selectByValue(optionValue);
        } catch (final TimeoutException ex) {
            if (StringUtils.isEmpty(ifExists)) {
                throw ex;
            }
        }
    }

    @Override
    public void populateElement(final String locator, final String text, final String ifExists) {
        populateElement(locator, text, getDefaultExplicitWaitTime(), ifExists);
    }

    @Override
    public void populateElement(final String locator, final String text, final int waitTime, final String ifExists) {
        try {
            SIMPLE_BY.getElement(
                    getWebDriver(),
                    locator,
                    waitTime,
                    by -> ExpectedConditions.presenceOfElementLocated(by)).sendKeys(text);
        } catch (final TimeoutException ex) {
            if (StringUtils.isEmpty(ifExists)) {
                throw ex;
            }
        }
    }

    @Override
    public void clearIfExists(final String locator, final String ifExists) {
        clearIfExists(locator, getDefaultExplicitWaitTime(), ifExists);
    }

    @Override
    public void clearIfExists(final String locator, final int waitTime, final String ifExists) {
        try {
        SIMPLE_BY.getElement(
                getWebDriver(),
                locator,
                waitTime,
                by -> ExpectedConditions.presenceOfElementLocated(by)).clear();
        } catch (final TimeoutException ex) {
            if (StringUtils.isEmpty(ifExists)) {
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
    public String getTextFromElementIfExists(final String locator, final String ifExists) {
        return getTextFromElementIfExists(locator, getDefaultExplicitWaitTime(), ifExists);
    }

    @Override
    public String getTextFromElementIfExists(final String locator, final int waitTime, final String ifExists) {
        try {
            final WebElement element =  SIMPLE_BY.getElement(
                    getWebDriver(),
                    locator,
                    waitTime,
                    by -> ExpectedConditions.presenceOfElementLocated(by));

            if (StringUtils.isNotBlank(element.getAttribute("value"))) {
                return element.getAttribute("value");
            }

            return element.getText();
        } catch (final TimeoutException ex) {
            if (StringUtils.isEmpty(ifExists)) {
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
    public void scrollElementIntoViewIfExists(final String locator, final String offset, final String ifExists) {
        scrollElementIntoViewIfExists(locator, offset, getDefaultExplicitWaitTime(), ifExists);
    }

    @Override
    public void scrollElementIntoViewIfExists(final String locator, final String offset, final int waitTime, final String ifExists) {
        try {

            final int scrollTime = 1000;
            final WebElement element = SIMPLE_BY.getElement(
                    getWebDriver(),
                    locator,
                    waitTime,
                    by -> ExpectedConditions.presenceOfElementLocated(by));
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
                return parent.scrollTop !== undefined ? parent.scrollTop : parent.scrollY;
            }

            function getScrollingHeight(parent) {
                return parent.scrollHeight !== undefined ? parent.scrollHeight : document.body.scrollHeight;
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

                    parent.scrollTo(0, startingY + diff * percent)

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
        } catch (final TimeoutException ex) {
            if (StringUtils.isEmpty(ifExists)) {
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
                by -> ExpectedConditions.presenceOfElementLocated(by)).sendKeys(Keys.ESCAPE);
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
        final String style = "z-index: 999999; position: absolute; bottom: 50px; height: 100px; line-height: 100px; left: 50px; right: 50px; text-align: center; background: rgba(0, 0, 0, 1); color: white; font-family: Arial, Helvetica, sans-serif; font-size: 2em; opacity: 0.7; transition: opacity " + fadeOut + "s linear;";
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
    public void verifyTextFromElementIfExists(final String locator, final String regex, final String ifExists) {
        verifyTextFromElementIfExists(locator, regex, getDefaultExplicitWaitTime(), ifExists);
    }

    @Override
    public void verifyTextFromElementIfExists(final String locator, final String regex, final int waitTime, final String ifExists) {
        try {
            final String content = SIMPLE_BY.getElement(
                    getWebDriver(),
                    locator,
                    waitTime,
                    by -> ExpectedConditions.presenceOfElementLocated(by)).getText();

            if (!Pattern.compile(regex).matcher(content).matches()) {
                throw new ValidationException("The text content \"" + content + "\" does not match the regex \"" + regex + "\"");
            }
        } catch (final TimeoutException ex) {
            if (StringUtils.isEmpty(ifExists)) {
                throw ex;
            }
        }
    }
}