Feature: Ensure performance of the Octopus UI
  Scenario: Log in and browser around
    Given I open the browser "ChromeNoImplicitWait"
    And I set the following aliases:
      | UserName	| #userName |
      | Password	| #password |
      | Sign In | .style_globalActionButtonAndLinks__1-zdv > button:nth-child(1) > div:nth-child(1) > div:nth-child(1) > span:nth-child(1) |
      | Add Step | div.style_globalActionButton__2W3W_ > button:nth-child(1) > div:nth-child(1)                                            |
      | Deploy a Package | .styles_templates__3rz1U > li:nth-child(3) > div:nth-child(1) |
      | Uptime      | tr.style_dataTableRow__1qs4i:nth-child(5) > td:nth-child(2)   |
    # This default wait time enforces the responsiveness of the UI. Any attempt to find an element that takes longer than this will result
    # in the test failing.
    And I set the default explicit wait time to "15" seconds
    When I open the URL "HostedUrl"
    And I maximize the window
    And I populate the "UserName" textbox with the text "OctopusUser"
    And I populate the "Password" textbox with the text "OctopusPassword"
    And I click the "Sign In" button
    And I click the "Projects" link
    And I click the "Dashboard" link
    And I click the "RunScript" link
    And I click the "Process" link
    And I click the "Script" link
    And I click the "Process" link
    And I click the "Add Step" link
    # Force the step tiles to be displayed
    And I get the text from the "Deploy a Package" tile
    And I click the "Infrastructure" link
    And I click the "Configuration" link
    And I click the "Diagnostics" link
    # Force the uptime to be displayed
    And I get the text from the "Uptime" tile
    And I close the browser