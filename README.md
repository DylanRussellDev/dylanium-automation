# Selenium-Automation
An open-source BDD Selenium Java test automation framework.

# Features

This framework includes a variety of features that can handle many validation tasks. 

- Masterthought and Extent Report test execution reports
- Validate downloaded files and their text
- Screen recorder that embeds the video within the execution report
- Perform database validations
- Read data from Excel files
- Log network DevTools errors
- Parallel execution

# How to run

To see a demo of the various tools included, run this from the command line in the project directory:
mvn clean test -Dcucumber.filter.tags="@FrameworkShowcase" -DBrowser=chrome -DHeadless=false -DThreads=1

- DBrowser can be used to specify which browser to execute the tests on.
- DHeadless will enable/disable headless (without GUI) execution.
- DThreads specifies how many scenarios to execute in parallel.
