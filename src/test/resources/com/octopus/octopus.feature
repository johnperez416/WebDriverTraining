Feature: Ensure performance of the Octopus UI
  Scenario: Log in and browser around
    Given I open the browser "ChromeNoImplicitWait"
    And I set the following aliases:
      | Sign In | .style_globalActionButtonAndLinks__1-zdv > button:nth-child(1) > div:nth-child(1) > div:nth-child(1) > span:nth-child(1) |
    And I set the default explicit wait time to "10" seconds
    When I open the URL "HostedUrl"
    And I maximize the window
    And I populate the "userName" textbox with the text "OctopusUser"
    And I populate the "password" textbox with the text "OctopusPassword"
    And I click the "Sign In" button
    And I click the "Projects" link
    And I click the "Dashboard" link
    And I click the "RunScript" link
    And I click the "Process" link
    And I click the "Script" link
    And I click the "Infrastructure" link
    And I click the "Configuration" link
    And I click the "Diagnostics" link
    And I close the browser