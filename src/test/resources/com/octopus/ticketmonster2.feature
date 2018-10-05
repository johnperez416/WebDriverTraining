Feature: Test TicketMonster With Default Wait
  Scenario: Purchase Tickets with default wait time
    Given I open the browser "ChromeNoImplicitWait"
    And I set the default explicit wait time to "30" seconds
    When I open the URL "https://ticket-monster.herokuapp.com"
    And I click the "Buy tickets now" button
    And I click the "Concert" link
    And I click the "Rock concert of the decade" link
    And I select the option "Toronto : Roy Thomson Hall" from the "venueSelector" drop down list
    And I click the "bookButton" button
    And I select the option "A - Premier platinum reserve" from the "sectionSelect" drop down list
    And I populate the "tickets-1" text box with the text "2"
    And I click the "add" button
    And I populate the "email" text box with the text "email@example.org"
    And I click the "submit" button
    Then I close the browser