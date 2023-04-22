@FrameworkShowcase
Feature: Various test cases to demonstrate the framework's capabilities

	@CalculatorTest
	Scenario: A basic Selenium scenario
		Given the user navigates to the calculator website
		When the user clicks two plus two
		Then verify the output is "4"

	@ScreenRecord
	Scenario: Demonstration of the screen recorder tool
		Given the screen recorder is started
		And the user navigates to the Selenium Demo website
		When the user selects the "WYSIWYG Editor" as an Available Example
		Then the user is able to input "Hello World" into the text area
		And the screen recorder is stopped

	@Decryption
	Scenario: Demonstrate the decryption functionality
		Given the user navigates to the Selenium Demo website
		When the user selects the "Form Authentication" as an Available Example
		And the user enters the username
		And the user enters the decrypted password
		And the user clicks the login button
		Then a success message will be displayed

	@DevTools
	Scenario: Log network error responses during execution
		Given the user navigates to the Selenium Demo website
		When the user selects the "Status Codes" as an Available Example
		And the user clicks the "500" code
		Then print the DevTools error information in the report

	@Logging
	Scenario: Log actions during execution
		Given the user navigates to the Selenium Demo website
		When the user selects the "Add/Remove Elements" as an Available Example
		Then log the user actions when they click different buttons

	@SensitiveInformation
	Scenario: Blur out sensitive information on a page and take a screenshot
		Given the user navigates to the Selenium Demo website
		When the user selects the "Form Authentication" as an Available Example
		Then take a screenshot of the page with the username and password fields blurred out

	@ManualFailure
	Scenario: Manually trigger a failure and show a user friendly exception message in the report
		Given the user navigates to the Selenium Demo website
		When the user selects the "Inputs" as an Available Example
		And the user attempts to interact with a label
		Then the scenario will fail and a user friendly exception message will display on the report

