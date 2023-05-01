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
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.time.Duration;

public class Hooks {

    protected static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    protected static ThreadLocal<Scenario> scenario = new ThreadLocal<>();

    public static Capabilities cap;
    private static final LoggerClass LOGGER = new LoggerClass(Hooks.class);

    /**
     * Return the WebDriver object for the current thread.
     */
    public static WebDriver getDriver() {
        return driver.get();
    } // end getDriver()

    /**
     * Set the WebDriver while keeping it thread safe.
     */
    public static void setDriver(WebDriver d) {
        driver.set(d);
    } // end setDriver()

    /**
     * Return the Scenario object for the current thread.
     */
    public static Scenario getScenario() {
        return scenario.get();
    } // end getScenario()

    /**
     * Code that executes before every test.
     *
     * @param  scenObj      Scenario object
     * @throws Exception
     */
    @Before
    public void start(Scenario scenObj) throws Exception {
        scenario.set(scenObj);

        // Set the name of the thread to be the scenario name
        Thread.currentThread().setName(getScenario().getName());

        // Setup the WebDriver
        WebDriverSetter.setDriver();

        // Set the page timeout
        getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(Constants.TIMEOUT));

        // Get the browser name and version to include in the reports
        cap = ((RemoteWebDriver) getDriver()).getCapabilities();
        LOGGER.logCucumberReport("Executing on: " + RuntimeInfo.getBrowserVersion(cap));

    }

    /**
     * Code that executes after every test.
     *
     * @param  scenario      Scenario object
     */
    @After
    public void afterScenario(Scenario scenario) {

        if (scenario.isFailed()) {
            CommonMethods.screenshot(getDriver(), Capture.FULL);
            DevToolsListener.logDevToolErrors();
        } // end outer if

        // Quit the driver
        getDriver().quit();

        // Remove the driver from ThreadLocal
        driver.remove();

    }

} // end Hooks.java
