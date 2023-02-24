/*
 * Filename: Hooks.java
 * Purpose: Setup Before and After options for test execution. Methods are called from the BrowserPreferences.java file
 * 			to help launch the drivers with the correct preferences.
 */

package utilities.core;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.remote.RemoteWebDriver;
import utilities.browsers.WebDriverSetter;
import utilities.helpers.DevToolsListener;

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

//		ExtentReport.startReporting();

		// Start DevTools listener
		DevToolsListener.startDevToolsListener(driver.get());
	} // end start()
	
	@After
	public void afterScenario(Scenario scenario) {

		if (scenario.isFailed()) {
			CommonMethods.screenshot(driver.get());
			// Print DevTools errors
			if (!DevToolsListener.devtoolErrors.isEmpty()) {
				Set<String> set = new HashSet<>(DevToolsListener.devtoolErrors);

				for (String s : set) {
					scenario.log(s);
				} // end for

			} // end inner if

		} // end outer if

		driver.get().quit();

//		ExtentReport.endReporting();

	} // end afterScenario()

} // end Hooks.java
