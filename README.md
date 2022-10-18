# Selenium-Automation
A template Selenium test automation framework that is open-source and robust.

Tools used in this project:

- Selenium
- Java
- Maven
- Cucumber

# Features

This framework includes a variety of features that can handle many validation tasks. 

- Masterthough test execution reports
- Validate downloaded files and the contents within them
- Screen recorder that embeds the video within the Mastethought report
- Perform database validations
- Read data from Excel files
- Capture and print network DevTools errors
- Parallel execution

# How to run

mvn clean test -Dcucumber.filter.tags="@INSERT_TAGS_HERE" -DBrowser=chrome -DHeadless=false -DThreads=1

- DBrowser can be used to specify which browser to execute the tests on.
- DHeadless will enable/disable headless (without GUI) execution.
- DThreads specifies how many scenarios can be run in parallel at a given time.
