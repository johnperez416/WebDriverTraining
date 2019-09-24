package com.octopus.decorators;

import com.octopus.AutomatedBrowser;
import com.octopus.Constants;
import com.octopus.decoratorbase.AutomatedBrowserBase;
import com.octopus.utils.SimpleBy;
import com.octopus.utils.SystemPropertyUtils;
import com.octopus.utils.impl.SimpleByImpl;
import com.octopus.utils.impl.SystemPropertyUtilsImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.HashMap;
import java.util.Map;

public class HighlightDecorator extends AutomatedBrowserBase {
    private static final SimpleBy SIMPLE_BY = new SimpleByImpl();
    private static final SystemPropertyUtils SYSTEM_PROPERTY_UTILS = new SystemPropertyUtilsImpl();
    private Map<String, String> originalStyles = new HashMap<>();

    public HighlightDecorator() {

    }

    public HighlightDecorator(final AutomatedBrowser automatedBrowser) {
        super(automatedBrowser);
    }

    @Override
    public void elementHighlight(final String location, final String locator,final String offset) {
        this.elementHighlight(location, locator, offset, getDefaultExplicitWaitTime());
    }

    @Override
    public void elementHighlight(final String location, final String locator, final String offset, final int waitTime) {
        if (SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.DISABLE_HIGHLIGHTS, false)) {
            return;
        }

        final int offsetValue = NumberUtils.toInt(offset, 10);

        final WebElement element = SIMPLE_BY.getElement(
                getWebDriver(),
                locator,
                waitTime,
                by -> ExpectedConditions.presenceOfElementLocated(by));

        originalStyles.put(locator, element.getAttribute("style"));

        if (StringUtils.equals(location.trim(), "inside")) {
            ((JavascriptExecutor) getWebDriver()).executeScript(
                    "arguments[0].style.border = '5px solid rgb(0, 204, 101)';",
                    element);
        } else {
            ((JavascriptExecutor) getWebDriver()).executeScript(
                    "arguments[0].style.outline = '5px solid rgb(0, 204, 101)'; arguments[0].style['outline-offset'] = '" + offsetValue + "px';",
                    element);
        }
    }

    @Override
    public void removeElementHighlight(final String locator) {
        removeElementHighlight(locator, getDefaultExplicitWaitTime());
    }

    @Override
    public void removeElementHighlight(final String locator, final int waitTime) {
        if (SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.DISABLE_HIGHLIGHTS, false)) {
            return;
        }

        if (originalStyles.containsKey(locator)) {
            final WebElement element = SIMPLE_BY.getElement(
                    getWebDriver(),
                    locator,
                    waitTime,
                    by -> ExpectedConditions.presenceOfElementLocated(by));

            ((JavascriptExecutor) getWebDriver()).executeScript(
                    "arguments[0].setAttribute('style', '" + originalStyles.get(locator) + "');",
                    element);

            originalStyles.remove(locator);
        }
    }
}
