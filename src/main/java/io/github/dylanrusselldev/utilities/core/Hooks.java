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
import io.github.dylanrusselldev.utilities.browsers.WebDriverSetter;
import io.github.dylanrusselldev.utilities.helpers.DevToolsListener;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.time.Duration;

public class Hooks {

    public static Capabilities cap;

    public static final String browser = System.getProperty("Browser").toLowerCase();
    public static final String headless = System.getProperty("Headless").toLowerCase();

    public static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    public static final ThreadLocal<Scenario> scenario = new ThreadLocal<>();

    /**
     * Return the WebDriver object set for test execution
     */
    public static WebDriver getDriver() {
        return driver.get();
    } // end getDriver()

    /**
     * Code that executes before every test
     *
     * @param scenObj Scenario object
     * @throws Exception
     */
    @Before
    public void start(Scenario scenObj) throws Exception {
        scenario.set(scenObj);

        // Set the name of the thread to be the scenario name
        Thread.currentThread().setName(scenario.get().getName());

        // Setup the WebDriver
        WebDriverSetter.setDriver();

        // Set the page timeout
        driver.get().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(Constants.TIMEOUT));

        // Get the browser name and version to include in the reports
        cap = ((RemoteWebDriver) getDriver()).getCapabilities();
        scenario.get().log("Executing on: " + CommonMethods.browserInfo(cap));

    } // end start()

    /**
     * Code that executes after every test
     *
     * @param scenario Scenario object
     */
    @After
    public void afterScenario(Scenario scenario) {

        if (scenario.isFailed()) {
            CommonMethods.screenshot(driver.get(), Capture.FULL);
            DevToolsListener.logDevToolErrors();
        } // end outer if

        // Quit the driver
        driver.get().quit();

    } // end afterScenario()

} // end Hooks.java
