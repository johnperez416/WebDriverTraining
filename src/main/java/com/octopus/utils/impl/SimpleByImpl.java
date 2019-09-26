package com.octopus.utils.impl;

import com.octopus.exceptions.WebElementException;
import com.octopus.utils.ExpectedConditionCallback;
import com.octopus.utils.SimpleBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class SimpleByImpl implements SimpleBy {

    private static final int MILLISECONDS_PER_SECOND = 1000;
    private static final int TIME_SLICE = 100;

    @Override
    public WebElement getElement(
            WebDriver webDriver,
            String locator,
            int waitTime,
            ExpectedConditionCallback expectedConditionCallback) {

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
                            TIME_SLICE,
                            TimeUnit.MILLISECONDS);
                    final ExpectedCondition<WebElement> condition =
                            expectedConditionCallback.getExpectedCondition(by);
                    final WebElement element = wait.until(condition);

                    final List<WebElement> matched = webDriver.findElements(by);
                    if (matched.size() > 1) {
                        System.out.println("\nMatched " + matched.size() + " elements with the locator:");
                        matched.stream().forEach(e -> System.out.println(
                                e.getTagName() +
                                        " X: " + e.getLocation().x +
                                        " Y: " + e.getLocation().y));
                        System.out.println(locator);
                        System.out.println("Consider fixing the locator to be specific to a single element.");
                    }

                    return element;
                } catch (final Exception ignored) {
                  /*
                    Do nothing
                  */
                }

                time += TIME_SLICE;
            }
        }

        throw new WebElementException("All attempts to find element failed");
    }
}