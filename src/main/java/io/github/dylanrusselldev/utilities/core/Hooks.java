/*
 * Filename: Hooks.java
 * Author: Dylan Russell
 * Purpose: Setup Before and After options for test execution. Methods are called from the BrowserPreferences.java file
 * 			to help launch the drivers with the correct preferences.
 */

package io.github.dylanrusselldev.utilities.core;

import com.assertthat.selenium_shutterbug.core.Capture;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.github.dylanrusselldev.utilities.browser.DevToolsListener;
import io.github.dylanrusselldev.utilities.browser.WebDriverSetter;
import io.github.dylanrusselldev.utilities.runtime.RuntimeInfo;
import io.github.dylanrusselldev.utilities.screenrecorder.ScreenRecorderUtil;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.IOException;
import java.time.Duration;

public class Hooks {

    protected static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    protected static ThreadLocal<Scenario> scenario = new ThreadLocal<>();

    public static Capabilities cap;
    private static final LoggerClass LOGGER = new LoggerClass(Hooks.class);

    /**
     * Code that executes before every test.
     *
     * @param scenObj the Scenario object
     */
    @Before
    public void start(Scenario scenObj) throws Exception {
        scenario.set(scenObj);

        // Set the name of the thread to be the cucumber tag
        Thread.currentThread().setName(RuntimeInfo.getUniqueScenarioTag());

        // Setup the WebDriver
        WebDriverSetter.setDriver();

        // Set the page timeout
        getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(Constants.TIMEOUT));

        // Get the browser name and version to include in the reports
        cap = ((RemoteWebDriver) getDriver()).getCapabilities();
        LOGGER.logCucumberReport("Executing on: " + RuntimeInfo.getBrowserVersion(cap));

    } // end start

    /**
     * Code that executes after every test.
     */
    @After
    public void afterScenario(Scenario scenario) throws IOException {

        // Stop the screen recording if it was started
        ScreenRecorderUtil.stopRecord();

        // If the test failed, take a screenshot and print the DevTools errors
        if (scenario.isFailed()) {
            CommonMethods.screenshot(getDriver(), Capture.FULL);
            DevToolsListener.logDevToolErrors();
        } // end outer if

        // Quit the driver
        getDriver().quit();

        // Remove the driver from the thread
        driver.remove();

    } // end afterScenario

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
