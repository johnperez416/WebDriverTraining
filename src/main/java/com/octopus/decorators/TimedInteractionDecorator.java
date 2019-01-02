package com.octopus.decorators;

import com.octopus.AutomatedBrowser;
import com.octopus.decoratorbase.AutomatedBrowserBase;
import com.octopus.utils.TimedResult;
import com.octopus.utils.impl.TimedExecutionImpl;

import java.util.concurrent.Callable;

public class TimedInteractionDecorator extends AutomatedBrowserBase {
    
    private int numberWaitCount = 0;
    private long totalWaitTime = 0;

    public TimedInteractionDecorator(final AutomatedBrowser automatedBrowser) {
        super(automatedBrowser);
    }

    private <T> T addNewTimedCall(final Callable<T> timedExecution) {
        final TimedResult<T> result =
                new TimedExecutionImpl<T>().timedExecution(timedExecution);
        ++numberWaitCount;
        totalWaitTime += result.getMillis();

        // Make the value available outside of Cucumber
        AutomatedBrowserBase.setStaticAverageWaitTime(getAverageWaitTime());

        return result.getResult();
    }
    
    public double getAverageWaitTime() {
        if (numberWaitCount == 0) {
            return 0;
        }
        
        return totalWaitTime / (double)numberWaitCount;
    }

    @Override
    public void clickElementWithId(final String id) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().clickElementWithId(id);
                return null;
            });
        }
    }

    @Override
    public void clickElementWithId(final String id, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().clickElementWithId(id, waitTime);
                return null;
            });
        }
    }

    @Override
    public void selectOptionByTextFromSelectWithId(final String optionText, final String id) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().selectOptionByTextFromSelectWithId(optionText, id);
                return null;
            });
        }
    }

    @Override
    public void selectOptionByTextFromSelectWithId(final String optionText, final String id, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().selectOptionByTextFromSelectWithId(
                        optionText,
                        id,
                        waitTime);
                return null;
            });
        }
    }

    @Override
    public void populateElementWithId(final String id, final String text) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().populateElementWithId(
                        id,
                        text);
                return null;
            });
        }
    }

    @Override
    public void populateElementWithId(final String id, final String text, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().populateElementWithId(
                        id,
                        text,
                        waitTime);
                return null;
            });
        }
    }

    @Override
    public String getTextFromElementWithId(final String id) {
        if (getAutomatedBrowser() != null) {
            return addNewTimedCall(() ->
                    getAutomatedBrowser().getTextFromElementWithId(id)
            );
        }

        return null;
    }

    @Override
    public String getTextFromElementWithId(final String id, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            return addNewTimedCall(() -> getAutomatedBrowser().getTextFromElementWithId(id, waitTime));
        }

        return null;
    }
    
    @Override
    public void clickElementWithXPath(final String xpath) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().clickElementWithXPath(xpath);
                return null;
            });
        }
    }
    
    @Override
    public void clickElementWithXPath(final String xpath, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().clickElementWithXPath(xpath, waitTime);
                return null;
            });
        }
    }
    
    @Override
    public void selectOptionByTextFromSelectWithXPath(final String optionText, final String xpath) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().selectOptionByTextFromSelectWithXPath(
                        optionText,
                        xpath);
                return null;
            });
        }
    }
    
    @Override
    public void selectOptionByTextFromSelectWithXPath(final String optionText, final String xpath, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().selectOptionByTextFromSelectWithXPath(
                        optionText,
                        xpath,
                        waitTime);
                return null;
            });
        }
    }
    
    @Override
    public void populateElementWithXPath(final String xpath, final String text) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().populateElementWithXPath(
                        xpath,
                        text);
                return null;
            });
        }
    }
    
    @Override
    public void populateElementWithXPath(final String xpath, final String text, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().populateElementWithXPath(
                        xpath,
                        text,
                        waitTime);
                return null;
            });
        }
    }

    @Override
    public String getTextFromElementWithXPath(final String xpath) {
        if (getAutomatedBrowser() != null) {
            return addNewTimedCall(() -> getAutomatedBrowser().getTextFromElementWithXPath(xpath));
        }

        return null;
    }

    @Override
    public String getTextFromElementWithXPath(final String xpath, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            return addNewTimedCall(() -> getAutomatedBrowser().getTextFromElementWithXPath(xpath, waitTime));
        }

        return null;
    }
    
    @Override
    public void clickElementWithCSSSelector(final String cssSelector) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().clickElementWithCSSSelector(cssSelector);
                return null;
            });
        }
    }
    
    @Override
    public void clickElementWithCSSSelector(final String cssSelector, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().clickElementWithCSSSelector(cssSelector, waitTime);
                return null;
            });
        }
    }
    
    @Override
    public void selectOptionByTextFromSelectWithCSSSelector(final String optionText, final String cssSelector) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().selectOptionByTextFromSelectWithCSSSelector(
                        optionText,
                        cssSelector);
                return null;
            });
        }
    }
    
    @Override
    public void selectOptionByTextFromSelectWithCSSSelector(final String optionText, final String cssSelector, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().selectOptionByTextFromSelectWithCSSSelector(
                        optionText,
                        cssSelector,
                        waitTime);
                return null;
            });
        }
    }
    
    @Override
    public void populateElementWithCSSSelector(final String cssSelector, final String text) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().populateElementWithCSSSelector(
                        cssSelector,
                        text);
                return null;
            });
        }
    }
    
    @Override
    public void populateElementWithCSSSelector(final String cssSelector, final String text, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().populateElementWithCSSSelector(
                        cssSelector,
                        text,
                        waitTime);
                return null;
            });
        }
    }

    @Override
    public String getTextFromElementWithCSSSelector(final String cssSelector) {
        if (getAutomatedBrowser() != null) {
            return addNewTimedCall(() -> getAutomatedBrowser().getTextFromElementWithCSSSelector(
                    cssSelector));
        }

        return null;
    }

    @Override
    public String getTextFromElementWithCSSSelector(final String cssSelector, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            return addNewTimedCall(() -> getAutomatedBrowser().getTextFromElementWithCSSSelector(
                    cssSelector,
                    waitTime));
        }

        return null;
    }
    
    @Override
    public void clickElementWithName(final String name) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().clickElementWithName(name);
                return null;
            });
        }
    }
    
    @Override
    public void clickElementWithName(final String name, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().clickElementWithName(name, waitTime);
                return null;
            });
        }
    }
    
    @Override
    public void selectOptionByTextFromSelectWithName(final String optionText, final String name) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().selectOptionByTextFromSelectWithName(
                        optionText,
                        name);
                return null;
            });
        }
    }
    
    @Override
    public void selectOptionByTextFromSelectWithName(final String optionText, final String name, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().selectOptionByTextFromSelectWithName(
                        optionText,
                        name,
                        waitTime);
                return null;
            });
        }
    }
    
    @Override
    public void populateElementWithName(final String name, final String text) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().populateElementWithName(
                        name,
                        text);
                return null;
            });
        }
    }
    
    @Override
    public void populateElementWithName(final String name, final String text, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().populateElementWithName(
                        name,
                        text,
                        waitTime);
                return null;
            });
        }
    }

    @Override
    public String getTextFromElementWithName(final String name) {
        if (getAutomatedBrowser() != null) {
            return addNewTimedCall(() -> getAutomatedBrowser().getTextFromElementWithName(name));
        }

        return null;
    }

    @Override
    public String getTextFromElementWithName(final String name, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            return addNewTimedCall(() -> getAutomatedBrowser().getTextFromElementWithName(
                    name,
                    waitTime));
        }

        return null;
    }
    
    @Override
    public void clickElement(final String locator) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().clickElement(locator);
                return null;
            });
        }
    }
    
    @Override
    public void clickElement(final String locator, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().clickElement(locator, waitTime);
                return null;
            });
        }
    }
    
    @Override
    public void selectOptionByTextFromSelect(final String optionText, final String locator) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().selectOptionByTextFromSelect(
                        optionText,
                        locator);
                return null;
            });
        }
    }
    
    @Override
    public void selectOptionByTextFromSelect(final String optionText, final String locator, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().selectOptionByTextFromSelect(
                        optionText,
                        locator,
                        waitTime);
                return null;
            });
        }
    }
    
    @Override
    public void populateElement(final String locator, final String text) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().populateElement(
                        locator,
                        text);
                return null;
            });
        }
    }
    
    @Override
    public void populateElement(final String locator, final String text, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().populateElement(
                        locator,
                        text,
                        waitTime);
                return null;
            });
        }
    }
    
    @Override
    public String getTextFromElement(final String locator) {
        if (getAutomatedBrowser() != null) {
            return addNewTimedCall(() -> {
                final String text = getAutomatedBrowser().getTextFromElement(locator);
                return text;
            });
        }

        return null;
    }
    
    @Override
    public String getTextFromElement(final String locator, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            return addNewTimedCall(() -> {
                final String text = getAutomatedBrowser().getTextFromElement(
                        locator,
                        waitTime);
                return text;
            });
        }

        return null;
    }
    
    @Override
    public String getRegexGroupFromElement(
            final String group,
            final String regex,
            final String locator) {
        if (getAutomatedBrowser() != null) {
            return addNewTimedCall(() -> {
                final String text = getAutomatedBrowser().getRegexGroupFromElement(
                        group,
                        regex,
                        locator);
                return text;
            });
        }

        return null;
    }
    
    @Override
    public String getRegexGroupFromElement(
            final String group,
            final String regex,
            final String locator,
            final int waitTime) {
        if (getAutomatedBrowser() != null) {
            return addNewTimedCall(() -> {
                final String text = getAutomatedBrowser().getRegexGroupFromElement(
                        group,
                        regex,
                        locator,
                        waitTime);
                return text;
            });
        }

        return null;
    }
    
    @Override
    public void verifyTextFromElement(final String locator, final String regex) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().verifyTextFromElement(
                        locator,
                        regex);
                return null;
            });
        }
    }
    
    @Override
    public void verifyTextFromElement(final String locator, final String regex, final int waitTime) {
        if (getAutomatedBrowser() != null) {
            addNewTimedCall(() -> {
                getAutomatedBrowser().verifyTextFromElement(
                        locator,
                        regex,
                        waitTime);
                return null;
            });
        }
    }
}
