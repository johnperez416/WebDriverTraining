Feature: Test TicketMonster
  Scenario: Purchase Tickets
    Given I open the browser "ChromeNoImplicitWait"
    When I open the URL "https://ticket-monster.herokuapp.com"
    And I sleep for "1" second
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

  Scenario: Purchase Tickets with default wait time
    Given I open the browser "ChromeNoImplicitWait"
    And I set the default explicit wait time to "30" seconds
    When I open the URL "https://ticket-monster.herokuapp.com"
    And I sleep for "1" second
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

  Scenario: Purchase Tickets with default wait time and aliases
    Given I open the browser "ChromeNoImplicitWait"
    And I set the following aliases:
      | Venue | venueSelector |
      | Book | bookButton |
      | Section | sectionSelect |
      | Adult Ticket Count | tickets-1 |
      | Add Tickets | add |
      | Checkout | submit |
    And I set the default explicit wait time to "30" seconds
    When I open the URL "https://ticket-monster.herokuapp.com"
    And I sleep for "1" second
    And I click the "Buy tickets now" button
    And I click the "Concert" link
    And I click the "Rock concert of the decade" link
    And I select the option "Toronto : Roy Thomson Hall" from the "Venue" drop down list
    And I click the "Book" button
    And I select the option "A - Premier platinum reserve" from the "Section" drop down list
    And I populate the "Adult Ticket Count" text box with the text "2"
    And I click the "Add Tickets" button
    And I populate the "email" text box with the text "email@example.org"
    And I click the "Checkout" button
    Then I close the browser