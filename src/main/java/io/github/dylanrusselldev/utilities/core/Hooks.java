/*
 * Filename: Hooks.java
 * Purpose: Setup Before and After options for test execution. Methods are called from the BrowserPreferences.java file
 * 			to help launch the drivers with the correct preferences.
 */

package io.github.dylanrusselldev.utilities.core;

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

    public static WebDriver getDriver() {
		return driver.get();
    } // end getDriver()

	@Before
	public void start(Scenario scenObj) throws Exception {
		scenario.set(scenObj);

		// Setup the WebDriver
        WebDriverSetter.setDriver();

		// Set the page timeout
		driver.get().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(Constants.TIMEOUT));

		// Get the browser name and version to include in the reports
		cap = ( (RemoteWebDriver) getDriver()).getCapabilities();
		scenario.get().log("Executing on: " + CommonMethods.browserInfo(cap));

		// Start the Extent Report
		ExtentReport.startReporting();

		// Start DevTools listener
		DevToolsListener.startDevToolsListener(driver.get());
	} // end start()
	
	@After
	public void afterScenario(Scenario scenario) {

		if (scenario.isFailed()) {
			CommonMethods.screenshot(driver.get());
			DevToolsListener.logDevToolErrors();
		} // end outer if

		// Quit the driver
		driver.get().quit();

		// End the Extent Report
		ExtentReport.endReporting();

	} // end afterScenario()

} // end Hooks.java
