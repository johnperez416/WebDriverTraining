Feature: A simple test
  Scenario: Open and close the browser
    Given I open the browser "FirefoxNoImplicitWait"
    And I set the window size to "1024" x "768"
    Then I close the browser