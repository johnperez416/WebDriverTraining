package com.octopus.utils.impl;

import com.octopus.Constants;
import com.octopus.exceptions.WebElementException;
import com.octopus.utils.ExpectedConditionCallback;
import com.octopus.utils.SimpleBy;
import com.octopus.utils.SystemPropertyUtils;
import io.vavr.control.Try;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class SimpleByImpl implements SimpleBy {
    static final Logger LOGGER = Logger.getLogger(SimpleByImpl.class.toString());
    private static final SystemPropertyUtils SYSTEM_PROPERTY_UTILS = new SystemPropertyUtilsImpl();
    private static final int MILLISECONDS_PER_SECOND = 1000;
    private static final int SUB_SECOND_TIME_SLICE = 100;
    private static final int SECOND_TIME_SLICE = 1000;

    @Override
    public WebElement getElement(
            final WebDriver webDriver,
            final String locator,
            final int waitTime,
            final ExpectedConditionCallback expectedConditionCallback) {

        // If the wait time is less than a second, poll every 100 milliseconds. Otherwise poll every second.
        return getElement(webDriver, locator, waitTime, expectedConditionCallback, Math.min(SUB_SECOND_TIME_SLICE, SECOND_TIME_SLICE));
    }

    @Override
    public WebElement getElement(
            final WebDriver webDriver,
            final String locator,
            final int waitTime,
            final ExpectedConditionCallback expectedConditionCallback,
            final int timeSlice) {

        final By[] byInstances = new By[] {
                By.id(locator),
                By.xpath(locator),
                By.cssSelector(locator),
                By.className(locator),
                By.linkText(locator),
                By.name(locator)
        };

        long time = -1;

        while (time < waitTime * MILLISECONDS_PER_SECOND) {
            for (final By by : byInstances) {
                try {
                    final WebDriverWaitEx wait = new WebDriverWaitEx(
                            webDriver,
                            timeSlice,
                            TimeUnit.MILLISECONDS);
                    final ExpectedCondition<WebElement> condition =
                            expectedConditionCallback.getExpectedCondition(by);
                    final WebElement element = wait.until(condition);

                    saveMultipleElements(webDriver, by, locator);

                    return element;
                } catch (final Exception ignored) {
                  /*
                    Do nothing
                  */
                }

                time += timeSlice;
            }
        }

        throw new WebElementException("All attempts to find element located with " + locator + " failed after " + waitTime + " seconds");
    }

    private void saveMultipleElements(final WebDriver webDriver, final By by, final String locator) {
        final List<WebElement> matched = webDriver.findElements(by);
        if (matched.size() <= 1) {
            return;
        }

        LOGGER.info("\nMatched " + matched.size() + " elements with the locator on the page " + webDriver.getCurrentUrl());
        LOGGER.info(locator);
        matched.stream().forEach(e -> {
            LOGGER.info(e.getTagName() +
                    " X: " + e.getLocation().x +
                    " Y: " + e.getLocation().y);

            if (SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.SAVE_SCREENSHOTS_OF_MATCHED_ELEMENTS, false)) {
                Try.run(() -> {
                    final Path temp = Files.createTempFile("element", ".png");
                    FileUtils.copyFile(e.getScreenshotAs(OutputType.FILE), temp.toFile());
                    LOGGER.info(temp.toFile().getCanonicalPath());
                });
            }
        });
        LOGGER.info("Consider fixing the locator to be specific to a single element.");

    }
}