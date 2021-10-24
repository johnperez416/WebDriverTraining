package com.octopus;

import java.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class FactoryTest {

  private static final AutomatedBrowserFactory AUTOMATED_BROWSER_FACTORY
      = new AutomatedBrowserFactory();

  private String browser;

  public FactoryTest(final String browser) {
    this.browser = browser;
  }

  @Parameterized.Parameters
  public static Iterable data() {
    return Arrays.asList(
        "FirefoxHeadless",
        "ChromeHeadless",
        "FirefoxNoImplicitWaitNoProxy",
        "ChromeNoImplicitWaitNoProxy",
        "FirefoxNoImplicitWait",
        "Firefox",
        "Chrome",
        "ChromeNoImplicitWait"
    );
  }

  @Test
  public void openURL() {
    final AutomatedBrowser automatedBrowser =
        AUTOMATED_BROWSER_FACTORY.getAutomatedBrowser(browser);
    automatedBrowser.init();
    try {
      automatedBrowser.goTo("https://google.com/");
    } finally {
      automatedBrowser.destroy();
    }
  }
}