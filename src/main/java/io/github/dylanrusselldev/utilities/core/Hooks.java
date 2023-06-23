/*
 * Filename: Hooks.java
 * Author: Dylan Russell
 * Purpose: Setup Before and After options for test execution. Methods are called from the BrowserPreferences.java file
 * 			to help launch the drivers with the correct preferences.
 */

package io.github.dylanrusselldev.utilities.core;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.dylanrusselldev.utilities.browser.DevToolsListener;
import io.github.dylanrusselldev.utilities.browser.WebDriverSetter;
import io.github.dylanrusselldev.utilities.runtime.CommandRunner;
import io.github.dylanrusselldev.utilities.runtime.RuntimeInfo;
import io.github.dylanrusselldev.utilities.screenrecorder.ScreenRecorderUtil;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.IExecutionListener;

import java.io.IOException;
import java.time.Duration;

public class Hooks implements IExecutionListener {

    protected static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    protected static ThreadLocal<Scenario> scenario = new ThreadLocal<>();

    private static Capabilities capabilities;
    private static final LoggerClass LOGGER = new LoggerClass(Hooks.class);

    /**
     * Code that executes before the entire test suite.
     */
    @BeforeAll
    public static void beforeAll() {
        LOGGER.info("*** TEST SUITE STARTED ***");
    }

    /**
     * Code that executes before every cucumber scenario
     *
     * @param scenObj the Scenario object
     */
    @Before
    public void start(Scenario scenObj) throws Exception {

        // Set the scenario object
        scenario.set(scenObj);

        // Set the name of the thread to be the cucumber tag
        Thread.currentThread().setName(RuntimeInfo.getUniqueScenarioTag());

        // Setup the WebDriver
        WebDriverSetter.setDriver();

        // Set the page timeout
        getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(Constants.TIMEOUT));

        // Get the browser name and version to include in the reports
        capabilities = ((RemoteWebDriver) getDriver()).getCapabilities();
        LOGGER.logCucumberReport("Executing on: " + RuntimeInfo.getBrowserVersion(capabilities));

        LOGGER.info("Beginning Scenario: " + Thread.currentThread().getName());

    } // end start

    /**
     * Code that executes after every cucumber scenario.
     */
    @After
    public void afterScenario() throws IOException {

        // Stop the screen recording if it was started
        ScreenRecorderUtil.stopRecord();

        // If the test failed, take a screenshot and print the DevTools errors
        if (getScenario().isFailed()) {
            CommonMethods.screenshot(getDriver());
            DevToolsListener.logDevToolErrors();
        } // end outer if

        // Quit the driver
        getDriver().quit();

        // Remove the driver from the thread
        driver.remove();

    } // end afterScenario

    /**
     * Code that executes after the entire test suite.
     */
    public void onExecutionFinish() {
        // Generate the Masterthought report
        MasterthoughtReport.generateTestReport();

        // End any potentially hanging drivers
        CommandRunner.endDriverExe();
        LOGGER.info("*** TEST SUITE FINISHED ***");

        // Open the Masterthought report and html log file after execution has finished.
        WebDriverManager.chromedriver().setup();
        ChromeDriver driver = new ChromeDriver();

        driver.get(Constants.LOG_FOLDER_PATH + "execution-log.html");
        CommonMethods.pauseForSeconds(1);

        driver.manage().window().maximize();
        driver.switchTo().newWindow(WindowType.TAB);
        driver.get(Constants.MASTERTHOUGHT_REPORT_PATH + "cucumber-html-reports\\overview-features.html");
    }

    /**
     * Return the WebDriver object for the current thread.
     *
     * @return the WebDriver object
     */
    public static WebDriver getDriver() {
        return driver.get();
    } // end getDriver

    /**
     * Return the Scenario object for the current thread.
     */
    public static Scenario getScenario() {
        return scenario.get();
    } // end getScenario

    /**
     * Set the WebDriver while keeping it thread safe.
     */
    public static void setDriver(WebDriver d) {
        driver.set(d);
    } // end setDriver

} // end Hooks.java
