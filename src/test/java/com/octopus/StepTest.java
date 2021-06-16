package com.octopus;

import org.junit.Test;

import java.net.URISyntaxException;
import java.util.Objects;

import static junit.framework.TestCase.assertEquals;

public class StepTest {
    private static final String BROWSER = "FirefoxNoImplicitWaitNoProxy";
    private static final AutomatedBrowserFactory AUTOMATED_BROWSER_FACTORY = new AutomatedBrowserFactory();

    @Test
    public void windowInteraction() throws URISyntaxException {
        final AutomatedBrowser automatedBrowser = AUTOMATED_BROWSER_FACTORY.getAutomatedBrowser(BROWSER);

        try {
            automatedBrowser.init();

            automatedBrowser.goTo(StepTest.class.getResource("/form.html").toURI().toString());

            automatedBrowser.maximizeWindow();
            automatedBrowser.setWindowSize(800, 600);
            automatedBrowser.browserZoomIn();
            automatedBrowser.browserZoomOut();
            automatedBrowser.refresh();
            automatedBrowser.verifyUrl(".*?form\\.html");

        } finally {
            automatedBrowser.destroy();
        }
    }

    @Test
    public void keyInteraction() throws URISyntaxException {
        final AutomatedBrowser automatedBrowser = AUTOMATED_BROWSER_FACTORY.getAutomatedBrowser(BROWSER);

        try {
            automatedBrowser.init();
            automatedBrowser.goTo(StepTest.class.getResource("/form.html").toURI().toString());
            automatedBrowser.pressEnter("textarea_element");
            automatedBrowser.pressEscape("textarea_element");
            assertEquals("Text Area Changed", automatedBrowser.getTextFromElementWithId("message"));
        } finally {
            automatedBrowser.destroy();
        }
    }

    @Test
    public void keyInteractionForce() throws URISyntaxException {
        final AutomatedBrowser automatedBrowser = AUTOMATED_BROWSER_FACTORY.getAutomatedBrowser(BROWSER);

        try {
            automatedBrowser.init();
            automatedBrowser.goTo(StepTest.class.getResource("/form.html").toURI().toString());
            automatedBrowser.pressEnter("force", "textarea_element");
            automatedBrowser.pressEscape("force", "textarea_element");
            assertEquals("Text Area Changed", automatedBrowser.getTextFromElementWithId("message"));
        } finally {
            automatedBrowser.destroy();
        }
    }

    @Test
    public void javascriptTest() throws URISyntaxException {
        final AutomatedBrowser automatedBrowser = AUTOMATED_BROWSER_FACTORY.getAutomatedBrowser(BROWSER);

        try {
            automatedBrowser.init();
            automatedBrowser.goTo(StepTest.class.getResource("/form.html").toURI().toString());
            automatedBrowser.runJavascript(" document.getElementById('message').textContent = 'Raw JavaScript';");
            assertEquals("Raw JavaScript", automatedBrowser.getTextFromElementWithId("message"));
        } finally {
            automatedBrowser.destroy();
        }
    }

    @Test
    public void javascriptTestWithReturn() throws URISyntaxException {
        final AutomatedBrowser automatedBrowser = AUTOMATED_BROWSER_FACTORY.getAutomatedBrowser(BROWSER);

        try {
            automatedBrowser.init();
            automatedBrowser.goTo(StepTest.class.getResource("/form.html").toURI().toString());
            final Object value = automatedBrowser.runJavascript("return 'test';");
            assertEquals("test", Objects.toString(value));
        } finally {
            automatedBrowser.destroy();
        }
    }

    @Test
    public void verifyDoesNotExist() throws URISyntaxException {
        final AutomatedBrowser automatedBrowser = AUTOMATED_BROWSER_FACTORY.getAutomatedBrowser(BROWSER);

        try {
            automatedBrowser.init();
            automatedBrowser.goTo(StepTest.class.getResource("/form.html").toURI().toString());
            automatedBrowser.verifyElementDoesNotExist("thisdoesnotexist", 2);
        } finally {
            automatedBrowser.destroy();
        }
    }
}
