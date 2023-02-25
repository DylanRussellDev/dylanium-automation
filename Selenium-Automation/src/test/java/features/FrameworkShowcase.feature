@FrameworkShowcase
Feature: Various test cases to demonstrate the framework's capabilities

  @CalculatorTest
  Scenario: A basic Selenium scenario
    Given the user navigates to the calculator website
    When the user clicks two plus two
    Then verify the output is "4"

  @ScreenRecord
  Scenario: Demo of the screen recorder tool
    Given the screen recorder is started
    And the user navigates to the Selenium Demo website
    When the user selects the "WYSIWYG Editor" as an Available Example
    Then the user is able to input "Hello World" into the text area
    And the screen recorder is stopped

  @Decryption
  Scenario: Demo of the screen recorder tool
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
    And the user clicks the "404" code
    Then the error information is captured and will be available in the report