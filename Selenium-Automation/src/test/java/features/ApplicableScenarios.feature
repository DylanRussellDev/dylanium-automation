Feature: Realistic test scenarios for a mock ecommerce site

  @CompareProducts
  Scenario: Validate Compare Product functionality
    Given the user navigates to the ecommerce site
    And the user goes to the Latops and Notebooks section of the site
    When the user selects the Compare button for the "MacBook Air"
    And the user selects the Compare button for the "MacBook Pro"
    And clicks the Product Compare link
    Then the two Macbooks will be compared side by side

