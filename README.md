# Selenium-Automation
An open-source, robust, and scalable test automation framework for performing UI tests.

Tools used in this project:
- Selenium
- Java
- Maven
- Cucumber

# How to run

mvn clean test -Dcucumber.filter.tags="@INSERT_TAGS_HERE" -DBrowser=chrome -DHeadless=false -DThreads=1

- DBrowser can be used to specify which browser to execute the tests on.
- DHeadless will enable/disable headless (without GUI) execution.
- DThreads specifies how many scenarios can be run in parallel at a given time.
