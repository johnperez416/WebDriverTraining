package com.octopus;

import com.kevinmost.junit_retry_rule.Retry;
import com.octopus.decorators.BrowserStackDecorator;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.net.URISyntaxException;

import static junit.framework.TestCase.assertEquals;

public class FormTest {

    private static final AutomatedBrowserFactory AUTOMATED_BROWSER_FACTORY = new AutomatedBrowserFactory();

    @Test
    public void formTestByID() throws URISyntaxException {
        final AutomatedBrowser automatedBrowser =
                 AUTOMATED_BROWSER_FACTORY.getAutomatedBrowser("FirefoxNoImplicitWait");

        try {
            automatedBrowser.init();
            automatedBrowser.setDefaultExplicitWaitTime(2);
            automatedBrowser.goTo(FormTest.class.getResource("/form.html").toURI().toString());

            automatedBrowser.populateElementWithId("text_element", "test text");
            assertEquals("Text Input Changed", automatedBrowser.getTextFromElementWithId("message"));

            automatedBrowser.populateElementWithId("textarea_element", "test text");
            assertEquals("Text Area Changed", automatedBrowser.getTextFromElementWithId("message"));

            automatedBrowser.selectOptionByTextFromSelectWithId("Option 2.1", "select_element");
            assertEquals("Select Changed", automatedBrowser.getTextFromElementWithId("message"));

            automatedBrowser.clickElementWithId("radio3_element");
            assertEquals("Radio Button Changed", automatedBrowser.getTextFromElementWithId("message"));

            automatedBrowser.clickElementWithId("checkbox2_element");
            assertEquals("Checkbox Changed", automatedBrowser.getTextFromElementWithId("message"));

            automatedBrowser.clickElementWithId("image_element");
            assertEquals("Image Clicked", automatedBrowser.getTextFromElementWithId("message"));

            automatedBrowser.clickElementWithId("div_element");
            assertEquals("Div Clicked", automatedBrowser.getTextFromElementWithId("message"));
        } finally {
            automatedBrowser.destroy();
        }
    }

    @Test
    public void formTestByIDFirefox() throws URISyntaxException {

        final AutomatedBrowser automatedBrowser =
                AUTOMATED_BROWSER_FACTORY.getAutomatedBrowser("Firefox");

        try {
            automatedBrowser.init();
            automatedBrowser.setDefaultExplicitWaitTime(2);

            automatedBrowser.goTo(FormTest.class.getResource("/form.html").toURI().toString());

            automatedBrowser.populateElementWithId("text_element", "test text");
            assertEquals("Text Input Changed", automatedBrowser.getTextFromElementWithId("message"));

            automatedBrowser.populateElementWithId("textarea_element", "test text");
            assertEquals("Text Area Changed", automatedBrowser.getTextFromElementWithId("message"));

            automatedBrowser.selectOptionByTextFromSelectWithId("Option 2.1", "select_element");
            assertEquals("Select Changed", automatedBrowser.getTextFromElementWithId("message"));

            automatedBrowser.clickElementWithId("radio3_element");
            assertEquals("Radio Button Changed", automatedBrowser.getTextFromElementWithId("message"));

            automatedBrowser.clickElementWithId("checkbox2_element");
            assertEquals("Checkbox Changed", automatedBrowser.getTextFromElementWithId("message"));

            automatedBrowser.clickElementWithId("image_element");
            assertEquals("Image Clicked", automatedBrowser.getTextFromElementWithId("message"));

            automatedBrowser.clickElementWithId("div_element");
            assertEquals("Div Clicked", automatedBrowser.getTextFromElementWithId("message"));
        } finally {
            //automatedBrowser.destroy();
        }
    }

    @Test
    public void formTestByIDHeadless() throws URISyntaxException {
        final AutomatedBrowser automatedBrowser =
                AUTOMATED_BROWSER_FACTORY.getAutomatedBrowser("FirefoxHeadless");

        try {
            automatedBrowser.init();
            automatedBrowser.setDefaultExplicitWaitTime(2);
            automatedBrowser.goTo(FormTest.class.getResource("/form.html").toURI().toString());

            automatedBrowser.populateElementWithId("text_element", "test text");
            assertEquals("Text Input Changed", automatedBrowser.getTextFromElementWithId("message"));

            automatedBrowser.populateElementWithId("textarea_element", "test text");
            assertEquals("Text Area Changed", automatedBrowser.getTextFromElementWithId("message"));

            automatedBrowser.selectOptionByTextFromSelectWithId("Option 2.1", "select_element");
            assertEquals("Select Changed", automatedBrowser.getTextFromElementWithId("message"));

            automatedBrowser.clickElementWithId("radio3_element");
            assertEquals("Radio Button Changed", automatedBrowser.getTextFromElementWithId("message"));

            automatedBrowser.clickElementWithId("checkbox2_element");
            assertEquals("Checkbox Changed", automatedBrowser.getTextFromElementWithId("message"));

            automatedBrowser.clickElementWithId("image_element");
            assertEquals("Image Clicked", automatedBrowser.getTextFromElementWithId("message"));

            automatedBrowser.clickElementWithId("div_element");
            assertEquals("Div Clicked", automatedBrowser.getTextFromElementWithId("message"));
        } finally {
            automatedBrowser.destroy();
        }
    }

    @Test
    public void formTestByIDHeadlessFirefox() throws URISyntaxException {
        final AutomatedBrowser automatedBrowser =
                AUTOMATED_BROWSER_FACTORY.getAutomatedBrowser("FirefoxHeadless");

        try {
            automatedBrowser.init();
            automatedBrowser.goTo(FormTest.class.getResource("/form.html").toURI().toString());

            automatedBrowser.populateElementWithId("text_element", "test text");
            assertEquals("Text Input Changed", automatedBrowser.getTextFromElementWithId("message"));

            automatedBrowser.populateElementWithId("textarea_element", "test text");
            assertEquals("Text Area Changed", automatedBrowser.getTextFromElementWithId("message"));

            automatedBrowser.selectOptionByTextFromSelectWithId("Option 2.1", "select_element");
            assertEquals("Select Changed", automatedBrowser.getTextFromElementWithId("message"));

            automatedBrowser.clickElementWithId("radio3_element");
            assertEquals("Radio Button Changed", automatedBrowser.getTextFromElementWithId("message"));

            automatedBrowser.clickElementWithId("checkbox2_element");
            assertEquals("Checkbox Changed", automatedBrowser.getTextFromElementWithId("message"));

            automatedBrowser.clickElementWithId("image_element");
            assertEquals("Image Clicked", automatedBrowser.getTextFromElementWithId("message"));

            automatedBrowser.clickElementWithId("div_element");
            assertEquals("Div Clicked", automatedBrowser.getTextFromElementWithId("message"));
        } finally {
            automatedBrowser.destroy();
        }
    }

    @Test
    public void formTestByXPath() throws URISyntaxException {

        final AutomatedBrowser automatedBrowser =  AUTOMATED_BROWSER_FACTORY.getAutomatedBrowser("FirefoxNoImplicitWait");

        try {
            automatedBrowser.init();
            automatedBrowser.setDefaultExplicitWaitTime(2);
            automatedBrowser.goTo(FormTest.class.getResource("/form.html").toURI().toString());

            automatedBrowser.populateElementWithXPath("//*[@id=\"text_element\"]", "test text");
            assertEquals("Text Input Changed", automatedBrowser.getTextFromElementWithId("message"));

            automatedBrowser.populateElementWithXPath("//*[@id=\"textarea_element\"]", "test text");
            assertEquals("Text Area Changed", automatedBrowser.getTextFromElementWithId("message"));

            automatedBrowser.selectOptionByTextFromSelectWithXPath("Option 2.1", "//*[@id=\"select_element\"]");
            assertEquals("Select Changed", automatedBrowser.getTextFromElementWithId("message"));

            automatedBrowser.clickElementWithXPath("//*[@id=\"radio3_element\"]");
            assertEquals("Radio Button Changed", automatedBrowser.getTextFromElementWithId("message"));

            automatedBrowser.clickElementWithXPath("//*[@id=\"checkbox2_element\"]");
            assertEquals("Checkbox Changed", automatedBrowser.getTextFromElementWithId("message"));

            automatedBrowser.clickElementWithXPath("//*[@id=\"image_element\"]");
            assertEquals("Image Clicked", automatedBrowser.getTextFromElementWithId("message"));

            automatedBrowser.clickElementWithXPath("//*[@id=\"div_element\"]");
            assertEquals("Div Clicked", automatedBrowser.getTextFromElementWithId("message"));
        } finally {
            automatedBrowser.destroy();
        }
    }

    @Test
    public void formTestByCSSSelector() throws URISyntaxException {
        final AutomatedBrowser automatedBrowser =  AUTOMATED_BROWSER_FACTORY.getAutomatedBrowser("FirefoxNoImplicitWait");

        try {
            automatedBrowser.init();
            automatedBrowser.setDefaultExplicitWaitTime(2);

            automatedBrowser.goTo(FormTest.class.getResource("/form.html").toURI().toString());

            automatedBrowser.populateElementWithCSSSelector("#text_element", "test text");
            assertEquals("Text Input Changed", automatedBrowser.getTextFromElementWithId("message"));

            automatedBrowser.populateElementWithCSSSelector("#textarea_element", "test text");
            assertEquals("Text Area Changed", automatedBrowser.getTextFromElementWithId("message"));

            automatedBrowser.selectOptionByTextFromSelectWithCSSSelector("Option 2.1", "#select_element");
            assertEquals("Select Changed", automatedBrowser.getTextFromElementWithId("message"));

            automatedBrowser.clickElementWithCSSSelector("#radio3_element");
            assertEquals("Radio Button Changed", automatedBrowser.getTextFromElementWithId("message"));

            automatedBrowser.clickElementWithCSSSelector("#checkbox2_element");
            assertEquals("Checkbox Changed", automatedBrowser.getTextFromElementWithId("message"));

            automatedBrowser.clickElementWithCSSSelector("#image_element");
            assertEquals("Image Clicked", automatedBrowser.getTextFromElementWithId("message"));

            automatedBrowser.clickElementWithCSSSelector("#div_element");
            assertEquals("Div Clicked", automatedBrowser.getTextFromElementWithId("message"));
        } finally {
            automatedBrowser.destroy();
        }
    }

    @Test
    public void formTestWithSimpleBy() throws URISyntaxException {
        final AutomatedBrowser automatedBrowser = AUTOMATED_BROWSER_FACTORY.getAutomatedBrowser("FirefoxNoImplicitWait");

        final String formButtonLocator = "button_element";
        final String formTextBoxLocator = "text_element";
        final String formTextAreaLocator = "textarea_element";
        final String formDropDownListLocator = "[name=select_element]";

        final String formCheckboxLocator = "//*[@name=\"checkbox1_element\"]";

        final String messageLocator = "message";

        try {
            automatedBrowser.init();

            automatedBrowser.goTo(FormTest.class.getResource("/form.html").toURI().toString());

            // Mouse click also triggers a mouse move, so force the click
            automatedBrowser.clickElementIfExists("force", formButtonLocator, null);
            assertEquals("Button Clicked", automatedBrowser.getTextFromElement(messageLocator));

            automatedBrowser.populateElement(formTextBoxLocator, "test text", 10);
            automatedBrowser.verifyTextFromElement(formTextBoxLocator, "test text");
            assertEquals("Text Input Changed", automatedBrowser.getTextFromElement(messageLocator));

            automatedBrowser.clear(formTextBoxLocator);
            automatedBrowser.verifyTextFromElement(formTextBoxLocator, "");

            automatedBrowser.populateElement(formTextAreaLocator, "test text", 10);
            automatedBrowser.verifyTextFromElement(formTextAreaLocator, "test text");
            assertEquals("Text Area Changed", automatedBrowser.getTextFromElement(messageLocator));

            automatedBrowser.clear(formTextAreaLocator);
            automatedBrowser.verifyTextFromElement(formTextAreaLocator, "");

            automatedBrowser.selectOptionByTextFromSelect("", "Option 2.1", formDropDownListLocator, 10);
            assertEquals("Select Changed", automatedBrowser.getTextFromElement(messageLocator));

            automatedBrowser.clickElement(formCheckboxLocator, 10);
            assertEquals("Checkbox Changed", automatedBrowser.getTextFromElement(messageLocator));

            automatedBrowser.selectOptionByValueFromSelect("", "option22", formDropDownListLocator, 10);
            assertEquals("Select Changed", automatedBrowser.getTextFromElement(messageLocator));

            automatedBrowser.selectOptionByTextFromSelect("force", "Option 2.1", formDropDownListLocator, 10);
            automatedBrowser.selectOptionByValueFromSelect("force", "option22", formDropDownListLocator, 10);

            automatedBrowser.clickElementIfExists("thisdoesnotexist", 2, "true");

            automatedBrowser.verifyElementExists(formTextBoxLocator);
        } finally {
            automatedBrowser.destroy();
        }
    }

    @Test
    public void formTestWithSimpleByForceInteraction() throws URISyntaxException {
        final AutomatedBrowser automatedBrowser = AUTOMATED_BROWSER_FACTORY.getAutomatedBrowser("FirefoxNoImplicitWait");

        final String formButtonLocator = "button_element";

        final String messageLocator = "message";

        try {
            automatedBrowser.init();

            automatedBrowser.goTo(FormTest.class.getResource("/form.html").toURI().toString());

            automatedBrowser.clickElementIfExists("true", formButtonLocator, 10, null);
            assertEquals("Button Clicked", automatedBrowser.getTextFromElement(messageLocator));

        } finally {
            automatedBrowser.destroy();
        }
    }

    @Test
    public void formTestWithSimpleByMoveTo() throws URISyntaxException {
        final AutomatedBrowser automatedBrowser = AUTOMATED_BROWSER_FACTORY.getAutomatedBrowser("FirefoxNoImplicitWait");

        final String formButtonLocator = "button_element";

        final String messageLocator = "message";

        try {
            automatedBrowser.init();

            automatedBrowser.goTo(FormTest.class.getResource("/form.html").toURI().toString());

            automatedBrowser.mouseOver(formButtonLocator, 10);
            assertEquals("Button Mouse Over", automatedBrowser.getTextFromElement(messageLocator));

        } finally {
            automatedBrowser.destroy();
        }
    }

    @Test
    public void captureHarFile() {
        final AutomatedBrowser automatedBrowser =
                 AUTOMATED_BROWSER_FACTORY.getAutomatedBrowser("FirefoxNoImplicitWait");

        try {
            automatedBrowser.init();
            automatedBrowser.captureHarFile();
            automatedBrowser.goTo("https://octopus.com/");
        } finally {
            try {
                automatedBrowser.saveHarFile("test.har");
            } finally {
                automatedBrowser.destroy();
            }
        }
    }

    @Test
    public void captureCompleteHarFile() {

        final AutomatedBrowser automatedBrowser =
                 AUTOMATED_BROWSER_FACTORY.getAutomatedBrowser("FirefoxNoImplicitWait");

        try {
            automatedBrowser.init();
            automatedBrowser.captureCompleteHarFile();
            automatedBrowser.goTo("https://octopus.com/");
        } finally {
            try {
                automatedBrowser.saveHarFile("test.har");
            } finally {
                automatedBrowser.destroy();
            }
        }
    }

    @Test
    @Retry
    public void browserStackEdgeTest() {
        if (StringUtils.isBlank(System.getenv(BrowserStackDecorator.USERNAME_ENV)) ||
                StringUtils.isBlank(System.getenv(BrowserStackDecorator.AUTOMATE_KEY_ENV))) {
            return;
        }

        final AutomatedBrowser automatedBrowser =
                AUTOMATED_BROWSER_FACTORY.getAutomatedBrowser("BrowserStackEdge");

        final String formButtonLocator = "button_element";
        final String formTextBoxLocator = "text_element";
        final String formTextAreaLocator = "textarea_element";
        final String formDropDownListLocator = "[name=select_element]";
        final String formCheckboxLocator = "//*[@name=\"checkbox1_element\"]";

        final String messageLocator = "message";

        try {
            automatedBrowser.init();

            automatedBrowser.maximizeWindow();

            automatedBrowser.goTo("https://s3.amazonaws.com/webdriver-testing-website/form.html");

            // A normal click also triggers a mouse over in Edge, so we force the click instead
            automatedBrowser.clickElementIfExists("force", formButtonLocator, null);
            assertEquals("Button Clicked", automatedBrowser.getTextFromElement(messageLocator));

            automatedBrowser.populateElement(formTextBoxLocator, "test text");

            assertEquals("Text Input Changed", automatedBrowser.getTextFromElement(messageLocator));

            automatedBrowser.populateElement(formTextAreaLocator, "test text");

            assertEquals("Text Area Changed", automatedBrowser.getTextFromElement(messageLocator));

            automatedBrowser.selectOptionByTextFromSelect("", "Option 2.1", formDropDownListLocator);
            assertEquals("Select Changed", automatedBrowser.getTextFromElement(messageLocator));

            automatedBrowser.clickElement(formCheckboxLocator);
            assertEquals("Checkbox Changed", automatedBrowser.getTextFromElement(messageLocator));
        } finally {
            automatedBrowser.destroy();
        }
    }

    @Test
    @Retry
    public void browserStackAndroidTest() {

        if (StringUtils.isBlank(System.getenv(BrowserStackDecorator.USERNAME_ENV)) ||
            StringUtils.isBlank(System.getenv(BrowserStackDecorator.AUTOMATE_KEY_ENV))) {
            return;
        }

        final AutomatedBrowser automatedBrowser =
                AUTOMATED_BROWSER_FACTORY.getAutomatedBrowser("BrowserStackAndroid");

        final String formButtonLocator = "button_element";
        final String formTextBoxLocator = "text_element";
        final String formTextAreaLocator = "textarea_element";
        final String formDropDownListLocator = "[name=select_element]";
        final String formCheckboxLocator =  "//*[@name=\"checkbox1_element\"]";
        final String messageLocator = "message";

        try {
            automatedBrowser.init();

            automatedBrowser.maximizeWindow();

            automatedBrowser.goTo("https://s3.amazonaws.com/webdriver-testing-website/form.html");

            // A normal click also triggers a mouse over in Android, so we force the click instead
            automatedBrowser.clickElementIfExists("force", formButtonLocator, null);
            assertEquals("Button Clicked", automatedBrowser.getTextFromElement(messageLocator));

            automatedBrowser.populateElement(formTextBoxLocator, "test text");
            assertEquals("Text Input Changed", automatedBrowser.getTextFromElement(messageLocator));

            automatedBrowser.populateElement(formTextAreaLocator, "test text");
            assertEquals("Text Area Changed", automatedBrowser.getTextFromElement(messageLocator));

            automatedBrowser.selectOptionByTextFromSelect("", "Option 2.1", formDropDownListLocator);
            assertEquals("Select Changed", automatedBrowser.getTextFromElement(messageLocator));

            automatedBrowser.clickElement(formCheckboxLocator);
            assertEquals("Checkbox Changed", automatedBrowser.getTextFromElement(messageLocator));
        } finally {
            automatedBrowser.destroy();
        }
    }
}