package com.octopus.decorators;

import com.octopus.decoratorbase.AutomatedBrowserBase;
import com.octopus.exceptions.SaveException;
import com.octopus.utils.SimpleBy;
import com.octopus.utils.impl.SimpleByImpl;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;

public class WebDriverDecorator extends AutomatedBrowserBase {
    private static final SimpleBy SIMPLE_BY = new SimpleByImpl();
    private int defaultExplicitWaitTime;
    private WebDriver webDriver;

    @Override
    public void setDefaultExplicitWaitTime(final int waitTime) {
        defaultExplicitWaitTime = waitTime;
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
    public void goTo(final String url) {
        webDriver.get(url);
    }

    @Override
    public void takeScreenshot(final String file)
    {
        try {
            final File screenshot = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(screenshot, new File(file));
        } catch (final IOException ex) {
            throw new SaveException("Failed to copy the screenshot to " + file, ex);
        }
    }

    @Override
    public void setWindowSize(final String width, final String height) {
        webDriver.manage().window().setSize(new Dimension(Integer.parseInt(width), Integer.parseInt(height)));
    }

    @Override
    public void clickElementWithId(final String id) {
        if (defaultExplicitWaitTime <= 0) {
            webDriver.findElement(By.id(id)).click();
        } else {
            clickElementWithId(id, defaultExplicitWaitTime);
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
        if (defaultExplicitWaitTime <= 0) {
            new Select(webDriver.findElement(By.id(selectId))).selectByVisibleText(optionText);
        } else {
            selectOptionByTextFromSelectWithId(optionText, selectId, defaultExplicitWaitTime);
        }
    }

    @Override
    public void selectOptionByTextFromSelectWithId(final String optionText, final String id, final int waitTime) {
        if (waitTime <= 0) {
            selectOptionByTextFromSelectWithId(id, optionText);
        } else {
            final WebDriverWait wait = new WebDriverWait(webDriver, waitTime);
            new Select(wait.until(ExpectedConditions.elementToBeClickable((By.id(id))))).selectByVisibleText(optionText);
        }
    }

    @Override
    public void populateElementWithId(final String id, final String text) {
        if (defaultExplicitWaitTime <= 0) {
            webDriver.findElement(By.id(id)).sendKeys(text);
        } else {
            populateElementWithId(id, text, defaultExplicitWaitTime);
        }
    }

    @Override
    public void populateElementWithId(final String id, final String text, final int waitTime) {
        if (waitTime <= 0) {
            populateElementWithId(id, text);
        } else {
            final WebDriverWait wait = new WebDriverWait(webDriver, waitTime);
            wait.until(ExpectedConditions.elementToBeClickable((By.id(id)))).sendKeys(text);
        }
    }

    @Override
    public String getTextFromElementWithId(final String id) {
        if (defaultExplicitWaitTime <= 0) {
            return webDriver.findElement(By.id(id)).getText();
        } else {
            return getTextFromElementWithId(id, defaultExplicitWaitTime);
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
        if (defaultExplicitWaitTime <= 0) {
            webDriver.findElement(By.xpath(xpath)).click();
        } else {
            clickElementWithXPath(xpath, defaultExplicitWaitTime);
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
        if (defaultExplicitWaitTime <= 0) {
            new Select(webDriver.findElement(By.xpath(xpath))).selectByVisibleText(optionText);
        } else {
            selectOptionByTextFromSelectWithXPath(optionText, xpath, defaultExplicitWaitTime);
        }
    }

    @Override
    public void selectOptionByTextFromSelectWithXPath(final String optionText, final String xpath, final int waitTime) {
        if (waitTime <= 0) {
            selectOptionByTextFromSelectWithXPath(xpath, optionText);
        } else {
            final WebDriverWait wait = new WebDriverWait(webDriver, waitTime);
            new Select(wait.until(ExpectedConditions.elementToBeClickable((By.xpath(xpath))))).selectByVisibleText(optionText);
        }
    }

    @Override
    public void populateElementWithXPath(final String xpath, final String text) {
        if (defaultExplicitWaitTime <= 0) {
            webDriver.findElement(By.xpath(xpath)).sendKeys(text);
        } else {
            populateElementWithXPath(xpath, text, defaultExplicitWaitTime);
        }
    }

    @Override
    public void populateElementWithXPath(final String xpath, final String text, final int waitTime) {
        if (waitTime <= 0) {
            populateElementWithXPath(xpath, text);
        } else {
            final WebDriverWait wait = new WebDriverWait(webDriver, waitTime);
            wait.until(ExpectedConditions.elementToBeClickable((By.xpath(xpath)))).sendKeys(text);
        }
    }

    @Override
    public String getTextFromElementWithXPath(final String xpath) {
        if (defaultExplicitWaitTime <= 0) {
            return webDriver.findElement(By.xpath(xpath)).getText();
        } else {
            return getTextFromElementWithXPath(xpath, defaultExplicitWaitTime);
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
        if (defaultExplicitWaitTime <= 0) {
            webDriver.findElement(By.cssSelector(cssSelector)).click();
        } else {
            clickElementWithCSSSelector(cssSelector, defaultExplicitWaitTime);
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
        if (defaultExplicitWaitTime <= 0) {
            new Select(webDriver.findElement(By.cssSelector(cssSelector))).selectByVisibleText(optionText);
        } else {
            selectOptionByTextFromSelectWithCSSSelector(optionText, cssSelector);
        }
    }


    @Override
    public void selectOptionByTextFromSelectWithCSSSelector(final String optionText, final String css, final int waitTime) {
        if (waitTime <= 0) {
            selectOptionByTextFromSelectWithCSSSelector(css, optionText);
        } else {
            final WebDriverWait wait = new WebDriverWait(webDriver, waitTime);
            new Select(wait.until(ExpectedConditions.elementToBeClickable((By.cssSelector(css))))).selectByVisibleText(optionText);
        }
    }

    @Override
    public void populateElementWithCSSSelector(final String cssSelector, final String text) {
        if (defaultExplicitWaitTime <= 0) {
            webDriver.findElement(By.cssSelector(cssSelector)).sendKeys(text);
        } else {
            populateElementWithCSSSelector(cssSelector, text);
        }
    }

    @Override
    public void populateElementWithCSSSelector(String css, final String text, final int waitTime) {
        if (waitTime <= 0) {
            populateElementWithCSSSelector(css, text);
        } else {
            final WebDriverWait wait = new WebDriverWait(webDriver, waitTime);
            wait.until(ExpectedConditions.elementToBeClickable((By.cssSelector(css)))).sendKeys(text);
        }
    }

    @Override
    public String getTextFromElementWithCSSSelector(final String cssSelector) {
        if (defaultExplicitWaitTime <= 0) {
            return webDriver.findElement(By.cssSelector(cssSelector)).getText();
        } else {
            return getTextFromElementWithCSSSelector(cssSelector, defaultExplicitWaitTime);
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
        if (defaultExplicitWaitTime <= 0) {
            webDriver.findElement(By.name(name)).click();
        } else {
            clickElementWithName(name, defaultExplicitWaitTime);
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
        if (defaultExplicitWaitTime <= 0) {
            new Select(webDriver.findElement(By.name(name))).selectByVisibleText(optionText);
        } else {
            selectOptionByTextFromSelectWithName(optionText, name, defaultExplicitWaitTime);
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
        if (defaultExplicitWaitTime <= 0) {
            webDriver.findElement(By.name(name)).sendKeys(text);
        } else {
            populateElementWithName(name, text, defaultExplicitWaitTime);
        }
    }

    @Override
    public void populateElementWithName(final String name, final String text, final int waitTime) {
        if (waitTime <= 0) {
            populateElementWithName(name, text);
        } else {
            final WebDriverWait wait = new WebDriverWait(webDriver, waitTime);
            wait.until(ExpectedConditions.elementToBeClickable((By.name(name)))).sendKeys(text);
        }
    }

    @Override
    public String getTextFromElementWithName(final String name) {
        if (defaultExplicitWaitTime <= 0) {
            return webDriver.findElement(By.name(name)).getText();
        } else {
            return getTextFromElementWithName(name, defaultExplicitWaitTime);
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
    public void clickElement(final String locator) {
        clickElement(locator, defaultExplicitWaitTime);
    }

    @Override
    public void clickElement(final String locator, final int waitTime) {
        SIMPLE_BY.getElement(
                getWebDriver(),
                locator,
                waitTime,
                by -> ExpectedConditions.elementToBeClickable(by)).click();
    }

    @Override
    public void selectOptionByTextFromSelect(final String optionText, final String locator) {
        selectOptionByTextFromSelect(optionText, locator, defaultExplicitWaitTime);
    }

    @Override
    public void selectOptionByTextFromSelect(final String optionText, final String locator, final int waitTime) {
        new Select(SIMPLE_BY.getElement(
                getWebDriver(),
                locator,
                waitTime,
                by -> ExpectedConditions.elementToBeClickable(by))).selectByVisibleText(optionText);
    }

    @Override
    public void populateElement(final String locator, final String text) {
        populateElement(locator, text, defaultExplicitWaitTime);
    }

    @Override
    public void populateElement(final String locator, final String text, final int waitTime) {
        SIMPLE_BY.getElement(
                getWebDriver(),
                locator,
                waitTime,
                by -> ExpectedConditions.elementToBeClickable(by)).sendKeys(text);
    }

    @Override
    public String getTextFromElement(final String locator) {
        return getTextFromElement(locator, defaultExplicitWaitTime);
    }

    @Override
    public String getTextFromElement(final String locator, final int waitTime) {
        return SIMPLE_BY.getElement(
                getWebDriver(),
                locator,
                waitTime,
                by -> ExpectedConditions.presenceOfElementLocated(by)).getText();
    }

    @Override
    public void maximizeWindow() {
        webDriver.manage().window().maximize();
    }
}