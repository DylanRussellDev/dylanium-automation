Feature: Testing Selenium

@TestSelenium
Scenario: Selenium Test
  Given I navigate to the calculator website
  When I click "2" plus "2"
  Then verify the output is "4"

@TestGoogle
Scenario: Testing Google
	Given I navigate to Google
	When I get to Google
	Then I take a screenshot