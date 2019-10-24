package com.octopus.decorators;

import com.octopus.AutomatedBrowser;
import com.octopus.Constants;
import com.octopus.decoratorbase.AutomatedBrowserBase;
import com.octopus.exceptions.WebElementException;
import com.octopus.utils.MouseMovementUtils;
import com.octopus.utils.SimpleBy;
import com.octopus.utils.SystemPropertyUtils;
import com.octopus.utils.impl.MouseMovementUtilsImpl;
import com.octopus.utils.impl.SimpleByImpl;
import com.octopus.utils.impl.SystemPropertyUtilsImpl;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MouseMovementDecorator extends AutomatedBrowserBase {
    private static final MouseMovementUtils MOUSE_MOVEMENT_UTILS = new MouseMovementUtilsImpl();
    private static final SystemPropertyUtils SYSTEM_PROPERTY_UTILS = new SystemPropertyUtilsImpl();
    private static final SimpleBy SIMPLE_BY = new SimpleByImpl();
    private int interactionCount = 0;

    private WebElement getElementById(final String id, final int waitTime) {
        if (waitTime <= 0) {
            return getWebDriver().findElement(By.id(id));
        } else {
            final WebDriverWait wait = new WebDriverWait(getWebDriver(), waitTime);
            return wait.until(ExpectedConditions.elementToBeClickable((By.id(id))));
        }
    }

    private WebElement getElementByXPath(final String xpath, final int waitTime) {
        if (waitTime <= 0) {
            return getWebDriver().findElement(By.xpath(xpath));
        } else {
            final WebDriverWait wait = new WebDriverWait(getWebDriver(), waitTime);
            return wait.until(ExpectedConditions.elementToBeClickable((By.xpath(xpath))));
        }
    }

    private WebElement getElementByCSSSelector(final String css, final int waitTime) {
        if (waitTime <= 0) {
            return getWebDriver().findElement(By.cssSelector(css));
        } else {
            final WebDriverWait wait = new WebDriverWait(getWebDriver(), waitTime);
            return wait.until(ExpectedConditions.elementToBeClickable((By.cssSelector(css))));
        }
    }

    private WebElement getElementByName(final String name, final int waitTime) {
        if (waitTime <= 0) {
            return getWebDriver().findElement(By.name(name));
        } else {
            final WebDriverWait wait = new WebDriverWait(getWebDriver(), waitTime);
            return wait.until(ExpectedConditions.elementToBeClickable((By.name(name))));
        }
    }

    public MouseMovementDecorator() {

    }

    public MouseMovementDecorator(final AutomatedBrowser automatedBrowser) {
        super(automatedBrowser);
    }

    @Override
    public void clickElementWithId(final String id) {
        ++interactionCount;

        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                () -> getElementById(id, getDefaultExplicitWaitTime()),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().clickElementWithId(
                    id,
                    SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.MOVE_CURSOR_TO_ELEMENT, false)
                            ? 0 : getDefaultExplicitWaitTime());
        }
    }

    @Override
    public void clickElementWithId(final String id, final int waitTime) {
        ++interactionCount;

        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                () -> getElementById(id, waitTime),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().clickElementWithId(
                    id,
                    SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.MOVE_CURSOR_TO_ELEMENT, false)
                            ? 0 : waitTime);
        }
    }

    @Override
    public void selectOptionByTextFromSelectWithId(final String optionText, final String id) {
        ++interactionCount;

        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                () -> getElementById(id, getDefaultExplicitWaitTime()),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().selectOptionByTextFromSelectWithId(
                    optionText,
                    id,
                    SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.MOVE_CURSOR_TO_ELEMENT, false)
                            ? 0 : getDefaultExplicitWaitTime());
        }
    }

    @Override
    public void selectOptionByTextFromSelectWithId(final String optionText, final String id, final int waitTime) {
        ++interactionCount;

        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                () -> getElementById(id, waitTime),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().selectOptionByTextFromSelectWithId(
                    optionText,
                    id,
                    SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.MOVE_CURSOR_TO_ELEMENT, false)
                            ? 0 : waitTime);
        }
    }

    @Override
    public void populateElementWithId(final String id, final String text) {
        ++interactionCount;

        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                () -> getElementById(id, getDefaultExplicitWaitTime()),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().populateElementWithId(
                    id,
                    text,
                    SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.MOVE_CURSOR_TO_ELEMENT, false)
                            ? 0 : getDefaultExplicitWaitTime());
        }
    }

    @Override
    public void populateElementWithId(final String id, final String text, final int waitTime) {
        ++interactionCount;

        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                () -> getElementById(id, waitTime),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().populateElementWithId(
                    id,
                    text,
                    SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.MOVE_CURSOR_TO_ELEMENT, false)
                            ? 0 : waitTime);
        }
    }

    @Override
    public void clickElementWithXPath(final String xpath) {
        ++interactionCount;

        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                () -> getElementByXPath(xpath, getDefaultExplicitWaitTime()),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().clickElementWithXPath(
                    xpath,
                    SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.MOVE_CURSOR_TO_ELEMENT, false)
                            ? 0 : getDefaultExplicitWaitTime());
        }
    }

    @Override
    public void clickElementWithXPath(final String xpath, final int waitTime) {
        ++interactionCount;

        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                () -> getElementByXPath(xpath, waitTime),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().clickElementWithXPath(
                    xpath,
                    SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.MOVE_CURSOR_TO_ELEMENT, false)
                            ? 0 : waitTime);
        }
    }

    @Override
    public void selectOptionByTextFromSelectWithXPath(final String optionText, final String xpath) {
        ++interactionCount;

        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                () -> getElementByXPath(xpath, getDefaultExplicitWaitTime()),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().selectOptionByTextFromSelectWithXPath(
                    optionText,
                    xpath,
                    SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.MOVE_CURSOR_TO_ELEMENT, false)
                            ? 0 : getDefaultExplicitWaitTime());
        }
    }

    @Override
    public void selectOptionByTextFromSelectWithXPath(final String optionText, final String xpath, final int waitTime) {
        ++interactionCount;

        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                () -> getElementByXPath(xpath, waitTime),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().selectOptionByTextFromSelectWithXPath(
                    optionText,
                    xpath,
                    SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.MOVE_CURSOR_TO_ELEMENT, false)
                            ? 0 : waitTime);
        }
    }

    @Override
    public void populateElementWithXPath(final String xpath, final String text) {
        ++interactionCount;

        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                () -> getElementByXPath(xpath, getDefaultExplicitWaitTime()),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().populateElementWithXPath(
                    xpath,
                    text,
                    SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.MOVE_CURSOR_TO_ELEMENT, false)
                            ? 0 : getDefaultExplicitWaitTime());
        }
    }

    @Override
    public void populateElementWithXPath(final String xpath, final String text, final int waitTime) {
        ++interactionCount;

        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                () -> getElementByXPath(xpath, waitTime),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().populateElementWithXPath(
                    xpath,
                    text,
                    SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.MOVE_CURSOR_TO_ELEMENT, false)
                            ? 0 : waitTime);
        }
    }

    @Override
    public void clickElementWithCSSSelector(final String cssSelector) {
        ++interactionCount;

        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                () -> getElementByCSSSelector(cssSelector, getDefaultExplicitWaitTime()),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().clickElementWithCSSSelector(
                    cssSelector,
                    SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.MOVE_CURSOR_TO_ELEMENT, false)
                            ? 0 : getDefaultExplicitWaitTime());
        }
    }

    @Override
    public void clickElementWithCSSSelector(final String cssSelector, final int waitTime) {
        ++interactionCount;

        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                () -> getElementByCSSSelector(cssSelector, waitTime),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().clickElementWithCSSSelector(
                    cssSelector,
                    SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.MOVE_CURSOR_TO_ELEMENT, false)
                            ? 0 : waitTime);
        }
    }

    @Override
    public void selectOptionByTextFromSelectWithCSSSelector(final String optionText, final String cssSelector) {
        ++interactionCount;

        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                () -> getElementByCSSSelector(cssSelector, getDefaultExplicitWaitTime()),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().selectOptionByTextFromSelectWithCSSSelector(
                    optionText,
                    cssSelector,
                    SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.MOVE_CURSOR_TO_ELEMENT, false)
                            ? 0 : getDefaultExplicitWaitTime());
        }
    }

    @Override
    public void selectOptionByTextFromSelectWithCSSSelector(final String optionText, final String cssSelector, final int waitTime) {
        ++interactionCount;

        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                () -> getElementByCSSSelector(cssSelector, waitTime),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().selectOptionByTextFromSelectWithCSSSelector(
                    optionText,
                    cssSelector,
                    SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.MOVE_CURSOR_TO_ELEMENT, false)
                            ? 0 : waitTime);
        }
    }

    @Override
    public void populateElementWithCSSSelector(final String cssSelector, final String text) {
        ++interactionCount;

        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                () -> getElementByCSSSelector(cssSelector, getDefaultExplicitWaitTime()),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().populateElementWithCSSSelector(
                    cssSelector,
                    text,
                    SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.MOVE_CURSOR_TO_ELEMENT, false)
                            ? 0 : getDefaultExplicitWaitTime());
        }
    }

    @Override
    public void populateElementWithCSSSelector(final String cssSelector, final String text, final int waitTime) {
        ++interactionCount;

        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                () -> getElementByCSSSelector(cssSelector, waitTime),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().populateElementWithCSSSelector(
                    cssSelector,
                    text,
                    SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.MOVE_CURSOR_TO_ELEMENT, false)
                            ? 0 : waitTime);
        }
    }

    @Override
    public void clickElementWithName(final String name) {
        ++interactionCount;

        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                () -> getElementByName(name, getDefaultExplicitWaitTime()),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().clickElementWithName(
                    name,
                    SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.MOVE_CURSOR_TO_ELEMENT, false)
                            ? 0 : getDefaultExplicitWaitTime());
        }
    }

    @Override
    public void clickElementWithName(final String name, final int waitTime) {
        ++interactionCount;

        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                () -> getElementByName(name, waitTime),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().clickElementWithName(
                    name,
                    SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.MOVE_CURSOR_TO_ELEMENT, false)
                            ? 0 : waitTime);
        }
    }

    @Override
    public void selectOptionByTextFromSelectWithName(final String optionText, final String name) {
        ++interactionCount;

        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                () -> getElementByName(name, getDefaultExplicitWaitTime()),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().selectOptionByTextFromSelectWithName(
                    optionText,
                    name,
                    SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.MOVE_CURSOR_TO_ELEMENT, false)
                            ? 0 : getDefaultExplicitWaitTime());
        }
    }

    @Override
    public void selectOptionByTextFromSelectWithName(final String optionText, final String name, final int waitTime) {
        ++interactionCount;

        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                () -> getElementByName(name, waitTime),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().selectOptionByTextFromSelectWithName(
                    optionText,
                    name,
                    SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.MOVE_CURSOR_TO_ELEMENT, false)
                            ? 0 : waitTime);
        }
    }

    @Override
    public void populateElementWithName(final String name, final String text) {
        ++interactionCount;

        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                () -> getElementByName(name, getDefaultExplicitWaitTime()),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().populateElementWithName(
                    name,
                    text,
                    SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.MOVE_CURSOR_TO_ELEMENT, false)
                            ? 0 : getDefaultExplicitWaitTime());
        }
    }

    @Override
    public void populateElementWithName(final String name, final String text, final int waitTime) {
        ++interactionCount;

        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                () -> getElementByName(name, waitTime),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().populateElementWithName(
                    name,
                    text,
                    SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.MOVE_CURSOR_TO_ELEMENT, false)
                            ? 0 : waitTime);
        }
    }

    @Override
    public void clickElementIfExists(final String force, final String locator, final String ifExistsOption) {
        ++interactionCount;

        try {
            MOUSE_MOVEMENT_UTILS.mouseGlide(
                    getWebDriver(),
                    (JavascriptExecutor) getWebDriver(),
                    () -> SIMPLE_BY.getElement(
                            getWebDriver(),
                            locator,
                            getDefaultExplicitWaitTime(),
                            by -> force == null
                                    ? ExpectedConditions.elementToBeClickable(by)
                                    : ExpectedConditions.presenceOfElementLocated(by)),
                    Constants.MOUSE_MOVE_TIME,
                    Constants.MOUSE_MOVE_STEPS,
                    force != null);

            if (getAutomatedBrowser() != null) {
                getAutomatedBrowser().clickElementIfExists(
                        force,
                        locator,
                        SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.MOVE_CURSOR_TO_ELEMENT, false)
                                ? 0 : getDefaultExplicitWaitTime(),
                        ifExistsOption);
            }
        } catch (final WebElementException ex) {
            if (StringUtils.isEmpty(ifExistsOption)) {
                throw ex;
            }
        }
    }

    @Override
    public void clickElementIfExists(final String force, final String locator, final int waitTime, final String ifExistsOption) {
        ++interactionCount;

        try {
            MOUSE_MOVEMENT_UTILS.mouseGlide(
                    getWebDriver(),
                    (JavascriptExecutor) getWebDriver(),
                    () -> SIMPLE_BY.getElement(
                            getWebDriver(),
                            locator,
                            waitTime,
                            by -> force == null
                                    ? ExpectedConditions.elementToBeClickable(by)
                                    : ExpectedConditions.presenceOfElementLocated(by)),
                    Constants.MOUSE_MOVE_TIME,
                    Constants.MOUSE_MOVE_STEPS,
                    force != null);

            if (getAutomatedBrowser() != null) {
                getAutomatedBrowser().clickElementIfExists(
                        force,
                        locator,
                        SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.MOVE_CURSOR_TO_ELEMENT, false)
                                ? 0 : waitTime,
                        ifExistsOption);
            }
        } catch (final WebElementException ex) {
            if (StringUtils.isEmpty(ifExistsOption)) {
                throw ex;
            }
        }
    }

    @Override
    public void selectOptionByTextFromSelectIfExists(final String optionText, final String locator, final String ifExistsOption) {
        ++interactionCount;

        try {
            MOUSE_MOVEMENT_UTILS.mouseGlide(
                    getWebDriver(),
                    (JavascriptExecutor) getWebDriver(),
                    () -> SIMPLE_BY.getElement(
                            getWebDriver(),
                            locator,
                            getDefaultExplicitWaitTime(),
                            by -> ExpectedConditions.elementToBeClickable(by)),
                    Constants.MOUSE_MOVE_TIME,
                    Constants.MOUSE_MOVE_STEPS);

            if (getAutomatedBrowser() != null) {
                getAutomatedBrowser().selectOptionByTextFromSelectIfExists(
                        optionText,
                        locator,
                        SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.MOVE_CURSOR_TO_ELEMENT, false)
                                ? 0 : getDefaultExplicitWaitTime(),
                        ifExistsOption);
            }
        } catch (final WebElementException ex) {
            if (StringUtils.isEmpty(ifExistsOption)) {
                throw ex;
            }
        }
    }

    @Override
    public void selectOptionByTextFromSelectIfExists(final String optionText, final String locator, final int waitTime, final String ifExistsOption) {
        ++interactionCount;

        try {
            MOUSE_MOVEMENT_UTILS.mouseGlide(
                    getWebDriver(),
                    (JavascriptExecutor) getWebDriver(),
                    () -> SIMPLE_BY.getElement(
                            getWebDriver(),
                            locator,
                            waitTime,
                            by -> ExpectedConditions.elementToBeClickable(by)),
                    Constants.MOUSE_MOVE_TIME,
                    Constants.MOUSE_MOVE_STEPS);

            if (getAutomatedBrowser() != null) {
                getAutomatedBrowser().selectOptionByTextFromSelectIfExists(
                        optionText,
                        locator,
                        SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.MOVE_CURSOR_TO_ELEMENT, false)
                                ? 0 : waitTime,
                        ifExistsOption);
            }
        } catch (final WebElementException ex) {
            if (StringUtils.isEmpty(ifExistsOption)) {
                throw ex;
            }
        }
    }

    @Override
    public void selectOptionByValueFromSelectIfExists(final String optionValue, final String locator, final int waitTime, final String ifExistsOption) {
        ++interactionCount;

        try {
            MOUSE_MOVEMENT_UTILS.mouseGlide(
                    getWebDriver(),
                    (JavascriptExecutor) getWebDriver(),
                    () -> SIMPLE_BY.getElement(
                            getWebDriver(),
                            locator,
                            waitTime,
                            by -> ExpectedConditions.elementToBeClickable(by)),
                    Constants.MOUSE_MOVE_TIME,
                    Constants.MOUSE_MOVE_STEPS);

            if (getAutomatedBrowser() != null) {
                getAutomatedBrowser().selectOptionByValueFromSelectIfExists(
                        optionValue,
                        locator,
                        SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.MOVE_CURSOR_TO_ELEMENT, false)
                                ? 0 : waitTime,
                        ifExistsOption);
            }
        } catch (final WebElementException ex) {
            if (StringUtils.isEmpty(ifExistsOption)) {
                throw ex;
            }
        }
    }

    @Override
    public void populateElement(final String locator, final String text, final String ifExistsOption) {
        ++interactionCount;

        try {
            MOUSE_MOVEMENT_UTILS.mouseGlide(
                    getWebDriver(),
                    (JavascriptExecutor) getWebDriver(),
                    () -> SIMPLE_BY.getElement(
                            getWebDriver(),
                            locator,
                            getDefaultExplicitWaitTime(),
                            by -> ExpectedConditions.presenceOfElementLocated(by)),
                    Constants.MOUSE_MOVE_TIME,
                    Constants.MOUSE_MOVE_STEPS);

            if (getAutomatedBrowser() != null) {
                getAutomatedBrowser().populateElement(
                        locator,
                        text,
                        SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.MOVE_CURSOR_TO_ELEMENT, false)
                                ? 0 : getDefaultExplicitWaitTime(),
                        ifExistsOption);
            }
        } catch (final WebElementException ex) {
            if (StringUtils.isEmpty(ifExistsOption)) {
                throw ex;
            }
        }
    }

    @Override
    public void populateElement(final String locator, final String text, final int waitTime, final String ifExistsOption) {
        ++interactionCount;

        try {
            MOUSE_MOVEMENT_UTILS.mouseGlide(
                    getWebDriver(),
                    (JavascriptExecutor) getWebDriver(),
                    () -> SIMPLE_BY.getElement(
                            getWebDriver(),
                            locator,
                            waitTime,
                            by -> ExpectedConditions.presenceOfElementLocated(by)),
                    Constants.MOUSE_MOVE_TIME,
                    Constants.MOUSE_MOVE_STEPS);

            if (getAutomatedBrowser() != null) {
                getAutomatedBrowser().populateElement(
                        locator,
                        text,
                        SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.MOVE_CURSOR_TO_ELEMENT, false)
                                ? 0 : waitTime,
                        ifExistsOption);
            }
        } catch (final WebElementException ex) {
            if (StringUtils.isEmpty(ifExistsOption)) {
                throw ex;
            }
        }
    }

    @Override
    public void mouseOverIfExists(final String locator, final String ifExistsOption) {
        ++interactionCount;

        try {
            MOUSE_MOVEMENT_UTILS.mouseGlide(
                    getWebDriver(),
                    (JavascriptExecutor) getWebDriver(),
                    () -> SIMPLE_BY.getElement(
                            getWebDriver(),
                            locator,
                            getDefaultExplicitWaitTime(),
                            by -> ExpectedConditions.presenceOfElementLocated(by)),
                    Constants.MOUSE_MOVE_TIME,
                    Constants.MOUSE_MOVE_STEPS);

            final Actions action = new Actions(getWebDriver());
            final WebElement element = SIMPLE_BY.getElement(
                    getWebDriver(),
                    locator,
                    SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.MOVE_CURSOR_TO_ELEMENT, false)
                            ? 0 : getDefaultExplicitWaitTime(),
                    by -> ExpectedConditions.presenceOfElementLocated(by));
            action.moveToElement(element).perform();
        } catch (final WebElementException ex) {
            if (StringUtils.isEmpty(ifExistsOption)) {
                throw ex;
            }
        }
    }

    @Override
    public void mouseOverIfExists(final String locator, final int waitTime, final String ifExistsOption) {
        ++interactionCount;

        try {
            MOUSE_MOVEMENT_UTILS.mouseGlide(
                    getWebDriver(),
                    (JavascriptExecutor) getWebDriver(),
                    () -> SIMPLE_BY.getElement(
                            getWebDriver(),
                            locator,
                            waitTime,
                            by -> ExpectedConditions.presenceOfElementLocated(by)),
                    Constants.MOUSE_MOVE_TIME,
                    Constants.MOUSE_MOVE_STEPS);

            final Actions action = new Actions(getWebDriver());
            final WebElement element = SIMPLE_BY.getElement(
                    getWebDriver(),
                    locator,
                    SYSTEM_PROPERTY_UTILS.getPropertyAsBoolean(Constants.MOVE_CURSOR_TO_ELEMENT, false)
                            ? 0 : waitTime,
                    by -> ExpectedConditions.presenceOfElementLocated(by));
            action.moveToElement(element).perform();
        } catch (final WebElementException ex) {
            if (StringUtils.isEmpty(ifExistsOption)) {
                throw ex;
            }
        }
    }

    public int getInteractionCount() {
        return interactionCount;
    }
}
