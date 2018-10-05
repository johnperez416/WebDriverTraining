Feature: Test TicketMonster
  Scenario: Purchase Tickets
    Given I open the browser "ChromeNoImplicitWait"
    When I open the URL "https://ticket-monster.herokuapp.com"
    And I click the "Buy tickets now" button waiting up to "30" seconds
    And I click the "Concert" link waiting up to "30" seconds
    And I click the "Rock concert of the decade" link waiting up to "30" seconds
    And I select the option "Toronto : Roy Thomson Hall" from the "venueSelector" drop down list waiting up to "30" seconds
    And I click the "bookButton" button waiting up to "30" seconds
    And I select the option "A - Premier platinum reserve" from the "sectionSelect" drop down list waiting up to "30" seconds
    And I populate the "tickets-1" text box with the text "2" waiting up to "30" seconds
    And I click the "add" button waiting up to "30" seconds
    And I populate the "email" text box with the text "email@example.org" waiting up to "30" seconds
    And I click the "submit" button waiting up to "30" seconds
    Then I close the browser