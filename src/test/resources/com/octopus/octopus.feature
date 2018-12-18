Feature: TicketMonster Test
  Scenario: Purchase Tickets with default wait time and aliases
    Given I open the browser "ChromeNoImplicitWaitLambda"
    And I set the following aliases:
      | Sign In | .style_globalActionButtonAndLinks__1-zdv > button:nth-child(1) > div:nth-child(1) > div:nth-child(1) > span:nth-child(1) |
    And I set the default explicit wait time to "30" seconds
    When I open the URL "HostedUrl"
    And I populate the "userName" textbox with the text "OctopusUser"
    And I populate the "password" textbox with the text "OctopusPassword"
    And I click the "Sign In" button