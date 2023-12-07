package io.github.dylanrusselldev.steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import io.github.dylanrusselldev.utilities.browser.DevToolsListener;
import io.github.dylanrusselldev.utilities.browser.WebDriverSetter;
import io.github.dylanrusselldev.utilities.core.CommonMethods;
import io.github.dylanrusselldev.utilities.core.Constants;
import io.github.dylanrusselldev.utilities.logging.LoggerClass;
import io.github.dylanrusselldev.utilities.reporting.MasterthoughtReport;
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

/*
 * Filename: Hooks.java
 * Author: Dylan Russell
 * Purpose: Methods that perform actions before and after the test suites and scenarios.
 */
public class Hooks implements IExecutionListener {

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static final ThreadLocal<Scenario> scenario = new ThreadLocal<>();

    private static final LoggerClass LOGGER = new LoggerClass(Hooks.class);

    /**
     * Code that executes before the entire test suite.
     */
    @BeforeAll
    public static void beforeAll() {
        LOGGER.info("*** TEST SUITE STARTED ***");
    }

    /**
     * Code that executes before every cucumber scenario.
     *
     * @param scenObj the Scenario object
     */
    @Before
    public void start(Scenario scenObj) throws Exception {
        scenario.set(scenObj);
        Thread.currentThread().setName(RuntimeInfo.getUniqueScenarioTag());

        WebDriverSetter.setDriver();

        Capabilities capabilities = ((RemoteWebDriver) getDriver()).getCapabilities();
        LOGGER.logCucumberReport("Executing on: " + RuntimeInfo.getBrowserVersion(capabilities));

        LOGGER.info("Beginning Scenario: " + Thread.currentThread().getName());
    }

    /**
     * Code that executes after every cucumber scenario.
     */
    @After
    public void afterScenario() throws IOException {
        ScreenRecorderUtil.stopRecord();

        if (getScenario().isFailed()) {
            CommonMethods.partialScreenshot(getDriver());
            DevToolsListener.logDevToolErrors();
        }

        getDriver().quit();
        driver.remove();
    }

    /**
     * Code that executes after the entire test suite.
     */
    public void onExecutionFinish() {
        MasterthoughtReport.generateTestReport();
        CommandRunner.endDriverExe();

        LOGGER.info("*** TEST SUITE FINISHED ***");

        // Open the Masterthought report and html log file after execution has finished.
        ChromeDriver driver = new ChromeDriver();
        driver.get(Constants.LOG_FOLDER_PATH + "\\execution-log.html");
        CommonMethods.pauseForSeconds(1);
        driver.manage().window().maximize();
        driver.switchTo().newWindow(WindowType.TAB);
        driver.get(Constants.MASTERTHOUGHT_REPORT_PATH + "\\cucumber-html-reports\\overview-features.html");
    }

    /**
     * Return the WebDriver object for the current thread.
     *
     * @return the WebDriver object
     */
    public static WebDriver getDriver() {
        return driver.get();
    }

    /**
     * Return the Scenario object for the current thread.
     *
     * @return the Scenario object
     */
    public static Scenario getScenario() {
        return scenario.get();
    }

    /**
     * Set the WebDriver while keeping it thread safe.
     */
    public static void setDriver(WebDriver d) {
        driver.set(d);
    }

}
