# dylanium-automation
An open-source BDD Selenium Java test automation framework.

# Features

This framework includes a wide variety of features. Including, but not limited to:

- Detailed test execution reports
- Parallel execution
- Logging library
- User friendly exception messages
- Validation of text in PDFs
- Log Network DevTools errors
- A screen recorder
- Database validations
- Read data from Excel files

# How to Execute

To see a demo, run the following from the command line in the project directory:

mvn clean test -Dcucumber.filter.tags="@FrameworkShowcase" -DBrowser=chrome -DHeadless=false -DThreads=1

- DBrowser can be used to specify which browser to execute the tests on
- DHeadless will enable/disable headless (without GUI) execution
- DThreads specifies how many scenarios to execute in parallel
