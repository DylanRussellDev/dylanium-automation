Feature: Browser Actions

@ChromeUpdate
Scenario: Check for Google Chrome Update
  Given I am on the About Google page
  When check if Google Chrome has an available update
  Then close Chrome "Stable" if a new update needs applied