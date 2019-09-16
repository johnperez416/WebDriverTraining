package com.octopus.decorators;

import com.octopus.AutomatedBrowser;
import com.octopus.Constants;
import com.octopus.decoratorbase.AutomatedBrowserBase;
import com.octopus.utils.MouseMovementUtils;
import com.octopus.utils.SimpleBy;
import com.octopus.utils.impl.MouseMovementUtilsImpl;
import com.octopus.utils.impl.SimpleByImpl;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MouseMovementDecorator extends AutomatedBrowserBase {
    private static final MouseMovementUtils MOUSE_MOVEMENT_UTILS = new MouseMovementUtilsImpl();
    private static final SimpleBy SIMPLE_BY = new SimpleByImpl();

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
        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                getElementById(id, getDefaultExplicitWaitTime()),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().clickElementWithId(id, 0);
        }
    }

    @Override
    public void clickElementWithId(final String id, final int waitTime) {
        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                getElementById(id, waitTime),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().clickElementWithId(id, 0);
        }
    }

    @Override
    public void selectOptionByTextFromSelectWithId(final String optionText, final String id) {
        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                getElementById(id, getDefaultExplicitWaitTime()),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().selectOptionByTextFromSelectWithId(optionText, id, 0);
        }
    }

    @Override
    public void selectOptionByTextFromSelectWithId(final String optionText, final String id, final int waitTime) {
        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                getElementById(id, waitTime),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().selectOptionByTextFromSelectWithId(optionText, id, 0);
        }
    }

    @Override
    public void populateElementWithId(final String id, final String text) {
        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                getElementById(id, getDefaultExplicitWaitTime()),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().populateElementWithId(id, text, 0);
        }
    }

    @Override
    public void populateElementWithId(final String id, final String text, final int waitTime) {
        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                getElementById(id, waitTime),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().populateElementWithId(id, text, 0);
        }
    }

    @Override
    public void clickElementWithXPath(final String xpath) {
        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                getElementByXPath(xpath, getDefaultExplicitWaitTime()),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().clickElementWithXPath(xpath, 0);
        }
    }

    @Override
    public void clickElementWithXPath(final String xpath, final int waitTime) {
        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                getElementByXPath(xpath, waitTime),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().clickElementWithXPath(xpath, 0);
        }
    }

    @Override
    public void selectOptionByTextFromSelectWithXPath(final String optionText, final String xpath) {
        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                getElementByXPath(xpath, getDefaultExplicitWaitTime()),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().selectOptionByTextFromSelectWithXPath(optionText, xpath, 0);
        }
    }

    @Override
    public void selectOptionByTextFromSelectWithXPath(final String optionText, final String xpath, final int waitTime) {
        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                getElementByXPath(xpath, waitTime),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().selectOptionByTextFromSelectWithXPath(optionText, xpath, 0);
        }
    }

    @Override
    public void populateElementWithXPath(final String xpath, final String text) {
        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                getElementByXPath(xpath, getDefaultExplicitWaitTime()),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().populateElementWithXPath(xpath, text, 0);
        }
    }

    @Override
    public void populateElementWithXPath(final String xpath, final String text, final int waitTime) {
        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                getElementByXPath(xpath, waitTime),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().populateElementWithXPath(xpath, text, 0);
        }
    }

    @Override
    public void clickElementWithCSSSelector(final String cssSelector) {
        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                getElementByCSSSelector(cssSelector, getDefaultExplicitWaitTime()),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().clickElementWithCSSSelector(cssSelector, 0);
        }
    }

    @Override
    public void clickElementWithCSSSelector(final String cssSelector, final int waitTime) {
        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                getElementByCSSSelector(cssSelector, waitTime),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().clickElementWithCSSSelector(cssSelector, 0);
        }
    }

    @Override
    public void selectOptionByTextFromSelectWithCSSSelector(final String optionText, final String cssSelector) {
        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                getElementByCSSSelector(cssSelector, getDefaultExplicitWaitTime()),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().selectOptionByTextFromSelectWithCSSSelector(optionText, cssSelector, 0);
        }
    }

    @Override
    public void selectOptionByTextFromSelectWithCSSSelector(final String optionText, final String cssSelector, final int waitTime) {
        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                getElementByCSSSelector(cssSelector, waitTime),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().selectOptionByTextFromSelectWithCSSSelector(optionText, cssSelector, 0);
        }
    }

    @Override
    public void populateElementWithCSSSelector(final String cssSelector, final String text) {
        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                getElementByCSSSelector(cssSelector, getDefaultExplicitWaitTime()),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().populateElementWithCSSSelector(cssSelector, text, 0);
        }
    }

    @Override
    public void populateElementWithCSSSelector(final String cssSelector, final String text, final int waitTime) {
        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                getElementByCSSSelector(cssSelector, waitTime),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().populateElementWithCSSSelector(cssSelector, text, 0);
        }
    }

    @Override
    public void clickElementWithName(final String name) {
        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                getElementByName(name, getDefaultExplicitWaitTime()),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().clickElementWithName(name, 0);
        }
    }

    @Override
    public void clickElementWithName(final String name, final int waitTime) {
        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                getElementByName(name, waitTime),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().clickElementWithName(name, 0);
        }
    }

    @Override
    public void selectOptionByTextFromSelectWithName(final String optionText, final String name) {
        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                getElementByName(name, getDefaultExplicitWaitTime()),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().selectOptionByTextFromSelectWithName(optionText, name, 0);
        }
    }

    @Override
    public void selectOptionByTextFromSelectWithName(final String optionText, final String name, final int waitTime) {
        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                getElementByName(name, waitTime),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().selectOptionByTextFromSelectWithName(optionText, name, 0);
        }
    }

    @Override
    public void populateElementWithName(final String name, final String text) {
        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                getElementByName(name, getDefaultExplicitWaitTime()),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().populateElementWithName(name, text, 0);
        }
    }

    @Override
    public void populateElementWithName(final String name, final String text, final int waitTime) {
        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                getElementByName(name, waitTime),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().populateElementWithName(name, text, 0);
        }
    }

    @Override
    public void clickElement(final String locator) {
        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                SIMPLE_BY.getElement(
                        getWebDriver(),
                        locator,
                        getDefaultExplicitWaitTime(),
                        by -> ExpectedConditions.elementToBeClickable(by)),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().clickElement(locator, 0);
        }
    }

    @Override
    public void clickElement(final String locator, final int waitTime) {
        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                SIMPLE_BY.getElement(
                        getWebDriver(),
                        locator,
                        waitTime,
                        by -> ExpectedConditions.elementToBeClickable(by)),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().clickElement(locator, 0);
        }
    }

    @Override
    public void selectOptionByTextFromSelect(final String optionText, final String locator) {
        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                SIMPLE_BY.getElement(
                        getWebDriver(),
                        locator,
                        getDefaultExplicitWaitTime(),
                        by -> ExpectedConditions.elementToBeClickable(by)),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().selectOptionByTextFromSelect(optionText, locator, 0);
        }
    }

    @Override
    public void selectOptionByTextFromSelect(final String optionText, final String locator, final int waitTime) {
        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                SIMPLE_BY.getElement(
                        getWebDriver(),
                        locator,
                        waitTime,
                        by -> ExpectedConditions.elementToBeClickable(by)),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().selectOptionByTextFromSelect(optionText, locator, 0);
        }
    }

    @Override
    public void populateElement(final String locator, final String text) {
        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                SIMPLE_BY.getElement(
                        getWebDriver(),
                        locator,
                        getDefaultExplicitWaitTime(),
                        by -> ExpectedConditions.elementToBeClickable(by)),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().populateElement(locator, text, 0);
        }
    }

    @Override
    public void populateElement(final String locator, final String text, final int waitTime) {
        MOUSE_MOVEMENT_UTILS.mouseGlide(
                getWebDriver(),
                (JavascriptExecutor) getWebDriver(),
                SIMPLE_BY.getElement(
                        getWebDriver(),
                        locator,
                        waitTime,
                        by -> ExpectedConditions.elementToBeClickable(by)),
                Constants.MOUSE_MOVE_TIME,
                Constants.MOUSE_MOVE_STEPS);

        if (getAutomatedBrowser() != null) {
            getAutomatedBrowser().populateElement(locator, text, 0);
        }
    }
}
